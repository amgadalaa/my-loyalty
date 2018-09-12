package com.loyalty.usermanagement.lusr;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loyalty.common.general.exceptions.ResourceNotExistException;
import com.loyalty.usermanagement.entities.LUsrEntity;
import com.loyalty.usermanagement.services.systemusers.SystemUsersServiceImpl;

@Service
public class LUsrService extends SystemUsersServiceImpl<LUsrEntity> {

	@Autowired
	private LUserRepo repo;

	@Override
	public List<LUsrEntity> getAll() {
		List<LUsrEntity> list = new ArrayList<>();
		repo.findAll().forEach(list::add);
		return list;
	}

	@Override
	public LUsrEntity getById(final Integer id) throws ResourceNotExistException {
		return repo.findById(id)
				.orElseThrow(() -> new ResourceNotExistException(String.format("LUsr: %s not found", id)));
	}
}
