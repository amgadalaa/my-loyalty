package com.loyality.usermanagement.entities;

/**
 * 
 * @author eamgmuh
 * 
 *         This class represents the Cashier point Admin who has access to the
 *         system. This user is basically the one who defines the CPnts.
 */
public class CAdmEntity extends AbstractSystemUser {

	public CAdmEntity() {
		super();
	}

	public CAdmEntity(Long userId, String username, String password, String email, String fullName, boolean isEnabled,
			boolean isDeleted) {
		super(userId, username, password, email, fullName, isEnabled, isDeleted);
	}

}
