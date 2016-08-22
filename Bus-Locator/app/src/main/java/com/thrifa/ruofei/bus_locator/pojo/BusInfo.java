package com.thrifa.ruofei.bus_locator.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ruofeixu on 7/24/16.
 */
public class BusInfo {

    @Expose
    @SerializedName("busID")
    private String busID;

    @Expose
    @SerializedName("lat")
    private double lat;

    @Expose
    @SerializedName("lng")
    private double lng;

    @Expose
    @SerializedName("routeNameView")
    private String routeName;

    public String getBusID() {
        return busID;
    }

    public void setBusID(String busID) {
        this.busID = busID;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }
}
