package com.capgemini.jdbc.addressbook;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
			assertEquals(3, contacts.size());
		} catch (AddressBookException e) {
		}
	}
}
