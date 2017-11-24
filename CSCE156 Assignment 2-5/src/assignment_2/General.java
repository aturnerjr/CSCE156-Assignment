package assignment_2;

public class General extends Customer{

	public General(String customerCode, String type, String name, Address address, Person person) {
		super(customerCode, "General", name, address, person);
	}

	@Override
	public double getDiscount() {
		return 0.0;
	}

	@Override
	public double getAditionalFee() {
		return 0.0;
	}


}
