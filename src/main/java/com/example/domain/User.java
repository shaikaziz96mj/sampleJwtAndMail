package com.example.domain;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user")
public class User {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name="external_id")
	private String externalId;
	@Column(name="first_name")
	private String firstName;
	@Column(name="middle_name")
	private String middleName;
	@Column(name="last_name")
	private String lastName;
	@Column(name="full_name")
	private String fullName;
	@Column(name="address")
	private String address;
	@Column(name="email_address")
	private String emailAddress;
	@Column(name="contact_number")
	private String contactNumber;
	@Column(name="password")
	private String password;
	@Column(name="created_at")
	private Calendar createdAt;
	@Column(name="modified_at")
	private Calendar modidiedAt;
	@Column(name="modified_by")
	private String modifiedBy;
	
	public Long getId() {
		return id;
	}
	public String getExternalId() {
		return externalId;
	}
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
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
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
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
	public Calendar getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Calendar createdAt) {
		this.createdAt = createdAt;
	}
	public Calendar getModidiedAt() {
		return modidiedAt;
	}
	public void setModidiedAt(Calendar modidiedAt) {
		this.modidiedAt = modidiedAt;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public User(String externalId, String firstName, String middleName, String lastName, String fullName,
			String address, String emailAddress, String contactNumber, String password, Calendar createdAt,
			Calendar modidiedAt, String modifiedBy) {
		super();
		this.externalId = externalId;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.fullName = fullName;
		this.address = address;
		this.emailAddress = emailAddress;
		this.contactNumber = contactNumber;
		this.password = password;
		this.createdAt = createdAt;
		this.modidiedAt = modidiedAt;
		this.modifiedBy = modifiedBy;
	}

	public User() {
		super();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", externalId=" + externalId + ", firstName=" + firstName + ", middleName="
				+ middleName + ", lastName=" + lastName + ", fullName=" + fullName + ", address=" + address
				+ ", emailAddress=" + emailAddress + ", contactNumber=" + contactNumber + ", password=" + password
				+ ", createdAt=" + createdAt + ", modidiedAt=" + modidiedAt + ", modifiedBy=" + modifiedBy + "]";
	}
	
}