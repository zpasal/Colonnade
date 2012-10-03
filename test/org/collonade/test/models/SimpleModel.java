package org.collonade.test.models;

import org.colonnade.annotations.Column;
import org.colonnade.annotations.Id;
import org.colonnade.annotations.Table;

@Table(name="test_table")
public class SimpleModel  {
	
	// This is used for ID
	@Id private String id;
	@Column(family="data1") private String email;
	@Column(family="data1") private double income;
	@Column(family="data1") private Integer birthYear;
	
	public SimpleModel() {
		
	}
	public SimpleModel(String id, String email, double income, Integer birthYear) {
		super();
		this.id = id;
		this.email = email;
		this.income = income;
		this.birthYear = birthYear;
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
	public double getIncome() {
		return income;
	}
	public void setIncome(double income) {
		this.income = income;
	}
	public Integer getBirthYear() {
		return birthYear;
	}
	public void setBirthYear(Integer birthYear) {
		this.birthYear = birthYear;
	}
	
		
}
