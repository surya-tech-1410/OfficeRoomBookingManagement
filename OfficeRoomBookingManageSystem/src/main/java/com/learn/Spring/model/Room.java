package com.learn.Spring.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Room {
	
	@Id
	private String roomId;
	private String roomName;
	private String roomCategory;
	private boolean isAvailable;
	
	
	
	@ManyToOne
	@JoinColumn(name = "office_id")
	private Office office; // many-1 entity relationship that means each room belongs to one office and that office has location.
	
	@OneToMany(mappedBy = "room" , cascade = CascadeType.ALL , orphanRemoval = true)
	private List<Booking> bookings;
	
	@OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
	private List<Waitlist> waitList;

	public Room() {}
	
	public Room(String roomId, String roomName, String roomCategory , boolean isAvailable, Office office) {
		
		this.roomId = roomId;
		this.roomName = roomName;
		this.roomCategory = roomCategory;
		this.isAvailable = isAvailable;
		this.office = office;
		
	}

	public String getRoomId() {
		return roomId;
	}



	public void setRoomId(String string) {
		this.roomId = string;
	}



	public String getRoomName() {
		return roomName;
	}


	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}


	public String getRoomCategory() {
		return roomCategory;
	}


	public void setRoomCategory(String roomCategory) {
		this.roomCategory = roomCategory;
	}


	public boolean isAvailable() {
		return isAvailable;
	}


	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}


	public Office getOffice() {
		return office;
	}


	public void setOffice(Office office) {
		this.office = office;
	}

	
}
