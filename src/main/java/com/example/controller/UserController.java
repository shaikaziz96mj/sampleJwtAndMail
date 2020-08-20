package com.example.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.LoginRequest;
import com.example.domain.ResponseObject;
import com.example.domain.UserRegistration;
import com.example.service.IUserService;
import com.example.utils.LoggerUtil;

@RestController
@RequestMapping("/api/v1")
public class UserController {
	
	private Logger logger=LoggerUtil.getLoggerObject(UserController.class);
	
	@Autowired
	private IUserService userService;

	@PostMapping("/registerUser")
	public ResponseObject userRegistration(@RequestBody UserRegistration userRegistration) {
		logger.info("userRegistration() initiated");
		return userService.registerUser(userRegistration);
	}
	
	@PostMapping("/userLogin")
	public ResponseObject userLogin(@RequestBody LoginRequest loginRequest) {
		logger.info("userLogin() initiated");
		return userService.userLogin(loginRequest);
	}
	
	@GetMapping("/allUsers")
	public ResponseObject fetchAllUsers(@RequestHeader("Authorization") String authToken) {
		logger.info("fetchAllUsers() initiated");
		return userService.fetchAllUsers(authToken);
	}
	
}