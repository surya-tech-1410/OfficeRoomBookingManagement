package com.learn.Spring.Services;

import java.io.IOException;


import java.text.SimpleDateFormat;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.learn.Spring.BookingRepo;
import com.learn.Spring.BookingStatusRepo;
import com.learn.Spring.UserRepo;
import com.learn.Spring.model.Booking;
import com.learn.Spring.model.BookingStatus;
import com.learn.Spring.model.User;

import jakarta.servlet.http.HttpServletResponse;

//import jakarta.transaction.Transactional;

@Service @Transactional
public class BookingService {

	public BookingService() {}
	
	@Autowired
	BookingRepo bookingrepo;

	@Autowired 
	BookingStatusRepo bookingStatusRepo;
	
	@Autowired
	BookingStatusService bookingStatusService;
	
	@Autowired
	UserService userservice;
	
	@Autowired
	RoomService roomservice;
	
	@Autowired
	OfficeService officeservice;
	
	@Autowired
	WaitListService waitlistservice;
	
	String redirecturl = "http://localhost:8080/login/notification/google";
	// need to use response object in controllers.
	public String bookRoom(Booking book , String OfficeName) throws IOException {
		/*
		 *  Sample json data ...
		
		{
		    "bookId": 123, 
		    "startTime": "2023-10-21T10:00:00", 
		    "endTime": "2023-10-21T12:00:00", 
		    "bookingInitiationTime": "2023-10-21T09:30:00", 
		    "room": {
		        "roomId": 456 // Replace with the ID of the room associated with this booking
		    },
		    "user": {
		        "userId": 789 // Replace with the ID of the user who made the booking
		    },
		    "bookingParticipants": [
		        {
		            "userId": 789 // Add the user IDs for participants in the booking
		        },
		        {
		            "userId": 101 // Add more user IDs if there are more participants
		        }
		    ],
		    "bookingStatus": {
		        "statusId": 1 // Replace with the ID for the booking status (e.g., "confirmed")
		    }
		}
		
		*/
		
		
		/*
		 * 
		 * check whether the entered user is correct user ..
		 * Validate the timings... (ET > ST && within the same month ...) (Logic done)
		 * check the room availability , if the room is not avaliable then give the user whether he needed the booking and let them know it will be in waitlist.
		 * update the status ..in bookingstatus db.
		 * send the confirmation mail to all the users. (Solution Found)
		 * block their calendars (outlook) (SOLUTION FOUND)
		 * if any of the meeting is cancelled , send the meeting cancelled mail and book the room for the first waitlist member. repeat 5 & 6.
		
		*/
		
		Date StartTime = book.getStartTime();
		Date EndTime = book.getEndTime();
		
		System.out.println(StartTime);
		System.out.println(EndTime);
		System.out.println("roomName : " + book.getRoom().getRoomName());
		
		
		boolean isValidTime = isValidTimings (StartTime , EndTime);
		
		if(!isValidTime) {
			
			return  "Timings are not under the criteria ... ";
//			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//			response.getWriter().write("Timings are not under the criteria ... ");
			//return new ResponseEntity<>("Timings are not under the criteria ... ", HttpStatus.BAD_REQUEST);
		}
		String roomId = roomservice.getRoom(book.getRoom().getRoomName(),officeservice.getOfficeId(OfficeName)).getRoomId();
		System.out.println("roomId : " + roomId);
		List<Booking> Overlap = bookingrepo.findOverLappingBookings(book.getStartTime(), book.getEndTime(),roomId);
		if(Overlap.size() >0) {
			//return new ResponseEntity<>("Timings are overlapping so cannot book room , You can go for wailist... ", HttpStatus.BAD_REQUEST);
//			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//			response.getWriter().write("Timings are overlapping so cannot book room , You can go for wailist...");
			
			return "Timings are overlapping so cannot book room , You can go for wailist...";
		}
		

			
			try {
		    //book.setUsers(userRepo.findAllById(null));	
		//	List<User> userIds = book.getUsers();
		//	List<User> userInfos = userservice.getUsersInfo();
		    book.setUsers(userservice.getUserInfos(book.getUsers()));
		    System.out.println("Checking users...");
		    System.out.println(book.getUsers());
		    book.setUser(userservice.getOrganiserInfo(book.getUser())); // will do it later , once it was handled in jsp ......
		    String officeId = officeservice.getOfficeId(OfficeName);
		    book.setRoom(roomservice.getRoom(book.getRoom().getRoomName(),officeId));
			BookingStatus status =  bookingStatusService.addStatus("Confirm", book);
			book.setStatus(status);
			
		//	System.out.println(r)
			
			bookingrepo.save(book);
		    return redirecturl;
		    
		   // return null; // need to check ....
		//	return new ResponseEntity<>("Room is Successfully booked.",HttpStatus.CREATED);
			}
			
			catch (Exception e) {
				e.printStackTrace();
				return "Booking was failed : Internal server Error " + e.getMessage();
//				response.setStatus(HttpServletResponse.SC_CONFLICT);
//				response.getWriter().write("Booking was failed " + e.getMessage());
				//return new ResponseEntity<>("Booking was failed",HttpStatus.CONFLICT);
			}
		}

	
	
	public String cancelBooking(String BookingId, HttpServletResponse response) throws Exception{
		Booking book = bookingrepo.getById(BookingId);
		System.out.println(book);
		if(book==null) {
			return "Invalid Booking..Please Refresh the page";
		}
		else {
			try {
			bookingrepo.deleteById(BookingId);
		//	emailservice.sendNotificationToWaitlisters(book);
			
			waitlistservice.getWaitListAndSendNotifications(book.getStartTime(),book.getEndTime(),book.getRoom());
			
			// send email to the receipents.......
			return redirecturl;
			}
			//bookingStatusService.updateStatus("Cancelled", BookingId);
			//notificationService.sendNotifications();
			catch(Exception e) {
				return "Not able to cancel : Internal server Error " + e.getMessage();
			}
			
		}
		
	}

	public String editBooking(Booking book) throws Exception{
		@SuppressWarnings("deprecation")
		
		Booking currentBooking = getBooking(book.getBookId());
		Date StartTime = book.getStartTime();
		Date EndTime = book.getEndTime();
		
		System.out.println(StartTime);
		System.out.println(EndTime);
		System.out.println(currentBooking.getStartTime());
		System.out.println(currentBooking.getEndTime());
		
		boolean isValidTime = isValidTimings (StartTime , EndTime);
		
		if(!isValidTime) {
			
			return  "Timings are not under the criteria ... ";
			/*
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("Timings are not under the criteria ... ");
			return new ResponseEntity<>("Timings are not under the criteria ... ", HttpStatus.BAD_REQUEST);
			 */
			 
		}
		
		List<Booking> Overlap = bookingrepo.findOverLappingBookings(book.getStartTime(), book.getEndTime(),book.getRoom().getRoomId());
		if(Overlap.size() >0) {
			return "Timings are overlapping so cannot book room , You can go for wailist... So edit booking was not Performed ....";
		}
		
		try {
		
		currentBooking.setBookId(book.getBookId());
		currentBooking.setEndTime(book.getEndTime());
		currentBooking.setStartTime(book.getStartTime());
		currentBooking.setSubject(book.getSubject());
		currentBooking.setSummary(book.getSummary());
		bookingrepo.save(currentBooking);
		}
		
		catch (Exception e) {
			System.out.println(e);
			return "Internal server Error : " + e.getMessage(); 
		}
		
		/*
		 * 
		 * Get all booking details from the sql DB and update the new edited booking object ... Object ...
		 * 
		 */

		return redirecturl;
		
	}
	
	
	public Booking getBooking(String bookId) {
		return bookingrepo.getById(bookId);
	}



	private boolean isValidTimings(Date startTime, Date endTime) {
		
		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        
	        Date currentDate = new Date();
	        
	       // System.out.println(currentDate);
	        
	        // Create a calendar instance and add one month to the current date
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(currentDate);
	        calendar.add(Calendar.MONTH, 1);
	        Date oneMonthFromNow = calendar.getTime();
	        
	        try {
	        	
	        	System.out.println("CheckTimings : " + startTime.after(currentDate));
	        	
	            if (isSameDate(startTime, endTime) && startTime.after(currentDate) && startTime.before(oneMonthFromNow)) {
	                System.out.println("Booking is within a one-month duration and on the same date.");
	                return true;
	            } else {
	                System.out.println("Error: Booking start and end times should be on the same date and within a one-month duration.");
	                return false;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		
		return false;
	}
	
	
	
    public static boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        System.out.println(cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH));
        
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }



	public List<Booking> getTimingsOfRoom(String roomId, Date date) {
		List<Booking> book = bookingrepo.getBookings(date, roomId);
		return book;
	}



	public List<Booking> getAllBookings(String userId) {
		return bookingrepo.getAllUserBookings(userId);
	}
	
	public String getEventId(String BookingId) {
		Optional <String> eventIdOptional = bookingrepo.getEventIdUsingBookingId(BookingId);
		
		String eventid = eventIdOptional.orElse(null);
		return eventid;
	}
}


