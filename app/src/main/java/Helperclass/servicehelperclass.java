package Helperclass;

import java.util.ArrayList;
import java.util.List;

public class servicehelperclass {
    private List <String> servicename = new ArrayList<>();
    private List <String> serviceimage= new ArrayList<>();

    public servicehelperclass() {
    }

    public servicehelperclass(List<String> servicename, List<String> serviceimage) {
        this.servicename = servicename;
        this.serviceimage = serviceimage;
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
}
