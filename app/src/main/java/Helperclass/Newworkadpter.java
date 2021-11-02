package Helperclass;

public class Newworkadpter {
    String name,location,phonenumber,worktype,fixeddate,status,profilephoto,intogarage;

    public Newworkadpter(String name, String location, String phonenumber, String worktype, String fixeddate,
                         String status, String profilephoto,String intogarage) {
        this.name = name;
        this.location = location;
        this.phonenumber = phonenumber;
        this.worktype = worktype;
        this.fixeddate = fixeddate;
        this.status = status;
        this.profilephoto = profilephoto;
        this.intogarage = intogarage;
    }

    public Newworkadpter() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getWorktype() {
        return worktype;
    }

    public void setWorktype(String worktype) {
        this.worktype = worktype;
    }

    public String getFixeddate() {
        return fixeddate;
    }

    public void setFixeddate(String fixeddate) {
        this.fixeddate = fixeddate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfilephoto() {
        return profilephoto;
    }

    public void setProfilephoto(String profilephoto) {
        this.profilephoto = profilephoto;
    }

    public String getIntogarage() {
        return intogarage;
    }

    public void setIntogarage(String intogarage) {
        this.intogarage = intogarage;
    }
}
