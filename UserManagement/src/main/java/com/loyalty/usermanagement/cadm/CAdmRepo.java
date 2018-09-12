package com.loyalty.usermanagement.cadm;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.loyalty.usermanagement.entities.CAdmEntity;

public interface CAdmRepo extends CrudRepository<CAdmEntity, Integer> {

	
	@Query("select distinct cadm from CAdmEntity cadm left join fetch cadm.cpnts relatedcpnts")
	List<CAdmEntity> getAll();
	
	@Query("select distinct cadm from CAdmEntity cadm left join fetch cadm.cpnts relatedcpnts where cadm.userId = ?1")
	Optional<CAdmEntity> findById(Integer id);
}
