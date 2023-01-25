/*
 *   XFi - (X)Cross-reference (F)ile (i)nterface
 *     Use this interface class to create various class implementations
 *       of a customer's invoice file type.  (eg) .PDF, .XLS, .CVS, .TXT, etc.  
 */

package com.prairiefarms;

import java.util.ArrayList;


public interface XFi_Invoice {

	void open(String title);
	
	void setLocation(Location location);
	void setProductSummary(ArrayList<Item> items);
	void addInvoice(Invoice invoice, String frequency);
	
	void setLogo();
	
	void setBlock_Summary();    	
	void setBlock_SummaryHeader();
	void setBlock_SummaryFooter();	
	
	void setBlock_Remit();
	void setBlock_Customer();	
	void setBlock_Sales();
	
	void setBlock_Item();
	void setItems();	
	
	void setBlock_Total();
	
	void setBlock_ProductSummary();
	
	void setBlock_ThankYou();
	
	void close(); 
}
