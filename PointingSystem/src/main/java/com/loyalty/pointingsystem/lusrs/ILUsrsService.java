package com.loyalty.pointingsystem.lusrs;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.loyalty.pointingsystem.entities.LUserEntity;
import com.loyalty.pointingsystem.lusrs.dto.AddPointsRequest;

public interface ILUsrsService {

	// Add points between LUsr and CPnt

	// remove LUsr and all of his points

	// query all points for LUsr

	// remove CPnts???

	// query all points CPnt

	Optional<LUserEntity> getLUsrById(Long lUsrId) throws IllegalArgumentException;

	LUserEntity saveLusr(LUserEntity toSaveUsr) throws IllegalArgumentException;

	boolean deleteLUsr(Long lUsrId) throws IllegalArgumentException;

	LUserEntity addPointsForLoyalUserToCachierPoint(Long lUsrId, AddPointsRequest newPoints);
}
