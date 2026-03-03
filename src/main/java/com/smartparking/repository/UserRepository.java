package com.smartparking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartparking.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	// Find user by username
	User findByUsername(String username);

}
