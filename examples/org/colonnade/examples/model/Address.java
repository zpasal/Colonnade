package org.colonnade.examples.model;

public class Address {
	private String address;
	private String city;
	private String postalCode;

	public Address() {
	}
	
	public Address(String address, String city, String postalCode) {
		this.address = address;
		this.city = city;
		this.postalCode = postalCode;
	}

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
	
}
