package com.learn.Spring.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.learn.Spring.RoomRepo;
import com.learn.Spring.model.Room;


@Service
public class RoomService {

	@Autowired
	RoomRepo roomRepo;

	public ResponseEntity<String> addRoom(Room room) {
		String id =IDGenerator.getRoomId();
		System.out.println(id);
		room.setRoomId(id);
		roomRepo.save(room);
		return new ResponseEntity<>("Successfully added room to office", HttpStatus.CREATED);

	}

	public ResponseEntity<Optional<Room>> getRoomInfo(String Id) {

		Optional<Room> room = roomRepo.findById(Id);
	    //	Room roomInfo = room.get();
		// System.out.println(roomInfo.getOffice().getOfficeName());
		System.out.println(room);
		return new ResponseEntity<>(room, HttpStatus.OK);
	}

	public List<Room> getRoomsbyOfficeId(String officeId) {
		List<Room> roomList = roomRepo.findByOfficeOfficeId(officeId);
		return roomList;
	}

	
	public Room getRoom(String roomName ,String officeId) {
		return roomRepo.findRoomByRoomNameAndOfficeId(roomName,officeId);
	}
}
