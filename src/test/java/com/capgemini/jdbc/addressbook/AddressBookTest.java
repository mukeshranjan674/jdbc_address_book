package com.capgemini.jdbc.addressbook;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AddressBookTest {
	AddressBookService addressBookService;

	@Before
	public void setUp() {
		addressBookService = new AddressBookService();
	}

	/**
	 * UC1
	 */
	@Test
	public void whenDatabaseRetrievedSholdReturnListOfContacts() {
		try {
			List<Contact> contacts = addressBookService.getContacts();
			assertEquals(4, contacts.size());
		} catch (AddressBookException e) {
		}
	}

	@Test
	public void whenConatactAddedSholdAddToTheDatabase() {
		String address_book_name = "book1";
		int address_book_id = 101;
		int contact_id = 1001;
		String first_name = "Alex";
		String last_name = "Roax";
		String phone_number = "8785652320";
		String email = "alex@gmail.com";
		String contact_type = "friend";
		String address = "manhatton, nyc";
		String city = "NYC";
		String state = "New York State";
		String zip = "023520";
		String date_added = "2020-10-10";
		Contact contact = new Contact(address_book_name, address_book_id, contact_id, first_name, last_name,
				phone_number, email, contact_type, address, city, state, zip, date_added);
		try {
			addressBookService.addContact(contact);
			boolean result = addressBookService.checkIfSyncWithDatabase(contact_id);
			assertTrue(result);
		} catch (AddressBookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * UC2
	 */
	@Test
	public void whenGivenIdShouldModifyTheContactInDataBase() {
		int contact_id = 1001;
		String phone_number = "100000000";
		String email = "raxo@gmail.com";
		try {
			addressBookService.updateContact(contact_id, phone_number, email);
			boolean result = addressBookService.checkIfSyncWithDatabase(contact_id);
			assertTrue(result);
		} catch (AddressBookException e) {
		}
	}
	
	/**
	 * UC3
	 */
	@Test
	public void whenGivenDateRangeShouldReturnContacts() {
		Date start_date = Date.valueOf("2018-01-01");
		Date end_date = Date.valueOf("2020-12-12");
		List<Contact> contacts;
		try {
			contacts = addressBookService.getContacts(start_date, end_date);
			assertEquals(3, contacts.size());
		} catch (AddressBookException e) {
		}
	}
}
