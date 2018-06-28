package com.loyalty.pointingsystem.lusrs;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.loyalty.common.general.responses.models.ErrorResponse;
import com.loyalty.common.general.responses.models.GeneralApplicationErrorCodes;
import com.loyalty.common.spring.logging.Loggable;
import com.loyalty.pointingsystem.entities.LUserEntity;
import com.loyalty.pointingsystem.lusrs.dto.AddPointsRequest;

@Loggable
@RestController
@RequestMapping("/lusrs")
public class LusrsResource {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private ILUsrsService lUsrSrv;

	/**
	 * End point to delete LUsr also all his relations to CPnts will be deleted
	 * as well.
	 * 
	 * @param lUsrId
	 *            the LUsr to delete
	 * @return {@link ResponseEntity} which represents the status of the
	 *         request.
	 */
	@RequestMapping(path = "/{lUserId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteLUsr(@PathVariable(name = "lUserId", required = true) Long lUsrId) {
		Optional<LUserEntity> lUsrOptional = lUsrSrv.getLUsrById(lUsrId);

		if (lUsrOptional.isPresent()) {
			if (lUsrSrv.deleteLUsr(lUsrId)) {
				return ResponseEntity.ok().build();
			} else {
				ErrorResponse customError = new ErrorResponse(INTERNAL_SERVER_ERROR.value(),
						INTERNAL_SERVER_ERROR.getReasonPhrase(), GeneralApplicationErrorCodes.NO_APPLICATION_CODE,
						String.format("Failed to delete LUser %s", lUsrId));

				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customError);
			}
		} else {
			ErrorResponse customError = new ErrorResponse(NOT_FOUND.value(), NOT_FOUND.getReasonPhrase(),
					GeneralApplicationErrorCodes.NO_APPLICATION_CODE, String.format("LUser %s not found", lUsrId));

			return ResponseEntity.status(NOT_FOUND).body(customError);
		}
	}

	/*
	 * Endpoint to find LUsr and return his details.
	 * 
	 * @param lUsrId the LUsr to find.
	 * 
	 * @return {@link ResponseEntity} which represents the status of the
	 * request.
	 */
	@RequestMapping(path = "/{lUserId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getLUsrDetails(@PathVariable(name = "lUserId", required = true) Long lUsrId) {

		Optional<LUserEntity> lUsrOptional = lUsrSrv.getLUsrById(lUsrId);
		if (lUsrOptional.isPresent()) {
			return ResponseEntity.ok(lUsrOptional.get());
		} else {
			ErrorResponse customError = new ErrorResponse(NOT_FOUND.value(), NOT_FOUND.getReasonPhrase(),
					GeneralApplicationErrorCodes.NO_APPLICATION_CODE, String.format("LUser %s not found", lUsrId));

			return ResponseEntity.status(NOT_FOUND).body(customError);

		}
	}

	/**
	 * The end point that is responsible for adding newly acquired points to
	 * LUser's wallet.
	 * 
	 * @param lUsrId
	 *            the Id of the LUsr which points will be added to his wallet
	 * @param newPoints
	 *            the points to add
	 * @return {@link ResponseEntity} which represents the status of the
	 *         request. In case of success it should contain {@link LUserEntity}
	 *         in the body, in case of error the body will contain
	 *         {@link ErrorResponse}
	 */
	@RequestMapping(path = "/{lUserId}/points/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addPointsForLoyalUserToCachierPoint(
			@PathVariable(name = "lUserId", required = true) Long lUsrId,
			@Valid @RequestBody AddPointsRequest newPoints) {

		// TODO unit tests
		// ToDO check can be used for negative value as well?
		LUserEntity lUsr = lUsrSrv.addPointsForLoyalUserToCachierPoint(lUsrId, newPoints);
		return ResponseEntity.ok(lUsr);
	}

}
