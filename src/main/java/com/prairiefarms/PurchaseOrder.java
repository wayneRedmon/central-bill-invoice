package com.prairiefarms;

public class PurchaseOrder {

    private String contract;

    public String getContract() {
        return contract == null ? "" : contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }
}
