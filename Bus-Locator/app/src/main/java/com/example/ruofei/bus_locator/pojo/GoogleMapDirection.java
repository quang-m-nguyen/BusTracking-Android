package com.example.ruofei.bus_locator.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ruofei on 5/29/2016.
 */
public class GoogleMapDirection {


    @Expose
    @SerializedName("routes")
    private List<Route> routes;

    public List<Route> getRoute() {
        return routes;
    }

    public void setRoute(List<Route> routes) {
        this.routes = routes;
    }
}
