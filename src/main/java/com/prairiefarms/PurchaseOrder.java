package com.prairiefarms;


public class PurchaseOrder {
	
	private String contract;
	
	
	public String getContract() {
		if (contract == null) {
			contract = "";
		}
		
		return contract;
	}

	public void setContract(String contract) {
		this.contract = contract;
	}
}
