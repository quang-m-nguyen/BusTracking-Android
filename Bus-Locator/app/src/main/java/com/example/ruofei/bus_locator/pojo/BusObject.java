package com.example.ruofei.bus_locator.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ruofeixu on 7/24/16.
 */
public class BusObject {

    @Expose
    @SerializedName("busID")
    private int stopNum;

    @Expose
    @SerializedName("lat")
    private double latitude;

    @Expose
    @SerializedName("lng")
    private double longtitude;

    @Expose
    @SerializedName("routeName")
    private String stopName;
}
