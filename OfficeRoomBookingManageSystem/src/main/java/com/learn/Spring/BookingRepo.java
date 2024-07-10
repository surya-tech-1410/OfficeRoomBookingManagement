package com.learn.Spring;

import java.util.Date;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.learn.Spring.model.Booking;

@Repository
public interface BookingRepo extends JpaRepository<Booking, String> {
	
	
	@Transactional
	@Query("SELECT b FROM Booking b WHERE ((:newStartTime BETWEEN b.startTime AND b.endTime) OR (:newEndTime BETWEEN b.startTime AND b.endTime) OR (b.startTime BETWEEN :newStartTime AND :newEndTime) OR (b.endTime BETWEEN :newStartTime AND :newEndTime)) AND b.room.roomId = :roomId")
	List<Booking> findOverLappingBookings(@Param ("newStartTime") Date newStartTime , @Param ("newEndTime") Date newEndTime ,@Param ("roomId") String roomId );
	
	@Transactional
	@Modifying
	@Query("UPDATE Booking b SET b.eventId = :eventId WHERE b.bookId = :bookId")
	void addEventIdForBookingId(@Param ("eventId") String eventId , @Param ("bookId") String bookId);
	
	@Transactional
	@Query("SELECT b.eventId FROM Booking b WHERE b.bookId = :bookId")
	Optional<String> getEventIdUsingBookingId(@Param ("bookId") String bookId);
	
	@Transactional
	@Query("SELECT b from Booking b WHERE b.room.roomId = :roomId AND DATE(b.startTime) = DATE(:StartTime)")
	List<Booking> getBookings(@Param ("StartTime") Date StartTime ,@Param ("roomId") String roomId );
	
	@Transactional
	@Query("SELECT b from Booking b WHERE b.user.userEmail = :userEmail")
	List<Booking> getAllUserBookings(@Param ("userEmail") String userEmail );
	
}
