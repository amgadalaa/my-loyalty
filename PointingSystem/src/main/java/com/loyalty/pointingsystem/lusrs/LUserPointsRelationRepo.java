package com.loyalty.pointingsystem.lusrs;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.loyalty.pointingsystem.entities.PointsRelationEntity;

public interface LUserPointsRelationRepo extends CrudRepository<PointsRelationEntity, Long> {

	Optional<PointsRelationEntity> findByinternalIdEquals(Long internalId);
}
