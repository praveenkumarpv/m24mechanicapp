package Helperclass;

import java.util.List;

public class verification {
    List<String> Licencenolist;
    String licenceno;
    String password;

    public verification() {
    }

    public verification(String licenceno, String password) {
        this.licenceno = licenceno;
        this.password = password;
    }

    public verification(List<String> licencenolist) {
        Licencenolist = licencenolist;
    }

    public List<String> getLicencenolist() {
        return Licencenolist;
    }

    public void setLicencenolist(List<String> licencenolist) {
        Licencenolist = licencenolist;
    }

    public String getLicenceno() {
        return licenceno;
    }

    public void setLicenceno(String licenceno) {
        this.licenceno = licenceno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
