package com.capgemini.jdbc.addressbook;

public class AddressBookException extends Exception {
	public enum Type {
		CONNECTION_ERROR
	}

	private Type type;

	public AddressBookException(String message, Type type) {
		super(message);
		this.type = type;
	}
}
