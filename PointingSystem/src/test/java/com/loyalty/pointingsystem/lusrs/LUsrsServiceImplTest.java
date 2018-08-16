package com.loyalty.pointingsystem.lusrs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import com.loyalty.pointingsystem.entities.CPntEntity;
import com.loyalty.pointingsystem.entities.LUserEntity;
import com.loyalty.pointingsystem.entities.PointsRelationEntity;
import com.loyalty.pointingsystem.lusrs.dto.AddPointsRequest;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@ContextConfiguration(classes = LUsrsServiceImpl.class)
@RunWith(JUnitParamsRunner.class)
public class LUsrsServiceImplTest {

	@ClassRule
	public static final SpringClassRule springClassRule = new SpringClassRule();

	@Rule
	public final SpringMethodRule springMethodRule = new SpringMethodRule();

	@Autowired
	private LUsrsServiceImpl serviceImpl;

	@MockBean
	private LUserPointsRepo lUserPointsRepo;

	@Test(expected = IllegalArgumentException.class)
	public void test_getLUsrById_When_LUsrNull_then_ThrowException() {

		// Given
		when(lUserPointsRepo.findById(Mockito.isNull())).thenThrow(new IllegalArgumentException("Null argument"));
		// When
		serviceImpl.getLUsrById(null);
	}

	@Test
	public void test_getLUsrById_When_LUsrExists_thenReturnLUsr() {

		// Given
		Long lUsrId = 1L;
		LUserEntity lUsr = new LUserEntity();
		lUsr.setSystemLUserId(lUsrId);
		when(lUserPointsRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(lUsr));

		// When
		Optional<LUserEntity> optionalLUsr = serviceImpl.getLUsrById(lUsrId);

		// Then
		assertTrue("No LUsr returned", optionalLUsr.isPresent());
		assertEquals("Returned LUsr is not correct", lUsrId, optionalLUsr.get().getSystemLUserId());
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_saveLUsrById_When_LUsrNull_then_ThrowException() {

		// Given
		when(lUserPointsRepo.save(Mockito.isNull())).thenThrow(new IllegalArgumentException("Null argument"));
		// When
		serviceImpl.saveLusr(null);
	}

	@Test
	public void test_saveLUsrById_When_LUsrGiven_thenReturnLUsr() {

		// Given
		Long lUsrId = 1L;
		LUserEntity lUsr = new LUserEntity();
		lUsr.setSystemLUserId(lUsrId);
		when(lUserPointsRepo.save(Mockito.any(LUserEntity.class))).thenReturn(lUsr);

		// When
		LUserEntity lUserSaved = serviceImpl.saveLusr(lUsr);

		// Then
		assertEquals("Returned LUsr is not correct", lUsrId, lUserSaved.getSystemLUserId());
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_deleteLUsrById_When_LUsrNull_then_ThrowException() {

		// Given
		doThrow(new IllegalArgumentException("Null argument")).when(lUserPointsRepo).deleteById(Mockito.isNull());
		// When
		serviceImpl.deleteLUsr(null);
	}

	@Test
	public void test_deleteLUsrById_When_LUsrExists_thenReturnTrue() {

		// Given
		Long lUsrId = 1L;
		// LUserEntity lUsr = new LUserEntity();
		// lUsr.setSystemLUserId(lUsrId);
		// when(lUserPointsRepo.save(Mockito.any(LUserEntity.class))).thenReturn(lUsr);

		// When
		Boolean result = serviceImpl.deleteLUsr(lUsrId);

		// Then
		assertTrue("Can't delete LUsr", result);
	}

	@Test
	@Parameters(method = "provideCasesFor_test_addPointsForLoyalUserToCachierPoint")
	public void test_addPointsForLoyalUserToCachierPoint(Long InLUsrId, AddPointsRequest InNewPoints,
			Optional<LUserEntity> mockLUsrInDB, Double resultPoints, String testName, int numberOfRelations) {
		// Given
		Long inCPnt = InNewPoints.getCashierPntID();

		// Mock from DB
		when(lUserPointsRepo.findById(Mockito.anyLong())).thenReturn(mockLUsrInDB);
		// Return input as output when calling save
		when(lUserPointsRepo.save(Mockito.any(LUserEntity.class))).thenAnswer(i -> i.getArguments()[0]);

		// When LUserEntity savedLUsr =
		LUserEntity savedLUsr = serviceImpl.addPointsForLoyalUserToCachierPoint(InLUsrId, InNewPoints);

		// Then
		// verify points in relation with CPnts
		long countOfRecordsWithCorrectResult = savedLUsr.getlUserPoints().stream().filter(
				x -> (x.getcPnt().getSystemCPntId() == inCPnt.longValue()) && x.getPoints().equals(resultPoints))
				.count();
		assertTrue(testName + ": Wrong result returned:" + savedLUsr, countOfRecordsWithCorrectResult == 1);
		assertTrue(testName + ": Wrong number of relations:", savedLUsr.getlUserPoints().size() == numberOfRelations);
	}

	private Object[] provideCasesFor_test_addPointsForLoyalUserToCachierPoint() {
		Long lUsrId = 1L;
		Long cPntId = 11L;
		Long otherCPntId = 12L;
		AddPointsRequest inNewPoints = null;
		List<Object> oneCase = null;
		List<Object> casesList = new ArrayList<>();

		// Case #1
		oneCase = new ArrayList<>();
		oneCase.add(lUsrId); // LusrId
		inNewPoints = new AddPointsRequest();
		inNewPoints.setCashierPntID(cPntId);
		inNewPoints.setPoints(12.0);
		oneCase.add(inNewPoints); // InNewPoints
		oneCase.add(Optional.empty()); // mockLUsrInDB
		oneCase.add(12.0); // resultPoints
		oneCase.add("LUser and relation deson't exist");
		oneCase.add(1); // number of relations

		casesList.add(oneCase.toArray());

		// Case #2
		oneCase = new ArrayList<>();
		oneCase.add(lUsrId); // LusrId
		inNewPoints = new AddPointsRequest();
		inNewPoints.setCashierPntID(cPntId);
		inNewPoints.setPoints(12.0);
		oneCase.add(inNewPoints); // InNewPoints
		oneCase.add(Optional.of(constructLUserEntity(lUsrId, cPntId, 10.0))); // mockLUsrInDB
		oneCase.add(22.0); // resultPoints
		oneCase.add("LUser exists and relation exists");
		oneCase.add(1); // number of relations

		casesList.add(oneCase.toArray());

		// Case #3
		oneCase = new ArrayList<>();
		oneCase.add(lUsrId); // LusrId
		inNewPoints = new AddPointsRequest();
		inNewPoints.setCashierPntID(cPntId);
		inNewPoints.setPoints(12.0);
		oneCase.add(inNewPoints); // InNewPoints
		oneCase.add(Optional.of(constructLUserEntity(lUsrId, otherCPntId, 10.0))); // mockLUsrInDB
		oneCase.add(12.0); // resultPoints
		oneCase.add("LUser exists but relation doesn't");
		oneCase.add(2); // number of relations

		casesList.add(oneCase.toArray());

		return casesList.toArray();
	}

	private LUserEntity constructLUserEntity(Long lUsrId, Long cPntId, Double points) {
		LUserEntity mockLUsrInDB = new LUserEntity();
		mockLUsrInDB.setSystemLUserId(lUsrId);

		PointsRelationEntity relation = new PointsRelationEntity();
		relation.setPoints(points);
		CPntEntity cPnt = new CPntEntity();
		cPnt.setSystemCPntId(cPntId);
		relation.setcPnt(cPnt);
		relation.setlUser(mockLUsrInDB);

		mockLUsrInDB.getlUserPoints().add(relation);

		return mockLUsrInDB;

	}
}
