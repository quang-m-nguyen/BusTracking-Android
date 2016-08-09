package com.thrifa.ruofei.bus_locator.api;

import com.thrifa.ruofei.bus_locator.pojo.GoogleMapDirection;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ruofei on 5/29/2016.
 */
public interface GoogleMapApi {
//    @GET("json?origin={oriLat},{oriLng}&destination={destLat},{destLng}&sensor=false&mode=driving&alternatives=false&key=" + Constants.GOOGLE_MAP_API_KEY)
    @GET("json")
    Call<GoogleMapDirection> getRoutePath(@Query("origin") String oriLatLng,
                                          @Query("destination") String destLat,
                                          //@Query("waypoints")String waypoints,
                                          @Query("sensor")boolean sensor,
                                          @Query("mode") String mode,
                                          @Query("alternatives") boolean alternatives,
                                          @Query("key") String key
                                          );
}
