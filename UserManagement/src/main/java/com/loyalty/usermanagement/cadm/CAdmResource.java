package com.loyalty.usermanagement.cadm;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.loyalty.common.general.exceptions.ResourceAlreadyExists;
import com.loyalty.common.general.exceptions.ResourceNotExistException;
import com.loyalty.common.general.exceptions.ValidationException;
import com.loyalty.common.spring.logging.Loggable;
import com.loyalty.usermanagement.entities.CAdmEntity;

/**
 * 
 * @author eamgmuh This class provide the endpoints needed to manipulate
 *         {@code CAdmEntity}
 */
@Loggable
@RestController
@RequestMapping("/cadms")
public class CAdmResource {

	@Autowired
	private CAdmService cadmService;

	@RequestMapping(path = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAll() {
		List<CAdmEntity> allLUsrs = cadmService.getAll();
		return ResponseEntity.ok(allLUsrs);
	}

	@RequestMapping(path = "/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getuser(@PathVariable(name = "userId") int lUserId) throws ResourceNotExistException {
		CAdmEntity foundLusr = cadmService.getById(lUserId);
		return ResponseEntity.ok(foundLusr);
	}

	@RequestMapping(path = "/{userId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deletetUser(@PathVariable(name = "userId") int lUserId) throws ResourceNotExistException {
		cadmService.deleteById(lUserId);
		return ResponseEntity.ok().build();
	}

	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addUser(@Valid @RequestBody final CAdmEntity newLusr) throws ResourceAlreadyExists {
		CAdmEntity addedLusr = cadmService.addUser(newLusr);
		return ResponseEntity.ok(addedLusr);
	}

	@RequestMapping(path = "/{userId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> editUser(@PathVariable(name = "userId") int lUserId,
			@Valid @RequestBody final CAdmEntity editLusr) throws ResourceNotExistException, ValidationException {
		CAdmEntity editedLusr = cadmService.editUser(lUserId, editLusr);
		return ResponseEntity.ok(editedLusr);
	}

}
