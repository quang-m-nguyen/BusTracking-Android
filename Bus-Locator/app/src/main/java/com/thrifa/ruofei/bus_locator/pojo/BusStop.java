package com.thrifa.ruofei.bus_locator.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ruofei on 5/24/2016.
 */
public class BusStop {

    @Expose
    @SerializedName("stopnum")
    private int stopNum;

    @Expose
    @SerializedName("lat")
    private double latitude;

    @Expose
    @SerializedName("long")
    private double longtitude;

    @Expose
    @SerializedName("stopname")
    private String stopName;

    public int getStopNum() { return stopNum;};

    public double getLatitude() {
        return latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopNum(int stopNum) {
        this.stopNum = stopNum;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }
}
