package com.example.domain;

public class UserRegistration {

	private String firstName;
	private String middleName;
	private String lastName;
	private String address;
	private String emailAddress;
	private String contactNumber;
	private String password;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public UserRegistration(String firstName, String middleName, String lastName, String address, String emailAddress,
			String contactNumber, String password) {
		super();
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.address = address;
		this.emailAddress = emailAddress;
		this.contactNumber = contactNumber;
		this.password = password;
	}
	
	public UserRegistration() {
		super();
	}
	
	@Override
	public String toString() {
		return "UserRegistration [firstName=" + firstName + ", middleName=" + middleName + ", lastName=" + lastName
				+ ", address=" + address + ", emailAddress=" + emailAddress + ", contactNumber=" + contactNumber
				+ ", password=" + password + "]";
	}

}