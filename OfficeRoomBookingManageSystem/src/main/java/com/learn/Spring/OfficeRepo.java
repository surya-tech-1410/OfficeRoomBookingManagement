package com.learn.Spring;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learn.Spring.model.Office;


@Repository
public interface OfficeRepo extends JpaRepository<Office, String> {

	Office findByOfficeName(String officeName);
	

}
