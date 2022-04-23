package Helperclass;

import java.util.ArrayList;
import java.util.List;

public class servicehelperclass {
    private List <String> servicename = new ArrayList<>();
    private List <String> serviceimage= new ArrayList<>();
    private List <String> brandname= new ArrayList<>();

    public servicehelperclass() {
    }

    public servicehelperclass(List<String> servicename, List<String> serviceimage, List<String> brandname) {
        this.servicename = servicename;
        this.serviceimage = serviceimage;
        this.brandname = brandname;
    }

    public List<String> getServicename() {
        return servicename;
    }

    public void setServicename(List<String> servicename) {
        this.servicename = servicename;
    }

    public List<String> getServiceimage() {
        return serviceimage;
    }

    public void setServiceimage(List<String> serviceimage) {
        this.serviceimage = serviceimage;
    }

    public List<String> getBrandname() {
        return brandname;
    }

    public void setBrandname(List<String> brandname) {
        this.brandname = brandname;
    }
}
