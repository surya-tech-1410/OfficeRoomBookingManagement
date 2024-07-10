package com.learn.Spring;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.learn.spring.model")
public class OfficeRoomBookingManageSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(OfficeRoomBookingManageSystemApplication.class, args);
	}

}
 