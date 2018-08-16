package com.loyalty.pointingsystem.cpnt;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loyalty.common.general.exceptions.ResourceAlreadyExists;
import com.loyalty.common.general.exceptions.ResourceNotExistException;
import com.loyalty.common.general.exceptions.ValidationException;
import com.loyalty.common.general.messaging.dto.CpntMessagingDto;
import com.loyalty.common.spring.validators.CpntMessagingDtoValidator;
import com.loyalty.pointingsystem.entities.CPntEntity;

@Service
public class CPntService implements ICPntService {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private CpntRepo cpntRepo;

	@Autowired
	private CpntMessagingDtoValidator validator;

	@Override
	public boolean isCPntExists(Long cPntId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void deleteCpntById(Long cPntId) throws ResourceNotExistException {
		// check if exists
		Optional<CPntEntity> existingCpnt = cpntRepo.findById(cPntId);
		if (!existingCpnt.isPresent()) {
			throw new ResourceNotExistException("Entity does not exists");
		} else {
			// delete
			cpntRepo.deleteById(cPntId);
		}
	}

	@Override
	public CPntEntity insertNewCPnt(final CpntMessagingDto toInsert) throws ValidationException, ResourceAlreadyExists {

		// validate input
		isValidDto(toInsert);

		Long toInsertCPntSystemId = toInsert.getSystemCPntId();
		String toInsertCPntName = toInsert.getcPntName();

		// make sure doesn't not exists
		Optional<CPntEntity> existingEntity = cpntRepo.findById(toInsertCPntSystemId);
		if (existingEntity.isPresent()) {
			throw new ResourceAlreadyExists(
					String.format("Cpnt entity %s with Id:%s already exists", toInsertCPntName, toInsertCPntSystemId));
		}

		// insert
		CPntEntity newCpnt = new CPntEntity();
		newCpnt.setSystemCPntId(toInsert.getSystemCPntId());
		newCpnt.setcPntName(toInsert.getcPntName());

		newCpnt = cpntRepo.save(newCpnt);
		return newCpnt;
	}

	public boolean isValidDto(CpntMessagingDto object) throws ValidationException {

		List<String> violations = validator.isValid(object);
		if (violations.isEmpty()) {
			// No violation found
			return true;
		} else {
			throw new ValidationException("Violations found while validating CPnt DTO", violations);
		}
	}

	@Override
	public CPntEntity updateCPnt(CpntMessagingDto toupdate) throws ResourceNotExistException {

		// check if exists
		Optional<CPntEntity> existingCpnt = cpntRepo.findById(toupdate.getSystemCPntId());
		// update
		CPntEntity existingCpntEntity = existingCpnt
				.orElseThrow(() -> new ResourceNotExistException("Entity does not exists"));

		existingCpntEntity.setcPntName(toupdate.getcPntName());
		existingCpntEntity = cpntRepo.save(existingCpntEntity);
		return existingCpntEntity;
	}

}
