package com.capgemini.jdbc.addressbook;

public class Contact {
	private String address_book_name;
	private Integer address_book_id;
	private Integer contact_id;
	private String first_name;
	private String last_name;
	private String phone_number;
	private String email;
	private String contact_type;
	private String address;
	private String city;
	private String state;
	private String zip;
	private String date_added;

	public Contact(String addBook_name, int add_id, int contact_id, String first_name, String last_name,
			String phone_number, String email, String contact_type, String address, String city, String state,
			String zip, String date_added) {
		this.address_book_name = addBook_name;
		this.address_book_id = add_id;
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
		this.date_added = date_added;
	}
	
	public Contact(String first_name, String last_name, String address, String city, String state, String zip,
				   String phone_number, String email) {
		this.first_name = first_name;
		this.last_name = last_name;
		this.phone_number = phone_number;
		this.email = email;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}

	public int getAddress_book_id() {
		return address_book_id;
	}

	public String getAddress_book_name() {
		return address_book_name;
	}

	public int getContact_id() {
		return contact_id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public String getEmail() {
		return email;
	}

	public String getContact_type() {
		return contact_type;
	}

	public String getAddress() {
		return address;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getZip() {
		return zip;
	}
	
	public String getDate_added() {
		return date_added;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + contact_id;
		return result;
	}
}
