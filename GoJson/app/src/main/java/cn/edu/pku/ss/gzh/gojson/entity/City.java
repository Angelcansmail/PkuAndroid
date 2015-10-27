package cn.edu.pku.ss.gzh.gojson.entity;

/**
 * Created by Angel on 2015/10/17.
 */
public class City {
    private String province;
    private String city;
    private String number;
    private String apply;
    private String allfirstpy;
    private String firstpy;

    public void City(String province,String city,String number,String apply,String allfirstpy,String firstpy){
        this.province = province;this.city = city;this.number = number;this.apply = apply;this.allfirstpy = allfirstpy;this.firstpy = firstpy;
    }
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getApply() {
        return apply;
    }

    public void setApply(String apply) {
        this.apply = apply;
    }

    public String getAllfirstpy() {
        return allfirstpy;
    }

    public void setAllfirstpy(String allfirstpy) {
        this.allfirstpy = allfirstpy;
    }

    public String getFirstpy() {
        return firstpy;
    }

    public void setFirstpy(String firstpy) {
        this.firstpy = firstpy;
    }
}
