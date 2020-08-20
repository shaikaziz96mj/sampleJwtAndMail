package com.example.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
import com.example.utils.LoggerUtil;

@Service
public class UserService implements IUserService {

	private Logger logger = LoggerUtil.getLoggerObject(UserService.class);

	@Autowired
	private UserDetailsRepository userDetailsRepository;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtTokenProvider tokenProvider;
	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	public UserService(AuthenticationManager authenticationManager, UserDetailsRepository userRepository,
			PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
		this.authenticationManager = authenticationManager;
		this.userDetailsRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.tokenProvider = tokenProvider;
	}

	public boolean emailChecker(String email) {
		logger.debug("emailChecker(-) initiated with email:" + email);
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";
		Pattern pat = Pattern.compile(emailRegex);
		if (email == null) {
			return false;
		}
		return pat.matcher(email).matches();
	}

	public boolean contactNumberChecker(String contactNumber) {
		logger.debug("contactNumber(-) initiated with number:" + contactNumber);
		String regex = "[0-9]+";
		Pattern pat = Pattern.compile(regex);
		if (contactNumber == null) {
			return false;
		}
		return pat.matcher(contactNumber).matches();
	}

	private void triggerMail(String msgs, String subject, String mailAddress) {
		logger.debug("triggerMail(-) initiated with email and subject:" + mailAddress + "," + subject);
		MimeMessage message = null;
		MimeMessageHelper helper = null;
		try {
			message = mailSender.createMimeMessage();
			// prepare email messages
			helper = new MimeMessageHelper(message, true);
			helper.setTo(new InternetAddress(mailAddress));
			helper.setSentDate(new Date());
			helper.setText(msgs);
			helper.setSubject(subject);
			mailSender.send(message);
			System.out.println("message delivered successfully");
		} // try
		catch (MessagingException me) {
			me.printStackTrace();
			logger.error("email sending failed due to " + me.getCause());
		}
	}

	@Override
	public ResponseObject registerUser(UserRegistration userRegistration) {

		String spaceRegExp = "\\s{2,}";
		logger.info("userRegistration object:" + userRegistration);
		if (CommonUtils.isNull(userRegistration.getFirstName()) && CommonUtils.isNull(userRegistration.getMiddleName())
				&& CommonUtils.isNull(userRegistration.getLastName())
				&& CommonUtils.isNull(userRegistration.getAddress())
				&& CommonUtils.isNull(userRegistration.getEmailAddress())
				&& CommonUtils.isNull(userRegistration.getContactNumber())
				&& CommonUtils.isNull(userRegistration.getPassword())) {
			return new ResponseObject(null, ErrorMessages.DETAILS_REQUIRED, HttpStatus.BAD_REQUEST);
		}
		if (CommonUtils.isNull(userRegistration.getFirstName())) {
			return new ResponseObject(null, ErrorMessages.FIRST_NAME_REQUIRED, HttpStatus.BAD_REQUEST);
		}
		if (CommonUtils.isNull(userRegistration.getLastName())) {
			return new ResponseObject(null, ErrorMessages.LAST_NAME_REQUIRED, HttpStatus.BAD_REQUEST);
		}
		if (CommonUtils.isNull(userRegistration.getAddress())) {
			return new ResponseObject(null, ErrorMessages.ADDRESS_REQUIRED, HttpStatus.BAD_REQUEST);
		}
		if (CommonUtils.isNull(userRegistration.getEmailAddress())) {
			return new ResponseObject(null, ErrorMessages.EMAIL_ADDRESS_REQUIRED, HttpStatus.BAD_REQUEST);
		}
		boolean emailChecker = emailChecker(userRegistration.getEmailAddress());
		if (emailChecker == false) {
			return new ResponseObject(null, ErrorMessages.CHECK_EMAIL_FORMAT, HttpStatus.BAD_REQUEST);
		}
		if (CommonUtils.isNull(userRegistration.getContactNumber())) {
			return new ResponseObject(null, ErrorMessages.CONTACT_NUMBER_REQUIRED, HttpStatus.BAD_REQUEST);
		}
		if (userRegistration.getContactNumber().trim().length() > 10
				|| userRegistration.getContactNumber().trim().length() <= 9) {
			return new ResponseObject(null, ErrorMessages.CONTACT_NUMBER_LENGTH, HttpStatus.BAD_REQUEST);
		}
		boolean contactChecker = contactNumberChecker(userRegistration.getContactNumber());
		if (contactChecker == false) {
			return new ResponseObject(null, ErrorMessages.ONLY_NUMBERS, HttpStatus.BAD_REQUEST);
		}
		if (CommonUtils.isNull(userRegistration.getPassword())) {
			return new ResponseObject(null, ErrorMessages.PASSWORD_REQUIRED, HttpStatus.BAD_REQUEST);
		}
		try {

			List<User> users = userDetailsRepository.findAll();

			for (User user : users) {
				if (user.getEmailAddress().equalsIgnoreCase(userRegistration.getEmailAddress())
						&& user.getContactNumber().equalsIgnoreCase(userRegistration.getContactNumber())) {
					return new ResponseObject(null, ErrorMessages.EMAIL_CONTACT_EXITS, HttpStatus.BAD_REQUEST);
				}
				if (user.getEmailAddress().equalsIgnoreCase(userRegistration.getEmailAddress())) {
					return new ResponseObject(null, ErrorMessages.EMAIL_ALREADY_EXITS, HttpStatus.BAD_REQUEST);
				}
				if (user.getContactNumber().equals(userRegistration.getContactNumber())) {
					return new ResponseObject(null, ErrorMessages.CONTACT_ALREADY_EXITS, HttpStatus.BAD_REQUEST);
				}
			}

			String fullName = (userRegistration.getFirstName().trim() + " " + userRegistration.getMiddleName().trim()
					+ " " + userRegistration.getLastName().trim()).replaceAll(spaceRegExp, " ");
			User register = new User(UUID.randomUUID().toString(), userRegistration.getFirstName(),
					userRegistration.getMiddleName(), userRegistration.getLastName(), fullName,
					userRegistration.getAddress(), userRegistration.getEmailAddress().toLowerCase(),
					userRegistration.getContactNumber(), passwordEncoder.encode(userRegistration.getPassword()),
					Calendar.getInstance(), Calendar.getInstance(), null);
			User response = userDetailsRepository.saveAndFlush(register);
			logger.info("User registration successful");
			String registrationMsg = "Dear User,\r\n" + "\r\n"
					+ "Thank you for your registration with our Sample Development Application.!" + "\r\n"
					+ "You can now login using the registered email id\r\n" + "\r\n" + "Best Regards\r\n"
					+ "Developer Team";
			triggerMail(registrationMsg, MessageConstants.REGISTRATION, userRegistration.getEmailAddress());
			logger.info("E-mail send to registered email id");
			if (response == null) {
				logger.error("User registration failed");
				return new ResponseObject(null, ErrorMessages.USER_REGISTRATION_FAILED, HttpStatus.BAD_REQUEST);
			} else {
				logger.info("User registered");
				return new ResponseObject(SuccessMessages.USER_REGISTERED_SUCCESS, null, HttpStatus.OK);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("User registration failed due to following reason " + e.getCause());
			return new ResponseObject(null, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseObject userLogin(LoginRequest loginRequest) {

		User userDetails = new User();
		logger.info("loginRequest object:" + loginRequest);
		if (CommonUtils.isNull(loginRequest.getUserId()) && CommonUtils.isNull(loginRequest.getPassword())) {
			return new ResponseObject(null, ErrorMessages.CREDENTIALS_REQUIRED, HttpStatus.BAD_REQUEST);
		}
		if (CommonUtils.isNull(loginRequest.getUserId())) {
			return new ResponseObject(null, ErrorMessages.USER_ID_REQUIRED, HttpStatus.BAD_REQUEST);
		}
		boolean emailChecker = emailChecker(loginRequest.getUserId());
		if (emailChecker == false) {
			return new ResponseObject(null, ErrorMessages.CHECK_EMAIL_FORMAT, HttpStatus.BAD_REQUEST);
		}
		if (loginRequest.getUserId().contains("@")) {
			userDetails = userDetailsRepository.getUserByEmailAddress(loginRequest.getUserId().toLowerCase());
			if (userDetails == null) {
				return new ResponseObject(null, ErrorMessages.NOT_REGISTERED_YET, HttpStatus.BAD_REQUEST);
			}
		}
		if (CommonUtils.isNull(loginRequest.getPassword())) {
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
			LoginResponse loginResponse = new LoginResponse(jwtToken, MessageConstants.BEARER,
					userDetails.getFirstName(), userDetails.getFullName(), userDetails.getEmailAddress(),
					userDetails.getContactNumber(), userDetails.getExternalId());
			logger.info("user login response:" + loginResponse);
			String logInMsg = "Dear User,\r\n" + "\r\n" + "You have logged in on Sample Development Application at "
					+ new Date() + "\r\n" + "\r\nBest Regards\r\n" + "Developer Team";
			triggerMail(logInMsg, MessageConstants.LOGGED_IN, loginRequest.getUserId());
			logger.info("E-mail send to registered email for user login");
			return new ResponseObject(loginResponse, null, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error occured in userLogin() due to " + e.getCause());
			return new ResponseObject(null, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseObject fetchAllUsers(String authToken) {
		logger.info("User auth token(fetchAllUSers()):" + authToken);
		if (CommonUtils.isNull(authToken)) {
			return new ResponseObject(null, ErrorMessages.AUTHENTICATION_TOKEN_REQUIRED, HttpStatus.BAD_REQUEST);
		}
		try {
			List<User> userList = userDetailsRepository.findAll(Sort.by(Direction.ASC, "id"));
			if (userList.isEmpty()) {
				return new ResponseObject(null, ErrorMessages.NO_RECORDS_FOUND, HttpStatus.BAD_REQUEST);
			}
			return new ResponseObject(userList, null, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error occured in fetchAllUsers() due to "+e.getCause());
			return new ResponseObject(null, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}