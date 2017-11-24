package assignment_2;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ceg.ext.DatabaseInfo;
import com.ceg.ext.InvoiceData;

public class DataConverter {
	
	public static void main(String[] args) throws IOException {
		
		ArrayList<Person> personList = new ArrayList<Person>();
		ArrayList<Customer> customerList = new ArrayList<Customer>();
		ArrayList<Product> productList = new ArrayList<Product>();
		ArrayList<Invoice> invoiceList = new ArrayList<Invoice>();
		
		ArrayList<Address> addressList = new ArrayList<Address>();
		ArrayList<String> emailList = new ArrayList<String>();
		ArrayList<BoughtProduct> boughtProductList = new ArrayList<BoughtProduct>();
		
		ResultSet persons = getTableData("Person");
		//try block creates person objects and adds them to a list
		try {
			
			while(persons.next()){
				
				Address a = getPersonAddress(persons.getInt(1));
				addressList.add(a);
				
				emailList = getEmailList(persons.getInt(1));
				
				Person p = new Person(persons.getString(3), persons.getString(4), persons.getString(5), a, emailList);
				personList.add(p);
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				persons.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		ResultSet customers = getTableData("Customer");
		//try block creates customer objects and adds them to a list
		try {
			
			while(customers.next()){
				
				Address a = getCustomerAddress(customers.getInt(1));
				addressList.add(a);
				
				String personCode = getPerson(customers.getInt(3));
				int indexVal = 0;
				for(int i = 0; i < personList.size(); i++){
					if(personCode.equals(personList.get(i).getPersonCode())){
						indexVal = i;
					}
				}
				
				if(customers.getString(6).equals("Student")){
					Customer c = new Student(customers.getString(5), customers.getString(6), customers.getString(4), a, personList.get(indexVal));
					customerList.add(c);
				}else{
					Customer c = new General(customers.getString(5), customers.getString(6), customers.getString(4), a, personList.get(indexVal));
					customerList.add(c);
				}
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				customers.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		ResultSet products = getTableData("Product");
		//try block creates product objects and adds them to a list
		try {
			
			while(products.next()){
				
				if(products.getString(3).equals("MovieTicket")){
					
					Product p = getProduct(products, "MovieTicket");
					productList.add(p);
					
				}else if(products.getString(3).equals("SeasonPass")){
					
					Product p = getProduct(products, "SeasonPass");
					productList.add(p);
					
				}else if(products.getString(3).equals("ParkingPass")){
					
					Product p = getProduct(products, "ParkingPass");
					productList.add(p);
					
				}else if(products.getString(3).equals("Refreshment")){
					
					Product p = getProduct(products, "Refreshment");
					productList.add(p);
					
				}
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				products.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		ResultSet invoices = getTableData("Invoice");
		//try block creates a list of invoices 
		try {
			
			//keeps track of the invoice currently being produced
			int indexOfInvoice = 0;
			
			//iterates through all of the invoices in the result set
			while(invoices.next()){
				indexOfInvoice++;
				
				String customerCode = getCustomer(invoices.getInt(2));
				String personCode = getPerson(invoices.getInt(3));
				
				int customerIndexVal = 0;
				for(int i = 0; i < customerList.size(); i++){
					if(customerCode.equals(customerList.get(i).getCustomerCode())){
						customerIndexVal = i;
					}
				}
				int personIndexVal = 0;
				for(int i = 0; i < personList.size(); i++){
					if(personCode.equals(personList.get(i).getPersonCode())){
						personIndexVal = i;
					}
				}
				
				//makes a list of the bought products on the invoice
				boughtProductList = getBoughtProductList(indexOfInvoice, productList);
				Invoice i = new Invoice(invoices.getString(4), invoices.getString(5), customerList.get(customerIndexVal), personList.get(personIndexVal), boughtProductList);
				invoiceList.add(i);
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				invoices.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//sets all invoice totals and product totals to their respective locations
		for(int i = 0; i < invoiceList.size(); i++){
			
			for(int j = 0; j < invoiceList.get(i).getBoughtProduct().size(); j++){
				
				invoiceList.get(i).getBoughtProduct().get(j).setSubtotal(invoiceList.get(i).getBoughtProduct(), invoiceList.get(i).getInvoiceDate());
				invoiceList.get(i).getBoughtProduct().get(j).setTax();
				invoiceList.get(i).getBoughtProduct().get(j).setTotal();
				
			}
			
			invoiceList.get(i).setInvoiceTotals();
			
		}
		
		//creates the ordered invoice list from the invoice list
		InvoiceList orderedInvoiceList = new InvoiceList(new TotalComparator());
		for(int i = 0; i < invoiceList.size(); i++){
			
			orderedInvoiceList.add(invoiceList.get(i));
			
		}
		
		PrintInvoice.toConsole(orderedInvoiceList);
		InvoiceData.removeAllInvoices();
		
	}
	
	private static ArrayList<BoughtProduct> getBoughtProductList(int invoiceID, ArrayList<Product> productList) {
		
		Connection conn = DatabaseInfo.getConnection();
		
		PreparedStatement  getTableInfo = null;
		String getTableQuery = "SELECT * FROM InvoiceProduct WHERE invoiceID = ?";
		ResultSet tableInfo = null;
		
		PreparedStatement  getTableInfo2 = null;
		String getTableQuery2 = "SELECT productCode FROM Product WHERE productID = ?";
		ResultSet tableInfo2 = null;
		
		ArrayList<BoughtProduct> boughtProductList = new ArrayList<BoughtProduct>();
		
		try {
			getTableInfo = conn.prepareStatement(getTableQuery);
			getTableInfo.setInt(1, invoiceID);
			//returns everything in InvoiceProduct with specific invoiceID
			tableInfo = getTableInfo.executeQuery();
			
			//gets the product codes of the products in a single invoice
			while(tableInfo.next()){
				int productID = tableInfo.getInt(2);
				getTableInfo2 = conn.prepareStatement(getTableQuery2);
				getTableInfo2.setInt(1, productID);
				//returns productCode for a specific product
				tableInfo2 = getTableInfo2.executeQuery();
				
				
				while(tableInfo2.next()){
					
					int productIndexVal = 0;
					for(int i = 0; i < productList.size(); i++){
						if(tableInfo2.getString(1).equals(productList.get(i).getCode())){
							productIndexVal = i;
						}
					}
					
					if(tableInfo.getString(4) == null){
						BoughtProduct bp = new BoughtProduct(productList.get(productIndexVal), tableInfo.getInt(3));
						boughtProductList.add(bp);
					}else{
						BoughtProduct bp = new BoughtProduct(productList.get(productIndexVal), tableInfo.getInt(3), tableInfo.getString(4));
						boughtProductList.add(bp);
					}
					
				}

			}
			
			return boughtProductList;
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				tableInfo.close(); 
				getTableInfo.close();
				tableInfo2.close(); 
				getTableInfo2.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		}
		
		return null;
		
	}

	private static Address getProductAddress(int addressID) {
		
		Connection conn = DatabaseInfo.getConnection();
		
		PreparedStatement  getTableInfo = null;
		String getTableQuery = "SELECT Address.address, Address.city, Address.state, Address.zip, Address.country FROM Address WHERE Address.addressID = ?";
		ResultSet tableInfo = null;
		
		try {
			getTableInfo = conn.prepareStatement(getTableQuery);
			getTableInfo.setInt(1, addressID);
			tableInfo = getTableInfo.executeQuery();
			tableInfo.next();
			Address a = new Address(tableInfo.getString(1), tableInfo.getString(2),tableInfo.getString(3),tableInfo.getString(4),tableInfo.getString(5));
			return a;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				tableInfo.close(); 
				getTableInfo.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		}
		
		return null;
		
	}
	
	private static Product getProduct(ResultSet products, String tableName){
		
		Connection conn = DatabaseInfo.getConnection();
		
		PreparedStatement  getTableInfo = null;
		String getTableQuery = "SELECT * FROM Product JOIN --- ON Product.productID = ---.productID WHERE Product.productID = ?";
		String query = getTableQuery.replace("---", tableName);
		ResultSet tableInfo = null;
		
		try {
			getTableInfo = conn.prepareStatement(query);
			getTableInfo.setInt(1, products.getInt(1));
			tableInfo = getTableInfo.executeQuery();
			tableInfo.next();
			Product p = null;
			
			if(tableName.equals("MovieTicket")){
				
				Address a = getProductAddress(tableInfo.getInt(7));
				p = new MovieTicket(products.getString(2), products.getString(3), tableInfo.getString(8), tableInfo.getString(9), a, tableInfo.getString(10), products.getDouble(4));
				return p;
				
			}else if(tableName.equals("SeasonPass")){
				
				p = new SeasonPass(products.getString(2), products.getString(3), tableInfo.getString(7), tableInfo.getString(8), tableInfo.getString(9), products.getDouble(4));
				return p;
				
			}else if(tableName.equals("ParkingPass")){
				
				p = new ParkingPass(products.getString(2), products.getString(3), products.getDouble(4));
				return p;
				
			}else if(tableName.equals("Refreshment")){
				
				p = new Refreshment(products.getString(2), products.getString(3), tableInfo.getString(7), products.getDouble(4));
				return p;
				
			}
			
			return p;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				tableInfo.close(); 
				getTableInfo.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		}
		
		return null;
		
	}
		
	
	public static ResultSet getTableData(String tableName){
		
		Connection conn = DatabaseInfo.getConnection();
		
		PreparedStatement  getTableInfo = null;
		String getTableQuery = "SELECT * FROM ---";
		String query = getTableQuery.replace("---", tableName);
		ResultSet tableInfo = null;
		
		try {
			getTableInfo = conn.prepareStatement(query);
			tableInfo = getTableInfo.executeQuery();
			return tableInfo;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}

	private static Address getCustomerAddress(int customerID) {
		
		Connection conn = DatabaseInfo.getConnection();
		
		PreparedStatement  getTableInfo = null;
		String getTableQuery = "SELECT Address.address, Address.city, Address.state, Address.zip, Address.country FROM Address JOIN Customer ON Address.addressID = Customer.addressID WHERE Customer.customerID = ?";
		ResultSet tableInfo = null;
		
		try {
			getTableInfo = conn.prepareStatement(getTableQuery);
			getTableInfo.setInt(1, customerID);
			tableInfo = getTableInfo.executeQuery();
			tableInfo.next();
			Address a = new Address(tableInfo.getString(1), tableInfo.getString(2),tableInfo.getString(3),tableInfo.getString(4),tableInfo.getString(5));
			return a;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				tableInfo.close(); 
				getTableInfo.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		}
		
		return null;
		
	}
	
	private static Address getPersonAddress(int personID) {
		
		Connection conn = DatabaseInfo.getConnection();
		
		PreparedStatement  getTableInfo = null;
		String getTableQuery = "SELECT Address.address, Address.city, Address.state, Address.zip, Address.country FROM Address JOIN Person ON Address.addressID = Person.addressID WHERE Person.PersonID = ?";
		ResultSet tableInfo = null;
		
		try {
			getTableInfo = conn.prepareStatement(getTableQuery);
			getTableInfo.setInt(1, personID);
			tableInfo = getTableInfo.executeQuery();
			tableInfo.next();
			Address a = new Address(tableInfo.getString(1), tableInfo.getString(2),tableInfo.getString(3),tableInfo.getString(4),tableInfo.getString(5));
			return a;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				tableInfo.close(); 
				getTableInfo.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		}
		
		return null;
		
	}
	
	private static ArrayList<String> getEmailList(int personID) {
		
		Connection conn = DatabaseInfo.getConnection();
		
		PreparedStatement  getTableInfo = null;
		String getTableQuery = "SELECT Email.email FROM Email JOIN Person ON Email.personID = Person.personID WHERE Person.PersonID = ?";
		ResultSet tableInfo = null;
		ArrayList<String> emailList = new ArrayList<String>();
		
		try {
			getTableInfo = conn.prepareStatement(getTableQuery);
			getTableInfo.setInt(1, personID);
			tableInfo = getTableInfo.executeQuery();
			while(tableInfo.next()){
				emailList.add(tableInfo.getString(1));
			}
			return emailList;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				tableInfo.close(); 
				getTableInfo.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		}
		
		return null;
		
	}
	
	private static String getPerson(int personID) {
		
		Connection conn = DatabaseInfo.getConnection();
		
		PreparedStatement  getTableInfo = null;
		String getTableQuery = "SELECT personCode FROM Person WHERE PersonID = ?";
		ResultSet tableInfo = null;
		
		try {
			getTableInfo = conn.prepareStatement(getTableQuery);
			getTableInfo.setInt(1, personID);
			tableInfo = getTableInfo.executeQuery();
			tableInfo.next();
			return tableInfo.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				tableInfo.close(); 
				getTableInfo.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		}
		
		return null;
		
	}
	
	private static String getCustomer(int customerID) {
		
		Connection conn = DatabaseInfo.getConnection();
		
		PreparedStatement  getTableInfo = null;
		String getTableQuery = "SELECT customerCode FROM Customer WHERE customerID = ?";
		ResultSet tableInfo = null;
		
		try {
			getTableInfo = conn.prepareStatement(getTableQuery);
			getTableInfo.setInt(1, customerID);
			tableInfo = getTableInfo.executeQuery();
			tableInfo.next();
			return tableInfo.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				tableInfo.close(); 
				getTableInfo.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		}
		
		return null;
		
	}
}
