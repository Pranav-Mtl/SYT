package com.syt.balram.syt.BE;

import java.util.List;

/**
 * Created by Balram on 4/24/2015.
 */
public class SellerFragmentBE {

    private String name[];
    private String category[];
    private String zip[];
    private String id[];

    private List arraylist;

    public String[] getName() {
        return name;
    }

    public void setName(String name[]) {
        this.name = name;
    }

    public String[] getCategory() {
        return category;
    }

    public void setCategory(String[] category) {
        this.category = category;
    }

    public String[] getZip() {
        return zip;
    }

    public void setZip(String[] zip) {
        this.zip = zip;
    }

    public String[] getId() {
        return id;
    }

    public void setId(String[] id) {
        this.id = id;
    }
}
