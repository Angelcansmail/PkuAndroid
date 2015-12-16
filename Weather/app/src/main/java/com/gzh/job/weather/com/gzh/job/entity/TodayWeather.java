package com.gzh.job.weather.com.gzh.job.entity;

import java.io.Serializable;

/**
 * Created by Angel on 2015/10/7.
 */
public class TodayWeather implements Serializable{
    private String city;
    private String updatetime;
    private String shidu;
    private String pm25;
    private int pm25Val;
    private String quality;
    private String wendu;
    private String fengli;
    private String fengxiang;
    private String weather_pic;
    private String date;
    private String low;
    private String high;
    private String type;
    private String wind;
    private String suggest;
    private String MajorPollutants;

    private String fdate0;
    private String fhigh0;
    private String flow0;
    private String ftype0;
    private String ffengxiang0;

    @Override
    public String toString() {
        return "TodayWeather{" +
                "city='" + city + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", shidu='" + shidu + '\'' +
                ", pm25='" + pm25 + '\'' +
                ", pm25Val=" + pm25Val +
                ", quality='" + quality + '\'' +
                ", wendu='" + wendu + '\'' +
                ", fengli='" + fengli + '\'' +
                ", fengxiang='" + fengxiang + '\'' +
                ", weather_pic='" + weather_pic + '\'' +
                ", date='" + date + '\'' +
                ", low='" + low + '\'' +
                ", high='" + high + '\'' +
                ", type='" + type + '\'' +
                ", wind='" + wind + '\'' +
                ", suggest='" + suggest + '\'' +
                ", MajorPollutants='" + MajorPollutants + '\'' +
                '}';
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getShidu() {
        return shidu;
    }

    public void setShidu(String shidu) {
        this.shidu = shidu;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public String getPm25() {
        return pm25;
    }

    public int getPm25Img() {
        return pm25Val;
    }

    public void setPm25Img(int pm25Val) {
        if(pm25Val < 50)
            this.pm25Val = 0;
        else if (pm25Val < 100)
            this.pm25Val = 1;
        else if (pm25Val < 150)
            this.pm25Val = 2;
        else if (pm25Val < 200)
            this.pm25Val = 3;
        else if (pm25Val < 300)
            this.pm25Val = 4;
        else
            this.pm25Val = 5;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getWendu() {
        return wendu;
    }

    public void setWendu(String wendu) {
        this.wendu = wendu;
    }

    public String getFengli() {
        return fengli;
    }

    public void setFengli(String fengli) {
        this.fengli = fengli;
    }

    public String getFengxiang() {
        return fengxiang;
    }

    public void setFengxiang(String fengxiang) {
        this.fengxiang = fengxiang;
    }

    public String getWeather_pic() {
        return weather_pic;
    }

    public void setWeather_pic(String weather_pic) {
        this.weather_pic = weather_pic;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public String getMajorPollutants() {
        return MajorPollutants;
    }

    public void setMajorPollutants(String majorPollutants) {
        MajorPollutants = majorPollutants;
    }

    public int getPm25Val() {
        return pm25Val;
    }

    public void setPm25Val(int pm25Val) {
        this.pm25Val = pm25Val;
    }

    public String getFdate0() {
        return fdate0;
    }

    public void setFdate0(String fdate0) {
        this.fdate0 = fdate0;
    }

    public String getFhigh0() {
        return fhigh0;
    }

    public void setFhigh0(String fhigh0) {
        this.fhigh0 = fhigh0;
    }

    public String getFlow0() {
        return flow0;
    }

    public void setFlow0(String flow0) {
        this.flow0 = flow0;
    }

    public String getFtype0() {
        return ftype0;
    }

    public void setFtype0(String ftype0) {
        this.ftype0 = ftype0;
    }

    public String getFfengxiang0() {
        return ffengxiang0;
    }

    public void setFfengxiang0(String ffengxiang0) {
        this.ffengxiang0 = ffengxiang0;
    }
}
