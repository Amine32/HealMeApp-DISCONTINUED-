package ru.tsu.hits.healmeapp.misc;

import java.io.Serializable;

public class AddressName implements Serializable {
    private String address_ID;
    private String user_ID;
    private String country;
    private String district;
    private String building;
    private String houseName;
    private String street;
    private String floor;

    public String getAddress_ID() {
        return address_ID;
    }

    public void setAddress_ID(String address_ID) {
        this.address_ID = address_ID;
    }

    public String getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    @Override
    public String toString() {
        return "AddressName{" +
                "address_ID='" + address_ID + '\'' +
                ", user_ID='" + user_ID + '\'' +
                ", country='" + country + '\'' +
                ", district='" + district + '\'' +
                ", building='" + building + '\'' +
                ", houseName='" + houseName + '\'' +
                ", street='" + street + '\'' +
                ", floor='" + floor + '\'' +
                '}';
    }
}

