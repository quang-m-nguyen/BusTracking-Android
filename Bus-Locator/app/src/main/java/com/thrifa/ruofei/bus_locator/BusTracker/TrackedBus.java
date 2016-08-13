package com.thrifa.ruofei.bus_locator.BusTracker;

/**
 * Created by ruofeixu on 6/15/16.
 */
public class TrackedBus {

    private String routeName, routeID, estimatedTime;
    private String busstopNum, stopID;

    public TrackedBus(){

    }

    public TrackedBus(String routeID, String routeName, String estimatedTime, String busstopNum, String stopID){
        this.routeName = routeName;
        this.estimatedTime = estimatedTime;
        this.busstopNum = busstopNum;
        this.routeID = routeID;
        this.stopID = stopID;
    }



    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getBusstopNum() {
        return busstopNum;
    }

    public void setBusstopNum(String busstopNum) {
        this.busstopNum = busstopNum;
    }

    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    public String getStopID() {
        return stopID;
    }

    public void setStopID(String stopID) {
        this.stopID = stopID;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null)
            return false;
        TrackedBus other = (TrackedBus)o;
        if(this.routeID == null || other.routeID == null || this.stopID == null || other.stopID == null) return false;
        if(this.routeID.equals(other.routeID) && this.stopID.equals(other.stopID))
            return true;
        return false;
    }
}
