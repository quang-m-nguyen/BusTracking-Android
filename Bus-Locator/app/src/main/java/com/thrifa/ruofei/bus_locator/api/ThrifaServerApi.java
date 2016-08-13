package com.thrifa.ruofei.bus_locator.api;

import com.thrifa.ruofei.bus_locator.pojo.BusInfo;
import com.thrifa.ruofei.bus_locator.pojo.BusStop;
import com.thrifa.ruofei.bus_locator.pojo.BusTracker;
import com.thrifa.ruofei.bus_locator.routes.Route;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by ruofei on 5/24/2016.
 */
public interface ThrifaServerApi {
    @GET("GetBusStops/{name}")
    Call<List<BusStop>> getBusStop(@Path("name") String routeName);

    @GET("SubscribeBusCoordinate/{id}/{token}")
    Call<Void> subscribeBus(@Path("id") String busID,
                            @Path("token") String token);

    @GET("GetBusInfo/{id}")
    Call<BusInfo> getBusLocation(@Path("id") String busID);

    @GET("Simulation/UnsubscribeBusstop/{busstopID}/{token}")
    Call<Void> unsubscribeBusstop(
            @Path("busstopID") String busstopID,
            @Path("token") String token);

    @GET("Simulation/SubscribeBusAlarm/{routeID}/{busstopID}/{token}/{os}")
    Call<Void> subscribeBusAlarm(
            @Path("routeID") String routeID,
            @Path("busstopID") String busstopID,
            @Path("token") String token,
            @Path("os") String os
    );

    @GET("Simulation/unsubscribeBusAlarm/{routeID}/{busstopID}/{token}")
    Call<Void> unsubscribeBusAlarm(
            @Path("routeID") String routeID,
            @Path("busstopID") String busstopID,
            @Path("token") String token);

    @GET("Register/{token}/{route}/{busStop}")
    Call<Void> sendToken(@Path("token") String token,
                         @Path("route") int routeID,
                         @Path("busStop") int busStopID);

    @GET("Simulation/SubscribeBusstop/{busstopID}/{token}/{os}")
    Call<List<BusTracker>> getBusTracker(
            @Path("busstopID") String busstopID,
            @Path("token") String token,
            @Path("os") String os
    );

    @GET("GetRoute")
    Call<List<Route>> getBusRoute();

    @GET("GetInfo/{zipCode}")
    Call<List<Route>> getCityRouteInfo(
            @Path("zipCode") String zipCode
    );
}
