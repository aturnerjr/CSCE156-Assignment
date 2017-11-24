package assignment_2;

import java.util.ArrayList;
import java.util.Date;

public abstract class Product implements Discount{
	
	String code;
	String type;
	double pricePerUnit;
	
	public abstract double getSalesTax();
	
	public String getCode() {
		return this.code;
	}
	public String getType() {
		return this.type;
	}
	public double getPricePerUnit(){
		return this.pricePerUnit;
	}

	public abstract double getAddedFees();

	public abstract double getDiscount(ArrayList<BoughtProduct> boughtProduct, Date invoiceDate);

}
