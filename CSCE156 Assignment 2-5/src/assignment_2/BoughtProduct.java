package assignment_2;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

public class BoughtProduct {
	
	private Product product;
	private int units;
	private String ticketCode;
	
	private double subtotal;
	private double tax;
	private double total;
	private double discount;
	
	public BoughtProduct(Product product, int units) {
		this.product = product;
		this.units = units;
	}
	public BoughtProduct(Product product, int units, String ticketCode) {
		this.product = product;
		this.units = units;
		this.ticketCode = ticketCode;
	}
	
	public Product getProduct() {
		return this.product;
	}
	public int getUnits() {
		return this.units;
	}
	public String getTicketCode() {
		return this.ticketCode;
	}
	public void setSubtotal(ArrayList<BoughtProduct> boughtProduct, Date invoiceDate) {
		this.subtotal = (this.product.getPricePerUnit() * this.units) - ((this.product.getPricePerUnit() * this.units) * this.product.getDiscount(boughtProduct, invoiceDate)) + (this.product.getAddedFees() * this.units);
	}
	public double getSubtotal() {
		return this.subtotal;
	}
	public void setTax() {
		this.tax = getSubtotal() * this.product.getSalesTax();
	}
	public double getTax() {
		return this.tax;
	}
	public void setTotal() {
		this.total= getSubtotal() + getTax();
	}
	public double getTotal() {
		return this.total;
	}
	public void getItemDescription() {
		
		DecimalFormat df = new DecimalFormat("0.00");
		String itemDesc = "";
		String itemDescLine2 = "";
		if(getProduct().getType().equals("MovieTicket")){
			
			itemDesc = getProduct().getType() + " '" + ((MovieTicket) getProduct()).getMovieName() + "' @ " +
			((MovieTicket) getProduct()).getAddressObject().getAddress();
			itemDescLine2 = ((MovieTicket) getProduct()).getDateTime().toString();
			
			if(getDiscount() > 0){
				itemDescLine2 = itemDescLine2 + " (" + getUnits() + " units @ $" + getProduct().getPricePerUnit() + "/unit - Tue/Thu " + df.format((getDiscount()*100)) + "% off)";
			}
			else{
				itemDescLine2 = itemDescLine2 + " (" + getUnits() + " units @ $" + getProduct().getPricePerUnit() + "/unit)";
			}
			
			System.out.printf("%-10s %-80s $%10s $%10s $%10s", getProduct().getCode(), itemDesc, df.format(getSubtotal()), df.format(getTax()), df.format(getTotal()));
			System.out.println();
			System.out.printf("%-10s %-80s", "", itemDescLine2);
			System.out.println();
			
		}
		
		else if(getProduct().getType().equals("SeasonPass")){
			
			if(getDiscount() > 0){
				itemDesc = getProduct().getType() + " - " + ((SeasonPass) getProduct()).getName() + "' @ ";
				itemDescLine2 = "(" + getUnits() + " units @ $" + getProduct().getPricePerUnit() + "/unit prorated " + df.format(getDiscount()) + " days + $" + getProduct().getAddedFees() + " fee/unit)";
			}
			else{
				itemDesc = getProduct().getType() + " - " + ((SeasonPass) getProduct()).getName() + "' @ ";
						itemDescLine2 = "(" + getUnits() + " units @ $" + getProduct().getPricePerUnit() + "/unit + $" + getProduct().getAddedFees() + " fee/unit)";
			}
			
			System.out.printf("%-10s %-80s $%10s $%10s $%10s", getProduct().getCode(), itemDesc, df.format(getSubtotal()), df.format(getTax()), df.format(getTotal()));
			System.out.println();
			System.out.printf("%-10s %-80s", "", itemDescLine2);
			System.out.println();
			
		}
		
		else if(getProduct().getType().equals("Refreshment")){
			
			if(getDiscount() > 0){
				itemDesc = ((Refreshment) getProduct()).getName() + " (" + getUnits() + " units @ $" + getProduct().getPricePerUnit() + "/unit with " + df.format((getDiscount()*100)) + "% off)";
			}
			else{
				itemDesc = ((Refreshment) getProduct()).getName() + " (" + getUnits() + " units @ $" + getProduct().getPricePerUnit() + "/unit)";
			}
			
			System.out.printf("%-10s %-80s $%10s $%10s $%10s", getProduct().getCode(), itemDesc, df.format(getSubtotal()), df.format(getTax()), df.format(getTotal()));
			System.out.println();
			
		}
		
		else if(getProduct().getType().equals("ParkingPass")){
			
			if(getDiscount() > 0){
				itemDesc = getProduct().getType() + " " + ((ParkingPass) getProduct()).getCode() + " (" + getUnits() + " units @ $" + getProduct().getPricePerUnit() + "/unit with " + df.format((getDiscount()*100)) + "% off)";
			}
			else{
				itemDesc = getProduct().getType() + " " + " (" + getUnits() + " units @ $" + getProduct().getPricePerUnit() + "/unit)";
			}
			
			System.out.printf("%-10s %-80s $%10s $%10s $%10s", getProduct().getCode(), itemDesc, df.format(getSubtotal()), df.format(getTax()), df.format(getTotal()));
			System.out.println();
			
		}
		
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public double getDiscount() {
		return discount;
	}

}
