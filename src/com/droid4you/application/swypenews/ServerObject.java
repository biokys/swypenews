package com.droid4you.application.swypenews;

final public class ServerObject {

    final private String url;

    final private String name;

    final private String countryCode;

    public ServerObject(String countryCode, String url) {

        this(countryCode, url, url);
    }

    public ServerObject(String countryCode, String url, String name) {
        this.url = url;
        this.name = name;
        this.countryCode = countryCode;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getCountryCode() {
        return countryCode;
    }
}