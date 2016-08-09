package com.thrifa.ruofei.bus_locator.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ruofei on 5/29/2016.
 */
public class GoogleMapDirection {


    @Expose
    @SerializedName("routes")
    private List<RouteInfo> routeInfos;

    public List<RouteInfo> getRoute() {
        return routeInfos;
    }

    public void setRoute(List<RouteInfo> routeInfos) {
        this.routeInfos = routeInfos;
    }
}
