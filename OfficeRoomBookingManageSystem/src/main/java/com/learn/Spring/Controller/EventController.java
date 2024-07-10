package com.learn.Spring.Controller;

import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.services.calendar.Calendar.Events;
import com.learn.Spring.Services.EventService;
import com.learn.Spring.model.Booking;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("Event")
public class EventController {

	public EventController() {}
	
	@Autowired
	EventService eventService;
	
	@GetMapping("createEvent")
	public ResponseEntity<String> createEventInCalendar(HttpSession session) throws IOException{
		Events event = (Events) session.getAttribute("CalendarEvent");
		Booking book =  (Booking) session.getAttribute("bookingDetails");
		String message = eventService.createEvent(event,book);
		if(message.contains("200")) {
		return ResponseEntity.status(HttpStatus.OK).body(message);
		}
		else {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
		}
	}
	
	@GetMapping("editEvent")
	public ResponseEntity<String> editEventInCalendar(HttpSession session) throws IOException{
		Events event = (Events) session.getAttribute("CalendarEvent");
		String bookingId = (String) session.getAttribute("bookingId");
		Booking book =  (Booking) session.getAttribute("bookingDetails");
		
		String message = eventService.updateEvent(event, bookingId , book);
		
		if(message.contains("Update invite Successfully")) {
			return ResponseEntity.status(HttpStatus.OK).body(message);
		}
		else if(message.contains("Invalid EventId")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);		
		}
		else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);	
		}	
		
		
	}
	
	@GetMapping("cancelEvent")
	public ResponseEntity<String> cancelEventInCalendar(HttpSession session){
		Events event = (Events) session.getAttribute("CalendarEvent");
		String bookingId = (String) session.getAttribute("bookingId");
		String eventid = (String) session.getAttribute("eventId");
		String message = eventService.cancelEvent(event,bookingId,eventid);
		
		if(message.contains("Cancel invite Successfully")) {
			return ResponseEntity.status(HttpStatus.OK).body(message);
		}
		else if(message.contains("Invalid EventId")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);		
		}
		else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);	
		}		
		
	}

}
