package assignment_2;

public class Student extends Customer{

	public Student(String customerCode, String type, String name, Address address, Person person) {
		super(customerCode, "Student", name, address, person);
	}

	@Override
	public double getDiscount() {
		return 0.08;
	}

	@Override
	public double getAditionalFee() {
		return 6.75;
	}

}
