package com.loyalty.pointingsystem.cpnt;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.loyalty.common.general.exceptions.ResourceAlreadyExists;
import com.loyalty.common.general.exceptions.ResourceNotExistException;
import com.loyalty.common.general.exceptions.ValidationException;
import com.loyalty.common.general.messaging.dto.CpntMessagingDto;
import com.loyalty.common.spring.validators.CpntMessagingDtoValidator;
import com.loyalty.pointingsystem.entities.CPntEntity;

@ContextConfiguration(classes = CPntService.class)
@RunWith(SpringRunner.class)
public class CPntServiceTest {

	@Autowired
	private CPntService cPntService;

	@MockBean
	private CpntRepo cpntRepo;

	@MockBean
	private CpntMessagingDtoValidator validator;

	@Test(expected = ValidationException.class)
	public void test_insertNewCPnt_when_invalidDto_Then_ValidationException()
			throws ValidationException, ResourceAlreadyExists {
		// Given
		when(validator.isValid(Mockito.any(CpntMessagingDto.class))).thenReturn(Collections.singletonList("Not valid"));
		CpntMessagingDto toInsert = new CpntMessagingDto();

		// When
		cPntService.insertNewCPnt(toInsert);
	}

	@Test(expected = ResourceAlreadyExists.class)
	public void test_insertNewCPnt_when_CpntExists_Then_ResourceAlreadyExistsException()
			throws ValidationException, ResourceAlreadyExists {
		// Given
		long cPntId = 123;
		CpntMessagingDto toInsert = new CpntMessagingDto(cPntId, "cpntName");
		when(validator.isValid(Mockito.any(CpntMessagingDto.class))).thenReturn(Collections.emptyList());
		when(cpntRepo.findById(cPntId)).thenReturn(Optional.of(new CPntEntity()));

		// When
		cPntService.insertNewCPnt(toInsert);
	}

	public void test_insertNewCPnt_when_NewCpnt_Then_insert() throws ValidationException, ResourceAlreadyExists {
		// Given
		long cPntId = 123;
		CPntEntity cPntEntity = new CPntEntity();
		cPntEntity.setSystemCPntId(cPntId);
		cPntEntity.setcPntName("123");
		CpntMessagingDto toInsert = new CpntMessagingDto(cPntId, "cpntName");
		when(validator.isValid(Mockito.any(CpntMessagingDto.class))).thenReturn(Collections.emptyList());
		when(cpntRepo.findById(cPntId)).thenReturn(Optional.empty());
		when(cpntRepo.save(cPntEntity)).thenReturn(cPntEntity);
		// When
		CPntEntity insertedEntity = cPntService.insertNewCPnt(toInsert);

		// Then
		Assert.assertEquals("Cpnt was not inserted correctly", toInsert, insertedEntity);
	}

	@Test(expected = ResourceNotExistException.class)
	public void test_updateCPnt_when_CpntNotExists_Then_ResourceNotExistException() throws ResourceNotExistException {
		// Given
		long cPntId = 123;
		CpntMessagingDto toUpdate = new CpntMessagingDto(cPntId, "cpntName");
		when(cpntRepo.findById(cPntId)).thenReturn(Optional.empty());

		// When
		cPntService.updateCPnt(toUpdate);
	}

	@Test
	public void test_updateCPnt_when_Exists_Then_update() throws ResourceNotExistException {
		// Given
		long cPntId = 123;
		CPntEntity cPntEntity = new CPntEntity();
		cPntEntity.setSystemCPntId(cPntId);
		cPntEntity.setcPntName("123");
		CpntMessagingDto toUpdate = new CpntMessagingDto(cPntId, "cpntName");
		when(cpntRepo.findById(cPntId)).thenReturn(Optional.of(cPntEntity));
		// return as it is
		when(cpntRepo.save(Mockito.any())).then(i -> i.getArgument(0));

		// When
		CPntEntity insertedEntity = cPntService.updateCPnt(toUpdate);

		// Then
		Assert.assertEquals("Cpnt was not updated correctly", toUpdate.getcPntName(), insertedEntity.getcPntName());
	}

	@Test(expected = ResourceNotExistException.class)
	public void test_deleteCPnt_when_CpntNotExists_Then_ResourceNotExistException() throws ResourceNotExistException {
		// Given
		long cPntId = 123;
		when(cpntRepo.findById(cPntId)).thenReturn(Optional.empty());

		// When
		cPntService.deleteCpntById(cPntId);
	}

	@Test
	public void test_deleteCPnt_when_Exists_Then_delete() throws ResourceNotExistException {
		// Given
		long cPntId = 123;
		CPntEntity cPntEntity = new CPntEntity();
		cPntEntity.setSystemCPntId(cPntId);
		cPntEntity.setcPntName("123");
		when(cpntRepo.findById(cPntId)).thenReturn(Optional.of(cPntEntity));

		// When
		cPntService.deleteCpntById(cPntId);

		// Then
		verify(cpntRepo, Mockito.times(1)).deleteById(cPntId);
	}

}
