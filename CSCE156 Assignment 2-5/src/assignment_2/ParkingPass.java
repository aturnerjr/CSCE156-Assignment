package assignment_2;

import java.util.ArrayList;
import java.util.Date;

public class ParkingPass extends Service{
	
	public ParkingPass(String code, String type, Double pricePerUnit){
		
		this.code = code;
		this.type = "ParkingPass";
		this.pricePerUnit = pricePerUnit;
		
	}

	@Override
	public double getDiscount(ArrayList<BoughtProduct> boughtProduct, Date invoiceDate) {
		
		int seasonPassLocation = -1;
		for(int i = 0; i < boughtProduct.size(); i++){
			
			if(boughtProduct.get(i).getProduct().getType().equals("SeasonPass")){
				seasonPassLocation = i;
			}
			
		}
		
		if(seasonPassLocation >= 0){
			
			for(int i = 0; i < boughtProduct.size(); i++){
				
				if(boughtProduct.get(i).getProduct().getType().equals("ParkingPass")){
					
					if(boughtProduct.get(i).getTicketCode() != null && boughtProduct.get(seasonPassLocation).getProduct().getCode() != null){
						
						if(boughtProduct.get(i).getTicketCode().equals(boughtProduct.get(seasonPassLocation).getProduct().getCode())){
							
							double numFree =  boughtProduct.get(i).getUnits() - boughtProduct.get(seasonPassLocation).getUnits();
							
							if(numFree >= 0){
								boughtProduct.get(i).setDiscount(1.0 - ((numFree)/((double)boughtProduct.get(i).getUnits())));
								return 1.0 - ((numFree)/((double)boughtProduct.get(i).getUnits()));
							}
							else{
								boughtProduct.get(i).setDiscount(1.0);
								return 1.0;
							}
							
						}
						
					}
					
				}
				
			}
			
		}
		
		for(int i = 0; i < boughtProduct.size(); i++){
			
			if(boughtProduct.get(i).getProduct().getType().equals("ParkingPass")){
				
				for(int j = 0; j < boughtProduct.size(); j++){
					
					if(boughtProduct.get(i).getTicketCode() != null){
						
						if(boughtProduct.get(i).getTicketCode().equals(boughtProduct.get(j).getProduct().getCode())){
							
							double numFree = boughtProduct.get(i).getUnits() - boughtProduct.get(j).getUnits();
							
							if(numFree >= 0){
								boughtProduct.get(i).setDiscount(1.0 - ((numFree)/((double)boughtProduct.get(i).getUnits())));
								return 1.0 - ((numFree)/((double)boughtProduct.get(i).getUnits()));
							}
							else{
								boughtProduct.get(i).setDiscount(1.0);
								return 1.0;
							}
							
						}
						
					}
					
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
		return 0.0;
	}
	
}
