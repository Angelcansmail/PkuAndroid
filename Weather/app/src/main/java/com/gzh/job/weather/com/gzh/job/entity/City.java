package com.gzh.job.weather.com.gzh.job.entity;

/**
 * Created by Angel on 2015/10/17.
 */
public class City {
    private String province;
    private String city;
    private String number;
    private String allPY;
    private String allfirstpy;
    private String firstpy;

    public City(String province,String city,String number,String apply,String allfirstpy,String firstpy){
        this.province = province;this.city = city;this.number = number;this.allPY = allPY;this.allfirstpy = allfirstpy;this.firstpy = firstpy;
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

    public String getAllPY() {
        return allPY;
    }

    public void setAllPY(String allPY) {
        this.allPY = allPY;
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
