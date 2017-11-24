package assignment_2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MovieTicket extends Ticket {
	
	private Date dateTime;
	private String movieName;
	private Address address;
	private String screenNumber;
	
	public MovieTicket(String code, String type, String dateTime, String movieName, Address address, String screenNumber, Double pricePerUnit) {
		
		this.code = code;
		this.type = "MovieTicket";
		try {
			this.dateTime = new SimpleDateFormat("yyyy-MM-dd").parse(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.movieName = movieName;
		this.address = address;
		this.screenNumber = screenNumber;
		this.pricePerUnit = pricePerUnit;
		
	}

	public Date getDateTime() {
		return this.dateTime;
	}

	public String getMovieName() {
		return this.movieName;
	}

	public Address getAddressObject() {
		return this.address;
	}

	public String getScreenNumber() {
		return this.screenNumber;
	}

	public double getPricePerUnit() {
		return this.pricePerUnit;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public double getDiscount(ArrayList<BoughtProduct> boughtProduct, Date invoiceDate){
		if(dateTime.getDay() == 2 || dateTime.getDay() == 4){
			for(int i = 0; i < boughtProduct.size(); i++){
				
				if(boughtProduct.get(i).getProduct().getType().equals("MovieTicket")){
					boughtProduct.get(i).setDiscount(getDiscount());
					return getDiscount();
				}
				
			}
			
		}
		return 0.0;
		
	}

	@Override
	public double getAddedFees() {
		return 0.0;
	}

	@Override
	public double getDiscount() {
		return 0.07;
	}
	
}
