package com.example.android.hello;

public class Booking {
    private String personName,location,carNo;

    public Booking(String personName, String location, String carNo) {
        this.personName = personName;
        this.location = location;
        this.carNo = carNo;
    }

    public Booking() {
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }
}
