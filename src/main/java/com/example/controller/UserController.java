package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.ResponseObject;
import com.example.domain.UserRegistration;
import com.example.service.IUserService;

@RestController
@RequestMapping("/api/v1")
public class UserController {
	
	@Autowired
	private IUserService userService;

	@PostMapping("/registerUser")
	public ResponseObject userRegistration(@RequestBody UserRegistration userRegistration) {
		return userService.registerUser(userRegistration);
	}
	
}