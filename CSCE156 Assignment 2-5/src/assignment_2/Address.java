package assignment_2;

public class Address {
	
	private String address;
	private String city;
	private String state;
	private String zip;
	private String country;
	
	public Address(String address, String city, String state, String zip, String country) {
		super();
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.country = country;
	}
	
	public String getAddress() {
		return this.address;
	}

	public String getCity() {
		return this.city;
	}

	public String getState() {
		return this.state;
	}

	public String getZip() {
		return this.zip;
	}

	public String getCountry() {
		return this.country;
	}

}
