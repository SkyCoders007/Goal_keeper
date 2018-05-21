package com.mxi.goalkeeper.model;

/**
 * Created by sonali on 25/3/17.
 */
public class arena {
    String arena_id;
    String title;
    String address1;
    String address2;
    String city;
    String state_name;
    String country_name;
    String address_lat;

    public String getAddress_long() {
        return address_long;
    }

    public void setAddress_long(String address_long) {
        this.address_long = address_long;
    }

    public String getAddress_lat() {
        return address_lat;
    }

    public void setAddress_lat(String address_lat) {
        this.address_lat = address_lat;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArena_id() {
        return arena_id;
    }

    public void setArena_id(String arena_id) {
        this.arena_id = arena_id;
    }

    String address_long;
}
