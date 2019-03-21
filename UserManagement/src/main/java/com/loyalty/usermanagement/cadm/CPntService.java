package com.loyalty.usermanagement.cadm;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loyalty.common.general.exceptions.ResourceAlreadyExists;
import com.loyalty.common.general.exceptions.ResourceNotExistException;
import com.loyalty.common.general.exceptions.ValidationException;
import com.loyalty.usermanagement.entities.CAdmEntity;
import com.loyalty.usermanagement.entities.CpntEntity;
import com.loyalty.usermanagement.services.systemusers.SystemUsersServiceImpl;

@Service
public class CPntService {

	@Autowired
	private CAdmService cAdmServ;

	@Autowired
	private CPntRepo cPntRepo;

	/**
	 * 
	 * @param cAdmId
	 *            the Id of the CAdm in which the new Cpnt will be added
	 * @param newCPnt
	 *            the new Cpnt to be added
	 * @return {@code CAdmEntity} modified after adding new cpnt
	 * @throws ResourceNotExistException
	 *             in case {@code CAdmEntity} not exists for send {@code cAdmId}
	 * @throws ResourceAlreadyExists
	 *             in case {@code CpntEntity} already exists for {@code cAdmId}
	 */
	public CpntEntity addCpnt(Integer cAdmId, @Valid CpntEntity newCPnt)
			throws ResourceNotExistException, ResourceAlreadyExists {
		// get related CAdm by id
		CAdmEntity parentCAdm = getById(cAdmId);

		Set<CpntEntity> existingCPnts = parentCAdm.getCpnts();
		// make sure new cpnt doesn't exist already
		String newCpntName = newCPnt.getName();
		boolean cpntExists = existingCPnts.stream()
				.anyMatch((cpnt) -> !cpnt.isIsDeleted() && cpnt.getName().trim().equalsIgnoreCase(newCpntName));
		if (cpntExists) {
			throw new ResourceAlreadyExists(
					String.format("CPnt with name %s already exists for CAdm with Id: %d", newCpntName, cAdmId));
		}

		// add to DB
		newCPnt.setSystemUsers(parentCAdm);
		newCPnt.setIsEnabled(true);
		newCPnt.setIsDeleted(false);
		newCPnt = cPntRepo.save(newCPnt);
		return newCPnt;
	}

	public CpntEntity editCpnt(final Integer cAdmId, final CpntEntity toEditCPnt)
			throws ResourceNotExistException, ResourceAlreadyExists, ValidationException {

		// get related CAdm by id
		CAdmEntity parentCAdm = getById(cAdmId);

		// Get existing CPnt
		Integer toEditCpntId = toEditCPnt.getCpntId();
		if (toEditCpntId == null) {
			throw new ValidationException("Id field can't be null for edit operation", null);
		}

		Set<CpntEntity> existingCPnts = parentCAdm.getCpnts();
		Optional<CpntEntity> existingCpntOptional = existingCPnts.stream()
				.filter((cpnt) -> !cpnt.isIsDeleted() && cpnt.getCpntId().equals(toEditCpntId)).findFirst();

		CpntEntity existingCpnt = existingCpntOptional.orElseThrow(() -> new ResourceNotExistException(
				String.format("CPnt with Id %d not exists for CAdm with Id: %d", toEditCpntId, cAdmId)));

		// Check no Cpnt with the same new name
		String newCpntName = toEditCPnt.getName();
		boolean cpntExists = existingCPnts.stream().anyMatch((cpnt) -> cpnt != existingCpnt && !cpnt.isIsDeleted()
				&& cpnt.getName().trim().equalsIgnoreCase(newCpntName));
		if (cpntExists) {
			throw new ResourceAlreadyExists(
					String.format("CPnt with name %s already exists for CAdm with Id: %d", newCpntName, cAdmId));
		}

		existingCpnt.setName(toEditCPnt.getName());
		existingCpnt.setFullAddress(toEditCPnt.getFullAddress());
		existingCpnt.setIsEnabled(toEditCPnt.isIsEnabled());

		// save
		return cPntRepo.save(existingCpnt);
	}

	public void deleteCpnt(int cAdmId, int cPntId) throws ResourceNotExistException, ValidationException {
		// get related CAdm by id
		CAdmEntity parentCAdm = getById(cAdmId);

		Set<CpntEntity> existingCPnts = parentCAdm.getCpnts();
		Optional<CpntEntity> existingCpntOptional = existingCPnts.stream()
				.filter((cpnt) -> !cpnt.isIsDeleted() && cpnt.getCpntId().equals(cPntId)).findFirst();

		CpntEntity existingCpnt = existingCpntOptional.orElseThrow(() -> new ResourceNotExistException(
				String.format("CPnt with Id %d not exists for CAdm with Id: %d", cPntId, cAdmId)));

		// check if already deleted
		if (existingCpnt.isIsDeleted()) {
			throw new ValidationException(String.format("Cpnt with Id: %d is already deleted", cPntId), null);
		}

		existingCpnt.setIsDeleted(true);
		cPntRepo.save(existingCpnt);
	}

	private CAdmEntity getById(int cAdmId) throws ResourceNotExistException {
		return cAdmServ.getById(cAdmId);
	}

}
