package com.learn.Spring;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learn.Spring.model.BookingStatus;


@Repository
public interface BookingStatusRepo extends JpaRepository<BookingStatus, String>  {

	
}
