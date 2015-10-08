package com.syt.balram.syt.BE;

/**
 * Created by Balram on 4/10/2015.
 */

public class ProfileBE {

    private String firstName;
    private String lastName;
    private String category;
    private String subCategory;
    private String picture;
    private String id;
    private String appURL;

    public String getAppURL() {
        return appURL;
    }

    public void setAppURL(String appURL) {
        this.appURL = appURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
