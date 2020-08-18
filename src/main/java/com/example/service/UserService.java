package com.example.service;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.constants.ErrorMessages;
import com.example.constants.MessageConstants;
import com.example.constants.SuccessMessages;
import com.example.domain.LoginRequest;
import com.example.domain.LoginResponse;
import com.example.domain.ResponseObject;
import com.example.domain.User;
import com.example.domain.UserRegistration;
import com.example.repository.UserDetailsRepository;
import com.example.security.JwtTokenProvider;
import com.example.utils.CommonUtils;

@Service
public class UserService implements IUserService {

	@Autowired
	private UserDetailsRepository userDetailsRepository;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	public UserService(AuthenticationManager authenticationManager, UserDetailsRepository userRepository,
			PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
		this.authenticationManager = authenticationManager;
		this.userDetailsRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.tokenProvider = tokenProvider;
	}

	public boolean emailChecker(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";
		Pattern pat = Pattern.compile(emailRegex);
		if (email == null) {
			return false;
		}
		return pat.matcher(email).matches();
	}

	public boolean contactNumberChecker(String contactNumber) {
		String regex = "[0-9]+";
		Pattern pat = Pattern.compile(regex);
		if (contactNumber == null) {
			return false;
		}
		return pat.matcher(contactNumber).matches();
	}

	@Override
	public ResponseObject registerUser(UserRegistration userRegistration) {
		
		String spaceRegExp = "\\s{2,}";
		
		if(CommonUtils.isNull(userRegistration.getFirstName()) && CommonUtils.isNull(userRegistration.getMiddleName())
				&& CommonUtils.isNull(userRegistration.getLastName()) && CommonUtils.isNull(userRegistration.getAddress())
				&& CommonUtils.isNull(userRegistration.getEmailAddress()) && CommonUtils.isNull(userRegistration.getContactNumber())
				&& CommonUtils.isNull(userRegistration.getPassword())) {
			return new ResponseObject(null, ErrorMessages.DETAILS_REQUIRED, HttpStatus.BAD_REQUEST);
		}
		if(CommonUtils.isNull(userRegistration.getFirstName())) {
			return new ResponseObject(null, ErrorMessages.FIRST_NAME_REQUIRED, HttpStatus.BAD_REQUEST);
		}
		if(CommonUtils.isNull(userRegistration.getLastName())) {
			return new ResponseObject(null, ErrorMessages.LAST_NAME_REQUIRED, HttpStatus.BAD_REQUEST);
		}
		if(CommonUtils.isNull(userRegistration.getAddress())) {
			return new ResponseObject(null, ErrorMessages.ADDRESS_REQUIRED, HttpStatus.BAD_REQUEST);
		}
		if(CommonUtils.isNull(userRegistration.getEmailAddress())) {
			return new ResponseObject(null, ErrorMessages.EMAIL_ADDRESS_REQUIRED, HttpStatus.BAD_REQUEST);
		}
		boolean emailChecker=emailChecker(userRegistration.getEmailAddress());
		if(emailChecker==false) {
			return new ResponseObject(null, ErrorMessages.CHECK_EMAIL_FORMAT, HttpStatus.BAD_REQUEST);
		}
		if(CommonUtils.isNull(userRegistration.getContactNumber())) {
			return new ResponseObject(null, ErrorMessages.CONTACT_NUMBER_REQUIRED, HttpStatus.BAD_REQUEST);
		}
		if(userRegistration.getContactNumber().trim().length()>10 || userRegistration.getContactNumber().trim().length()<=9) {
			return new ResponseObject(null, ErrorMessages.CONTACT_NUMBER_LENGTH, HttpStatus.BAD_REQUEST);
		}
		boolean contactChecker=contactNumberChecker(userRegistration.getContactNumber());
		if(contactChecker==false) {
			return new ResponseObject(null, ErrorMessages.ONLY_NUMBERS, HttpStatus.BAD_REQUEST);
		}
		if(CommonUtils.isNull(userRegistration.getPassword())) {
			return new ResponseObject(null, ErrorMessages.PASSWORD_REQUIRED, HttpStatus.BAD_REQUEST);
		}
		try {
			
			List<User> users=userDetailsRepository.findAll();

			for(User user:users) {
				if(user.getEmailAddress().equalsIgnoreCase(userRegistration.getEmailAddress())
						&& user.getContactNumber().equalsIgnoreCase(userRegistration.getContactNumber())) {
					return new ResponseObject(null, ErrorMessages.EMAIL_CONTACT_EXITS, HttpStatus.BAD_REQUEST);
				}
				if(user.getEmailAddress().equalsIgnoreCase(userRegistration.getEmailAddress())) {
					return new ResponseObject(null, ErrorMessages.EMAIL_ALREADY_EXITS, HttpStatus.BAD_REQUEST);
				}
				if(user.getContactNumber().equals(userRegistration.getContactNumber())) {
					return new ResponseObject(null, ErrorMessages.CONTACT_ALREADY_EXITS, HttpStatus.BAD_REQUEST);
				}
			}
			
			String fullName=(userRegistration.getFirstName().trim()+" "+userRegistration.getMiddleName()
							.trim()+" "+userRegistration.getLastName().trim()).replaceAll(spaceRegExp, " ");
			User register=new User(UUID.randomUUID().toString(), userRegistration.getFirstName(), userRegistration.getMiddleName(), 
					userRegistration.getLastName(), fullName, userRegistration.getAddress(), userRegistration.getEmailAddress().toLowerCase(), 
					userRegistration.getContactNumber(), passwordEncoder.encode(userRegistration.getPassword()), 
					Calendar.getInstance(), Calendar.getInstance(), null);
			User response=userDetailsRepository.saveAndFlush(register);
			if(response==null) {
				return new ResponseObject(null, ErrorMessages.USER_REGISTRATION_FAILED, HttpStatus.BAD_REQUEST);
			}else {
				return new ResponseObject(SuccessMessages.USER_REGISTERED_SUCCESS, null, HttpStatus.OK);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseObject(null, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@Override
	public ResponseObject userLogin(LoginRequest loginRequest) {
		
		User userDetails=new User();
		
		if(CommonUtils.isNull(loginRequest.getUserId()) && CommonUtils.isNull(loginRequest.getPassword())) {
			return new ResponseObject(null, ErrorMessages.CREDENTIALS_REQUIRED, HttpStatus.BAD_REQUEST);
		}
		if(CommonUtils.isNull(loginRequest.getUserId())) {
			return new ResponseObject(null, ErrorMessages.USER_ID_REQUIRED, HttpStatus.BAD_REQUEST);
		}
		boolean emailChecker=emailChecker(loginRequest.getUserId());
		if(emailChecker==false) {
			return new ResponseObject(null, ErrorMessages.CHECK_EMAIL_FORMAT, HttpStatus.BAD_REQUEST);
		}
		if(loginRequest.getUserId().contains("@")) {
			userDetails=userDetailsRepository.getUserByEmailAddress(loginRequest.getUserId().toLowerCase());
			if(userDetails==null) {
				return new ResponseObject(null, ErrorMessages.NOT_REGISTERED_YET, HttpStatus.BAD_REQUEST);
			}
		}
		if(CommonUtils.isNull(loginRequest.getPassword())) {
			return new ResponseObject(null, ErrorMessages.ENTER_PASSWORD, HttpStatus.BAD_REQUEST);
		}
		if (!passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
			return new ResponseObject(null, ErrorMessages.INCORRECT_PASSWORD, HttpStatus.BAD_REQUEST);
		}
		
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(userDetails.getExternalId(), loginRequest.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwtToken = tokenProvider.generateJwtToken(authentication, userDetails);
			LoginResponse loginResponse=new LoginResponse(jwtToken, MessageConstants.BEARER, userDetails.getFirstName(), userDetails.getFullName(),
					userDetails.getEmailAddress(), userDetails.getContactNumber(), userDetails.getExternalId());
			return new ResponseObject(loginResponse, null, HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseObject(null, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
	}
	
}