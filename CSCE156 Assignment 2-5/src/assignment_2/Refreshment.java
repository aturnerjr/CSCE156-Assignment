package assignment_2;

import java.util.ArrayList;
import java.util.Date;

public class Refreshment extends Service{
	
	private String name;
	
	public Refreshment(String code, String type, String name, Double pricePerUnit){
		
		this.code = code;
		this.type = "Refreshment";
		this.name = name;
		this.pricePerUnit = pricePerUnit;
		
	}

	public String getName() {
		return this.name;
	}

	@Override
	public double getDiscount(ArrayList<BoughtProduct> boughtProduct, Date invoiceDate) {
		for(int i = 0; i < boughtProduct.size(); i++){
			
			if(boughtProduct.get(i).getProduct().getType().equals("MovieTicket") || boughtProduct.get(i).getProduct().getType().equals("SeasonPass")){
				
				for(int j = 0; j < boughtProduct.size(); j++){
					
					if(boughtProduct.get(j).getProduct().getType().equals("Refreshment")){
						boughtProduct.get(j).setDiscount(getDiscount());
					}
					
				}
				return getDiscount();
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
		return 0.05;
	}

}
