package com.capgemini.jdbc.addressbook;

import java.sql.Date;
import java.util.List;

public class AddressBookService {

	/**
	 * UC1
	 * 
	 * @return
	 * @throws AddressBookException
	 */
	public List<Contact> getContacts() throws AddressBookException {
		return AddressBookServiceDB.getInstance().getContacts();
	}

	/**
	 * UC5
	 * 
	 * @param contact
	 * @throws AddressBookException
	 */
	public void addContact(Contact contact) throws AddressBookException {
		AddressBookServiceDB.getInstance().addContact(contact);
	}

	public boolean checkIfSyncWithDatabase(int contact_id) throws AddressBookException {
		List<Contact> contacts = this.getContacts();
		boolean result = false;
		Contact contact = contacts.stream().filter(n -> n.getContact_id() == contact_id).findAny().orElse(null);
		if (contact != null)
			result = true;
		return result;
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
		AddressBookServiceDB.getInstance().updateContact(contact_id, phone_number, email);
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
		return AddressBookServiceDB.getInstance().getContacts(start_date, end_date);
	}

	/**
	 * UC4
	 * 
	 * @param city
	 * @return
	 * @throws AddressBookException
	 */
	public int countByCity(String city) throws AddressBookException {
		return AddressBookServiceDB.getInstance().countByCity(city);
	}

	public int countByState(String state) throws AddressBookException {
		return AddressBookServiceDB.getInstance().countByState(state);
	}

	/**
	 * UC6
	 * 
	 * @param listOfContacts
	 */
	public void addMultipleContacts(List<Contact> listOfContacts) {
		AddressBookServiceDB.getInstance().addMultipleContacts(listOfContacts);
	}
}
