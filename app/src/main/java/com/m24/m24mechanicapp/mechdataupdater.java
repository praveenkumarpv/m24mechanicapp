package com.m24.m24mechanicapp;

import java.util.List;

public class mechdataupdater {
    String name,storename,place,dist,pin,licno,phone,email;
    List<String> service;
    public mechdataupdater(String name, String storename, String place, String dist, String pin, String licno, String phone, String email, List<String> service) {
        this.name = name;
        this.storename = storename;
        this.place = place;
        this.dist = dist;
        this.pin = pin;
        this.licno = licno;
        this.phone = phone;
        this.email = email;
        this.service = service;
    }

    public mechdataupdater() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getLicno() {
        return licno;
    }

    public void setLicno(String licno) {
        this.licno = licno;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getService() {
        return service;
    }

    public void setService(List<String> service) {
        this.service = service;
    }
}
