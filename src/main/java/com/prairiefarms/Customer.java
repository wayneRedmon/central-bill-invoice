package com.prairiefarms;

public class Customer {

    private int id;
    private String name;
    private String address1;
    private String address2;
    private String city;
    private String stateProvince;
    private String postalCode;
    private String country;
    private String phone;
    private String fax;
    private int route;
    private int centralBill;
    private String billingFrequency;
    private int terms;
    private int salesperson;
    private double discount;
    private double taxRate;
    private String emailType;
    private String emailAddress;
    private String status;


    public int getID() {
        return id;
    }

    public void setID(int iD) {
        this.id = iD;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress_Formatted() {
        String zip = "";
        String zip4 = "";
        String address = "";

        try {
            zip = postalCode.substring(0, 5);
            zip4 = postalCode.substring(5, 9);

            if (!zip4.trim().equals("0000")) {
                zip = zip.trim() + "-" + zip4.trim();
            }

            address = city.trim() + ", " +
                    stateProvince.trim() + "  " +
                    zip.trim();
        } catch (IndexOutOfBoundsException ignore) {
            address = "";
        }

        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getPhone_Formatted() {
        String phoneFormatted = "";

        try {
            phoneFormatted = "(" + phone.substring(0, 3) + ") " +
                    phone.substring(3, 6) + "-" +
                    phone.substring(6, 10);
        } catch (IndexOutOfBoundsException ignore) {
            phoneFormatted = "";
        }

        return phoneFormatted;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public String getFax_Formatted() {
        String faxFormatted = "";

        try {
            faxFormatted = "(" + fax.substring(0, 3) + ") " +
                    fax.substring(3, 6) + "-" +
                    fax.substring(6, 10);

        } catch (IndexOutOfBoundsException ignore) {
            faxFormatted = "";
        }
        return faxFormatted;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public int getRoute() {
        return route;
    }

    public void setRoute(int route) {
        this.route = route;
    }

    public int getCentralBill() {
        return centralBill;
    }

    public void setCentralBill(int centralBill) {
        this.centralBill = centralBill;
    }

    public String getBillingFrequency() {
        return billingFrequency;
    }

    public void setBillingFrequency(String billingFrequency) {
        this.billingFrequency = billingFrequency;
    }

    public int getTerms() {
        return terms;
    }

    public void setTerms(int terms) {
        this.terms = terms;
    }

    public int getSalesperson() {
        return salesperson;
    }

    public void setSalesperson(int salesperson) {
        this.salesperson = salesperson;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTaxRateFixed() {
        return (taxRate * 100f);
    }

    public double getTaxRateRaw() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public String getEmailType() {
        return emailType;
    }

    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
