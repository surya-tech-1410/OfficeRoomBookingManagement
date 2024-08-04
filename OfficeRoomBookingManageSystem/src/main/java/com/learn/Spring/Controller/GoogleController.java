package com.learn.Spring.Controller;

import java.util.Collections;
import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;
import jakarta.servlet.http.HttpServletResponse;

import com.google.api.services.calendar.Calendar.Events;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("login")
public class GoogleController {

	private static final String APPLICATION_NAME = "";
	private static HttpTransport httpTransport;
	private static JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static com.google.api.services.calendar.Calendar client;

	GoogleClientSecrets clientSecrets;
        GoogleAuthorizationCodeFlow flow;
	Credential credential;
	
	@GetMapping("/notification/google")
	public String googleConnectionStatus(HttpSession session) throws Exception {
		return authorize();
	} 
	
	
	// return client from this endpoint ....
	@GetMapping("/oauth2/code/google")
	public RedirectView oauth2Callback(@RequestParam(value = "code") String code , HttpSession session) {

		System.out.println(code);
		com.google.api.services.calendar.model.Events eventList;
		String message;
		final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
		com.google.api.services.calendar.Calendar client;
		

		Credential credential;

		try {
	
			TokenResponse response = flow.newTokenRequest(code).setRedirectUri("http://localhost:8080/login/oauth2/code/google").execute();
			System.out.println(response);		
			credential = flow.createAndStoreCredential(response, "YOUR_PRIMARY_EMAIL");
			System.out.println("Credential : " + credential);
			client = new com.google.api.services.calendar.Calendar.Builder(httpTransport, JSON_FACTORY, credential)
					.setApplicationName("Google Calendar App test").build();
			System.out.println("Client : " + client);
			Events events = client.events();
			session.setAttribute("CalendarEvent", events);
			
			// above code is enough for this url .......
			
			System.out.println("HttpCode check : " + session.getAttribute("HttpRequest") );
			System.out.println("Book Object check : " + session.getAttribute("bookingDetails"));
			
			if(session.getAttribute("HttpRequest").equals("POST") || session.getAttribute("HttpRequest") == "POST") {
				return new RedirectView("http://localhost:8080/Event/createEvent");
			}
			
			else if(session.getAttribute("HttpRequest").equals("PUT")) {
				return new RedirectView("http://localhost:8080/Event/editEvent");
				
			}
			else {
				return new RedirectView("http://localhost:8080/Event/cancelEvent");
				
			}
		} catch(Exception e) {
			return new RedirectView("http://localhost:8080/home");
		}

	}
	
	private String authorize() throws Exception {
		AuthorizationCodeRequestUrl authorizationUrl;
		if (flow == null) {
			Details web = new Details();
			web.setClientId("YOUR_GOOGLE_CLIENT_ID");
			web.setClientSecret("YOUR_GOOGLE_CLIENT_SECRET");
			clientSecrets = new GoogleClientSecrets().setWeb(web);
			 httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
					Collections.singleton(CalendarScopes.CALENDAR)).build();
		}
		authorizationUrl = flow.newAuthorizationUrl().setRedirectUri("http://localhost:8080/login/oauth2/code/google");
		String var = authorizationUrl.build();
		return var;
	}
}
