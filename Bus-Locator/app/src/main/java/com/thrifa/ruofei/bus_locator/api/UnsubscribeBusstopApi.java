package com.thrifa.ruofei.bus_locator.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by ruofeixu on 6/27/16.
 */
public interface UnsubscribeBusstopApi {
            @GET("Simulation/UnsubscribeBusstop/{busstopID}/{token}")
            Call<Void> unsubscribeBusstop(
                @Path("busstopID") String busstopID,
                @Path("token") String token);
}
