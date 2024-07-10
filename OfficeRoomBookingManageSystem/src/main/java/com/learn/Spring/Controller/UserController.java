package com.learn.Spring.Controller;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learn.Spring.Services.UserService;
import com.learn.Spring.model.User;

@RestController
@RequestMapping("User")
public class UserController {

	
	@Autowired
	UserService userService;
	
    @PostMapping("RegisterUser")
    public ResponseEntity<String> registerInfo(@RequestBody User user){
        return userService.registerUser(user);
    }
	
	@GetMapping("getUser/{Id}")
    public ResponseEntity<Optional<User>> getUser(@PathVariable String Id){
        return userService.getUserInfo(Id);
    }
	
	@GetMapping("users")
    public List<User> getUsers(){
        return userService.getAllUsers();
    }
	
	@PostMapping("/signupUser")
	public void signupSubmit(@RequestBody User user) {
		System.out.println("check");
		userService.registerUser(user);
	}
	
    @GetMapping("/signup")
    public String showSignupForm() {
        return "Signup";
    }

}


