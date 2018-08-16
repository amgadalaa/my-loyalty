package com.loyalty.pointingsystem.cpnt;

import com.loyalty.common.general.exceptions.ResourceAlreadyExists;
import com.loyalty.common.general.exceptions.ResourceNotExistException;
import com.loyalty.common.general.exceptions.ValidationException;
import com.loyalty.common.general.messaging.dto.CpntMessagingDto;
import com.loyalty.pointingsystem.entities.CPntEntity;

public interface ICPntService {

	boolean isCPntExists(Long cPntId);

	CPntEntity insertNewCPnt(CpntMessagingDto toInsert) throws ValidationException, ResourceAlreadyExists;
	
	CPntEntity updateCPnt(CpntMessagingDto toupdate) throws  ResourceNotExistException;
	
	boolean isValidDto(CpntMessagingDto object) throws ValidationException;

	void deleteCpntById(Long cPntId) throws ResourceNotExistException;
}
