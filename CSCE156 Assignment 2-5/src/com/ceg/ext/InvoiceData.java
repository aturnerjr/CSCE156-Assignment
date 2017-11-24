package com.ceg.ext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 * 19 methods in total.
 * 1-6 helps remove info from database.
 * 7-19 adds info to the database.
 */

public class InvoiceData {

	//1. Removes all product and unit records from the database
	public static void removeAllProducts() {
		removeAllData("InvoiceProduct");
		removeAllData("MovieTicket");
		removeAllData("Refreshment");
		removeAllData("SeasonPass");
		removeAllData("ParkingPass");
		removeAllData("Product");
	}
	
	//2. Removes all invoice records from the database
	public static void removeAllInvoices() {
		removeAllProducts();
		removeAllData("Invoice");
	}
	
	//3. Removes all customer records from the database
	public static void removeAllCustomers() {
		removeAllData("Customer");		
	}
	
	//4. Removes all person and email records from the database
	public static void removeAllPersons() {
		removeAllData("Email");
		removeAllData("Person");
		removeAllAddresses();
	}
	
	//5. Removes all address records from the database
	public static void removeAllAddresses() {
		removeAllData("Address");
	}
	
	//6. The method for removing data from a table, can be reused by passing in the table name.
	public static void removeAllData(String table) {
		
		//gets connection from database
		Connection conn = DatabaseInfo.getConnection();
		PreparedStatement deleteTableData = null;
		String deleteTableDataQuery = "DELETE FROM ---";
		String query =deleteTableDataQuery.replace("---",table);
		
		try {
			
			deleteTableData = conn.prepareStatement(query);
			deleteTableData.executeUpdate();
			
			//closes everything when finished
			deleteTableData.close(); conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	//7. Method to add a person record to the database with the provided data.
	public static void addPerson(String personCode, String firstName, String lastName, String street, String city, String state, String zip, String country) {
		
		//gets connection from database
		Connection conn = DatabaseInfo.getConnection();
		
		PreparedStatement addPersonPS = null;
		PreparedStatement addAddressPS = null;
		PreparedStatement addressIDPS = null;
		
		String addPersonQuery = "INSERT INTO Person(addressID, personCode, firstName, lastName) VALUES(?,?,?,?)";
		String addAddressQuery = "INSERT INTO Address(address, city, state, zip, country) VALUES(?,?,?,?,?)";
		String addressIDQuery = "SELECT addressID FROM Address WHERE address = ?";
		
		ResultSet addressID = null;
		
		try {
			
			addAddressPS = conn.prepareStatement(addAddressQuery);
			addAddressPS.setString(1, street);
			addAddressPS.setString(2, city);
			addAddressPS.setString(3, state);
			addAddressPS.setString(4, zip);
			addAddressPS.setString(5, country);
			addAddressPS.executeUpdate();
			
			addressIDPS = conn.prepareStatement(addressIDQuery);
			addressIDPS.setString(1, street);
			addressID = addressIDPS.executeQuery();
			addressID.next();
			
			addPersonPS = conn.prepareStatement(addPersonQuery);
			addPersonPS.setInt(1, addressID.getInt(1));
			addPersonPS.setString(2, personCode);
			addPersonPS.setString(3, firstName);
			addPersonPS.setString(4, lastName);
			addPersonPS.executeUpdate();
			
			//closes everything when finished
			addressID.close(); addressIDPS.close(); addAddressPS.close(); addPersonPS.close(); conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	//8. Adds an email record corresponding person record corresponding to the
	public static void addEmail(String personCode, String email) {
		
		//gets connection from database
		Connection conn = DatabaseInfo.getConnection();
		
		PreparedStatement personIDPS = null;
		PreparedStatement addEmailPS = null;
		String personIDQuery = "SELECT personID FROM Person WHERE personCode = ?";
		String addEmailQuery = "INSERT INTO Email(personID, email) VALUES(?,?)";
		ResultSet personID;
		
		try {
			
			//returns the person id of the person with the personCode provided
			personIDPS = conn.prepareStatement(personIDQuery);
			personIDPS.setString(1, personCode);
			personID = personIDPS.executeQuery();
			
			//checks if there is a person linked with the personCode provided
			if(personID.next()){
				
				//following block adds the email to the person
				addEmailPS = conn.prepareStatement(addEmailQuery);
				addEmailPS.setInt(1, personID.getInt(1));
				addEmailPS.setString(2, email);
				addEmailPS.executeUpdate();
				
			}else{
				
			}
			
			//closes everything when finished
			personID.close(); addEmailPS.close(); personIDPS.close(); conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	//9. Method that adds a customer record to the database
	public static void addCustomer(String customerCode, String customerType, String primaryContactPersonCode, String name, String street, String city, String state, String zip, String country) {
		
		//gets connection from database
		Connection conn = DatabaseInfo.getConnection();
		
		PreparedStatement addCustomerPS = null;
		PreparedStatement addAddressPS = null;
		PreparedStatement addressIDPS = null;
		PreparedStatement personIDPS = null;
		
		String addCustomerQuery = "INSERT INTO Customer(addressID, personID, customerName, customerCode, customerType) VALUES(?,?,?,?,?)";
		String addAddressQuery = "INSERT INTO Address(address, city, state, zip, country) VALUES(?,?,?,?,?)";
		String addressIDQuery = "SELECT addressID FROM Address WHERE address = ?";
		String personIDQuery = "SELECT personID FROM Person WHERE personCode = ?";
		
		ResultSet addressID = null;
		ResultSet personID = null;
		
		try {
			
			addAddressPS = conn.prepareStatement(addAddressQuery);
			addAddressPS.setString(1, street);
			addAddressPS.setString(2, city);
			addAddressPS.setString(3, state);
			addAddressPS.setString(4, zip);
			addAddressPS.setString(5, country);
			addAddressPS.executeUpdate();
			
			addressIDPS = conn.prepareStatement(addressIDQuery);
			addressIDPS.setString(1, street);
			addressID = addressIDPS.executeQuery();
			addressID.next();
			
			personIDPS = conn.prepareStatement(personIDQuery);
			personIDPS.setString(1, primaryContactPersonCode);
			personID = personIDPS.executeQuery();
			
			//checks if there is a person linked with the primaryContactPersonCode provided
			if(personID.next()){
				
				addCustomerPS = conn.prepareStatement(addCustomerQuery);
				addCustomerPS.setInt(1, addressID.getInt(1));
				addCustomerPS.setInt(2, personID.getInt(1));
				addCustomerPS.setString(3, name);
				addCustomerPS.setString(4, customerCode);
				addCustomerPS.setString(5, customerType);
				addCustomerPS.executeUpdate();
				
			}else{
				
			}
			
			//closes everything when finished
			addressID.close(); addressIDPS.close(); addAddressPS.close(); addCustomerPS.close(); personIDPS.close(); personID.close(); conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public static void addProduct(String productCode, String productType, double pricePerUnit){
		
		//gets connection from database
		Connection conn = DatabaseInfo.getConnection();
		
		PreparedStatement addProductPS = null;
		
		String addProductQuery = "INSERT INTO Product(productCode, productType, pricePerUnit) VALUES(?,?,?)";
		
		
		
		try {
			
			addProductPS = conn.prepareStatement(addProductQuery);
			addProductPS.setString(1, productCode);
			addProductPS.setString(2, productType);
			addProductPS.setDouble(3, pricePerUnit);
			addProductPS.executeUpdate();
			
			//closes everything when finished
			addProductPS.close(); conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	//10. Adds an movieTicket record to the database with the provided data.
	public static void addMovieTicket(String productCode, String dateTime, String movieName, String street, String city,String state, String zip, String country, String screenNo, double pricePerUnit) {
		
		//checks if the product was added to the database
		PreparedStatement productInfoFromDB = null;
		String productInfoQuery = "SELECT * FROM Product WHERE productCode = ?";
		ResultSet r = null;
		
		//gets connection from database
		Connection conn = DatabaseInfo.getConnection();
		
		PreparedStatement addAddressPS = null;
		PreparedStatement addMovieTicketPS = null;
		PreparedStatement addressIDPS = null;
		PreparedStatement productIDPS = null;
		
		String addAddressQuery = "INSERT INTO Address(address, city, state, zip, country) VALUES(?,?,?,?,?)";
		String addMovieTicketQuery = "INSERT INTO MovieTicket(productID, addressID, dateAndTime, movieName, screenNumber) VALUES(?,?,?,?,?)";
		String addressIDQuery = "SELECT addressID FROM Address WHERE address = ?";
		String productIDQuery = "SELECT productID FROM Product WHERE productCode = ?";
		
		ResultSet addressID = null;
		ResultSet productID = null;
		addProduct(productCode, "MovieTicket", pricePerUnit);
		
		try {
			
			//checks if the product was added to the database
			productInfoFromDB = conn.prepareStatement(productInfoQuery);
			productInfoFromDB.setString(1, productCode);
			r = productInfoFromDB.executeQuery();
			r.next();
			System.out.println("Code: " + r.getString(2) + "     Type: " + r.getString(3) + "     Price Per Unit: " + r.getDouble(4));
			
			addAddressPS = conn.prepareStatement(addAddressQuery);
			addAddressPS.setString(1, street);
			addAddressPS.setString(2, city);
			addAddressPS.setString(3, state);
			addAddressPS.setString(4, zip);
			addAddressPS.setString(5, country);
			addAddressPS.executeUpdate();
			
			addressIDPS = conn.prepareStatement(addressIDQuery);
			addressIDPS.setString(1, street);
			addressID = addressIDPS.executeQuery();
			addressID.next();
			
			productIDPS = conn.prepareStatement(productIDQuery);
			productIDPS.setString(1, productCode);
			productID = productIDPS.executeQuery();
			
			if(productID.next()){
				
				addMovieTicketPS = conn.prepareStatement(addMovieTicketQuery);
				addMovieTicketPS.setInt(1, productID.getInt(1));
				addMovieTicketPS.setInt(2, addressID.getInt(1));
				addMovieTicketPS.setString(3, dateTime);
				addMovieTicketPS.setString(4, movieName);
				addMovieTicketPS.setString(5, screenNo);
				addMovieTicketPS.executeUpdate();
				
			}
			
			
			//closes everything when finished
			addressID.close(); productID.close(); addAddressPS.close(); addMovieTicketPS.close(); addressIDPS.close(); productIDPS.close(); conn.close();
			productInfoFromDB.close();
			r.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	//11. Adds a seasonPass record to the database with the provided data.
	public static void addSeasonPass(String productCode, String name, String seasonStartDate, String seasonEndDate,	double cost) {
		
		//checks if the product was added to the database
				PreparedStatement productInfoFromDB = null;
				String productInfoQuery = "SELECT * FROM Product WHERE productCode = ?";
				ResultSet r = null;
		
		//gets connection from database
		Connection conn = DatabaseInfo.getConnection();
		
		PreparedStatement addSeasonPassPS = null;
		PreparedStatement productIDPS = null;
		
		String addSeasonPassQuery = "INSERT INTO SeasonPass(productID, seasonPassName, startDate, endDate) VALUES(?,?,?,?)";
		String productIDQuery = "SELECT productID FROM Product WHERE productCode = ?";
		
		ResultSet productID = null;
		addProduct(productCode, "SeasonPass", cost);
		
		try {
			
			//checks if the product was added to the database
			productInfoFromDB = conn.prepareStatement(productInfoQuery);
			productInfoFromDB.setString(1, productCode);
			r = productInfoFromDB.executeQuery();
			r.next();
			System.out.println("Code: " + r.getString(2) + "     Type: " + r.getString(3) + "     Price Per Unit: " + r.getDouble(4));
			
			productIDPS = conn.prepareStatement(productIDQuery);
			productIDPS.setString(1, productCode);
			productID = productIDPS.executeQuery();
			
			if(productID.next()){
				
				addSeasonPassPS = conn.prepareStatement(addSeasonPassQuery);
				addSeasonPassPS.setInt(1, productID.getInt(1));
				addSeasonPassPS.setString(2, name);
				addSeasonPassPS.setString(3, seasonStartDate);
				addSeasonPassPS.setString(4, seasonEndDate);
				addSeasonPassPS.executeUpdate();
				
			}
			
			//closes everything when finished
			productID.close(); addSeasonPassPS.close(); productIDPS.close(); conn.close();
			productInfoFromDB.close();
			r.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	//12. Adds a ParkingPass record to the database with the provided data.
	public static void addParkingPass(String productCode, double parkingFee) {
		
		//checks if the product was added to the database
				PreparedStatement productInfoFromDB = null;
				String productInfoQuery = "SELECT * FROM Product WHERE productCode = ?";
				ResultSet r = null;
		
		//gets connection from database
		Connection conn = DatabaseInfo.getConnection();
		
		PreparedStatement addParkingPassPS = null;
		PreparedStatement productIDPS = null;
		
		String addParkingPassQuery = "INSERT INTO ParkingPass(productID) VALUES(?)";
		String productIDQuery = "SELECT productID FROM Product WHERE productCode = ?";
		
		ResultSet productID = null;
		addProduct(productCode, "ParkingPass", parkingFee);
		
		try {
			
			//checks if the product was added to the database
			productInfoFromDB = conn.prepareStatement(productInfoQuery);
			productInfoFromDB.setString(1, productCode);
			r = productInfoFromDB.executeQuery();
			r.next();
			System.out.println("Code: " + r.getString(2) + "     Type: " + r.getString(3) + "     Price Per Unit: " + r.getDouble(4));
			
			productIDPS = conn.prepareStatement(productIDQuery);
			productIDPS.setString(1, productCode);
			productID = productIDPS.executeQuery();
			
			if(productID.next()){
				
				addParkingPassPS = conn.prepareStatement(addParkingPassQuery);
				addParkingPassPS.setInt(1, productID.getInt(1));
				addParkingPassPS.executeUpdate();
				
			}
			
			//closes everything when finished
			productID.close(); addParkingPassPS.close(); productIDPS.close(); conn.close();
			productInfoFromDB.close();
			r.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	//13. Adds a refreshment record to the database with the provided data.
	public static void addRefreshment(String productCode, String name, double cost) {
		
		//checks if the product was added to the database
				PreparedStatement productInfoFromDB = null;
				String productInfoQuery = "SELECT * FROM Product WHERE productCode = ?";
				ResultSet r = null;
		
		//gets connection from database
		Connection conn = DatabaseInfo.getConnection();
		
		PreparedStatement addRefreshmentPS = null;
		PreparedStatement productIDPS = null;
		
		String addRefreshmentQuery = "INSERT INTO Refreshment(productID, refreshmentName) VALUES(?,?)";
		String productIDQuery = "SELECT productID FROM Product WHERE productCode = ?";
		
		ResultSet productID = null;
		addProduct(productCode, "Refreshment", cost);
		
		try {
			
			//checks if the product was added to the database
			productInfoFromDB = conn.prepareStatement(productInfoQuery);
			productInfoFromDB.setString(1, productCode);
			r = productInfoFromDB.executeQuery();
			r.next();
			System.out.println("Code: " + r.getString(2) + "     Type: " + r.getString(3) + "     Price Per Unit: " + r.getDouble(4));
			
			productIDPS = conn.prepareStatement(productIDQuery);
			productIDPS.setString(1, productCode);
			productID = productIDPS.executeQuery();
			
			if(productID.next()){
				
				addRefreshmentPS = conn.prepareStatement(addRefreshmentQuery);
				addRefreshmentPS.setInt(1, productID.getInt(1));
				addRefreshmentPS.setString(2, name);
				addRefreshmentPS.executeUpdate();
				
			}
			
			//closes everything when finished
			productID.close(); addRefreshmentPS.close(); productIDPS.close(); conn.close();
			productInfoFromDB.close();
			r.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	//14. Adds an invoice record to the database with the given data.
	public static void addInvoice(String invoiceCode, String customerCode, String salesPersonCode, String invoiceDate) {
		
		//gets connection from database
		Connection conn = DatabaseInfo.getConnection();
		
		PreparedStatement addInvoicePS = null;
		PreparedStatement personIDPS = null;
		PreparedStatement customerIDPS = null;
		
		String addInvoiceQuery = "INSERT INTO Invoice(customerID, personID, invoiceCode, invoiceDate) VALUES(?,?,?,?)";
		String personIDQuery = "SELECT personID FROM Person WHERE personCode = ?";
		String customerIDQuery = "SELECT customerID FROM Customer WHERE customerID = ?";
		
		ResultSet personID = null;
		ResultSet customerID = null;
		
		try {
			
			personIDPS = conn.prepareStatement(personIDQuery);
			personIDPS.setString(1, salesPersonCode);
			personID = personIDPS.executeQuery();
			
			customerIDPS = conn.prepareStatement(customerIDQuery);
			customerIDPS.setString(1, customerCode);
			customerID = customerIDPS.executeQuery();
			
			//if statements check if there is a matching customer and salesperson with the provided codes.
			if(personID.next()){
				
				if(customerID.next()){
					
					addInvoicePS = conn.prepareStatement(addInvoiceQuery);
					addInvoicePS.setInt(1, customerID.getInt(1));
					addInvoicePS.setInt(2, personID.getInt(1));
					addInvoicePS.setString(3, invoiceCode);
					addInvoicePS.setString(4, invoiceDate);
					addInvoicePS.executeUpdate();
					
				}else{
					
				}
				
			}else{
				
			}
			
			//closes everything when finished
			personID.close();
			customerID.close();
			personIDPS.close();
			customerIDPS.close();
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}

	//15. Used to add a product to an invoice, can be reused.
	public static void addProductToInvoice(String invoiceCode, String productCode, int quantity){
		
		//gets connection from database
		Connection conn = DatabaseInfo.getConnection();
		
		PreparedStatement addProductPS = null;
		String addProductQuery = "INSERT INTO InvoiceProduct(invoiceID, productID, units) VALUES(?,?,?)";
		
		PreparedStatement productIDPS = null;
		String productIDQuery = "SELECT productID FROM Product WHERE productCode = ?";
		ResultSet productID = null;
		
		PreparedStatement invoiceIDPS = null;
		String invoiceIDQuery = "SELECT invoiceID FROM Invoice WHERE invoiceCode = ?";
		ResultSet invoiceID = null;
				
		try {
			
			productIDPS = conn.prepareStatement(productIDQuery);
			productIDPS.setString(1, productCode);
			productID = productIDPS.executeQuery();
			
			invoiceIDPS = conn.prepareStatement(invoiceIDQuery);
			invoiceIDPS.setString(1, invoiceCode);
			invoiceID = invoiceIDPS.executeQuery();
			
			if(invoiceID != null && productID != null){
				addProductPS = conn.prepareStatement(addProductQuery);
				addProductPS.setInt(1, invoiceID.getInt(1));
				addProductPS.setInt(2, productID.getInt(1));
				addProductPS.setInt(3, quantity);
				addProductPS.executeUpdate();
			}
			
			
			//closes everything when finished
			productID.close(); invoiceID.close(); addProductPS.close(); invoiceIDPS.close(); productIDPS.close(); conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	//16. Adds a MovieTicket to an Invoice
	public static void addMovieTicketToInvoice(String invoiceCode, String productCode, int quantity) {
		addProductToInvoice(invoiceCode, productCode, quantity);
	}
	
	//17. Adds a SeasonPass to an Invoice
	public static void addSeasonPassToInvoice(String invoiceCode, String productCode, int quantity) {
		addProductToInvoice(invoiceCode, productCode, quantity);
	}
	
	//18. Adds a Refreshment to an Invoice
    public static void addRefreshmentToInvoice(String invoiceCode, String productCode, int quantity) {
    	addProductToInvoice(invoiceCode, productCode, quantity);
    }
    
	//19. Adds a ParkingPass to an Invoice
    public static void addParkingPassToInvoice(String invoiceCode, String productCode, int quantity, String ticketCode) {
    	
    	if(ticketCode == null){
    		addProductToInvoice(invoiceCode, productCode, quantity);
    	}else{
    		
    		//gets connection from database
    		Connection conn = DatabaseInfo.getConnection();
    		
    		PreparedStatement addProductPS = null;
    		String addProductQuery = "INSERT INTO InvoiceProduct(invoiceID, productID, units, ticketCode) VALUES(?,?,?,?)";
    		
    		PreparedStatement productIDPS = null;
    		String productIDQuery = "SELECT productID FROM Product WHERE productCode = ?";
    		ResultSet productID = null;
    		
    		PreparedStatement invoiceIDPS = null;
    		String invoiceIDQuery = "SELECT invoiceID FROM Invoice WHERE invoiceCode = ?";
    		ResultSet invoiceID = null;
    				
    		try {
    			
    			productIDPS = conn.prepareStatement(productIDQuery);
    			productIDPS.setString(1, productCode);
    			productID = productIDPS.executeQuery();
    			
    			invoiceIDPS = conn.prepareStatement(invoiceIDQuery);
    			invoiceIDPS.setString(1, invoiceCode);
    			invoiceID = invoiceIDPS.executeQuery();
    			
    			if(invoiceID != null && productID != null){
    				addProductPS = conn.prepareStatement(addProductQuery);
    				addProductPS.setInt(1, invoiceID.getInt(1));
        			addProductPS.setInt(2, productID.getInt(1));
        			addProductPS.setInt(3, quantity);
        			addProductPS.setString(4, ticketCode);
        			addProductPS.executeUpdate();
    			}
    			
    			
    			//closes everything when finished
    			productID.close(); invoiceID.close(); addProductPS.close(); invoiceIDPS.close(); productIDPS.close(); conn.close();
    			
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    		
    	}
    	
    }

}