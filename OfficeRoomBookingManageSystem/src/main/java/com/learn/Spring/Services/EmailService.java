package com.learn.Spring.Services;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.learn.Spring.model.Waitlist;

import jakarta.mail.MessagingException;

@Service
public class EmailService {


	public EmailService() {}



	public void sendNotificationToWaitlisters(List<Waitlist> waitlists, String roomName) throws MessagingException {

		  JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
	        javaMailSender.setHost("smtp.gmail.com"); // Set SMTP host.
	        javaMailSender.setPort(587); // Set port.
	        javaMailSender.setUsername("SET_USER_MAIL"); // Set UserName.
	        javaMailSender.setPassword("SET_USER_PASSWORD"); // Set Password.
	        
	        Properties props = javaMailSender.getJavaMailProperties();
	        props.put("mail.smtp.starttls.enable", "true");
	        

	        jakarta.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
		        // Set email properties
		        
		        
		        for(int i = 0 ;i<waitlists.size();i++) {
				helper.setTo(waitlists.get(i).getUser().getUserEmail());
				helper.setSubject("Room Availability Notification");
				helper.setText("Dear User, \n\nThe room is now available between " + waitlists.get(i).getStartTime() + " and " + waitlists.get(i).getEndTime() + " Please proceed to book it. " );
				javaMailSender.send(mimeMessage);
		        }
				
		
	}

}
