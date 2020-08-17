package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.domain.User;

public interface UserDetailsRepository extends JpaRepository<User, Long> {

	@Query(value = "select u from User u where u.externalId=:externalId", nativeQuery=false)
	public User getUserByExternalId(String externalId);

}