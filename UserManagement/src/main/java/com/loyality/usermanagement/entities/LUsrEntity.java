package com.loyality.usermanagement.entities;

/**
 * 
 * @author eamgmuh
 * 
 *         This class represents the Loyal User entity in User Management
 *         context
 */
public class LUsrEntity extends AbstractSystemUser {

	public LUsrEntity() {
		super();
	}

	public LUsrEntity(Long userId, String username, String password, String email, String fullName, boolean isEnabled,
			boolean isDeleted) {
		super(userId, username, password, email, fullName, isEnabled, isDeleted);
	}

}
