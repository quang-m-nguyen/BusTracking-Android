package com.thrifa.ruofei.bus_locator.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ruofeixu on 8/31/16.
 */
public class RoutePath {
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

    @Expose
    @SerializedName("pathcounter")
    private String pathNum;

    public int getStopNum() {
        return stopNum;
    }

    public void setStopNum(int stopNum) {
        this.stopNum = stopNum;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getStopName() {
        return stopName;
    }

    public String getPathNum() {
        return pathNum;
    }

    public void setPathNum(String pathNum) {
        this.pathNum = pathNum;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }
}
