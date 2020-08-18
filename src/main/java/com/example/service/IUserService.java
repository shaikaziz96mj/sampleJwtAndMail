package com.example.service;

import com.example.domain.ResponseObject;
import com.example.domain.UserRegistration;

public interface IUserService {

	public ResponseObject registerUser(UserRegistration userRegistration);

}