package com.loyalty.usermanagement.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 
 * @author eamgmuh
 * 
 *         This class represents the Loyal User entity in User Management
 *         context
 */
@Entity
@DiscriminatorValue(LUsrEntity.LUSER_TYPE_ID + "")
public class LUsrEntity extends SystemUsers {

	public static final int LUSER_TYPE_ID = 2;

	public LUsrEntity() {
		super();
	}

	public LUsrEntity(Integer userId, String username, String password, String email, String fullName,
			boolean isEnabled, boolean isDeleted) {
		super(userId, username, password, email, fullName, isEnabled, isDeleted);
	}

	@Override
	public String toString() {
		return "LUsrEntity [getUserId()=" + getUserId() + ", getLkUserType()=" + getLkUserType() + ", getUsername()="
				+ getUsername() + ", getPassword()=" + getPassword() + ", getEmail()=" + getEmail() + ", getFullName()="
				+ getFullName() + ", isIsEnabled()=" + isIsEnabled() + ", isIsDeleted()=" + isIsDeleted()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}

}
