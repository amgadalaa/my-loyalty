package com.loyalty.pointingsystem.lusrs;

import java.util.Optional;

import com.loyalty.pointingsystem.entities.LUserEntity;

public interface ILUsrsService {

	// Add points between LUsr and CPnt

	// remove LUsr and all of his points

	// query all points for LUsr

	// remove CPnts???

	// query all points CPnt

	Optional<LUserEntity> getLUsrById(Long lUsrId);

	LUserEntity saveLusr(LUserEntity toSaveUsr);

}
