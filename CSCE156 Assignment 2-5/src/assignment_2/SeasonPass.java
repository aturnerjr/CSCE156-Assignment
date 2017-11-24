package assignment_2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Days;

public class SeasonPass extends Ticket{
	
	private String name;
	private DateTime startDate;
	private DateTime endDate;
	
	public SeasonPass(String code, String type, String name, String startDate, String endDate, Double pricePerUnit) {
		
		this.code = code;
		this.type = "SeasonPass";
		this.name = name;
		try {
			Date d = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
			this.startDate = new DateTime(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			Date e = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
			this.endDate = new DateTime(e);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.pricePerUnit = pricePerUnit;
		
	}

	public String getName() {
		return this.name;
	}

	public DateTime getStartDate() {
		return this.startDate;
	}

	public DateTime getEndDate() {
		return this.endDate;
	}

	@Override
	public double getDiscount(ArrayList<BoughtProduct> boughtProduct, Date invoiceDate) {
		Days seasonPassLength = Days.daysBetween(this.getEndDate(), this.getStartDate());
		DateTime date = new DateTime(invoiceDate);
		Days nonusedDays = Days.daysBetween(date, this.getStartDate());
		if(nonusedDays.getDays() == seasonPassLength.getDays()){
			
			for(int i = 0; i < boughtProduct.size(); i++){
				
				if(boughtProduct.get(i).getProduct().getType().equals("SeasonPass")){
					
					boughtProduct.get(i).setDiscount(1.0);
					return 1.0;
					
				}
				
			}
			
		}
		
		if(this.getStartDate().isBefore(date)){
			for(int i = 0; i < boughtProduct.size(); i++){
				
				if(boughtProduct.get(i).getProduct().getType().equals("SeasonPass")){
					
					boughtProduct.get(i).setDiscount((double)nonusedDays.getDays()/seasonPassLength.getDays());
					return (double)nonusedDays.getDays()/seasonPassLength.getDays();
					
				}
				
			}
			
		}
		
		return 0.0;
		
	}

	@Override
	public double getAddedFees() {
		return 8.0;
	}

	@Override
	public double getDiscount() {
		return 0.0;
	}

}
