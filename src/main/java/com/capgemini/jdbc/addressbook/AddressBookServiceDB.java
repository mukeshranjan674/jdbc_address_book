package com.capgemini.jdbc.addressbook;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
					+ "c.phone_no, c.email, c.contact_type, "
					+ "d.address, d.city, d.state, d.zip, a.book_id, b.date_added from "
					+ "address_book a, person b, contact_details c, person_address d " + "where "
					+ "a.book_id = b.book_id and " + "b.contact_id = c.contact_id and "
					+ "b.contact_id = d.contact_id ";
			ResultSet resultSet = statement.executeQuery(sql);
			contacts = this.getContacts(resultSet);
		} catch (SQLException e) {
		}
		return contacts;
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

	/**
	 * UC3
	 * 
	 * @param start_date
	 * @param end_date
	 * @return
	 * @throws AddressBookException
	 */
	public List<Contact> getContacts(Date start_date, Date end_date) throws AddressBookException {
		List<Contact> contacts = new ArrayList<Contact>();
		Connection connection = new DBConnection().getConnection();
		try {
			Statement statement = connection.createStatement();
			String sql = String.format(
					"select a.name, b.contact_id, b.first_name, b.last_name, " + "c.phone_no, c.email, c.contact_type, "
							+ "d.address, d.city, d.state, d.zip, a.book_id, b.date_added from "
							+ "address_book a, person b, contact_details c, person_address d " + "where "
							+ "a.book_id = b.book_id and " + "b.contact_id = c.contact_id and "
							+ "b.contact_id = d.contact_id " + "and b.date_added between '%s' and '%s'",
					start_date, end_date);
			ResultSet resultSet = statement.executeQuery(sql);
			contacts = this.getContacts(resultSet);
		} catch (SQLException e) {
		}
		return contacts;
	}

	private List<Contact> getContacts(ResultSet resultSet) throws AddressBookException {
		List<Contact> contacts = new ArrayList<Contact>();
		try {
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
				String date_added = resultSet.getString(13);

				Contact contact = new Contact(address_book_name, address_book_id, contact_id, first_name, last_name,
						phone_number, email, contact_type, address, city, state, zip, date_added);
				contacts.add(contact);
			}
		} catch (SQLException e) {
			throw new AddressBookException("Wrong Query", Type.WRONG_QUERY);
		}
		return contacts;
	}

	/**
	 * UC4
	 * 
	 * @param city
	 * @return
	 * @throws AddressBookException
	 */
	public int countByCity(String city) throws AddressBookException {
		Connection connection = null;
		try {
			connection = new DBConnection().getConnection();
			Statement statement = connection.createStatement();
			String sql = String.format("select count(contact_id) from person_address where city = '%s' group by city",
					city);
			ResultSet resultSet = statement.executeQuery(sql);
			return this.getCount(resultSet);
		} catch (SQLException e) {
			throw new AddressBookException("Wrong Query", Type.WRONG_QUERY);
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
				}
		}
	}

	public int countByState(String state) throws AddressBookException {
		Connection connection = null;
		try {
			connection = new DBConnection().getConnection();
			Statement statement = connection.createStatement();
			String sql = String.format("select count(contact_id) from person_address where state = '%s' group by state",
					state);
			ResultSet resultSet = statement.executeQuery(sql);
			return this.getCount(resultSet);
		} catch (SQLException e) {
			throw new AddressBookException("Wrong Query", Type.WRONG_QUERY);
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
				}
		}
	}

	private int getCount(ResultSet resultSet) {
		int count_of_person = 0;
		try {
			while (resultSet.next())
				count_of_person = resultSet.getInt(1);
		} catch (SQLException e) {
		}
		return count_of_person;
	}

	/**
	 * UC5
	 * 
	 * @param contact
	 * @throws AddressBookException
	 */
	public synchronized void addContact(Contact contact) throws AddressBookException {
		Connection connection = null;
		List<Contact> contacts = this.getContacts();
		System.out.println("Contact Being Added : " + contact.getFirst_name());
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
					"insert into person (book_id, contact_id, first_name, last_name, date_added) values "
							+ "(%s, %s, '%s', '%s', '%s')",
					contact.getAddress_book_id(), contact.getContact_id(), contact.getFirst_name(),
					contact.getLast_name(), contact.getDate_added());
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
			System.out.println("Contact Added : " + contact.getFirst_name());
		} catch (SQLException e) {
			throw new AddressBookException("contact exist", Type.INSERTION_ERROR);
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
				}
		}
	}

	/**
	 * UC6
	 * 
	 * @param listOfContacts
	 */
	public void addMultipleContacts(List<Contact> listOfContacts) {
		Map<Integer, Boolean> addressBookStatus = new HashMap<>();
		for (Contact contact : listOfContacts) {
			Runnable task = () -> {
				addressBookStatus.put(contact.hashCode(), false);
				try {
					this.addContact(contact);
				} catch (AddressBookException a) {
					a.printStackTrace();
				}
				addressBookStatus.put(contact.hashCode(), true);
			};
			Thread thread = new Thread(task, contact.getFirst_name());
			thread.setPriority(10);
			thread.start();
		}
		while (addressBookStatus.containsValue(false)) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}