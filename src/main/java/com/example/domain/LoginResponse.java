package com.example.domain;

public class LoginResponse {

	private String accessToken;
	private String tokenType;
	private String firstName;
	private String fullName;
	private String email;
	private String contactNumber;
	private String externalId;
	
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getExternalId() {
		return externalId;
	}
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	
	public LoginResponse(String accessToken, String tokenType, String firstName, String fullName, String email,
			String contactNumber, String externalId) {
		super();
		this.accessToken = accessToken;
		this.tokenType = tokenType;
		this.firstName = firstName;
		this.fullName = fullName;
		this.email = email;
		this.contactNumber = contactNumber;
		this.externalId = externalId;
	}
	
	public LoginResponse() {
		super();
	}
	
	@Override
	public String toString() {
		return "LoginResponse [accessToken=" + accessToken + ", tokenType=" + tokenType + ", firstName=" + firstName
				+ ", fullName=" + fullName + ", email=" + email + ", contactNumber=" + contactNumber + ", externalId="
				+ externalId + "]";
	}
	
}