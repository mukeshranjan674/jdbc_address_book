package com.capgemini.jdbc.addressbook;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.capgemini.jdbc.addressbook.AddressBookException.Type;

public class AddressBookServiceDB {
	private static AddressBookServiceDB addressBookServiceJDBC;
	
	private AddressBookServiceDB() {
		
	}
	public static AddressBookServiceDB getInstance() {
		if(addressBookServiceJDBC == null)
			addressBookServiceJDBC = new AddressBookServiceDB();
		return addressBookServiceJDBC;
	}
	
	public List<Contact> getContacts() throws AddressBookException {
		List<Contact> contacts = new ArrayList<Contact>();
		Connection connection = new DBConnection().getConnection();
		try {
			Statement statement = connection.createStatement();
			String sql = "select a.name, b.contact_id, b.first_name, b.last_name, "
					   + "c.phone_no, c.email, c.contact_type, "
					   + "d.address, d.city, d.state, d.zip from "
					   + "address_book a, person b, contact_details c, person_address d "
					   + "where "
				   	   + "a.book_id = b.book_id and "
					   + "b.contact_id = c.contact_id and "
					   + "b.contact_id = d.contact_id ";
			ResultSet resultSet = statement.executeQuery(sql);
			while(resultSet.next()) {
				String comp_name = resultSet.getString(1);
				int contact_id = resultSet.getInt(2);
				String first_name = resultSet.getString(3);
				String last_name = resultSet.getString(4);
				String phone_number = resultSet.getString(5);
				String email = resultSet.getString(6);
				String contact_type = resultSet.getString(7);
				String address = resultSet.getString(8);
				String city = resultSet.getString(9);
				String state = resultSet.getString(10);
				String zip = resultSet.getString(11);
				
				Contact contact = new Contact(comp_name, contact_id, first_name, last_name, 
											  phone_number, email, contact_type, address, city, state, zip);
				contacts.add(contact);
			}
		} catch (SQLException e) {
			throw new AddressBookException("Statement Creation Failed !!", Type.CONNECTION_ERROR);
		}
		
		return contacts;
	}
	
}
