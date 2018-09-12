package com.loyalty.usermanagement.services.systemusers;


import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.loyalty.usermanagement.entities.SystemUsers;


public interface SystemUsersRepo  extends CrudRepository<SystemUsers, Integer> {

	Optional<SystemUsers> findByEmailIsAndIsDeletedFalse(final String email);
	
}
