package com.loyalty.usermanagement.cadm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.loyalty.usermanagement.entities.CAdmEntity;
import com.loyalty.usermanagement.entities.CpntEntity;
import com.loyalty.usermanagement.testhelper.DatabaseCleaner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = { DatabaseCleaner.class,
		UserManagementApplication.class })
public class CAdmResourceIT {

	@Autowired
	private WebTestClient webClient;

	@Autowired
	private DatabaseCleaner cleaner;

	@Autowired
	private CAdmRepo repo;

	private final String CADM_ENDPOINT = "/cadms/";
	private final String CADM_CPNT_SUB_RESOURCE = "/cpnts";
	private final String NON_EXISTING_CADM_ID = "101";

	@Before
	public void cleanDB() throws SQLException {
		cleaner.clean();
	}

	// -----------------------------------
	// Get all
	// -----------------------------------

	@Test
	public void whenSomeCAdmExistsThen200AndReturnRecords() throws SQLException {
		// Given
		CAdmEntity expectedEntity = repo.save(getValidTestCAdm());

		// When
		webClient.get().uri(CADM_ENDPOINT).exchange().expectStatus().is2xxSuccessful().expectBodyList(CAdmEntity.class)
				.isEqualTo(Collections.singletonList(expectedEntity));
	}

	@Test
	public void whenNonCAdmExistsThen200AndEmptyRecords() throws SQLException {
		// When
		webClient.get().uri(CADM_ENDPOINT).exchange().expectStatus().is2xxSuccessful().expectBodyList(CAdmEntity.class)
				.isEqualTo(Collections.emptyList());
	}

	// -----------------------------------
	// Get by Id
	// -----------------------------------

	@Test
	public void whenCAdmExistsThen200AndReturnRecord() throws SQLException {
		// Given
		CAdmEntity expectedEntity = repo.save(getValidTestCAdm());

		// When
		webClient.get().uri(String.format("%s/%d", CADM_ENDPOINT, expectedEntity.getUserId())).exchange().expectStatus()
				.is2xxSuccessful().expectBody(CAdmEntity.class).isEqualTo(expectedEntity);

	}

	@Test
	public void whenCAdmNotExistsThen404() throws SQLException {
		// When
		webClient.get().uri(String.format("%s/%s", CADM_ENDPOINT, NON_EXISTING_CADM_ID)).exchange().expectStatus()
				.isNotFound();

	}

	// -----------------------------------
	// Insert
	// -----------------------------------

	@Test
	public void whenInvalidInputForInsertThen400() {
		// Given
		CAdmEntity invalidCAdm = getInvalidPasswordTestCAdm();
		// when
		EntityExchangeResult<ErrorResponse> result = webClient.post().uri(CADM_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON).syncBody(invalidCAdm).exchange().expectStatus().isBadRequest()
				.expectBody(ErrorResponse.class).returnResult();
		ErrorResponse responseBodyObject = result.getResponseBody();
		Assert.assertEquals("Wrong application error code found",
				GeneralApplicationErrorCodes.FIELD_VALIDATION_FAILED.getErrorCode(),
				responseBodyObject.getApplicationErrorCode());
	}

	@Test
	public void whenvalidInputForInsertThen200AndReturnRecord() {
		// Given
		CAdmEntity validCAdm = getValidTestCAdm();
		// when
		EntityExchangeResult<CAdmEntity> result = webClient.post().uri(CADM_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON).syncBody(validCAdm).exchange().expectStatus().isOk()
				.expectBody(CAdmEntity.class).returnResult();
		CAdmEntity responseBodyObject = result.getResponseBody();
		validCAdm.setUserId(responseBodyObject.getUserId());
		Assert.assertEquals("CAdm wasn't inserted properly", validCAdm.toString(), responseBodyObject.toString());
	}

	@Test
	public void whenCAdmAlreadyExistsForInsertThen409() {
		// Given
		CAdmEntity validCAdm = getValidTestCAdm();
		repo.save(validCAdm);
		// when
		webClient.post().uri(CADM_ENDPOINT).contentType(MediaType.APPLICATION_JSON).syncBody(validCAdm).exchange()
				.expectStatus().isEqualTo(HttpStatus.CONFLICT);
	}

	// -----------------------------------
	// Edit
	// -----------------------------------

	@Test
	public void whenCAdmDoesNotExistsForEditThen404() {
		// Given
		CAdmEntity validCAdm = getValidTestCAdm();
		validCAdm.setUserId(Integer.parseInt(NON_EXISTING_CADM_ID));
		// when
		webClient.put().uri(String.format("%s/%s", CADM_ENDPOINT, NON_EXISTING_CADM_ID))
				.contentType(MediaType.APPLICATION_JSON).syncBody(validCAdm).exchange().expectStatus().isNotFound();
	}

	@Test
	public void whenCAdmExistsForEditAndChangeEmailThen400() {
		// Given
		CAdmEntity validCAdm = getValidTestCAdm();
		validCAdm = repo.save(validCAdm);
		validCAdm.setEmail("test2@test.com");
		// when
		webClient.put().uri(String.format("%s/%s", CADM_ENDPOINT, validCAdm.getUserId()))
				.contentType(MediaType.APPLICATION_JSON).syncBody(validCAdm).exchange().expectStatus().isBadRequest();
	}

	@Test
	public void whenCAdmIsValidEditThen200AndReturnRecord() {
		// Given
		CAdmEntity validCAdm = getValidTestCAdm();
		validCAdm = repo.save(validCAdm);

		final String newCAdmName = "Edited name";
		validCAdm.setUsername(newCAdmName);
		// when
		webClient.put().uri(String.format("%s/%s", CADM_ENDPOINT, validCAdm.getUserId()))
				.contentType(MediaType.APPLICATION_JSON).syncBody(validCAdm).exchange().expectStatus().isOk()
				.expectBody().jsonPath("$.username").isEqualTo(newCAdmName);
	}

	// -----------------------------------
	// Delete
	// -----------------------------------

	@Test
	public void whenDeleteCAdmExistsThen200() throws SQLException {
		// Given
		CAdmEntity expectedEntity = repo.save(getValidTestCAdm());

		// When
		webClient.delete().uri(String.format("%s/%d", CADM_ENDPOINT, expectedEntity.getUserId())).exchange()
				.expectStatus().is2xxSuccessful();

	}

	@Test
	public void whenDeleteCAdmNotExistsThen404() throws SQLException {
		// When
		webClient.delete().uri(String.format("%s/%s", CADM_ENDPOINT, NON_EXISTING_CADM_ID)).exchange().expectStatus()
				.isNotFound();
	}

	// -----------------------------------
	// CPnts
	// -----------------------------------

	// -----------------------------------
	// Get
	// -----------------------------------

	@Test
	public void whenSomeCPntsExistsThen200AndReturnRecords() throws SQLException {
		// Given
		Iterable<CAdmEntity> savedEntities = repo.saveAll(getValidTestCAdmWithCpnts(2));
		List<CAdmEntity> listOfSavedEntities = new ArrayList<>();
		savedEntities.forEach(listOfSavedEntities::add);

		// When
		EntityExchangeResult<List<CAdmEntity>> result = webClient.get().uri(CADM_ENDPOINT).exchange().expectStatus()
				.isOk().expectBodyList(CAdmEntity.class).returnResult();
		List<CAdmEntity> resultBody = result.getResponseBody();
		Assert.assertEquals("Invalid list size", listOfSavedEntities.size(), resultBody.size());

		Assert.assertTrue("Invalid CPnts list size", resultBody.stream().allMatch((x) -> x.getCpnts().size() == 2));
	}

	// -----------------------------------
	// Insert
	// -----------------------------------

	@Test
	public void whenvInsertValidCPntThen200AndReturnRecord() {
		// Given
		CAdmEntity savedEntity = repo.save(getValidTestCAdmWithCpnts(1).get(0));
		CpntEntity validCpnt = getValidCpnt();

		// when
		EntityExchangeResult<CAdmEntity> result = webClient.post()
				.uri(String.format("%s/%s/%s", CADM_ENDPOINT, savedEntity.getUserId(), CADM_CPNT_SUB_RESOURCE))
				.contentType(MediaType.APPLICATION_JSON).syncBody(validCpnt).exchange().expectStatus().isOk()
				.expectBody(CAdmEntity.class).returnResult();
		CAdmEntity responseBodyObject = result.getResponseBody();
		Assert.assertTrue("Cpnt wasn't inserted properly",
				responseBodyObject.getCpnts().size() == (savedEntity.getCpnts().size() + 1));
	}

	private CpntEntity getValidCpnt() {
		return new CpntEntity(null, "full Address_new", "CPnt test_new", true, false);
	}

	private List<CAdmEntity> getValidTestCAdmWithCpnts(int size) {
		List<CAdmEntity> cadms = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			CAdmEntity cadm = new CAdmEntity("test" + i, "pass$Test" + i, i + "test@test.com", "test full" + i, true,
					false);
			Set<CpntEntity> cpnts = new HashSet<CpntEntity>();
			cpnts.add((new CpntEntity(cadm, "full Address", "CPnt test", true, false)));
			cpnts.add(new CpntEntity(cadm, "full Address2", "CPnt test2", true, false));
			cadm.setCpnts(cpnts);
			cadms.add(cadm);
		}
		return cadms;
	}

	private CAdmEntity getValidTestCAdm() {
		return new CAdmEntity("test", "pass$Test1", "test@test.com", "test full", true, false);
	}

	private CAdmEntity getInvalidPasswordTestCAdm() {
		return new CAdmEntity("test", "invalid", "test@test.com", "test full", true, false);
	}

}
