package com.learn.Spring.Services;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar.Events;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.learn.Spring.BookingRepo;
import com.learn.Spring.model.Booking;
import com.learn.Spring.model.User;

@Service
public class EventService {

	
	
	private static final String APPLICATION_NAME = null;
	@Autowired
	BookingRepo bookingrepo;

	public String createEvent(Events events , Booking book ) throws IOException {
		
		
		Event event = getEvent(book);
	//	events.insert(APPLICATION_NAME , null);				
				

		  String calendarId = "primary"; // calendarId ......
		  
		  try {
			 Event eventDetail = events.insert(calendarId, event).setSendNotifications(true).execute();
			 String eventId = eventDetail.getId();
			 book.setEventId(eventId);
			 bookingrepo.addEventIdForBookingId(eventId,book.getBookId());
			 
		  }
		  catch (Exception e ) {
		  
			  return e.getMessage() + ": 400";
		  }
		
		return "Your booking was successfully completed and sent the notifications to the participants : 200"; // will change later ...
		
	}
	
	private Event getEvent(Booking book) {

        String subject = book.getSummary();
        String location = book.getRoom().getRoomName();
        String description = book.getSubject();
        Date startTime = book.getStartTime();
        Date endTime = book.getEndTime();

		
		Event event = new Event().setSummary(subject)
			    .setLocation(location)
			    .setDescription(description);		
		
			DateTime startDateTime = new DateTime(startTime); // Dates should be greater than or equal to current date..else invitation wont send.
			EventDateTime start = new EventDateTime()
			    .setDateTime(startDateTime)
			    .setTimeZone("Asia/Kolkata");
			event.setStart(start);
			
			DateTime endDateTime = new DateTime(endTime);
			EventDateTime end = new EventDateTime()
			    .setDateTime(endDateTime)
			    .setTimeZone("Asia/Kolkata");
			event.setEnd(end);
			
			List<User> booking_participants = book.getUsers();
			EventAttendee[] attendees = new EventAttendee[booking_participants.size()];
			
			for(int i= 0 ; i<booking_participants.size() ; i++) {
				attendees[i] = new EventAttendee().setEmail(booking_participants.get(i).getUserEmail());
				
			}
			
			event.setAttendees(Arrays.asList(attendees)); // uppdate ....
						
		return event;
	}

	public String updateEvent(Events events , String bookId , Booking book) throws IOException {
		   Optional <String> eventIdOptional = bookingrepo.getEventIdUsingBookingId(bookId);
		   String eventid = eventIdOptional.orElse(null);
		   System.out.println("checkEVent :" + eventid);
			Event event = getEvent(book);
			//events.insert(APPLICATION_NAME , null);	
		  
		   if(eventid != null) {
			   try {
				   String calendarId = "primary"; 
				   
				   events.update(calendarId,eventid,event).setSendNotifications(true).execute();
				   return "Updated Booking and Sent the Update invite Successfully.";
				   
			   }
			   catch (Exception e) {
				   System.out.println(e.getMessage());
				   
				   return "Updated Booking but Update invite was failed....." + e.getMessage();
				   
			   }
			   
		   }
		   else {
			   return "Updated Booking but Cannot send Meeting update invite , Invalid EventId or there is no EvenId for this Booking...";
		   }
	}
	
	public String cancelEvent (Events event , String bookId ,String eventid) {
	   
	   if(eventid != null) {
		   try {
			   
			   String calendarId = "primary";   
			   event.delete(calendarId, eventid).setSendNotifications(true).execute();
			   return "Cancelled Booking and Sent the Cancel invite Successfully.";
			   
		   }
		   catch (Exception e) {
			   
			   return "Cancelled Booking but Failed to Cancel the Event.." + e.getMessage();
			   
		   }
		   
	   }
	   else {
		   return "Cancelled Booking but Cannot send cancel invite , Invalid EventId or there is no EvenId for this Booking...";
	   }
	   
	}

}
