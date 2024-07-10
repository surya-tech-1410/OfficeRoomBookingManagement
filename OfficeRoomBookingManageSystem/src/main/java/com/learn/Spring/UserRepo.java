package com.learn.Spring;

import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.learn.Spring.model.Booking;
import com.learn.Spring.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, String>{
	
	
    Optional<User> findById(String id);
    
    
    @Transactional
    @Query("SELECT u from User u WHERE u.userEmail IN :userEmailsList")
	List<User> findByEmailIn(@Param ("userEmailsList") List<String> userEmailsList);


	Optional<User> findByUserEmail(String userEmail);


}
