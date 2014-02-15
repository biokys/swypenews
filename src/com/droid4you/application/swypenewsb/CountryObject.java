package com.droid4you.application.swypenews;

/**
 * Created with IntelliJ IDEA.
 * User: muller10
 * Date: 8/5/12
 * Time: 9:47 PM
 */
final public class CountryObject {

    private final String countryCode;

    private final String countryName;

    public CountryObject(String countryCode, String countryName) {
        this.countryCode = countryCode;
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getCountryName() {
        return countryName;
    }
}
