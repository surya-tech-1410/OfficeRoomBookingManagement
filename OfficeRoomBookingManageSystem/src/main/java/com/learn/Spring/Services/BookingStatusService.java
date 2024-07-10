package com.learn.Spring.Services;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.learn.Spring.BookingStatusRepo;
import com.learn.Spring.model.Booking;
import com.learn.Spring.model.BookingStatus;

@Service
public class BookingStatusService {
	
	@Autowired
	BookingStatusRepo bookingStatusRepo;
	

	public BookingStatusService() {}

	public BookingStatus addStatus(String status , Booking book) {
		
		String statusId = IDGenerator.getStatusId();
		BookingStatus bookStatus = new BookingStatus(statusId , status , book);
	//	bookingStatusRepo.save(bookStatus);
		return bookStatus;
		
		
	}
	
    public void updateStatus(String status , String BookingId) {
		
		String statusId = IDGenerator.getStatusId();
		BookingStatus bookStatus = new BookingStatus(statusId , status); // need to add edit options here .....
		bookingStatusRepo.save(bookStatus);
		
		
		
	}
	
	

}
