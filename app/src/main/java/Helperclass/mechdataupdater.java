package Helperclass;

import java.util.Date;
import java.util.List;

public class mechdataupdater {
    String name,storename,place,dist,pin,licno,phone1,password,geohashcode,propic,status,date;
    double latitude, longitude;
    List<String> service;
    List<String> selectedbrand;



    public mechdataupdater() {
    }

    public mechdataupdater(String name, String storename, String place, String dist, String pin, String licno, String phone1, String password, String geohashcode, String propic, String status,
                           String date, double latitude, double longitude, List<String> service, List<String> selectedbrand) {
        this.name = name;
        this.storename = storename;
        this.place = place;
        this.dist = dist;
        this.pin = pin;
        this.licno = licno;
        this.phone1 = phone1;
        this.password = password;
        this.geohashcode = geohashcode;
        this.propic = propic;
        this.status = status;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.service = service;
        this.selectedbrand = selectedbrand;
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

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getService() {
        return service;
    }

    public void setService(List<String> service) {
        this.service = service;
    }

    public List<String> getSelectedbrand() {
        return selectedbrand;
    }

    public void setSelectedbrand(List<String> selectedbrand) {
        this.selectedbrand = selectedbrand;
    }

    public String getGeohashcode() {
        return geohashcode;
    }

    public void setGeohashcode(String geohashcode) {
        this.geohashcode = geohashcode;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPropic() {
        return propic;
    }

    public void setPropic(String propic) {
        this.propic = propic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
