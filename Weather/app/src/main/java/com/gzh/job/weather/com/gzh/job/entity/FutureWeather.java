package com.gzh.job.weather.com.gzh.job.entity;

import java.io.Serializable;

/**
 * Created by Angel on 2015/10/13.
 */
public class FutureWeather implements Serializable{
    private String date;
    private String type;
    private String weather_pic;
    private String low;
    private String high;
    private String wendu;
    private String fengli;
    private String fengxiang;
    private String wind;

    @Override
    public String toString() {
        return "FutureWeather{" +
                "date='" + date + '\'' +
                ", type='" + type + '\'' +
                ", weather_pic='" + weather_pic + '\'' +
                ", low='" + low + '\'' +
                ", high='" + high + '\'' +
                ", wendu='" + wendu + '\'' +
                ", fengli='" + fengli + '\'' +
                ", fengxiang='" + fengxiang + '\'' +
                ", wind='" + wind + '\'' +
                '}';
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWeather_pic() {
        return weather_pic;
    }

    public void setWeather_pic(String weather_pic) {
        this.weather_pic = weather_pic;
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

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }
}
