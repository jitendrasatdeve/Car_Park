package com.example.android.hello;

public class AddSlot {

    private String s_location,noOfSlots,pricePerSlot,userId;

    public AddSlot(String s_location, String noOfSlots, String pricePerSlot, String userId) {
        this.s_location = s_location;
        this.noOfSlots = noOfSlots;
        this.pricePerSlot = pricePerSlot;
        this.userId = userId;
    }

    public AddSlot() {
    }

    public String getS_location() {
        return s_location;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setS_location(String s_location) {
        this.s_location = s_location;
    }

    public String getNoOfSlots() {
        return noOfSlots;
    }

    public void setNoOfSlots(String noOfSlots) {
        this.noOfSlots = noOfSlots;
    }

    public String getPricePerSlot() {
        return pricePerSlot;
    }

    public void setPricePerSlot(String pricePerSlot) {
        this.pricePerSlot = pricePerSlot;
    }
}
