package com.example.ruofei.bus_locator;

import android.util.Log;

/**
 * Created by ruofeixu on 6/15/16.
 */
public class TrackedBus {

    private String routeName, estimatedTime;
    private String busstopNum;

    public TrackedBus(){

    }

    public TrackedBus(String routeName, String estimatedTime, String busstopNum){
        this.routeName = routeName;
        this.estimatedTime = estimatedTime;
        this.busstopNum = busstopNum;
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

    @Override
    public boolean equals(Object o) {
        if(o == null)
            return false;
        TrackedBus other = (TrackedBus)o;
        if(this.routeName == null || other.routeName == null) return false;
        if(this.routeName.equals(other.routeName))
            return true;
        return false;
    }
}
