package com.capgemini.jdbc.addressbook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.capgemini.jdbc.addressbook.AddressBookException.Type;

public class AddressBookServiceDB {
	private static AddressBookServiceDB addressBookServiceJDBC;
	private PreparedStatement addressBookStatement;

	private AddressBookServiceDB() {

	}

	public static AddressBookServiceDB getInstance() {
		if (addressBookServiceJDBC == null)
			addressBookServiceJDBC = new AddressBookServiceDB();
		return addressBookServiceJDBC;
	}

	/**
	 * UC1
	 * 
	 * @return
	 * @throws AddressBookException
	 */
	public List<Contact> getContacts() throws AddressBookException {
		List<Contact> contacts = new ArrayList<Contact>();
		Connection connection = new DBConnection().getConnection();
		try {
			Statement statement = connection.createStatement();
			String sql = "select a.name, b.contact_id, b.first_name, b.last_name, "
					+ "c.phone_no, c.email, c.contact_type, " + "d.address, d.city, d.state, d.zip, a.book_id from "
					+ "address_book a, person b, contact_details c, person_address d " + "where "
					+ "a.book_id = b.book_id and " + "b.contact_id = c.contact_id and "
					+ "b.contact_id = d.contact_id ";
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				String address_book_name = resultSet.getString(1);
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
				int address_book_id = resultSet.getInt(12);

				Contact contact = new Contact(address_book_name, address_book_id, contact_id, first_name, last_name,
						phone_number, email, contact_type, address, city, state, zip);
				contacts.add(contact);
			}
		} catch (SQLException e) {
			throw new AddressBookException("Statement Creation Failed !!", Type.CONNECTION_ERROR);
		}

		return contacts;
	}

	public void addContact(Contact contact) throws AddressBookException {
		Connection connection = null;
		List<Contact> contacts = this.getContacts();
		try {
			boolean addressBookExist = contacts.stream()
					.anyMatch(n -> n.getAddress_book_id() == contact.getAddress_book_id());
			connection = new DBConnection().getConnection();
			connection.setAutoCommit(false);
			if (!addressBookExist) {
				Statement statement = connection.createStatement();
				String sql = String.format("insert into address_book values (%s, '%s')", contact.getAddress_book_id(),
						contact.getAddress_book_name());
				statement.executeUpdate(sql);
			}
		} catch (SQLException e) {
		}

		try {
			String sql = String.format(
					"insert into person (book_id, contact_id, first_name, last_name) values " + "(%s, %s, '%s', '%s')",
					contact.getAddress_book_id(), contact.getContact_id(), contact.getFirst_name(),
					contact.getLast_name());
			Statement statement = connection.createStatement();
			statement.executeUpdate(sql);
			sql = String.format(
					"insert into contact_details (contact_id, phone_no, email, contact_type) values "
							+ "(%s, '%s', '%s', '%s')",
					contact.getContact_id(), contact.getPhone_number(), contact.getEmail(), contact.getContact_type());
			statement = connection.createStatement();
			statement.executeUpdate(sql);
			sql = String.format(
					"insert into person_address (contact_id, address, city, state, zip) values "
							+ "(%s, '%s', '%s', '%s', '%s')",
					contact.getContact_id(), contact.getAddress(), contact.getCity(), contact.getState(),
					contact.getZip());
			statement = connection.createStatement();
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
			}
			throw new AddressBookException("contact exist", Type.INSERTION_ERROR);
		}
		try {
			connection.commit();
		} catch (SQLException e) {
			throw new AddressBookException("contact exist", Type.INSERTION_ERROR);
		}
	}

	/**
	 * UC2
	 * 
	 * @param contact_id
	 * @param phone_number
	 * @param email
	 * @throws AddressBookException
	 */
	public void updateContact(int contact_id, String phone_number, String email) throws AddressBookException {
		this.getPreparedStatement(contact_id, phone_number, email);
		try {
			addressBookStatement.executeUpdate();
		} catch (SQLException e) {
			throw new AddressBookException("contact exist", Type.UPDATION_ERROR);
		}
	}

	private void getPreparedStatement(int contact_id, String phone_number, String email) throws AddressBookException {
		Connection connection = new DBConnection().getConnection();
		String sql = String.format("update contact_details set phone_no = '%s', email = '%s' where contact_id = %s",
				phone_number, email, contact_id);
		try {
			addressBookStatement = connection.prepareStatement(sql);
		} catch (SQLException e) {
			throw new AddressBookException("contact exist", Type.UPDATION_ERROR);
		}
	}

}
