package com.capgemini.jdbc.addressbook;

public class Contact {
	private String company_name;
	private int contact_id;
	private String first_name;
	private String last_name;
	private String phone_number;
	private String email;
	private String contact_type;
	private String address;
	private String city;
	private String state;
	private String zip;

	public Contact(String comp_name, int contact_id, String first_name, String last_name, String phone_number,
			String email, String contact_type, String address, String city, String state, String zip) {
		this.company_name = comp_name;
		this.contact_id = contact_id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.phone_number = phone_number;
		this.email = email;
		this.contact_type = contact_type;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}

	public static void main(String[] args) {
		System.out.println("Hello World!");
	}
}
