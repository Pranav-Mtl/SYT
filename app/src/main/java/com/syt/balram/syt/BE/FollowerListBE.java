package com.syt.balram.syt.BE;

/**
 * Created by Balram on 5/26/2015.
 */
public class FollowerListBE {


    private String name;
    private boolean selected;

    public FollowerListBE(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
