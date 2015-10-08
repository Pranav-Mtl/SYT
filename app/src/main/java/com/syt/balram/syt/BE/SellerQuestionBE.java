package com.syt.balram.syt.BE;

import java.io.Serializable;

/**
 * Created by pranav on 4/16/2015.
 */
public class SellerQuestionBE implements Serializable {

    private String password;


    private String firstName;
    private String lastName;
    private String gender;

    private String dob;
    private String servicesWhom;
    private String category;
    private String subCategory;
    private String experience;
    private String description;
    private String phone;
    private String address;
    private String zip;
    private String country;
    private String servicesWhere;

    private String email;
    private String distance;
    private String serviceCharge;
    private String minPrice;
    private String maxPrice;
    private String servicesWhen;
    private String serviceNotified;
    private String imageURL;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getServicesWhen() {
        return servicesWhen;
    }

    public void setServicesWhen(String servicesWhen) {
        this.servicesWhen = servicesWhen;
    }

    public String getServiceNotified() {
        return serviceNotified;
    }

    public void setServiceNotified(String serviceNotified) {
        this.serviceNotified = serviceNotified;
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

    public String getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getServicesWhere() {
        return servicesWhere;
    }

    public void setServicesWhere(String servicesWhere) {
        this.servicesWhere = servicesWhere;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getServicesWhom() {
        return servicesWhom;
    }

    public void setServicesWhom(String servicesWhom) {
        this.servicesWhom = servicesWhom;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }



}
