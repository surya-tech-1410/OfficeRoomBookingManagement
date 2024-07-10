package com.learn.Spring.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.api.services.calendar.Calendar;
import com.learn.Spring.OfficeRepo;
import com.learn.Spring.model.Office;
import com.learn.Spring.model.Room;
import com.microsoft.graph.authentication.IAuthenticationProvider;

@Service
public class OfficeService {

	@Autowired
	OfficeRepo officeRepo;
	@Autowired
	RoomService roomService;

	public ResponseEntity<String> registerOffice(Office office) {
		office.setOfficeId(IDGenerator.getOfficeId());
		officeRepo.save(office);
//		return new Calendar(null, null, null).
		return new ResponseEntity<>("success", HttpStatus.CREATED);
	}

	public ResponseEntity<List<Room>> getAllRooms(String office_id) {
		
		List <Room> rooms = roomService.getRoomsbyOfficeId(office_id);
		return new ResponseEntity<>(rooms, HttpStatus.OK);
		
	}
	

	public void deleteOfficeByOfficeId(String office_id) throws Exception {
		try {
			officeRepo.deleteById(office_id);
			
		} catch (Exception e) {
			System.err.println("Failed with error: " + e);
			throw new Exception(e);
		}
	}

	public List<Office> getOffices() {
		return officeRepo.findAll();
	}

	public List<Room> getRoomsByOffice(String officeName) {
		Office office  = officeRepo.findByOfficeName(officeName);
		if(office == null) {
			return null;
		}
		System.out.println(office.getOfficeId());
		return roomService.getRoomsbyOfficeId(office.getOfficeId());
	}

	public String getOfficeId(String officeName) {
          return officeRepo.findByOfficeName(officeName).getOfficeId();
	}

}
