package com.learn.Spring.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Office {

	@Id
	private String officeId;
	private String officeName;
	public String officeLocation;

	@OneToMany(mappedBy = "office", cascade = CascadeType.ALL)
	private List<Room> roomList; // 1-many each office will have no.of.rooms;

	// Mappedby --> It is used to indicate the field in the owning side of the
	// relationship that is responsible for mapping the relationship..
	// OrpanRemoval --> here , if you remove a room entity from room collection of
	// office entity and then save the office , the removed room entity will also be
	// deleted from the database.

	public Office() {
	}

	public Office(String officeId, String officeLocation, String officeName) {
		this.officeId = officeId;
		this.officeLocation = officeLocation;
		this.officeName = officeName;
//		this.roomList = roomList;
	}

	public String getOfficeLocation() {
		return officeLocation;
	}

	public void setOfficeLocation(String officeLocation) {
		this.officeLocation = officeLocation;
	}

//	public List<Room> getRoomList() {
//		return roomList;
//	}
//
//	public void setRoomList(List<Room> roomList) {
//		this.roomList = roomList;
//	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getOfficeId() {
		return officeId;
	}
}
