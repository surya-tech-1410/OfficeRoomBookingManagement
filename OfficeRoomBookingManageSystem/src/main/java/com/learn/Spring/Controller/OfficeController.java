package com.learn.Spring.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.learn.Spring.Services.OfficeService;
import com.learn.Spring.model.Office;
import com.learn.Spring.model.Room;

@RestController
@RequestMapping("Office")
public class OfficeController {

	@Autowired
	OfficeService officeService;

	
	@PostMapping("RegisterOffice")
	public ResponseEntity<String> registerInfo(@RequestBody Office office) {
		return officeService.registerOffice(office);

	}
	

	@GetMapping("getRooms/{office_id}")
	public ResponseEntity<List<Room>> getAllRooms(@PathVariable("office_id") String office_id) {
		return officeService.getAllRooms(office_id);
	}
	
	@GetMapping("/offices")
	public List<Office> getOffices() {
		return officeService.getOffices();
	}
	
	@GetMapping("rooms/{officeName}") 
	public List<Room> getOffices(@PathVariable("officeName") String officeName) {
		return officeService.getRoomsByOffice(officeName);
	}
	
	@PostMapping("filterRooms")  // Need to edit this endpoint for filterrooms using modelAndView...
	public ModelAndView getRooms(@ModelAttribute("Office") Office office) {
		ModelAndView mv = new ModelAndView("roomList");
		String officeName =  office.getOfficeName();
		System.out.println( "check : " + officeName);
		List<Room> Allrooms = officeService.getRoomsByOffice(officeName);
		mv.addObject("Room",new Room()); // need to learn about this..
		System.out.println(Allrooms);
		mv.addObject("rooms", Allrooms);
		mv.addObject("officeName",officeName);
		return mv;
	}

	@DeleteMapping("delete/{office_id}")
	public String deleteRoom(@PathVariable("office_id") String office_id) throws Exception {
		officeService.deleteOfficeByOfficeId(office_id);
		return "successfully deleted";
	}

}
