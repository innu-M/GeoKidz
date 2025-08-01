package com.countryquiz.model;

public class Country {
    private String name = "";
    private String capital = "";
    private String language = "";
    private String flag = "";
    private String currency = "";

    // Getters with null checks
    public String getName() { return name != null ? name : ""; }
    public String getCapital() { return capital != null ? capital : ""; }
    public String getLanguage() { return language != null ? language : ""; }
    public String getFlag() { return flag != null ? flag : ""; }
    public String getCurrency() { return currency != null ? currency : ""; }

    // Setters with null checks
    public void setName(String name) { this.name = name != null ? name : ""; }
    public void setCapital(String capital) { this.capital = capital != null ? capital : ""; }
    public void setLanguage(String language) { this.language = language != null ? language : ""; }
    public void setFlag(String flag) { this.flag = flag != null ? flag : ""; }
    public void setCurrency(String currency) { this.currency = currency != null ? currency : ""; }
}