package com.loyalty.pointingsystem.cpnt;

import org.springframework.data.repository.CrudRepository;

import com.loyalty.pointingsystem.entities.CPntEntity;

public interface CpntRepo extends CrudRepository<CPntEntity, Long> {

}
