package assignment_2;

import java.util.Comparator;

public class InvoiceList{

	private InvoiceListNode start;
	private int size;
	private Comparator<Invoice> comp;
	
	public InvoiceList(Comparator<Invoice> comp){
		
		this.start = null;
		this.size = 0;
		this.comp = comp;
		
	}
	
	public Comparator<Invoice> getComp(){
		return this.comp;
	}
	
	public int getSize(){
		return this.size;
	}
	
	public InvoiceListNode getInvoiceListNode(int position) {
		
		InvoiceListNode currentNode = start;
		int count = 0;
		
		while(currentNode != null){
			
			if(count == position){
				
				return currentNode;
				
			}
			
			count++;
			currentNode = currentNode.getNext();
			
		}
		
		return null;
		
	}
	
	public void add(Invoice i){
    	
		InvoiceListNode node = new InvoiceListNode(i);
		
		//if the list is empty set the start node to current node
		if(start == null){
			start = node;
			size++;
			node.setNext(null);
		}
		
		//if there is only one item in the list currently
		else if(size == 1){
			
			//if the invoice being added is larger that the start invoice then add the invoice to the start
			if(getComp().compare(i, start.getInvoice()) > 0){
				
				start.setNext(node);
				node.setNext(null);
				size++;
				
			}else{
				
				node.setNext(start);
				start.setNext(null);
				start = node;
				size++;
				
			}
			
		}
		
		//if there are multiple items in the list already
		else{
			
			//if the new node is the smallest node in the list
			if(getComp().compare(i, start.getInvoice()) < 0){
				
				node.setNext(start);
				start = node;
				size++;
				
			}
			
			else if(getComp().compare(i, getInvoiceListNode(size-1).getInvoice()) > 0){
				
				getInvoiceListNode(size-1).setNext(node);
				size++;
				
			}
			
			//the node is larger than the start but smaller than node 2
			else if(getComp().compare(i, start.getNext().getInvoice()) < 0){
				
				node.setNext(start.getNext());
				start.setNext(node);
				size++;
			}
			
			//the node is larger than node 2 (or node3...)
			else{
				
				int position = 0;
				int k = 0;
				while(position == 0){
					
					if(getComp().compare(i, getInvoiceListNode(k).getInvoice()) < 0){
						
						position = k;
						
					}
					
					k++;
					
				}
				
				node.setNext(getInvoiceListNode(position));
				getInvoiceListNode(position-1).setNext(node);
				size++;
				
			}
			
		}
		
    }

}


