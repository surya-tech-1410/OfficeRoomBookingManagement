package com.learn.Spring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class BookingStatus {
	
	public BookingStatus() {}
	
	@Id
	private String Id;
	private String booking_status;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id" )
	private Booking book;
	
	public BookingStatus(String Id , String booking_status){
		this.Id =Id;
		this.booking_status = booking_status;
	}
	
	public BookingStatus(String Id , String booking_status , Booking book){
		this.Id =Id;
		this.booking_status = booking_status;
		this.book = book;
	}

	public String getBooking_status() {
		return booking_status;
	}

	public void setBooking_status(String booking_status) {
		this.booking_status = booking_status;
	}
	

}
