package com.campusretail.userservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_role")
public class UserRole {

	/**
	 * Class used to set the roles
	 * of the users as table in the database
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "role_name", unique = true)
	private String roleName;

	@OneToMany(mappedBy = "role")
	@JsonIgnore
	private List<User> users;

	public UserRole() {

	}

	public UserRole(Long id, String roleName, List<User> users) {
		this.id = id;
		this.roleName = roleName;
		this.users = users;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
}
