package com.loyalty.usermanagement.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * 
 * @author eamgmuh
 * 
 *         This class represents the Cashier point Admin who has access to the
 *         system. This user is basically the one who defines the CPnts.
 */
@Entity
@DiscriminatorValue(CAdmEntity.CADM_TYPE_ID + "")
public class CAdmEntity extends SystemUsers {

	public static final int CADM_TYPE_ID = 1;

	private Set<CpntEntity> cpnts = new HashSet<CpntEntity>(0);

	public CAdmEntity() {
		super();
	}

	public CAdmEntity(int userId, String username, String password, String email, String fullName, boolean isEnabled,
			boolean isDeleted, Set<CpntEntity> cpnts) {
		super(userId, username, password, email, fullName, isEnabled, isDeleted);
		this.cpnts = cpnts;
	}

	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "systemUsers")
	public Set<CpntEntity> getCpnts() {
		return this.cpnts;
	}

	public void setCpnts(Set<CpntEntity> cpnts) {
		this.cpnts = cpnts;
	}

	@Override
	public String toString() {
		return "CAdmEntity [cpnts=" + cpnts + ", getUserId()=" + getUserId() + ", getLkUserType()=" + getLkUserType()
				+ ", getUsername()=" + getUsername() + ", getPassword()=" + getPassword() + ", getEmail()=" + getEmail()
				+ ", getFullName()=" + getFullName() + ", isIsEnabled()=" + isIsEnabled() + ", isIsDeleted()="
				+ isIsDeleted() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

}
