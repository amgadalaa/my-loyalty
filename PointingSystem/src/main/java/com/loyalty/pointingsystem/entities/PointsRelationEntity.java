package com.loyalty.pointingsystem.entities;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import org.neo4j.ogm.annotation.Version;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.loyalty.pointingsystem.statics.Relationships;

@RelationshipEntity(type = Relationships.LUSER_HAS_POINTS_CPNT)
public class PointsRelationEntity {

	@Id
	@GeneratedValue
	private Long relationShipID;

	@Property
	private Double points;

	@Index(unique = true)
	private String internalId;

	@JsonBackReference
	@StartNode
	private LUserEntity lUser;

	@EndNode
	private CPntEntity cPnt;

	@Version
	protected Long version;

	public Long getRelationShipID() {
		return relationShipID;
	}

	public void setRelationShipID(Long relationShipID) {
		this.relationShipID = relationShipID;
	}

	public Double getPoints() {
		return points;
	}

	public void setPoints(Double points) {
		this.points = points;
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
	}

	public LUserEntity getlUser() {
		return lUser;
	}

	public void setlUser(LUserEntity lUser) {
		this.lUser = lUser;
	}

	public CPntEntity getcPnt() {
		return cPnt;
	}

	public void setcPnt(CPntEntity cPnt) {
		this.cPnt = cPnt;
	}

	public Double addPoints(Double toAddPoints) {
		this.points += toAddPoints;
		return this.points;
	}

	@Override
	public String toString() {
		return "PointsRelationEntity [relationShipID=" + relationShipID + ", points=" + points + ", internalId="
				+ internalId + ", lUser=" + lUser.getSystemLUserId() + ", cPnt=" + cPnt.getSystemCPntId() + ", version=" + version + "]";
	}

}
