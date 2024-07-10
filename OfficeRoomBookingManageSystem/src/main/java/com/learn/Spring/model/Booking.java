package com.learn.Spring.model;

import java.util.Date;
import java.util.List;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Booking {
	
	@Id
	private String bookId;
	private Date startTime;
	private Date endTime;
	private String eventId;
	private String Summary;
	private String Subject;

	@ManyToOne
	private Room room; // many-1 entity relationship that means more than 1 booking can belongs to single room in various timings..

	@ManyToOne
	private User user; // many-1 entity relationship that means more than one booking can be done by user.

	@ManyToMany
	@JoinTable(name = "booking_participants", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<User> users;
	
	// A booking can have multiple participants
	// A participants can belongs to the multiple bookings...
	
	@OneToOne(mappedBy = "book", cascade = CascadeType.ALL , orphanRemoval = true , fetch = FetchType.LAZY)
	private BookingStatus status;
	
	public Booking() {}

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

	public BookingStatus getStatus() {
		return status;
	}

	public void setStatus(BookingStatus status) {
		this.status = status;
	}



	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}



	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public String getSummary() {
		return Summary;
	}

	public void setSummary(String summary) {
		this.Summary = summary;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getSubject() {
		return Subject;
	}

	public void setSubject(String subject) {
		this.Subject = subject;
	}


}

