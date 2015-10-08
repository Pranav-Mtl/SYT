package com.syt.balram.syt.BE;

import java.io.Serializable;

/**
 * Created by Balram on 4/24/2015.
 */
public class RequirementBE implements Serializable {

    private String category;
    private String subCategory;
    private String experience;
    private String serviceDays;
    private String serviceLocation;
    private String serviceCharge;
    private String serviceContacted;
    private String email;
    private String minPrice;
    private String maxPrice;
    private String title;
    private String desc;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }



    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getServiceDays() {
        return serviceDays;
    }

    public void setServiceDays(String serviceDays) {
        this.serviceDays = serviceDays;
    }

    public String getServiceLocation() {
        return serviceLocation;
    }

    public void setServiceLocation(String serviceLocation) {
        this.serviceLocation = serviceLocation;
    }

    public String getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getServiceContacted() {
        return serviceContacted;
    }

    public void setServiceContacted(String serviceContacted) {
        this.serviceContacted = serviceContacted;
    }
}
