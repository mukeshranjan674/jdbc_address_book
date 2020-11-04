package com.capgemini.jdbc.addressbook;

import java.util.List;

public class AddressBookService {

	public List<Contact> getContacts() throws AddressBookException {
		return AddressBookServiceDB.getInstance().getContacts();
	}

}
