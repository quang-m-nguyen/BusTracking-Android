package com.thrifa.ruofei.bus_locator.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ruofeixu on 6/21/16.
 */
public class BusTracker {

    @Expose
    @SerializedName("RouteID")
    private String routeID;

    @Expose
    @SerializedName("RouteName")
    private String routeName;

    @Expose
    @SerializedName("StopNum")
    private String stopNum;

    @Expose
    @SerializedName("BusstopID")
    private String stopID;


    @Expose
    @SerializedName("Time")
    private String time;

    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getStopNum() {
        return stopNum;
    }

    public void setStopNum(String stopNum) {
        this.stopNum = stopNum;
    }

    public String getStopID() {
        return stopID;
    }

    public void setStopID(String stopID) {
        this.stopID = stopID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
