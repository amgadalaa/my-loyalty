package com.loyalty.pointingsystem.entities;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.loyalty.pointingsystem.statics.Relationships;

@NodeEntity
public class LUserEntity {

	@Id
	private Long systemLUserId;

	@JsonIgnore
	@Property
	private String userName;

	@JsonManagedReference
	@Relationship(type = Relationships.LUSER_HAS_POINTS_CPNT, direction = Relationship.OUTGOING)
	private Set<PointsRelationEntity> lUserPoints = new HashSet<>();

	// public Set<PointsRelationEntity> getlUserPoints() {
	// return lUserPoints;
	// }
	//
	// public void setlUserPoints(Set<PointsRelationEntity> lUserPoints) {
	// this.lUserPoints = lUserPoints;
	// }

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getSystemLUserId() {
		return systemLUserId;
	}

	public void setSystemLUserId(Long systemLUserId) {
		this.systemLUserId = systemLUserId;
	}

	public Set<PointsRelationEntity> getlUserPoints() {
		return lUserPoints;
	}

	public void setlUserPoints(Set<PointsRelationEntity> lUserPoints) {
		this.lUserPoints = lUserPoints;
	}

	@Override
	public String toString() {
		return "LUserEntity [systemLUserId=" + systemLUserId + ", userName=" + userName + ", lUserPoints=" + lUserPoints
				+ "]";
	}

}
