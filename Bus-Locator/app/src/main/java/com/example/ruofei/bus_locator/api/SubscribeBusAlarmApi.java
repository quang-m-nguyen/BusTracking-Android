package com.example.ruofei.bus_locator.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by ruofeixu on 7/4/16.
 */
public interface SubscribeBusAlarmApi {
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
}
