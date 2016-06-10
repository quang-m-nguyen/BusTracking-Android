package com.example.ruofei.bus_locator.api;

import com.example.ruofei.bus_locator.pojo.BusStop;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by ruofeixu on 6/6/16.
 */
public interface FirebaseNotificationApi {
    @GET("Register/{token}/{route}/{busStop}")
    Call<Void> sendToken(@Path("token") String token,
    @Path("route") int routeID,
    @Path("busStop") int busStopID);
}
