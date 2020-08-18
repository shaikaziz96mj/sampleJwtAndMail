package com.example.service;

import com.example.domain.LoginRequest;
import com.example.domain.ResponseObject;
import com.example.domain.UserRegistration;

public interface IUserService {

	public ResponseObject registerUser(UserRegistration userRegistration);

	public ResponseObject userLogin(LoginRequest loginRequest);

}