package com.m24.m24mechanicapp;


public class serviceadapter{
    String service;
    String iconsurl;


    public serviceadapter(String service, String iconsurl) {
        this.service = service;
        this.iconsurl = iconsurl;
    }

    public serviceadapter() {
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getIconsurl() {
        return iconsurl;
    }

    public void setIconsurl(String iconsurl) {
        this.iconsurl = iconsurl;
    }


}
