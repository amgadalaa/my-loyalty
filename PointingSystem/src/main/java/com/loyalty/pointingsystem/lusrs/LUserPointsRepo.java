package com.loyalty.pointingsystem.lusrs;

import org.springframework.data.repository.CrudRepository;

import com.loyalty.pointingsystem.entities.LUserEntity;

public interface LUserPointsRepo extends CrudRepository<LUserEntity, Long> {

}
