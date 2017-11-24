package assignment_2;

import java.util.ArrayList;

public class Person{
	
	private String personCode;
	private String lastName;
	private String firstName;
	private String fullName;
	private Address address;
	private ArrayList<String> email;
	
	public Person(String personCode, String firstName, String lastName, Address address, ArrayList<String> email) {
		
		this.personCode = personCode;
		this.address = address;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.fullName = firstName + ", " + lastName;
		
	}

	public Person(String personCode, String firstName, String lastName, Address address) {
		
		this.personCode = personCode;
		this.address = address;
		this.firstName = firstName;
		this.lastName = lastName;
		
	}

	public String getPersonCode() {
		return this.personCode;
	}

	public String getLastName() {
		return this.lastName;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getFullName() {
		return this.fullName;
	}

	public Address getAddressObject() {
		return this.address;
	}

	public ArrayList<String> getEmail() {
		return this.email;
	}

}