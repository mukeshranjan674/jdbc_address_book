package com.capgemini.jdbc.addressbook;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

public class DBConnection {
	static int connectionCounter = 0;
	String jdbcURL = "jdbc:mysql://localhost:3306/address_book_service?useSSL=false";
	String userName = "root";
	String password = "Mukesh@2016";
	Connection connection;

	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver loaded");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("cannot find driver in the classpath");
		}

		listDrivers();
	}

	public static void listDrivers() {
		Enumeration<java.sql.Driver> driverList = DriverManager.getDrivers();
		while (driverList.hasMoreElements()) {
			Driver driverClass = (Driver) driverList.nextElement();
			System.out.println(" " + driverClass.getClass().getName());
		}
	}

	public synchronized Connection getConnection() {
		connectionCounter++;
		try {
			System.out.println("Processing Thread : " + Thread.currentThread().getName() +
							    " Conntecting to database wiht ID : " + connectionCounter);
			connection = DriverManager.getConnection(jdbcURL, userName, password);
			System.out.println("Processing Thread : " + Thread.currentThread().getName() +
								" Connection is successful !!" + connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
}
