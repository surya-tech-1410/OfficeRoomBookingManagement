package com.learn.Spring.Controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.learn.Spring.Services.BookingService;
import com.learn.Spring.Services.IDGenerator;
import com.learn.Spring.model.Booking;
import com.learn.Spring.model.Room;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("Book")
public class BookController {
	
	@Autowired
	BookingService bookingService;

	public BookController() {}
	
	
	
	@PostMapping("bookDetails/{officeName}")
	public ModelAndView bookRoomForMeet(@ModelAttribute("Room") Room room, @PathVariable String officeName) {
		
		ModelAndView mv = new ModelAndView("index");
		mv.addObject("officeName",officeName);
		mv.addObject("roomName",room.getRoomName());
		System.out.println(room.getRoomName());
//		mv.addObject("Book", new Booking());
		return mv;
		
	}
	
    @PostMapping("edit/details")
    public ModelAndView editBookingDetails(@ModelAttribute("Booking") Booking bookingOBJ) {
    	
    	ModelAndView mv = new ModelAndView("index");
    	Booking booking = bookingService.getBooking(bookingOBJ.getBookId());
    	System.out.println(booking.getEndTime());
		mv.addObject("officeName",booking.getRoom().getOffice().getOfficeName());
		mv.addObject("roomName",booking.getRoom().getRoomName());
        mv.addObject("booking",booking);
		return mv;
    }
	
	
	
	
	@PostMapping("bookRoom/{officeName}")
//	@CrossOrigin(origins = "http://localhost:8080/Book/bookRoom")
	public void bookRoom(@RequestBody Booking book ,@PathVariable String officeName ,  HttpSession session , HttpServletResponse response) throws IOException {
		session.setAttribute("bookingDetails", book);
		session.setAttribute("HttpRequest", "POST");
		String bookingId = IDGenerator.getBookId();
		book.setBookId(bookingId);
		session.setAttribute("bookId",bookingId); /// store bookId here after redirect to new url , we need to get the data by using this bookingid and update event for this booking Id...
		String messageOrUrl =  bookingService.bookRoom(book , officeName);
		if(messageOrUrl.contains("notification")) {
			System.out.println("Redirecting...");
			response.sendRedirect(messageOrUrl); // url redirection for sending notification to participants...
		}
		
		else if (messageOrUrl.contains("Internal server Error")) {
			
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write(messageOrUrl);
		
			
		}
		
		else {
			
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		response.getWriter().write(messageOrUrl);
		
		}
	}
	
	@PostMapping("EditBooking")
	public void editBooking(@RequestBody Booking book , HttpSession session ,HttpServletResponse response ) throws Exception {
		System.out.println("getting in..");
		System.out.println(book.getBookId());
		session.setAttribute("bookingDetails", book);
		session.setAttribute("HttpRequest", "PUT");
		session.setAttribute("bookingId",book.getBookId());
		String messageOrUrl = bookingService.editBooking(book);
		
		if(messageOrUrl.contains("notification")) {
			System.out.println("Redirecting...");
			response.sendRedirect(messageOrUrl); // url redirection for sending notification to participants...
		}
		
		else if (messageOrUrl.contains("Internal server Error")) {
			
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write(messageOrUrl);
			
		}
		
		else {
			
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		response.getWriter().write(messageOrUrl);
		
		}
		


	}
	
	@PostMapping("cancel/DeleteBooking")
	public void CancelBooking(@RequestParam("bookId") String bookingId , HttpSession session, HttpServletResponse response) throws Exception {
		System.out.println(bookingId);
		session.setAttribute("HttpRequest", "DELETE");
		session.setAttribute("eventId",bookingService.getEventId(bookingId));
		session.setAttribute("bookingId",bookingId);
		String messageOrUrl = bookingService.cancelBooking(bookingId,response);
		if(messageOrUrl.contains("notification")) {
			response.sendRedirect(messageOrUrl); // url redirection for sending notification to participants...
		}
		else if (messageOrUrl.contains("Internal server Error")) {		
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write(messageOrUrl);	
		}	
		else {
			
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		response.getWriter().write(messageOrUrl);	
		}
	}
	
	@GetMapping("/userBookList")
	public ModelAndView showBookingList() {
	    ModelAndView modelAndView = new ModelAndView("UserBookingList");

	    try {
	        List<Booking> bookingList = null;
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String currentUserId = null;

	        if (authentication != null && authentication.isAuthenticated()) {
	            currentUserId = authentication.getName();
	        }
	        
	        bookingList = bookingService.getAllBookings(currentUserId);
	        modelAndView.addObject("bookingList", bookingList);
	        modelAndView.addObject("Booking", new Booking());
	    } catch (Exception e) {
	        modelAndView.addObject("error", "Failed to retrieve booking list: " + e.getMessage());
	    }

	    return modelAndView;
	}

}
