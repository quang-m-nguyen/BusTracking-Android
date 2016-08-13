package com.thrifa.ruofei.bus_locator.util;

import android.content.Context;
import android.util.Log;

import com.thrifa.ruofei.bus_locator.api.ThrifaServerApi;
import com.thrifa.ruofei.bus_locator.pojo.BusInfo;
import com.thrifa.ruofei.bus_locator.pojo.BusStop;
import com.thrifa.ruofei.bus_locator.pojo.BusTracker;
import com.thrifa.ruofei.bus_locator.routes.Route;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by ruofeixu on 8/12/16.
 */
public class ThrifaServer extends Server {

//    static ThrifaServer instance;

    private ThrifaServer(Context context) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient = new OkHttpClient.Builder();
        // add your other interceptors …
        // add logging as last interceptor
        httpClient.addInterceptor(logging);
        serverUrl = Constants.FIRE_BASE_NOTIFICATION_URL;

        //default api and url
        // TODO: update error handling if no api and url setup
        buildRetrofit(serverUrl);
        mApi = ThrifaServerApi.class;
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


    public Call<Void> sendNotification(String token, int routeID, int busStopID) {
        ThrifaServerApi service = (ThrifaServerApi) this.getService();
        return service.sendToken(token, routeID, busStopID);
    }

    public Call<Void> unsubscribeBusstop(String busStopID, String token) {
        ThrifaServerApi service = (ThrifaServerApi) this.getService();
        return service.unsubscribeBusstop(busStopID, token);
    }

    public Call<List<BusTracker>> getBusTrakerCall(String busstopID, String token) {
        ThrifaServerApi service = (ThrifaServerApi) this.getService();
        return service.getBusTracker(busstopID, token, "Android");
    }

    public Call<Void> subscribeBusAlarm(String routeID, String busstopID, String token) {
        ThrifaServerApi service = (ThrifaServerApi) this.getService();
        return service.subscribeBusAlarm(routeID, busstopID, token, "Android");
    }

    public Call<Void> unsubscribeBusAlarm(String routeID, String busstopID, String token) {
        ThrifaServerApi service = (ThrifaServerApi) this.getService();
        return service.unsubscribeBusAlarm(routeID, busstopID, token);
    }

    public Call<List<Route>> getBusRoute() {
        ThrifaServerApi service = (ThrifaServerApi) this.getService();
        return service.getBusRoute();
    }

    public Call<Void> subscribeBus(String id, String token) {
        ThrifaServerApi service = (ThrifaServerApi) this.getService();
        return service.subscribeBus(id, token);
    }

    public Call<BusInfo> getBusInfo(String busID) {
        ThrifaServerApi service = (ThrifaServerApi) this.getService();
        return service.getBusLocation(busID);
    }

    public Call<List<Route>> getCityRouteInfo(String zipCode){
        ThrifaServerApi service = (ThrifaServerApi) this.getService();
        return service.getCityRouteInfo(zipCode);
    }


    //clear shared preference
    public void reset() {
        storage.edit().clear().apply();
        instance = null;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public Object getService() {
        Object service = retrofit.create(mApi);
        return service;
    }
}
