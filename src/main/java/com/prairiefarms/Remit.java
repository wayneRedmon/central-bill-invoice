package com.prairiefarms;


import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;

public class Remit {

    private int id;
    private String name;
    private String address1;
    private String address2;
    private String city;
    private String stateProvince;
    private String postalCode;
    private String country;
    private Long phone;
    private Long fax;
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
        String addressFormatted = city.trim() + ", " +
                stateProvince.trim();

        String zip = "";

        try {
            zip = postalCode.substring(0, 5);

            if (postalCode.trim().length() == 9) {
                zip = zip.trim() + "-" + postalCode.substring(5, 9).trim();
            }

            addressFormatted = addressFormatted.trim() + "  " + zip.trim();

        } catch (IndexOutOfBoundsException e) {
            addressFormatted = addressFormatted.trim() + "  " + postalCode.trim();
        }

        return addressFormatted;
    }

    public Long getPhone() {
        return phone;
    }

    public String getPhone_Formatted() {
        DecimalFormat decimalFormat = new DecimalFormat("0000000000");
        String phoneText = decimalFormat.format(phone);
        return "(" + phoneText.substring(0, 3) + ") " +
                phoneText.substring(3, 6) + "-" +
                phoneText.substring(6, 10);
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public Long getFax() {
        return fax;
    }

    public String getFax_Formatted() {
        String faxFormatted = "";

        if (fax != 0L) {
            DecimalFormat decimalFormat = new DecimalFormat("0000000000");
            String faxText = decimalFormat.format(fax);

            try {
                faxFormatted = "(" + faxText.substring(0, 3) + ") " +
                        faxText.substring(3, 6) + "-" +
                        faxText.substring(6, 10);
            } catch (IndexOutOfBoundsException e) {
                faxFormatted = "(" + faxText.trim() + ")";
            }
        }

        return faxFormatted;
    }

    public void setFax(Long fax) {
        this.fax = fax;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
