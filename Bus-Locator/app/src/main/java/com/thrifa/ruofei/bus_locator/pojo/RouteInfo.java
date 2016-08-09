package com.thrifa.ruofei.bus_locator.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ruofei on 5/29/2016.
 */
public class RouteInfo {

    @Expose
    @SerializedName("legs")
    private List<Leg> leg;
    public List<Leg> getLeg() {
        return leg;
    }

    public void setLeg(List<Leg> leg) {
        this.leg = leg;
    }

    @Expose
    @SerializedName("overview_polyline")
    private PolylinePOJO polyline;

    public PolylinePOJO getPolyline() {
        return polyline;
    }

    public void setPolyline(PolylinePOJO polyline) {
        this.polyline = polyline;
    }
}
