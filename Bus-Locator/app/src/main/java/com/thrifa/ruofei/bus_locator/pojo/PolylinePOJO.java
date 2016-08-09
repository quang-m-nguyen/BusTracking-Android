package com.thrifa.ruofei.bus_locator.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ruofei on 5/30/2016.
 */
public class PolylinePOJO {
    @Expose
    @SerializedName("points")
    private String points;

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
