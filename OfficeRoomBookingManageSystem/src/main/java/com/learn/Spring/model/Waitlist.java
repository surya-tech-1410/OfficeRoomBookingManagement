package com.learn.Spring.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Waitlist {

	public Waitlist() {}
	
	@Id
	public String waitListId;
	private Date startTime;
	private Date endTime;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user; // many-1 entity relationship that means each room belongs to one office and that office has location.
	
	@ManyToOne
	@JoinColumn(name = "room_id")
	private Room room;

	public String getWaitListId() {
		return waitListId;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public void setWaitListId(String waitListId) {
		this.waitListId = waitListId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	

}
