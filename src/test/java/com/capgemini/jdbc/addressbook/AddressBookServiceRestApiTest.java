package com.capgemini.jdbc.addressbook;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AddressBookServiceRestApiTest {
	AddressBookServiceRestAPI addressBookServiceRestAPI;
	List<Contact> contacts;
	
	@Before
	public void Setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 3000;
		addressBookServiceRestAPI = new AddressBookServiceRestAPI(getContactList());
	}

	private List<Contact> getContactList() {
		Response response = RestAssured.get("/contacts");
		Contact[] contacts = new Gson().fromJson(response.asString(), Contact[].class);
		return Arrays.asList(contacts);
	}
	
	/**
	 * UC7
	 */
	@Test
	public void givenContactsInJSONServerwhenReadShouldMatchTheCount() {
		int result = addressBookServiceRestAPI.countEntries();
		assertEquals(4, result);
	}

	@Test
	public void givenContactWhenAddedToJSONServerShouldMatchWithStatusCode() {
		Contact contact = new Contact("Ramesh", "Kumar", "Sijua, dhanbad", "dhanbad", "jharkhand", 
									  "898989", "854523200", "ramesh@gmail.com");
		addressBookServiceRestAPI.addContact(contact);
		Response response = addContactToJsonServer(contact);
		boolean result = response.getStatusCode() == 201 && 
						 addressBookServiceRestAPI.countEntries() == 5;
		assertTrue(result);
	}

	private Response addContactToJsonServer(Contact contact) {
		String contactJson = new Gson().toJson(contact);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(contactJson);
		return request.post("/contacts");
	}
}
