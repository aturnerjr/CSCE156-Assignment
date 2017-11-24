package assignment_2;

public abstract class Customer implements Discount{
	
	private String customerCode;
	private String type;
	private String name;
	private Address address;
	private Person person;
	
	public Customer(String customerCode, String type, String name, Address address, Person person) {
		this.customerCode = customerCode;
		this.type = type;
		this.name = name;
		this.address = address;
		this.person = person;
	}
	
	public abstract double getDiscount();
	public abstract double getAditionalFee();

	public String getCustomerCode() {
		return this.customerCode;
	}

	public String getType() {
		return this.type;
	}

	public String getName() {
		return this.name;
	}

	public Address getAddressObject() {
		return this.address;
	}

	public Address getAddress() {
		return this.address;
	}

	public Person getPersonObject() {
		return this.person;
	}

}
