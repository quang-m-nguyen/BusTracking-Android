package com.thrifa.ruofei.bus_locator.routes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ruofei on 6/11/2016.
 */
public class Route {
    @Expose
    @SerializedName("RouteId")
    private String routeID;

    @Expose
    @SerializedName("RouteName")
    private String routeName;

    @Expose
    @SerializedName("RouteNum")
    private String routeNum;

    @Expose
    @SerializedName("OpDays")
    private String opDays;

    @Expose
    @SerializedName("OpHours")
    private String opHours;

    @Expose
    @SerializedName("Color")
    private String color;


    public Route() {

    }

    public Route(String routeName) {
        this.routeName = routeName;
    }

    public Route(String routeNum, String routeName, String routeID) {
        this.routeNum = routeNum;
        this.routeName = routeName;
        this.routeID = routeID;
    }

    public Route(String routeNum, String routeName, String routeID, String opDays, String opHours, String color) {
        this.routeNum = routeNum;
        this.routeName = routeName;
        this.routeID = routeID;
        this.opDays = opDays;
        this.opHours = opHours;
        this.color = color;
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

    public String getOpDays() {
        return opDays;
    }

    public void setOpDays(String opDays) {
        this.opDays = opDays;
    }

    public String getOpHours() {
        return opHours;
    }

    public void setOpHours(String opHours) {
        this.opHours = opHours;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
