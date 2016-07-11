package com.example.ruofei.bus_locator.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.ruofei.bus_locator.api.BusLocatorApi;
import com.example.ruofei.bus_locator.api.BusTrakerApi;
import com.example.ruofei.bus_locator.api.FirebaseNotificationApi;
import com.example.ruofei.bus_locator.api.GoogleMapApi;
import com.example.ruofei.bus_locator.api.SubscribeBusAlarmApi;
import com.example.ruofei.bus_locator.api.UnsubscribeBusstopApi;
import com.example.ruofei.bus_locator.pojo.BusStop;
import com.example.ruofei.bus_locator.pojo.BusTracker;
import com.example.ruofei.bus_locator.pojo.GoogleMapDirection;

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
    static final String TAG = "SERVER";
    static volatile Server instance;

    Context context;
    String serverUrl = "http://52.33.19.46/";

    Retrofit retrofit;
    SharedPreferences storage;

    OkHttpClient.Builder httpClient;

//    Class mApi;
    Class<?> mApi;

    public enum Status {
        OK,
        NO_CONNECTION,
    }

    private Server(Context context){
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient = new OkHttpClient.Builder();
        // add your other interceptors …
        // add logging as last interceptor
        httpClient.addInterceptor(logging);

        //default api and url
        // TODO: update error handling if no api and url setup
        buildRetrofit(serverUrl);
        mApi = BusLocatorApi.class;
    }


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

    public Call<List<BusStop>> getBusStopsCall(String routeName)
    {
        this.buildRetrofit(Constants.BUS_LOCATOR_URL);
        this.setApi(BusLocatorApi.class);
        BusLocatorApi service = (BusLocatorApi) this.getService();
        return service.getBusStop(routeName);
    }
    public Call<GoogleMapDirection> getRouteCall(String oriLatLng, String destLatLng)
    {
        this.buildRetrofit(Constants.GOOGLE_MAP_URL);
        this.setApi(GoogleMapApi.class);
        GoogleMapApi service = (GoogleMapApi) this.getService();
        return service.getRoutePath(oriLatLng, destLatLng, false, "driving", false, Constants.GOOGLE_MAP_API_KEY);
    }

    public Call<Void> sendNotification(String token, int routeID, int busStopID)
    {
        this.buildRetrofit(Constants.FIRE_BASE_NOTIFICATION_URL);
        this.setApi(FirebaseNotificationApi.class);
        FirebaseNotificationApi service = (FirebaseNotificationApi)this.getService();
        return service.sendToken(token,routeID,busStopID);
    }

    public Call<Void> unsubscribeBusstop(String busStopID, String token)
    {
        this.buildRetrofit(Constants.FIRE_BASE_NOTIFICATION_URL);
        this.setApi(UnsubscribeBusstopApi.class);
        UnsubscribeBusstopApi service = (UnsubscribeBusstopApi)this.getService();
        return service.unsubscribeBusstop(busStopID,token);
    }

    public Call<List<BusTracker>> getBusTrakerCall(String busstopID, String token){
        this.buildRetrofit(Constants.FIRE_BASE_NOTIFICATION_URL);
        this.setApi(BusTrakerApi.class);
        BusTrakerApi service = (BusTrakerApi)this.getService();
        return service.getBusTracker(busstopID,token);
    }

    public Call<Void> subscribeBusAlarm(String routeID, String busstopID, String token){
        this.buildRetrofit(Constants.FIRE_BASE_NOTIFICATION_URL);
        this.setApi(SubscribeBusAlarmApi.class);
        SubscribeBusAlarmApi service = (SubscribeBusAlarmApi)this.getService();
        return service.subscribeBusAlarm(routeID,busstopID,token);
    }

    public Call<Void> unsubscribeBusAlarm(String routeID, String busstopID, String token){
        this.buildRetrofit(Constants.FIRE_BASE_NOTIFICATION_URL);
        this.setApi(SubscribeBusAlarmApi.class);
        SubscribeBusAlarmApi service = (SubscribeBusAlarmApi)this.getService();
        return service.unsubscribeBusAlarm(routeID,busstopID,token);
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
