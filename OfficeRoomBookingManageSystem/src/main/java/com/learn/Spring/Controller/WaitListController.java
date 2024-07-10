package com.learn.Spring.Controller;


import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learn.Spring.Services.WaitListService;
import com.learn.Spring.model.Waitlist;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@RestController
@RequestMapping("Waitlist")
public class WaitListController {
	
	@Autowired
	WaitListService waitlistService;
	

	public WaitListController() {}
	
	@PostMapping("bookRoom/{officeName}")
	public void bookRoom(@RequestBody Waitlist waitlist ,@PathVariable String officeName ,  HttpSession session , HttpServletResponse response) throws IOException {
		
		try {
			waitlistService.addWaitlist(waitlist,officeName);
			
		}
		catch (Exception e) {
			System.out.print(e);
		}
	
	}

}
