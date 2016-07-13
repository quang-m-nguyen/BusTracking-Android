package com.example.ruofei.bus_locator.routes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ruofei on 6/11/2016.
 */
public class Route {
    @Expose
    @SerializedName("RouteID")
    private String routeID;

    @Expose
    @SerializedName("RouteName")
    private String routeName;

    @Expose
    @SerializedName("RouteNum")
    private String routeNum;


//    private String routeNum, routeName;

    public Route(){

    }

    public Route(String routeName){
       this.routeName = routeName;
    }

    public Route(String routeNum, String routeName)
    {
        this.routeNum = routeNum;
        this.routeName = routeName;
    }

    public String getRouteNum() {
        return routeNum;
    }

    public void setRouteNum(String routeNum) {
        this.routeNum = routeNum;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }
}
