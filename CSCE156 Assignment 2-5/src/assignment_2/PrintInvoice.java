package assignment_2;

import java.text.DecimalFormat;

public class PrintInvoice {
	
	public static void toConsole(InvoiceList invoiceList){
		
		//these are the overall grand totals for the invoices... used later on
		double subtotal = 0.0;
		double fees = 0.0;
		double taxes = 0.0;
		double discount = 0.0;
		double total = 0.0;
		
		DecimalFormat df = new DecimalFormat("0.00");
		System.out.println("Individual Invoice Detail Reports");
		System.out.println("==================================================");
		//this for-loop prints the calculated totals for each item in the invoice
		for(int i = 0; i < invoiceList.getSize(); i++){
			invoiceList.getInvoiceListNode(i).getInvoice().printInvoice();
		}
		
		//everything here down focuses on printing the invoice overview
		System.out.println("=========================");
		System.out.println("Executive Summary Reoprt");
		System.out.println("=========================");
		System.out.printf("%-10s %-40s %-21s %10s %11s %11s %11s %11s", "Invoice", "Customer", "Salesperson", "Subtotal", "Fees", "Taxes", "Discount", "Total");
		System.out.println();
		
		//this for-loop finds the totals of all the invoices
		for(int i = 0; i < invoiceList.getSize(); i++){
			
			subtotal += invoiceList.getInvoiceListNode(i).getInvoice().getInvoiceSubtotal();
			fees += invoiceList.getInvoiceListNode(i).getInvoice().getCustomerObject().getAditionalFee();
			taxes += invoiceList.getInvoiceListNode(i).getInvoice().getInvoiceTax();
			discount += invoiceList.getInvoiceListNode(i).getInvoice().getInvoiceDiscount();
			total += invoiceList.getInvoiceListNode(i).getInvoice().getInvoiceTotal();
			System.out.printf("%-10s %-40s %-20s $%10s $%10s $%10s $%10s $%10s",
					invoiceList.getInvoiceListNode(i).getInvoice().getInvoiceCode(),
					invoiceList.getInvoiceListNode(i).getInvoice().getCustomerObject().getName() + " [" + invoiceList.getInvoiceListNode(i).getInvoice().getCustomerObject().getType() + "]",
					invoiceList.getInvoiceListNode(i).getInvoice().getPersonObject().getFullName(),
					df.format(invoiceList.getInvoiceListNode(i).getInvoice().getInvoiceSubtotal()),
					df.format(invoiceList.getInvoiceListNode(i).getInvoice().getCustomerObject().getAditionalFee()),
					df.format(invoiceList.getInvoiceListNode(i).getInvoice().getInvoiceTax()),
					df.format(invoiceList.getInvoiceListNode(i).getInvoice().getInvoiceDiscount()),
					df.format(invoiceList.getInvoiceListNode(i).getInvoice().getInvoiceTotal()));
			System.out.println();
			
		}
		
		System.out.println("====================================================================================================================================");
		
		
		//this prints the totals to the invoice overview
		System.out.printf("%-72s $%10s $%10s $%10s $%10s $%10s",
				"Totals",
				df.format(subtotal),
				df.format(fees),
				df.format(taxes),
				df.format(discount),
				df.format(total));
		
	}

}
