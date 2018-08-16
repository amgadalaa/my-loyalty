package com.loyalty.pointingsystem.lusrs;

import java.util.Optional;

import com.loyalty.pointingsystem.entities.LUserEntity;
import com.loyalty.pointingsystem.lusrs.dto.AddPointsRequest;

public interface ILUsrsService {

	Optional<LUserEntity> getLUsrById(Long lUsrId) throws IllegalArgumentException;

	LUserEntity saveLusr(LUserEntity toSaveUsr) throws IllegalArgumentException;

	boolean deleteLUsr(Long lUsrId) throws IllegalArgumentException;

	LUserEntity addPointsForLoyalUserToCachierPoint(Long lUsrId, AddPointsRequest newPoints);
}
