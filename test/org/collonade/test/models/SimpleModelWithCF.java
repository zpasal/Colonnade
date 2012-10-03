package org.collonade.test.models;

import org.colonnade.annotations.*;

@Table(name="test_table")
public class SimpleModelWithCF {	
	// This is used for ID
	@Id private String id;
	@Column(family="data1") private String firstname;
	@Column(family="data2") private String lastname;
	
	public SimpleModelWithCF() {
	}
	public SimpleModelWithCF(String id, String firstname, String lastname) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	
	
}
