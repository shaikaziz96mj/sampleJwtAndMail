package com.example.domain;

public class SignUpResponse {

	private String fullName;
	private String email;
	private String contactNumber;
	private String address;
	
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public SignUpResponse(String fullName, String email, String contactNumber, String address) {
		super();
		this.fullName = fullName;
		this.email = email;
		this.contactNumber=contactNumber;
		this.address = address;
	}
	
	public SignUpResponse() {
		super();
	}
	
	@Override
	public String toString() {
		return "SignUpResponse [fullName=" + fullName + ", email=" + email
				+ ", contactNumber=" + contactNumber + ", address=" + address + "]";
	}

}