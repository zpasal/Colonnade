package org.collonade.examples.model;

import java.util.ArrayList;

import org.collonade.annotations.Column;
import org.collonade.annotations.Id;
import org.collonade.annotations.Table;
import org.collonade.serializer.JSONSerializer;

// We are mappint to table "user"
@Table(name="user")
public class User  {
	
	// This is used for ID
	@Id private String id;
	@Column(family="data") private String email;
	@Column(family="data") private String password;
	@Column(family="data") private String firstname;
	@Column(family="data") private String lastname;
	@Column(family="data") private Integer birthYear;
	@Column(family="data", serializer=JSONSerializer.class) private ArrayList<Address> addresses;
	
	public User() {	
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Integer getBirthYear() {
		return birthYear;
	}

	public void setBirthYear(Integer birthYear) {
		this.birthYear = birthYear;
	}

	public ArrayList<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(ArrayList<Address> addresses) {
		this.addresses = addresses;
	}
	
}
