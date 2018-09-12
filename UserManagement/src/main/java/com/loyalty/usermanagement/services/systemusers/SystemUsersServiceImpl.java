package com.loyalty.usermanagement.services.systemusers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loyalty.common.general.exceptions.ResourceAlreadyExists;
import com.loyalty.common.general.exceptions.ResourceNotExistException;
import com.loyalty.common.general.exceptions.ValidationException;
import com.loyalty.usermanagement.entities.SystemUsers;

@Service
public abstract class SystemUsersServiceImpl<T extends SystemUsers> implements SystemUsersService<T> {

	@Autowired
	private SystemUsersRepo repo;

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void deleteById(final Integer id) throws ResourceNotExistException {
		// check if exists
		getUsers(id);

		// if exists the delete
		repo.deleteById(id);

		log.info("LUser with Id: {} deleted", id);
		// TODO send notification to other systems
	}

	private SystemUsers getUsers(final Integer id) throws ResourceNotExistException {
		// TODO get only non deleted
		SystemUsers foundLusr = repo.findById(id)
				.orElseThrow(() -> new ResourceNotExistException(String.format("User: %s not found", id)));
		return foundLusr;
	}

	@Override
	public T addUser(final T newLusr) throws ResourceAlreadyExists {

		String email = newLusr.getEmail();
		// Make sure it doesn't exists
		// make sure email is unique
		Optional<SystemUsers> existingEntityOptional = repo.findByEmailIsAndIsDeletedFalse(email);
		if (existingEntityOptional.isPresent()) {
			log.debug("Lusr with Email: {} already exists", email);
			throw new ResourceAlreadyExists(String.format("User with Email: %s already exists", email));
		}

		// add
		newLusr.setIsDeleted(false);
		newLusr.setIsEnabled(true);
		newLusr.setUserId(null);
		SystemUsers createdEntity = repo.save(newLusr);

		log.info("new LUser with Id: {} and email: {} added, details: {}", createdEntity.getUserId(), email,
				createdEntity);
		return (T) createdEntity;
	}

	@Override
	public T editUser(final Integer id, final T editLusr) throws ResourceNotExistException, ValidationException {

		Integer modelId = editLusr.getUserId();

		if (!id.equals(modelId)) {
			// Mismatched ID found
			log.debug("Mismatched Id received , {} != {}", id, modelId);
			throw new ValidationException(String.format("Mismatched Id received , %d != %d", id, modelId), null);
		}

		SystemUsers foundLusr = repo.findById(modelId)
				.orElseThrow(() -> new ResourceNotExistException(String.format("Luser: %s not found", modelId)));

		// can't change: email use appropriate endpoint
		String originalMail = foundLusr.getEmail();
		String toEditUserMail = editLusr.getEmail();
		if (!originalMail.equalsIgnoreCase(toEditUserMail)) {
			log.debug("LUser with Id: {} failed to edit, mail can't be changed using edit", id);

			throw new ValidationException(
					String.format("Mail can't be changed using edit , new email %s", toEditUserMail), null);
		}

		foundLusr.setFullName(editLusr.getFullName());
		foundLusr.setPassword(editLusr.getPassword());
		foundLusr.setUsername(editLusr.getUsername());
		foundLusr.setIsEnabled(editLusr.isIsEnabled());

		foundLusr = repo.save(foundLusr);

		log.info("LUser with Id: {} edited, details before: {} and after", id, foundLusr, editLusr);

		return (T) foundLusr;
	}
}
