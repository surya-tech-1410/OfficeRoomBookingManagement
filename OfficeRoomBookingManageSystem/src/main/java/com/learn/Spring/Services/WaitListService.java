package com.learn.Spring.Services;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.learn.Spring.WaitListRepo;
import com.learn.Spring.model.Room;
import com.learn.Spring.model.Waitlist;

import jakarta.mail.MessagingException;

//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;

@Service
public class WaitListService {
	
  //  private final JavaMailSender javaMailSender;

	@Autowired
	WaitListRepo waitlistRepo;
	
	@Autowired
	RoomService roomService;
	
	@Autowired
	OfficeService officeService;
	
	@Autowired
	EmailService emailService;
	
	public WaitListService() {}

	public void addWaitlist(Waitlist waitlist, String officeName) throws jakarta.mail.MessagingException{
	
		
		Room room = roomService.getRoom(waitlist.getRoom().getRoomName(), officeService.getOfficeId(officeName));
		waitlist.setRoom(room);
		waitlist.setWaitListId(IDGenerator.getWaitlistId());
		waitlistRepo.save(waitlist);		
		
	}

	public void getWaitListAndSendNotifications(Date startTime, Date endTime, Room room) throws MessagingException {
		
		List<Waitlist> waitlists = waitlistRepo.getWaitlist(startTime, endTime, room.getRoomId());
		System.out.println("waitlists..... " + waitlists);
		
		emailService.sendNotificationToWaitlisters(waitlists,room.getRoomName());
		
	}

}
