package com.example.ruofei.bus_locator.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.ruofei.bus_locator.api.BusLocatorApi;
import com.example.ruofei.bus_locator.api.GoogleMapApi;
import com.example.ruofei.bus_locator.pojo.BusStop;
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
    static Server instance;

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
        buildRetrofit(serverUrl);
        mApi = BusLocatorApi.class;
    }


    public static synchronized Server getInstance(Context context) {
        if (instance == null) {
            instance = new Server(context);
        }
        return instance;
    }

    public void buildRetrofit(String url)
    {
        serverUrl = url;
        retrofit = new Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }

    public void setApi(Class<?> api )
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