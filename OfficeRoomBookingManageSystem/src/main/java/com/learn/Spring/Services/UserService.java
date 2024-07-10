package com.learn.Spring.Services;

import java.util.ArrayList;


import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.learn.Spring.UserRepo;
import com.learn.Spring.model.User;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	UserRepo userRepo;
	
	
	public ResponseEntity<String> registerUser(User user) {
		user.setUserId(IDGenerator.getUserId());
		userRepo.save(user);
	
		return new ResponseEntity<>("success",HttpStatus.CREATED);
	}


	public ResponseEntity<Optional<User>> getUserInfo(String id) {
		 try {
			   Optional<User> user = userRepo.findById(id);
	            return new ResponseEntity<>(user,HttpStatus.OK);
//					   return null;
	        }catch (Exception e){
	            e.printStackTrace();
	        }
		 
		 return null;
	}


	public List<User> getUserInfos(List<User> users) {
		
		// From request we have only userids in UserObject .... so usinf this function we will get userobject using userid.
		
//		List<String> userids = new ArrayList<>();
//		
//		for(int i = 0 ; i<users.size(); i++) {
//			userids.add(users.get(i).getUserId());
//		}
//		
//		return userRepo.findAllById(userids);
		
		List<String> userEmailsList = new ArrayList<>();
		
		for(int i = 0; i<users.size(); i++) {
			userEmailsList.add(users.get(i).getUserEmail());
		}
		
		System.out.println(userEmailsList);
		
		return userRepo.findByEmailIn(userEmailsList);
	}
	
	public User getOrganiserInfo(User user) {   // will change once change made in Booking.jsp....
		return userRepo.findByUserEmail(user.getUserEmail()).get();
	}


	public List<User> getAllUsers() {
		return userRepo.findAll();
	}
	
	
	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		 
        User user = userRepo.findByUserEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        
//        System.out.println(org.springframework.security.core.userdetails.User.builder()
//                .username(user.getUserEmail())
//                .password(user.getUserPassword())
//                .roles(user.getRole()) // Assuming getRoles() returns a list of user roles  
//                .build());

        // Note Roles to be add and take reference from System.out.println Arguments....
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserEmail())
                .password(user.getUserPassword())
                .build();
    }


}
