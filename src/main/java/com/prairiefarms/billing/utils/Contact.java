package com.prairiefarms.billing.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class Contact {

    private final int id;
    private final String name;
    private final String street;
    private final String city;
    private final String state;
    private final int zip;
    private final long phone;
    private final List<String> email;

    public Contact(int id,
                   String name,
                   String street,
                   String city,
                   String state,
                   int zip,
                   long phone,
                   List<String> email) {
        this.id = id;
        this.name = name;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phone = phone;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStreet() {
        return StringUtils.normalizeSpace(street);
    }

    public String getAddress() {
        final String zipCode = StringUtils.normalizeSpace(String.format("%-9s", zip).replace(' ', '0'));

        return StringUtils.normalizeSpace(city) + ", " +
                StringUtils.normalizeSpace(state) + "  " +
                zipCode.substring(0, 5) + "-" +
                zipCode.substring(5, 9);
    }

    public String getPhone() {
        String phoneFormatted;

        try {
            phoneFormatted = "(" + String.valueOf(phone).substring(0, 3) + ") " +
                    String.valueOf(phone).substring(3, 6) + "-" +
                    String.valueOf(phone).substring(6, 10);
        } catch (Exception exception) {
            phoneFormatted = "";
        }

        return phoneFormatted;
    }

    public List<String> getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return id + " " + name + " " + street + " " + city + " " + state + " " +
                zip + " " + phone;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object object) {
        boolean isEquals = false;

        if (object instanceof Contact) {
            if (StringUtils.equals(this.toString(), object.toString())) {
                isEquals = true;
            }
        }

        return isEquals;
    }
}
