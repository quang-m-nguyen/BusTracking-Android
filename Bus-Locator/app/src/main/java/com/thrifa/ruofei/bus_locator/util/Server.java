package com.thrifa.ruofei.bus_locator.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.thrifa.ruofei.bus_locator.api.ThrifaServerApi;
import com.thrifa.ruofei.bus_locator.api.GoogleMapApi;
import com.thrifa.ruofei.bus_locator.pojo.BusInfo;
import com.thrifa.ruofei.bus_locator.pojo.BusStop;
import com.thrifa.ruofei.bus_locator.pojo.BusTracker;
import com.thrifa.ruofei.bus_locator.pojo.GoogleMapDirection;
import com.thrifa.ruofei.bus_locator.routes.Route;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ruofei on 5/26/2016.
 */
public class Server {
    final String TAG = this.getClass().getName();
    static volatile Server instance;
    Context context;
    String serverUrl;
    Retrofit retrofit;
    SharedPreferences storage;
    OkHttpClient.Builder httpClient;

    Class<?> mApi;

    public enum Status {
        OK,
        NO_CONNECTION,
    }

    public Server(){

    }

    public Server(Context context){
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient = new OkHttpClient.Builder();
        // add your other interceptors …
        // add logging as last interceptor
        httpClient.addInterceptor(logging);

        serverUrl = Constants.GOOGLE_MAP_URL;

        buildRetrofit(serverUrl);
        mApi = GoogleMapApi.class;    }


    public static synchronized Server getInstance(final Context context) {
        if (instance == null) {
            instance = new Server(context);
        }
        return instance;
    }

    public synchronized void buildRetrofit(String url)
    {
        serverUrl = url;
        retrofit = new Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }

    public synchronized void setApi(Class<?> api )
    {
        mApi = api;
    }


//    public Call<List<BusStop>> getBusStopsCall(String routeNameView)
//    {
//        this.buildRetrofit(Constants.BUS_LOCATOR_URL);
//        this.setApi(ThrifaServerApi.class);
//        ThrifaServerApi service = (ThrifaServerApi) this.getService();
//        return service.getBusStop(routeNameView);
//    }
    public Call<GoogleMapDirection> getRouteCall(String oriLatLng, String destLatLng)
    {
        this.buildRetrofit(Constants.GOOGLE_MAP_URL);
        this.setApi(GoogleMapApi.class);
        GoogleMapApi service = (GoogleMapApi) this.getService();
        return service.getRoutePath(oriLatLng, destLatLng, false, "driving", false, Constants.GOOGLE_MAP_API_KEY);
    }
//
//    public Call<Void> sendNotification(String token, int routeID, int busStopID)
//    {
//        this.buildRetrofit(Constants.FIRE_BASE_NOTIFICATION_URL);
//        this.setApi(ThrifaServerApi.class);
//        ThrifaServerApi service = (ThrifaServerApi)this.getService();
//        return service.sendToken(token,routeID,busStopID);
//    }
//
//    public Call<Void> unsubscribeBusstop(String busStopID, String token)
//    {
//        this.buildRetrofit(Constants.FIRE_BASE_NOTIFICATION_URL);
//        this.setApi(ThrifaServerApi.class);
//        ThrifaServerApi service = (ThrifaServerApi)this.getService();
//        return service.unsubscribeBusstop(busStopID,token);
//    }
//
//    public Call<List<BusTracker>> getBusTrakerCall(String busstopID, String token){
//        this.buildRetrofit(Constants.FIRE_BASE_NOTIFICATION_URL);
//        this.setApi(ThrifaServerApi.class);
//        ThrifaServerApi service = (ThrifaServerApi)this.getService();
//        return service.getBusTracker(busstopID,token, "Android");
//    }
//
//    public Call<Void> subscribeBusAlarm(String routeID, String busstopID, String token){
//        this.buildRetrofit(Constants.FIRE_BASE_NOTIFICATION_URL);
//        this.setApi(ThrifaServerApi.class);
//        ThrifaServerApi service = (ThrifaServerApi)this.getService();
//        return service.subscribeBusAlarm(routeID,busstopID,token, "Android");
//    }
//
//    public Call<Void> unsubscribeBusAlarm(String routeID, String busstopID, String token){
//        this.buildRetrofit(Constants.FIRE_BASE_NOTIFICATION_URL);
//        this.setApi(ThrifaServerApi.class);
//        ThrifaServerApi service = (ThrifaServerApi)this.getService();
//        return service.unsubscribeBusAlarm(routeID,busstopID,token);
//    }
//
//    public Call<List<Route>> getBusRoute(){
//        this.buildRetrofit(Constants.FIRE_BASE_NOTIFICATION_URL);
//        this.setApi(ThrifaServerApi.class);
//        ThrifaServerApi service = (ThrifaServerApi) this.getService();
//        return service.getBusRoute();
//    }
//
//    public Call<Void> subscribeBus(String id, String token){
//        this.buildRetrofit((Constants.FIRE_BASE_NOTIFICATION_URL));
//        this.setApi((ThrifaServerApi.class));
//        ThrifaServerApi service = (ThrifaServerApi)this.getService();
//        return service.subscribeBus(id,token);
//    }
//
//    public Call<BusInfo> getBusInfo(String busID){
//        this.buildRetrofit((Constants.FIRE_BASE_NOTIFICATION_URL));
//        this.setApi((ThrifaServerApi.class));
//        ThrifaServerApi service = (ThrifaServerApi)this.getService();
//        Log.e(TAG,"busID:" + busID);
//        return service.getBusLocation(busID);
//    }

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
