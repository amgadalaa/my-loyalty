package com.loyalty.pointingsystem.entities;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity
public class CPntEntity {

	@Id
	private long systemCPntId;

	// @Property
	// private String cPntName;
	
	public long getSystemCPntId() {
		return systemCPntId;
	}

	public void setSystemCPntId(long systemCPntId) {
		this.systemCPntId = systemCPntId;
	}

}
