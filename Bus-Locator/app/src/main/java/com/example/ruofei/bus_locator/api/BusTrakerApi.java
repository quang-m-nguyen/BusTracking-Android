package com.example.ruofei.bus_locator.api;

import com.example.ruofei.bus_locator.pojo.BusTracker;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by ruofeixu on 6/21/16.
 */
public interface BusTrakerApi {
        @GET("Simulation/Busstop/{busstopID}/{token}")
        Call<List<BusTracker>> getBusTracker(
                @Path("busstopID") String busstopID,
                @Path("token") String token);
}
