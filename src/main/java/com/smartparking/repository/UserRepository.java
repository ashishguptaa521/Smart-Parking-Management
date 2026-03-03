package com.smartparking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartparking.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

	// Find user by username
	Optional<User> findByUsername(String username);

}
