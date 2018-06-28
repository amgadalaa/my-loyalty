package com.loyalty.pointingsystem.lusrs;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loyalty.pointingsystem.entities.CPntEntity;
import com.loyalty.pointingsystem.entities.LUserEntity;
import com.loyalty.pointingsystem.entities.PointsRelationEntity;
import com.loyalty.pointingsystem.lusrs.dto.AddPointsRequest;

@Service
public class LUsrsServiceImpl implements ILUsrsService {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private LUserPointsRepo lUserPointsRepo;

	@Override
	public Optional<LUserEntity> getLUsrById(Long lUsrId) throws IllegalArgumentException{
		return lUserPointsRepo.findById(lUsrId);
	}

	@Override
	public LUserEntity saveLusr(LUserEntity toSaveUsr) throws IllegalArgumentException{
		return lUserPointsRepo.save(toSaveUsr);
	}

	@Override
	public boolean deleteLUsr(Long lUsrId) throws IllegalArgumentException{
		lUserPointsRepo.deleteById(lUsrId);
		return true;
	}

	@Override
	public LUserEntity addPointsForLoyalUserToCachierPoint(Long lUsrId, AddPointsRequest newPoints) {
		Double toAddPoints = newPoints.getPoints();
		Long relatedCpnt = newPoints.getCashierPntID();

		Optional<LUserEntity> lUsrOptional = getLUsrById(lUsrId);
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

		lUsr = saveLusr(lUsr);

		return lUsr;

	}

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
