package com.thrifa.ruofei.bus_locator.api;

import com.thrifa.ruofei.bus_locator.BusTracker.TrackedBus;
import com.thrifa.ruofei.bus_locator.pojo.BusInfo;
import com.thrifa.ruofei.bus_locator.pojo.BusStop;
import com.thrifa.ruofei.bus_locator.pojo.BusTracker;
import com.thrifa.ruofei.bus_locator.pojo.RoutePath;
import com.thrifa.ruofei.bus_locator.routes.Route;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by ruofei on 5/24/2016.
 */
public interface ThrifaServerApi {
    @GET("GetbusstopInfo/{name}")
    Call<List<BusStop>> getBusStop(@Path("name") String routeID);


    @GET("GetBusPosition/{id}")
    Call<List<BusInfo>> getBusLocation(@Path("id") String busID);

    @GET("Simulation/UnsubscribeBusstop/{busstopID}/{token}")
    Call<Void> unsubscribeBusstop(
            @Path("busstopID") String busstopID,
            @Path("token") String token);


    @GET("Simulation/unsubscribeBusAlarm/{routeID}/{busstopID}/{token}")
    Call<Void> unsubscribeBusAlarm(
            @Path("routeID") String routeID,
            @Path("busstopID") String busstopID,
            @Path("token") String token);


    @GET("Simulation/SubscribeBusstop/{busstopID}/{token}/{os}")
    Call<List<BusTracker>> getBusTracker(
            @Path("busstopID") String busstopID,
            @Path("token") String token,
            @Path("os") String os
    );


    @GET("GetInfo/{zipCode}")
    Call<List<Route>> getCityRouteInfo(
            @Path("zipCode") String zipCode
    );


    @GET("GetRoutePath/{routeID}")
    Call<List<RoutePath>> getRoutePath(
            @Path("routeID") String routeID
    );

    @GET("GetRemainingTimeForARoute/{routeID}/{stopID}")
    Call<BusTracker> getTimeInfoForARoute(
            @Path("routeID") String routeID,
            @Path("stopID") String stopID
    );
    @GET("Simulation/SubscribeBusstop/{busstopID}/{token}")
    Call<List<BusTracker>> getBusstopTimeInfo2(
            @Path("busstopID") String busstopID,
            @Path("token") String token
    );
}
