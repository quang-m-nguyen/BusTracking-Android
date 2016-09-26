package com.thrifa.ruofei.bus_locator.util;

import android.content.Context;
import android.util.Log;

import com.thrifa.ruofei.bus_locator.BusTracker.TrackedBus;
import com.thrifa.ruofei.bus_locator.api.ThrifaServerApi;
import com.thrifa.ruofei.bus_locator.pojo.BusInfo;
import com.thrifa.ruofei.bus_locator.pojo.BusStop;
import com.thrifa.ruofei.bus_locator.pojo.BusTracker;
import com.thrifa.ruofei.bus_locator.pojo.RoutePath;
import com.thrifa.ruofei.bus_locator.routes.Route;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ruofeixu on 8/12/16.
 */
public class ThrifaServer {

    //    static ThrifaServer instance;
    final String TAG = this.getClass().getName();
    static volatile ThrifaServer instance;
    Context context;
    String serverUrl;
    Retrofit retrofit;
    OkHttpClient.Builder httpClient;

    Class<?> mThrifaApi;

    private ThrifaServer(Context context) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient = new OkHttpClient.Builder();
        // add your other interceptors …
        // add logging as last interceptor
        httpClient.addInterceptor(logging);
        serverUrl = Parameters.CURRENT_SERVER_PRODUCT_DOMAIN;

        //default api and url
        // TODO: update error handling if no api and url setup
        buildRetrofit(serverUrl);
        mThrifaApi = ThrifaServerApi.class;
    }

    public synchronized void buildRetrofit(String url) {
        serverUrl = url;
        retrofit = new Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }


    public static synchronized ThrifaServer getInstance(final Context context) {
        if (instance == null) {
            instance = new ThrifaServer(context);
        }
        return (ThrifaServer) instance;
    }

    public Call<List<BusStop>> getBusStopsCall(String routeName) {
        ThrifaServerApi service = (ThrifaServerApi) this.getService();
        return service.getBusStop(routeName);
    }

    public Call<Void> unsubscribeBusstop(String busStopID, String token) {
        ThrifaServerApi service = (ThrifaServerApi) this.getService();
        return service.unsubscribeBusstop(busStopID, token);
    }

    public Call<List<BusTracker>> getBusTrakerCall(String busstopID, String token) {
        ThrifaServerApi service = (ThrifaServerApi) this.getService();
        return service.getBusTracker(busstopID, token, "Android");
    }


    public Call<Void> unsubscribeBusAlarm(String routeID, String busstopID, String token) {
        ThrifaServerApi service = (ThrifaServerApi) this.getService();
        return service.unsubscribeBusAlarm(routeID, busstopID, token);
    }



    public Call<List<BusInfo>> getBusInfo(String busID) {
        ThrifaServerApi service = (ThrifaServerApi) this.getService();
        return service.getBusLocation(busID);
    }

    public Call<List<Route>> getCityRouteInfo(String zipCode) {
        ThrifaServerApi service = (ThrifaServerApi) this.getService();
        return service.getCityRouteInfo(zipCode);
    }

    public Call<List<BusTracker>> getBusstopInfo(String stopID) {
        ThrifaServerApi service = (ThrifaServerApi) this.getService();
        return service.getBusstopTimeInfo2(stopID,"N/A");
    }

    public Call<List<RoutePath>> getRoutePath(String routeID) {
        ThrifaServerApi service = (ThrifaServerApi) this.getService();
        return service.getRoutePath(routeID);
    }

    public Call<BusTracker> getTimeInfoForARoute(String routeID, String stopID){
        ThrifaServerApi service = (ThrifaServerApi) this.getService();
        return service.getTimeInfoForARoute(routeID,stopID);
    }


    public Object getService() {
        Object service = retrofit.create(mThrifaApi);
        return service;
    }
}
