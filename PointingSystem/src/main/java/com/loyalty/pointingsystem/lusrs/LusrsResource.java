package com.loyalty.pointingsystem.lusrs;

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
import com.loyalty.pointingsystem.entities.CPntEntity;
import com.loyalty.pointingsystem.entities.LUserEntity;
import com.loyalty.pointingsystem.entities.PointsRelationEntity;
import com.loyalty.pointingsystem.lusrs.dto.AddPointsRequest;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Loggable
@RestController
@RequestMapping("/lusrs")
public class LusrsResource {

	private Logger log = LoggerFactory.getLogger(getClass());

	// @Autowired
	// private LUserPointsRepo luserRepo;
	//
	// @Autowired
	// private LUserPointsRelationRepo lUserPointsRelationRepo;

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
			if (lUsrSrv.deleteUsr(lUsrId)) {
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
	 * @param lUsrId the LUsr to find.
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

		// ToDO check can be used for negative value as well?
		// TODO unit tests
		Double toAddPoints = newPoints.getPoints();
		Long relatedCpnt = newPoints.getCashierPntID();

		Optional<LUserEntity> lUsrOptional = lUsrSrv.getLUsrById(lUsrId);
		LUserEntity lUsr = null;

		CPntEntity cPnt = new CPntEntity();
		cPnt.setSystemCPntId(relatedCpnt);

		if (lUsrOptional.isPresent()) {
			lUsr = lUsrOptional.get();
			// Check relation exists or not with Cpnts
			log.debug("Check relation exists or not with Cpnts");
			Optional<PointsRelationEntity> foundRelationOptional = lUsr.getlUserPoints().stream()
					.filter(relation -> relation.getcPnt().getSystemCPntId() == newPoints.getCashierPntID().longValue())
					.findFirst();
			// if exists
			if (foundRelationOptional.isPresent()) {

				// then update
				log.debug("Relation exists, updating");
				PointsRelationEntity foundRelation = foundRelationOptional.get();
				foundRelation.addPoints(toAddPoints);

			} else {

				// create new relation for existing lusr
				log.debug("Relation doesn't exist, creating new relation");
				createRelationInLUsr(lUsr, cPnt, toAddPoints);
			}

		} else {
			// new usr to create and new relation should be created
			log.debug("LUsr doesn't exist, creating usr with its relation");
			lUsr = new LUserEntity();
			lUsr.setSystemLUserId(lUsrId);
			createRelationInLUsr(lUsr, cPnt, toAddPoints);
		}

		lUsr = lUsrSrv.saveLusr(lUsr);

		return ResponseEntity.ok(lUsr);
	}

	// @RequestMapping(path = "/{lUserId}/cPnt/{cPntId}/points/{points}/",
	// method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	// public void addPointsForLoyalUserToCachierPoint(@PathVariable("points")
	// double points,
	// @PathVariable("lUserId") long lUserId, @PathVariable("cPntId") long
	// cPntId) {
	// // ResponseEntity<>
	// log.info("Received request to add {} points for {} user to {} shop",
	// points, lUserId, cPntId);
	//
	// // load Luser data
	// // Optional<> = luserResp.findById(lUserId);
	// // if not exist create new
	//
	// // else update
	//
	// CPntEntity cpnt = new CPntEntity();
	// cpnt.setSystemCPntId(cPntId);
	//
	// LUserEntity lUser = new LUserEntity();
	// lUser.setSystemLUserId(lUserId);
	// lUser.setUserName("sds123");
	//
	// PointsRelationEntity pointsRelation = new PointsRelationEntity();
	// pointsRelation.setcPnt(cpnt);
	// pointsRelation.setlUser(lUser);
	// pointsRelation.setPoints(points);
	// pointsRelation.setInternalId(Long.parseLong(lUserId + "" + cPntId));
	// lUser.getlUserPoints().add(pointsRelation);
	// // lUser.setlUserPoints(pointsRelation);
	// //
	// pointsRelation = lUserPointsRelationRepo.save(pointsRelation);
	// // luserRepo.save(lUser);
	//
	// Optional<PointsRelationEntity> relation = lUserPointsRelationRepo
	// .findByinternalIdEquals(Long.parseLong(lUserId + "" + cPntId));
	// log.info("found relation: {}", relation.get());
	//
	// }

	private PointsRelationEntity createRelationInLUsr(LUserEntity lUsr, CPntEntity cPnt, Double toAddPoints) {

		PointsRelationEntity newRelationToCpnt = new PointsRelationEntity();
		newRelationToCpnt.setcPnt(cPnt);
		newRelationToCpnt.setInternalId(String.format("%s_%s", lUsr.getSystemLUserId(), cPnt.getSystemCPntId()));
		newRelationToCpnt.setlUser(lUsr);
		newRelationToCpnt.setPoints(toAddPoints);
		lUsr.getlUserPoints().add(newRelationToCpnt);

		return newRelationToCpnt;

	}

}
