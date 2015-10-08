package com.syt.balram.syt.BE;

/**
 * Created by Balram on 5/8/2015.
 */
public class ManageCategoryBE {

    private String subcategory;
    private String exp;
    private String expDetails;
    private String serviceLocation;
    private String availability;
    private String minPrice;
    private String maxPrice;
    private String chargeMode;

    public String getChargeMode() {
        return chargeMode;
    }

    public void setChargeMode(String chargeMode) {
        this.chargeMode = chargeMode;
    }

    public String getExpDetails() {
        return expDetails;
    }

    public void setExpDetails(String expDetails) {
        this.expDetails = expDetails;
    }

    public String getServiceLocation() {
        return serviceLocation;
    }

    public void setServiceLocation(String serviceLocation) {
        this.serviceLocation = serviceLocation;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getSubcategory() {

        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }
}
