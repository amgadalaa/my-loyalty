package com.loyalty.usermanagement.lusr;

import java.sql.SQLException;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.loyalty.common.general.responses.models.ErrorResponse;
import com.loyalty.common.general.responses.models.GeneralApplicationErrorCodes;
import com.loyalty.usermanagement.UserManagementApplication;
import com.loyalty.usermanagement.entities.LUsrEntity;
import com.loyalty.usermanagement.testhelper.DatabaseCleaner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = { DatabaseCleaner.class,
		UserManagementApplication.class })
public class LUsrResourceIT {

	@Autowired
	private WebTestClient webClient;

	@Autowired
	private DatabaseCleaner cleaner;

	@Autowired
	private LUserRepo repo;

	private final String LUSR_ENDPOINT = "/lusrs/";
	private final String NON_EXISTING_LUSR_ID = "101";

	@Before
	public void cleanDB() throws SQLException {
		cleaner.clean();
	}

	// -----------------------------------
	// Get all
	// -----------------------------------

	@Test
	public void whenSomeLUsrExistsThen200AndReturnRecords() throws SQLException {
		// Given
		LUsrEntity expectedEntity = repo.save(getValidTestLUsr());

		// When
		webClient.get().uri(LUSR_ENDPOINT).exchange().expectStatus().is2xxSuccessful().expectBodyList(LUsrEntity.class)
				.isEqualTo(Collections.singletonList(expectedEntity));
	}

	@Test
	public void whenNonLUsrExistsThen200AndEmptyRecords() throws SQLException {
		// When
		webClient.get().uri(LUSR_ENDPOINT).exchange().expectStatus().is2xxSuccessful().expectBodyList(LUsrEntity.class)
				.isEqualTo(Collections.emptyList());
	}

	// -----------------------------------
	// Get by Id
	// -----------------------------------

	@Test
	public void whenLUsrExistsThen200AndReturnRecord() throws SQLException {
		// Given
		LUsrEntity expectedEntity = repo.save(getValidTestLUsr());

		// When
		webClient.get().uri(String.format("%s/%d", LUSR_ENDPOINT, expectedEntity.getUserId())).exchange().expectStatus()
				.is2xxSuccessful().expectBody(LUsrEntity.class).isEqualTo(expectedEntity);

	}

	@Test
	public void whenLUsrNotExistsThen404() throws SQLException {
		// When
		webClient.get().uri(String.format("%s/%s", LUSR_ENDPOINT, NON_EXISTING_LUSR_ID)).exchange().expectStatus()
				.isNotFound();

	}

	// -----------------------------------
	// Insert
	// -----------------------------------

	@Test
	public void whenInvalidInputForInsertThen400() {
		// Given
		LUsrEntity invalidLusr = getInvalidPasswordTestLUsr();
		// when
		EntityExchangeResult<ErrorResponse> result = webClient.post().uri(LUSR_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON).syncBody(invalidLusr).exchange().expectStatus().isBadRequest()
				.expectBody(ErrorResponse.class).returnResult();
		ErrorResponse responseBodyObject = result.getResponseBody();
		Assert.assertEquals("Wrong application error code found",
				GeneralApplicationErrorCodes.FIELD_VALIDATION_FAILED.getErrorCode(),
				responseBodyObject.getApplicationErrorCode());
	}

	@Test
	public void whenvalidInputForInsertThen200AndReturnRecord() {
		// Given
		LUsrEntity validLusr = getValidTestLUsr();
		// when
		EntityExchangeResult<LUsrEntity> result = webClient.post().uri(LUSR_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON).syncBody(validLusr).exchange().expectStatus().isOk()
				.expectBody(LUsrEntity.class).returnResult();
		LUsrEntity responseBodyObject = result.getResponseBody();
		validLusr.setUserId(responseBodyObject.getUserId());
		Assert.assertEquals("LUsr wasn't inserted properly", validLusr.toString(), responseBodyObject.toString());
	}

	@Test
	public void whenLUsrAlreadyExistsForInsertThen409() {
		// Given
		LUsrEntity validLusr = getValidTestLUsr();
		repo.save(validLusr);
		// when
		webClient.post().uri(LUSR_ENDPOINT).contentType(MediaType.APPLICATION_JSON).syncBody(validLusr).exchange()
				.expectStatus().isEqualTo(HttpStatus.CONFLICT);
	}

	// -----------------------------------
	// Edit
	// -----------------------------------

	@Test
	public void whenLUsrDoesNotExistsForEditThen404() {
		// Given
		LUsrEntity validLusr = getValidTestLUsr();
		validLusr.setUserId(Integer.parseInt(NON_EXISTING_LUSR_ID));
		// when
		webClient.put().uri(String.format("%s/%s", LUSR_ENDPOINT, NON_EXISTING_LUSR_ID))
				.contentType(MediaType.APPLICATION_JSON).syncBody(validLusr).exchange().expectStatus().isNotFound();
	}

	@Test
	public void whenLUsrExistsForEditAndChangeEmailThen400() {
		// Given
		LUsrEntity validLusr = getValidTestLUsr();
		validLusr = repo.save(validLusr);
		validLusr.setEmail("test2@test.com");
		// when
		webClient.put().uri(String.format("%s/%s", LUSR_ENDPOINT, validLusr.getUserId()))
				.contentType(MediaType.APPLICATION_JSON).syncBody(validLusr).exchange().expectStatus().isBadRequest();
	}

	@Test
	public void whenLUsrIsValidEditThen200AndReturnRecord() {
		// Given
		LUsrEntity validLusr = getValidTestLUsr();
		validLusr = repo.save(validLusr);

		final String newLUsrName = "Edited name";
		validLusr.setUsername(newLUsrName);
		// when
		webClient.put().uri(String.format("%s/%s", LUSR_ENDPOINT, validLusr.getUserId()))
				.contentType(MediaType.APPLICATION_JSON).syncBody(validLusr).exchange().expectStatus().isOk()
				.expectBody().jsonPath("$.username").isEqualTo(newLUsrName);
	}

	// -----------------------------------
	// Delete
	// -----------------------------------

	@Test
	public void whenDeleteLUsrExistsThen200() throws SQLException {
		// Given
		LUsrEntity expectedEntity = repo.save(getValidTestLUsr());

		// When
		webClient.delete().uri(String.format("%s/%d", LUSR_ENDPOINT, expectedEntity.getUserId())).exchange()
				.expectStatus().isOk();

	}

	@Test
	public void whenDeleteLUsrNotExistsThen404() throws SQLException {
		// When
		webClient.delete().uri(String.format("%s/%s", LUSR_ENDPOINT, NON_EXISTING_LUSR_ID)).exchange().expectStatus()
				.isNotFound();

	}

	@Test
	public void whenReinsertDeletedLUsrNotExistsThen200() throws SQLException {
		LUsrEntity validLUsr = getValidTestLUsr();

		LUsrEntity expectedEntity = repo.save(validLUsr);

		webClient.delete().uri(String.format("%s/%d", LUSR_ENDPOINT, expectedEntity.getUserId())).exchange()
				.expectStatus().isOk();

		webClient.post().uri(LUSR_ENDPOINT).contentType(MediaType.APPLICATION_JSON).syncBody(validLUsr).exchange()
				.expectStatus().isOk();

	}

	private LUsrEntity getValidTestLUsr() {
		return new LUsrEntity("test", "pass$Test1", "test@test.com", "test full", true, false);
	}

	private LUsrEntity getInvalidPasswordTestLUsr() {
		return new LUsrEntity("test", "invalid", "test@test.com", "test full", true, false);
	}

}
