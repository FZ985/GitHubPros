package com.kmt.pro.event;

/**
 * Create by JFZ
 * date: 2020-07-06 15:00
 **/
public class CountryCodeEvent {
    private String countryName;
    private String countryCode;

    public CountryCodeEvent(String countryName, String countryCode) {
        this.countryName = countryName;
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }
}
