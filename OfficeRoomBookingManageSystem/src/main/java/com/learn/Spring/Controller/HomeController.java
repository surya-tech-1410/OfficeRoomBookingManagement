package com.learn.Spring.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.learn.Spring.Services.BookingService;
import com.learn.Spring.Services.OfficeService;
import com.learn.Spring.model.Office;
import com.learn.Spring.model.Room;

import jakarta.mail.MessagingException;

@Controller
public class HomeController {

	public HomeController() {}
	
	@Autowired
	OfficeService officeService;
	
	@Autowired
	BookingService bookingService;
	
	@ModelAttribute("Office")
	public Office getOffice() {
		return new Office();
	}
	
	@ModelAttribute("Room")
	public Room getRoom() {
		return new Room();
	}
	
	 
	@RequestMapping("/home") 
    public ModelAndView home() throws MessagingException {			
	ModelAndView mv = new ModelAndView("roomList");
    List<Room> rooms = officeService.getRoomsByOffice("AdpIndia");
	System.out.println(rooms);
    mv.addObject("rooms", rooms);
	mv.addObject("officeName","AdpIndia");
	return mv;
	}
	
	 

    @GetMapping("/loginForm")
    public String login() {
        return "login1"; 
    }
    
    @GetMapping("/signup")
    public String showSignupForm() {
        return "Signup";
    }

}
