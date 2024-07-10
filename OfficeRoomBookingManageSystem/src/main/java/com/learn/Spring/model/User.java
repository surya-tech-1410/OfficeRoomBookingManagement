package com.learn.Spring.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
public class User {
	
	@Id
	private String userId;
	private String userPassword;
	private String userEmail;
	private String userName;
	private String role;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Waitlist> waitList;
	
	@ManyToMany(mappedBy = "users")
	private List<Booking> bookings;
	
	public User() {}
	
	public User(String userId, String userPassword , String userEmail , String userName, String role) {
		
		this.userId = userId;
		this.userEmail = userEmail;
		this.userPassword = userPassword;
		this.userName = userName;
		this.role = role;
		
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

}
