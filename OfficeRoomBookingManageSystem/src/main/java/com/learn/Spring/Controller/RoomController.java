package com.learn.Spring.Controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.Spring.Services.BookingService;
import com.learn.Spring.Services.OfficeService;
import com.learn.Spring.Services.RoomService;
import com.learn.Spring.model.Booking;
import com.learn.Spring.model.Office;
import com.learn.Spring.model.Room;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("Room")
public class RoomController {

	
	   @Autowired
	   RoomService roomService;
	   
	   @Autowired
	   OfficeService officeservice;
	   
	   @Autowired
	   BookingService bookingservice;
		
		
	   @PostMapping("getCalendarTimings/{officeName}/{roomName}")
	   public ResponseEntity<String> showBookedSlotsForRoom(@PathVariable String officeName, @PathVariable String roomName, @RequestBody Map<String,String> requestBody , HttpSession session) throws JsonProcessingException {
	     ModelAndView mv = new ModelAndView("RoomBlockedTimings"); // Set the view name
           Map<String, List<String>> blockedTimings = new HashMap<>();
	       try {
	           String officeId = officeservice.getOfficeId(officeName);
	           String roomId = roomService.getRoom(roomName, officeId).getRoomId();
	           String dateVar = requestBody.get("date");
	           Date date;
	           System.out.println("DateVar : " + dateVar );

	           if(dateVar == null) {
	               // Default date handling
	               Calendar calendar = Calendar.getInstance();
	               calendar.set(Calendar.YEAR, 2024); 
	               calendar.set(Calendar.MONTH, Calendar.FEBRUARY); 
	               calendar.set(Calendar.DAY_OF_MONTH, 15); 
	               date = calendar.getTime();

	           } else {
	               // Parse the provided date
	               SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	               date = formatter.parse(dateVar);
	               System.out.println(date);
	               System.out.println("hii check");
	           }

	           // Fetch booking timings for the room
	           System.out.println(date);
	           List<Booking> book = bookingservice.getTimingsOfRoom(roomId, date);

	           // Process bookings and add to BlockedTimings map
	
	           SimpleDateFormat Hour = new SimpleDateFormat("HH");
	           SimpleDateFormat Minute = new SimpleDateFormat("mm");

	           for(Booking booking : book) {
	               String startTimeHour = Hour.format(booking.getStartTime());
	               String startTimeMinute = Minute.format(booking.getStartTime());
	               String endTimeHour = Hour.format(booking.getEndTime());
	               String endTimeMinute = Minute.format(booking.getEndTime());
	               String subject = booking.getSubject();
	               
	               // Add booking details to BlockedTimings map
	               
	               blockedTimings.put(booking.getBookId(), Arrays.asList(startTimeHour, startTimeMinute, endTimeHour, endTimeMinute, subject));
	           }
	
	           ObjectMapper objectMapper = new ObjectMapper();   
		       session.setAttribute("blockedTimings",objectMapper.writeValueAsString(blockedTimings));
		       
		       session.setAttribute("roomName", roomName);
		       session.setAttribute("officeName", officeName);
		       
		       SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		    // Format the date as a string
		      String formattedDate = dateFormat.format(date);

		    // Now, you can store the formatted date in your session attribute
		    session.setAttribute("date", formattedDate);
		       
		       System.out.println(roomName + " " + officeName + " " + date);
		       
		       return ResponseEntity.ok("Success");
	 
	       } catch (Exception e) {
	    	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
	       }
	       //  return blockedTimings;

	   }
	   
	   @GetMapping("RoomBookedList")
	    public ModelAndView getRoomBookedList(HttpSession session){
			ModelAndView mv = new ModelAndView("RoomBlockedTimings");
			//System.out.println(session.getAttribute("blockedTimings"));
			mv.addObject("blockedTimings",session.getAttribute("blockedTimings"));
			return mv;		   
	      
	    }

	 
	   @PostMapping("addRoom")
	    public ResponseEntity<String> registerInfo(@RequestBody Room room){
	        return roomService.addRoom(room);
	    }
	  
		
		@GetMapping("getRoom/{Id}")
	    public ResponseEntity<Optional<Room>> getRoom(@PathVariable String Id){
	        return roomService.getRoomInfo(Id);
	    }
		

}
