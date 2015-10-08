package com.syt.balram.syt.BE;

import java.io.Serializable;

/**
 * Created by Balram on 5/12/2015.
 */
public class AddServicesBE implements Serializable {

    private String category;
    private String subCategory;
    private String providerType;
    private String experience;
    private String serivceLocation;
    private String serviceCharge;
    private String minPrice;
    private String maxPrice;
    private String serviceDays;
    private String serviceNotified;
    private String detail;
    private String distance;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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

    public String getProviderType() {
        return providerType;
    }

    public void setProviderType(String providerType) {
        this.providerType = providerType;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getSerivceLocation() {
        return serivceLocation;
    }

    public void setSerivceLocation(String serivceLocation) {
        this.serivceLocation = serivceLocation;
    }

    public String getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
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

    public String getServiceDays() {
        return serviceDays;
    }

    public void setServiceDays(String serviceDays) {
        this.serviceDays = serviceDays;
    }

    public String getServiceNotified() {
        return serviceNotified;
    }

    public void setServiceNotified(String serviceNotified) {
        this.serviceNotified = serviceNotified;
    }
}
