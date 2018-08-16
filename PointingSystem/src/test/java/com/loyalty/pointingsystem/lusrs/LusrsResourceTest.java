package com.loyalty.pointingsystem.lusrs;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loyalty.common.general.responses.models.GeneralApplicationErrorCodes;
import com.loyalty.pointingsystem.PointingSystemApplication;
import com.loyalty.pointingsystem.configuration.CPntMessagingConfiguration;
import com.loyalty.pointingsystem.entities.CPntEntity;
import com.loyalty.pointingsystem.entities.LUserEntity;
import com.loyalty.pointingsystem.entities.PointsRelationEntity;
import com.loyalty.pointingsystem.lusrs.dto.AddPointsRequest;

@RunWith(SpringRunner.class)
@WebMvcTest( controllers = LusrsResource.class)
public class LusrsResourceTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ILUsrsService lUsrSrv;

	@Autowired
	private ObjectMapper objectMapper;

	private Long lUsrOne = 123L;
	private Long lUsrtwo = 124L;

	private Long cPntOne = 11L;

	private Logger log = LoggerFactory.getLogger(getClass());

	@Rule
	public TestRule watchman = new TestWatcher() {
		protected void starting(Description description) {
			log.info("Executing: {}", description.getMethodName());
		}
	};

	// -----------------------------------
	// Test DeleteLUsr
	// -----------------------------------

	@Test
	public void test_DeleteLUsr_when_NotCorrectLUsrId_Then_400() throws Exception {

		// When & then
		mockMvc.perform(MockMvcRequestBuilders.delete("/lusrs/sds")).andDo(print())
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.applicationErrorCode",
						equalTo(GeneralApplicationErrorCodes.NO_APPLICATION_CODE.getErrorCode())));
	}

	@Test
	public void test_DeleteLUsr_when_LUsrNotExists_Then_404() throws Exception {

		// Given
		when(lUsrSrv.getLUsrById(lUsrOne)).thenReturn(Optional.empty());

		// When & then
		mockMvc.perform(MockMvcRequestBuilders.delete("/lusrs/" + lUsrOne))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void test_DeleteLUsr_when_LUsrExistsAndDeleteFail_Then_500() throws Exception {

		// Given
		LUserEntity lusrEntity = new LUserEntity();
		lusrEntity.setSystemLUserId(lUsrOne);
		when(lUsrSrv.getLUsrById(lUsrOne)).thenReturn(Optional.of(lusrEntity));
		when(lUsrSrv.deleteLUsr(lUsrOne)).thenReturn(false);

		// When & then
		mockMvc.perform(MockMvcRequestBuilders.delete("/lusrs/" + lUsrOne))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	public void test_DeleteLUsr_when_LUsrExistsAndDeleteSuccess_Then_200() throws Exception {

		// Given
		LUserEntity lusrEntity = new LUserEntity();
		lusrEntity.setSystemLUserId(lUsrOne);
		when(lUsrSrv.getLUsrById(lUsrOne)).thenReturn(Optional.of(lusrEntity));
		when(lUsrSrv.deleteLUsr(lUsrOne)).thenReturn(true);

		// When & then
		mockMvc.perform(MockMvcRequestBuilders.delete("/lusrs/" + lUsrOne))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	// -----------------------------------
	// Test GetLUsrDetails
	// -----------------------------------

	@Test
	public void test_GetLUsrDetails_when_NotCorrectLUsrId_Then_400() throws Exception {

		// When & then
		mockMvc.perform(MockMvcRequestBuilders.get("/lusrs/sds")).andDo(print())
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.applicationErrorCode",
						equalTo(GeneralApplicationErrorCodes.NO_APPLICATION_CODE.getErrorCode())));
	}

	@Test
	public void test_GetLUsrDetails_when_LUsrNotExists_Then_404() throws Exception {

		// Given
		when(lUsrSrv.getLUsrById(lUsrOne)).thenReturn(Optional.empty());

		// When & then
		mockMvc.perform(MockMvcRequestBuilders.get("/lusrs/" + lUsrOne))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void test_GetLUsrDetails_when_InternalError_Then_500() throws Exception {

		// Given
		when(lUsrSrv.getLUsrById(lUsrOne)).thenThrow(new RuntimeException("Get LUser failed with exception"));

		// When & then
		mockMvc.perform(MockMvcRequestBuilders.get("/lusrs/" + lUsrOne)).andDo(print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(MockMvcResultMatchers.jsonPath("$.applicationErrorCode",
						equalTo(GeneralApplicationErrorCodes.NO_APPLICATION_CODE.getErrorCode())));
	}

	@Test
	public void test_GetLUsrDetails_when_LUsrExists_Then_200() throws Exception {

		// Given
		LUserEntity lusrEntity = new LUserEntity();
		lusrEntity.setSystemLUserId(lUsrOne);
		when(lUsrSrv.getLUsrById(lUsrOne)).thenReturn(Optional.of(lusrEntity));

		// When & then
		mockMvc.perform(MockMvcRequestBuilders.get("/lusrs/" + lUsrOne))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	// -----------------------------------
	// Test addPointsForLoyalUserToCachierPoint
	// -----------------------------------

	@Test
	public void test_addPointsForLoyalUserToCachierPoint_when_LUsrExists_Then_200() throws Exception {

		Double points = 10.0;
		// Given
		LUserEntity lusrEntity = new LUserEntity();
		lusrEntity.setSystemLUserId(lUsrOne);
		PointsRelationEntity relation = new PointsRelationEntity();
		relation.setPoints(points);
		CPntEntity cPnt = new CPntEntity();
		cPnt.setSystemCPntId(cPntOne);
		relation.setcPnt(cPnt);
		relation.setlUser(lusrEntity);
		lusrEntity.getlUserPoints().add(relation);

		when(lUsrSrv.addPointsForLoyalUserToCachierPoint(Mockito.anyLong(), Mockito.any(AddPointsRequest.class)))
				.thenReturn(lusrEntity);

		AddPointsRequest newPoints = new AddPointsRequest();
		newPoints.setCashierPntID(cPntOne);
		newPoints.setPoints(points);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.post(String.format("/lusrs/%s/points", lUsrOne))
				.content(objectMapper.writeValueAsString(newPoints)).accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andDo(print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(lusrEntity)));
	}

	@Test
	public void test_addPointsForLoyalUserToCachierPoint_when_EmptyInput_Then_400() throws Exception {

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.post(String.format("/lusrs/%s/points", lUsrOne))
				.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE)).andDo(print())
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.applicationErrorCode",
						equalTo(GeneralApplicationErrorCodes.NO_APPLICATION_CODE.getErrorCode())));
	}

	@Test
	public void test_addPointsForLoyalUserToCachierPoint_when_MissingInputField_Then_400() throws Exception {

		// when & then
		mockMvc.perform(
				MockMvcRequestBuilders.post(String.format("/lusrs/%s/points", lUsrOne)).content("{\"points\":\"15\"}")
						.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andDo(print()).andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.applicationErrorCode",
						equalTo(GeneralApplicationErrorCodes.FIELD_VALIDATION_FAILED.getErrorCode())));
	}

	@Test
	public void test_addPointsForLoyalUserToCachierPoint_when_InvalidField1_Then_400() throws Exception {

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.post(String.format("/lusrs/%s/points", lUsrOne))
				.content("{\"points\":\"15\",\"cashierPntID\":\"WRONG\"}").accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andDo(print())
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.applicationErrorCode",
						equalTo(GeneralApplicationErrorCodes.NO_APPLICATION_CODE.getErrorCode())));
	}

	@Test
	public void test_addPointsForLoyalUserToCachierPoint_when_InvalidField2_Then_400() throws Exception {

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.post(String.format("/lusrs/%s/points", lUsrOne))
				.content("{\"points\":\"-15\",\"cashierPntID\":\"123\"}").accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andDo(print())
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.applicationErrorCode",
						equalTo(GeneralApplicationErrorCodes.FIELD_VALIDATION_FAILED.getErrorCode())));
	}

	@Test
	public void test_addPointsForLoyalUserToCachierPoint_when_exception_Then_500() throws Exception {

		// Given
		Double points = 10.0;

		when(lUsrSrv.addPointsForLoyalUserToCachierPoint(Mockito.anyLong(), Mockito.any(AddPointsRequest.class)))
				.thenThrow(new RuntimeException("Failed to add points to user"));

		AddPointsRequest newPoints = new AddPointsRequest();
		newPoints.setCashierPntID(cPntOne);
		newPoints.setPoints(points);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.post(String.format("/lusrs/%s/points", lUsrOne))
				.content(objectMapper.writeValueAsString(newPoints)).accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andDo(print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(MockMvcResultMatchers.jsonPath("$.applicationErrorCode",
						equalTo(GeneralApplicationErrorCodes.NO_APPLICATION_CODE.getErrorCode())));
	}

}
