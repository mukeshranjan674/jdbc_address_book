package com.capgemini.jdbc.addressbook;

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
}
