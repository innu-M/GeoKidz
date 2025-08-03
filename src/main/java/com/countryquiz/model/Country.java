package com.countryquiz.model;

public class Country {
    private String country;  // Must match JSON field name exactly
    private String capital;
    private String language;
    private String flag;
    private String currency;

    // Getters and setters must match these exact field names
    public String getCountry() { return country; }
    public String getCapital() { return capital; }
    public String getLanguage() { return language; }
    public String getFlag() { return flag; }
    public String getCurrency() { return currency; }

    // Setters
    public void setCountry(String country) { this.country = country; }
    public void setCapital(String capital) { this.capital = capital; }
    public void setLanguage(String language) { this.language = language; }
    public void setFlag(String flag) { this.flag = flag; }
    public void setCurrency(String currency) { this.currency = currency; }
}