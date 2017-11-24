package assignment_2;

public class InvoiceListNode {
	
	private InvoiceListNode next;
    private Invoice invoice;
    
    public InvoiceListNode(Invoice i) {
		
		this.invoice = i;
		this.next = null;
		
	}
    
    public Invoice getInvoice() {
        return invoice;
    }

    public InvoiceListNode getNext() {
        return next;
    }

    public void setNext(InvoiceListNode next) {
        this.next = next;
    }
	
}