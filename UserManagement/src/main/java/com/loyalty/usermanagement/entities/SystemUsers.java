package com.loyalty.usermanagement.entities;
// Generated 17-Aug-2018 08:48:21 by Hibernate Tools 5.2.3.Final

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.Email;

@Entity
@Table(name = "system_users", schema = "user_management")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.INTEGER)
public abstract class SystemUsers implements java.io.Serializable {

	private Integer userId;
	@JsonIgnore
	private int lkUserType;
	@NotEmpty
	private String username;
	@NotEmpty
	@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "Password should Minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character.")
	private String password;
	@NotEmpty
	@Email
	private String email;
	@NotEmpty
	private String fullName;
	private boolean isEnabled = true;
	@JsonIgnore
	private boolean isDeleted = false;

	public SystemUsers() {
	}

	public SystemUsers(Integer userId, String username, String password, String email, String fullName,
			boolean isEnabled, boolean isDeleted) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.email = email;
		this.fullName = fullName;
		this.isEnabled = isEnabled;
		this.isDeleted = isDeleted;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", unique = true, nullable = false)
	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "user_type", nullable = false, updatable = false, insertable = false)
	public int getLkUserType() {
		return this.lkUserType;
	}

	@Column(name = "username", nullable = false, length = 100)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "password", nullable = false, length = 100)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "email", nullable = false, length = 100)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "full_name", nullable = false, length = 100)
	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
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

	public void setLkUserType(int lkUserType) {
		this.lkUserType = lkUserType;
	}

}
