package com.loyalty.pointingsystem.entities;

import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity
public class CPntEntity {

	@Id
	private Long systemCPntId;

	@Property
	private String cPntName;

	public Long getSystemCPntId() {
		return systemCPntId;
	}

	public void setSystemCPntId(Long systemCPntId) {
		this.systemCPntId = systemCPntId;
	}

	public String getcPntName() {
		return cPntName;
	}

	public void setcPntName(String cPntName) {
		this.cPntName = cPntName;
	}

	@Override
	public String toString() {
		return "CPntEntity [systemCPntId=" + systemCPntId + ", cPntName=" + cPntName + "]";
	}

}
