package com.loyalty.usermanagement.cadm;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loyalty.common.general.exceptions.ResourceNotExistException;
import com.loyalty.usermanagement.entities.CAdmEntity;
import com.loyalty.usermanagement.services.systemusers.SystemUsersServiceImpl;

@Service
public class CAdmService extends SystemUsersServiceImpl<CAdmEntity>  {

	@Autowired
	private CAdmRepo cAdmRepo;

	@Override
	public List<CAdmEntity> getAll() {
		return cAdmRepo.getAll();
	}

	@Override
	public CAdmEntity getById(Integer id) throws ResourceNotExistException {
		return cAdmRepo.findById(id)
				.orElseThrow(() -> new ResourceNotExistException(String.format("CAdm: %s not found", id)));
	}

}
