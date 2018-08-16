package com.loyality.usermanagement.entities;

public abstract class AbstractSystemUser {

	private Long userId;
	private String username;
	private String password;
	private String email;
	private String fullName;
	private boolean isEnabled = true;
	private boolean isDeleted = false;

	public AbstractSystemUser() {
		super();
	}

	public AbstractSystemUser(Long userId, String username, String password, String email, String fullName,
			boolean isEnabled, boolean isDeleted) {
		super();
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.email = email;
		this.fullName = fullName;
		this.isEnabled = isEnabled;
		this.isDeleted = isDeleted;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

}
