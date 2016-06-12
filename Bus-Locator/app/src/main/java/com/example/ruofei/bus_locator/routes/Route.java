package com.example.ruofei.bus_locator.routes;

/**
 * Created by ruofei on 6/11/2016.
 */
public class Route {
    private String routeNum, routeName;

    public Route(){

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
}
