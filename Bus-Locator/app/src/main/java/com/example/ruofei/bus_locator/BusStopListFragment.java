package com.example.ruofei.bus_locator;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ruofei.bus_locator.api.BusLocatorApi;
import com.example.ruofei.bus_locator.pojo.BusStop;
import com.example.ruofei.bus_locator.util.Constants;
import com.example.ruofei.bus_locator.util.Server;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class BusStopListFragment extends Fragment {

    public final static String BUS_STOP_NAME = "com.example.ruofei.bus_locator.BusStopName";
    public final static String BUS_STOP_LATITUDE = "com.example.ruofei.bus_locator.BusStopLatitude";
    public final static String BUS_STOP_LONGITUDE = "com.example.ruofei.bus_locator.BusStopLongtitude";
    static final String TAG = Constants.BUSSTOP_LIST_FRAGMENT_TAG;

    private ArrayAdapter<String> mBusStopAdapter;
    private List<BusStop> mBustStopList;

    public final static String url = "http://52.33.19.46/";

    // Using ListView for test
    // TODO: update to recycler view
    private ListView recyclerView;

    public BusStopListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            //TODO:get route name from instance state
            String RouteName;
            Log.e("test1", "test1");
            //Set to E for testing
            RouteName = "E";
            getBusStop(RouteName);
        }
    }

    public void getBusStop(String RouteName) {
//
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        // set your desired log level
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//        // add your other interceptors …
//        // add logging as last interceptor
//        httpClient.addInterceptor(logging);

//        String questUrl = url;
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(questUrl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(httpClient.build())
//                .build();
//        Log.e("URL", questUrl);
//
//        BusLocatorApi service = retrofit.create(BusLocatorApi.class);
        Server server =  Server.getInstance(this.getContext());
        server.buildRetrofit(Constants.BUS_LOCATOR_URL);
        server.setApi(BusLocatorApi.class);
        BusLocatorApi service = (BusLocatorApi)server.getService();

        Call<List<BusStop>> call = service.getBusStop("E");

        call.enqueue(new Callback<List<BusStop>>() {
            @Override
            public void onResponse(Call<List<BusStop>> call, Response<List<BusStop>> response) {
                ArrayList<String> busStops = new ArrayList<String>();
                int counter = 0;
                //TODO: error handling
                if (response != null) {
                    if (response.isSuccessful() == false)
                        Log.e("BUSSTOPLIST_REQUEST", "fail response");
                    mBustStopList = response.body();
                    for (int i = 0; i < response.body().size(); i++) {
                        String name = response.body().get(i).getStopName();
                        busStops.add(name);
                        Log.e("BUSSTOPLIST_NAME", name);
                    }
                }
                // Create a adapter for routes
                mBusStopAdapter =
                        new ArrayAdapter<String>(
                                getActivity(),
                                R.layout.basic_list_item,
                                R.id.basic_list_item_textview,
                                busStops
                        );

                Fragment frg = getFragmentManager().findFragmentByTag("busStopList");
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
            }

            @Override
            public void onFailure(Call<List<BusStop>> call, Throwable t) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("retro", "create view");
        // Inflate the layout for this fragment

        // Don't have dynamic route update from server
        // TODO: dynamic update after server updated
//        String[] data = {
//                "BusStop A",
//                "BusStop B",
//                "BusStop C"
//        };
//
//        List<String> busStops = new ArrayList<String>(Arrays.asList(data));

        View rootView = inflater.inflate(R.layout.fragment_bus_stop_list, container, false);

        recyclerView = (ListView) rootView.findViewById(R.id.recyclerview_bus_stop);
        recyclerView.setAdapter(mBusStopAdapter);

        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Pass bus stop name, latitude and longitude to Main Activity
                Intent intent = new Intent(getActivity(), MainActivity.class);
                String busStopName = recyclerView.getItemAtPosition(position).toString();
                double busStopLatitude = mBustStopList.get(position).getLatitude();
                double busStopLongitude =  mBustStopList.get(position).getLongtitude();
                Log.e("SEND_BUS_STOP", "lat:" + busStopLatitude + ",long:" + busStopLongitude + ",title:" + busStopName);

                intent.putExtra(Constants.INTENT_CALL_FROM_KEY,TAG);
                MainActivity.mMapDisplayType = MainActivity.MapDisplayType.DISPLAY_BUSSTOP;
//                MainActivity.mGoogleMap.clear(); // Clear old markers
//                MainActivity.mMakers.clear();
                //Marker busStopMarker = MainActivity.mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(busStopLatitude, busStopLongitude)).title(busStopName));
                BusStop busStop = new BusStop();
                busStop.setStopName(busStopName);
                busStop.setLatitude(busStopLatitude);
                busStop.setLongtitude(busStopLongitude);
                MainActivity.mBusStops.add(busStop);

                startActivity(intent);
            }
        });
        Log.e("retro", "create view end");
        return rootView;
    }
}