package assignment_2;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Invoice {
	
	private String invoiceCode;
	private Date invoiceDate;
	private Customer customer;
	private Person person;
	private ArrayList<BoughtProduct> boughtProduct;
	
	
	private double invoiceSubtotal;
	private double invoiceTotal;
	private double invoiceTax;
	private double invoiceDiscount;
	private double invoiceFees;
	
	public Invoice(String invoiceCode, String invoiceDate, Customer customer, Person person, ArrayList<BoughtProduct> boughtProduct) {
		
		this.invoiceCode = invoiceCode;
		try {
			this.invoiceDate = new SimpleDateFormat("yyyy-MM-dd").parse(invoiceDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.customer = customer;
		this.person = person;
		this.boughtProduct = boughtProduct;
	}

	public String getInvoiceCode() {
		return this.invoiceCode;
	}

	public Date getInvoiceDate() {
		return this.invoiceDate;
	}
	
	public Customer getCustomerObject() {
		return this.customer;
	}
	
	public Person getPersonObject() {
		return this.person;
	}

	public ArrayList<BoughtProduct> getBoughtProduct() {
		return this.boughtProduct;
	}

	public void setInvoiceTotals() {
		for(int i = 0; i < boughtProduct.size(); i++){
			this.invoiceTotal += boughtProduct.get(i).getTotal();
			this.invoiceSubtotal += boughtProduct.get(i).getSubtotal();
			this.invoiceTax += boughtProduct.get(i).getTax();
		}
	}
	
	public double getInvoiceSubtotal() {
		return this.invoiceSubtotal;
	}
	
	public double getInvoiceTotal() {
		return this.invoiceTotal;
	}
	
	public double getInvoiceTax() {
		return this.invoiceTax;
	}
	
	public void setInvoiceDiscountsAndFees(){
		this.invoiceDiscount = -(getInvoiceSubtotal() * customer.getDiscount() + getInvoiceTax());
		this.invoiceFees = customer.getAditionalFee();
		setInvoiceTotal(this.invoiceTotal + getInvoiceFees() + getInvoiceDiscount());
	}
	
	public void setInvoiceTotal(double totalAfterDiscounts) {
		this.invoiceTotal = totalAfterDiscounts;
	}

	public double getInvoiceDiscount() {
		return this.invoiceDiscount;
	}

	public double getInvoiceFees() {
		return this.invoiceFees;
	}
	
	public void printInvoice() {
		
		DecimalFormat df = new DecimalFormat("0.00");
		
		//lines 100-112 prints a description of the customer of an invoice
		System.out.println("Invoice: " + getInvoiceCode());
		System.out.println("========================");
		System.out.println("Salesperson: " + getPersonObject().getFullName());
		System.out.println("Customer Info:");
		System.out.println("  " + getCustomerObject().getName() + " (" + this.customer.getCustomerCode() + ")");
		System.out.println("  [" + getCustomerObject().getType() + "]");
		System.out.println("  " + getCustomerObject().getPersonObject().getFullName());
		System.out.println("  " + getCustomerObject().getAddressObject().getAddress());
		System.out.println("  " + getCustomerObject().getAddressObject().getCity() + " " + getCustomerObject().getAddressObject().getState() +
				" " + getCustomerObject().getAddressObject().getZip() + " " + getCustomerObject().getAddressObject().getCountry());
		System.out.println("------------------------------------------");
		System.out.printf("%-10s %-80s %11s %11s %11s", "Code", "Item", "SubTotal", "Tax", "Total");
		System.out.println();
		
		//this for-loop loops through all of the items bought on the invoice and prints their respective totals calculated earlier
		for(int i = 0; i < boughtProduct.size(); i++){
			
			boughtProduct.get(i).getItemDescription();
			
		}
		
		//lines 121-125 print the totals of the invoice
		System.out.printf("%127s", "===================================");
		System.out.println();
		System.out.printf("%-91s $%10s $%10s $%10s", "SUB-TOTALS", df.format(getInvoiceSubtotal()), df.format(getInvoiceTax()), df.format(getInvoiceTotal()));
		System.out.println();
		
		//if the customer is a student then there are different discounts and fees that are applied
		if(getCustomerObject().getType().equals("Student")){
			setInvoiceDiscountsAndFees();
			System.out.printf("%-115s $%10s", "DISCOUNT ( " + getCustomerObject().getDiscount()*100 +
					"%  STUDENT & NO TAX )", df.format(getInvoiceDiscount()));
			System.out.println();
			System.out.printf("%-115s $%10s", "ADDITIONAL FEE (Student)", df.format(getInvoiceFees()));
			System.out.println();
		}
		
		//printd the total after all discounts and fees are added in
		System.out.printf("%-115s $%10s", "TOTAL", df.format(getInvoiceTotal()));
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.printf("%60s", "Thank you for your purchase!");
		System.out.println();
		System.out.println();
		System.out.println();

	}
	
}