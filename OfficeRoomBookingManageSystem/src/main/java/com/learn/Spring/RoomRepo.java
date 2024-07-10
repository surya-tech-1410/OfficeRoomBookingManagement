package com.learn.Spring;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.learn.Spring.model.Booking;
import com.learn.Spring.model.Room;

@Repository
public interface RoomRepo extends JpaRepository<Room, String>{
	
    Optional<Room> findById(String id);
	List<Room> findByOfficeOfficeId(String officeId);
	List<Room> findByOfficeOfficeName(String Name);
	
	@Transactional
	@Query("SELECT r from Room r WHERE r.office.officeId = :officeId AND r.roomName = :roomName")
	Room findRoomByRoomNameAndOfficeId(@Param ("roomName") String roomName, @Param ("officeId") String officeId);
}
