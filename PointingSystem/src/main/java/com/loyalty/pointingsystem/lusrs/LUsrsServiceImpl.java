package com.loyalty.pointingsystem.lusrs;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loyalty.pointingsystem.entities.LUserEntity;

@Service
public class LUsrsServiceImpl implements ILUsrsService {

	@Autowired
	private LUserPointsRepo lUserPointsRepo;

	@Override
	public Optional<LUserEntity> getLUsrById(Long lUsrId) {
		return lUserPointsRepo.findById(lUsrId);
	}

	@Override
	public LUserEntity saveLusr(LUserEntity toSaveUsr) {
		return lUserPointsRepo.save(toSaveUsr);
	}

}
