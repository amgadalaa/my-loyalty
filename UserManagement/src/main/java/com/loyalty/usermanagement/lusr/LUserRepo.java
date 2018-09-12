package com.loyalty.usermanagement.lusr;

import org.springframework.data.repository.CrudRepository;

import com.loyalty.usermanagement.entities.LUsrEntity;

public interface LUserRepo extends CrudRepository<LUsrEntity, Integer> {
	
}
