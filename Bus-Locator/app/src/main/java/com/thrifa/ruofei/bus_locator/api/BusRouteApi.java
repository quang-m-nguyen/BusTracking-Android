package com.thrifa.ruofei.bus_locator.api;

import com.thrifa.ruofei.bus_locator.routes.Route;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by ruofeixu on 7/13/16.
 */
public interface BusRouteApi {

    @GET("GetRoute")
    Call<List<Route>> getBusRoute();
}
