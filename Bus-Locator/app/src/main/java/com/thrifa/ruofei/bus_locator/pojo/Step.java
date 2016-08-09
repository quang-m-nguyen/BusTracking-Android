package com.thrifa.ruofei.bus_locator.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ruofei on 5/30/2016.
 */
public class Step {
    @Expose
    @SerializedName("polyline")
    private PolylinePOJO polyline;

    public PolylinePOJO getPolyline() {
        return polyline;
    }

    public void setPolyline(PolylinePOJO polyline) {
        this.polyline = polyline;
    }
}
