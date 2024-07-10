package com.learn.Spring.Services;

import java.util.Random;

import org.springframework.stereotype.Service;


public class IDGenerator {

	public static String getUserId() {
		return "U" + new Random().nextInt();
	}

	public static String getRoomId() {
		return "R" + new Random().nextInt();
	}

	public static String getBookId() {
		return "B" + new Random().nextInt();
	}

	public static String getOfficeId() {
		return "O" + new Random().nextInt();
	}

	public static String getStatusId() {
		return "S" + new Random().nextInt();
	}
	
	public static String getWaitlistId() {
		return "W" + new Random().nextInt();
	}
}
