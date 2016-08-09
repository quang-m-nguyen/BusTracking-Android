package com.thrifa.ruofei.bus_locator.api;

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
