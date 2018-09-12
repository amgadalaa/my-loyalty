package com.loyalty.usermanagement.services.systemusers;

import java.util.List;

import com.loyalty.common.general.exceptions.ResourceAlreadyExists;
import com.loyalty.common.general.exceptions.ResourceNotExistException;
import com.loyalty.common.general.exceptions.ValidationException;
import com.loyalty.usermanagement.entities.SystemUsers;

public interface SystemUsersService<T extends SystemUsers> {

	List<T> getAll();

	T getById(final Integer id) throws ResourceNotExistException;

	void deleteById(Integer id) throws ResourceNotExistException;

	T addUser(T newLusr) throws ResourceAlreadyExists;

	T editUser(Integer id, T editusr)
			throws ResourceNotExistException, ValidationException;
	
}
