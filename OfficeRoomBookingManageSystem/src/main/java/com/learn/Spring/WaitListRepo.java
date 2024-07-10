package com.learn.Spring;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.learn.Spring.model.Waitlist;


@Repository
public interface WaitListRepo extends JpaRepository<Waitlist, String>{
	
	
	@Transactional
	@Query("SELECT w FROM Waitlist w WHERE ((:newStartTime BETWEEN w.startTime AND w.endTime) OR (:newEndTime BETWEEN w.startTime AND w.endTime) OR (w.startTime BETWEEN :newStartTime AND :newEndTime) OR (w.endTime BETWEEN :newStartTime AND :newEndTime)) AND w.room.roomId = :roomId")
	List<Waitlist> getWaitlist(@Param ("newStartTime") Date newStartTime , @Param ("newEndTime") Date newEndTime ,@Param ("roomId") String roomId );

}
