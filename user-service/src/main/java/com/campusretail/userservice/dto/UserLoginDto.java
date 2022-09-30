package com.campusretail.userservice.dto;


public class UserLoginDto {
	private Long id;
	private String login;
	private String token;
	
	private String role;
	
	public UserLoginDto() {
		
	}

	public UserLoginDto(Long id, String login, String token, String role) {
		this.id = id;
		this.login = login;
		this.token = token;
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
