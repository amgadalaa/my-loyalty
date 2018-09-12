package com.loyalty.usermanagement.entities;
// Generated 17-Aug-2018 08:48:21 by Hibernate Tools 5.2.3.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * This class represents the Cashier Point.
 */
@Entity
@Table(name = "cpnt", schema = "user_management")
public class CpntEntity implements java.io.Serializable {

	private int cpntId;
	private CAdmEntity systemUsers;
	private String fullAddress;
	private String name;
	private boolean isEnabled;
	private boolean isDeleted;

	public CpntEntity() {
	}

	public CpntEntity(int cpntId, CAdmEntity systemUsers, String fullAddress, String name, boolean isEnabled,
			boolean isDeleted) {
		this.cpntId = cpntId;
		this.systemUsers = systemUsers;
		this.fullAddress = fullAddress;
		this.name = name;
		this.isEnabled = isEnabled;
		this.isDeleted = isDeleted;
	}

	@Id

	@Column(name = "cpnt_id", unique = true, nullable = false)
	public int getCpntId() {
		return this.cpntId;
	}

	public void setCpntId(int cpntId) {
		this.cpntId = cpntId;
	}

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_user", nullable = false)
	public CAdmEntity getSystemUsers() {
		return this.systemUsers;
	}

	public void setSystemUsers(CAdmEntity systemUsers) {
		this.systemUsers = systemUsers;
	}

	@Column(name = "full_address", nullable = false, length = 500)
	public String getFullAddress() {
		return this.fullAddress;
	}

	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}

	@Column(name = "name", nullable = false, length = 500)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "is_enabled", nullable = false)
	public boolean isIsEnabled() {
		return this.isEnabled;
	}

	public void setIsEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	@Column(name = "is_deleted", nullable = false)
	public boolean isIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public String toString() {
		return "CpntEntity [cpntId=" + cpntId + ", fullAddress=" + fullAddress + ", name=" + name + ", isEnabled="
				+ isEnabled + ", isDeleted=" + isDeleted + "]";
	}

}
