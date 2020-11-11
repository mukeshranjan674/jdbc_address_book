package com.capgemini.jdbc.addressbook;

import java.util.ArrayList;
import java.util.List;

public class AddressBookServiceRestAPI {
	public static List<Contact> contactList;

	public AddressBookServiceRestAPI(List<Contact> contacts) {
		contactList = new ArrayList<Contact>(contacts);
	}

	public int countEntries() {
		return contactList.size();
	}

	public void addContact(Contact contact) {
		contactList.add(contact);
	}

	public Contact getContact(String name) {
		Contact contact = contactList.stream().filter(con -> con.getFirst_name().equals(name)).findAny().orElse(null);
		return contact;
	}

	public void deleteContact(Contact contact) {
		contactList.remove(contact);
	}
}
