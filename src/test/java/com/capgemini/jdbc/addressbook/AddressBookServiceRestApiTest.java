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
		addressBookServiceRestAPI = new AddressBookServiceRestAPI(getContactListFromJsonServer());
	}

	private List<Contact> getContactListFromJsonServer() {
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
		Contact contact = new Contact("Ramesh", "Kumar", "Sijua, dhanbad", "dhanbad", "jharkhand", "898989",
				"854523200", "ramesh@gmail.com");
		addressBookServiceRestAPI.addContact(contact);
		Response response = this.addContactToJsonServer(contact);
		boolean result = response.getStatusCode() == 201 && addressBookServiceRestAPI.countEntries() == 5;
		assertTrue(result);
	}

	/**
	 * UC8
	 */
	@Test
	public void givenContactListShouldGetAddedToTheJsonServer() {
		List<Contact> contacts = this.getNewContactList();
		for (Contact contact : contacts) {
			addressBookServiceRestAPI.addContact(contact);
			Response response = this.addContactToJsonServer(contact);
			boolean result = response.getStatusCode() == 201;
			assertTrue(result);
		}
	}

	/**
	 * UC9
	 */
	@Test
	public void givenContactShouldGetUpdatedInTheJsonServer() {
		Contact contact = new Contact("Suresh", "Kumar", "Sijua, dhanbad", "dhanbad", "jharkhand", "898989",
				"854523200", "suresh@gmail.com");
		String contactJson = new Gson().toJson(contact);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(contactJson);
		Response response = request.put("/contacts" + "/7");
		assertEquals(200, response.getStatusCode());
	}

	private List<Contact> getNewContactList() {
		Contact[] contacts = {
				new Contact("Kush", "Kumar", "Sijua, dhanbad", "dhanbad", "jharkhand", "898989", "854523200",
						"kush@gmail.com"),
				new Contact("Luv", "Kumar", "Sijua, dhanbad", "dhanbad", "jharkhand", "898989", "854523200",
						"luv@gmail.com"),
				new Contact("Mohan", "Kumar", "Sijua, dhanbad", "dhanbad", "jharkhand", "898989", "854523200",
						"mohan@gmail.com") };
		return Arrays.asList(contacts);
	}

	private Response addContactToJsonServer(Contact contact) {
		String contactJson = new Gson().toJson(contact);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(contactJson);
		return request.post("/contacts");
	}
}
