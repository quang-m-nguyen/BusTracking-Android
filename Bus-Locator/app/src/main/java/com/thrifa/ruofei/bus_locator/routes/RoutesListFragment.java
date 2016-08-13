package com.thrifa.ruofei.bus_locator.routes;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thrifa.ruofei.bus_locator.R;
import com.thrifa.ruofei.bus_locator.util.Server;
import com.thrifa.ruofei.bus_locator.util.ThrifaServer;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class RoutesListFragment extends Fragment {

    public final String TAG = this.getClass().getName();

    // TODO: Dydamic set message
    public final static String EXTRA_MESSAGE = "com.example.ruofei.bus_locator.Extra";
    public static RoutesAdapter mRouteAdapter;
    public static List<Route> routeList = new ArrayList<>();

    private RecyclerView recyclerView;

    public RoutesListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Don't have dynamic bus stop update from server
        // TODO: dynamic update after server updated

        routeList.clear();

        View rootView = inflater.inflate(R.layout.fragment_routes_list, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_route);
        mRouteAdapter = new RoutesAdapter(routeList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mRouteAdapter);


        //send notification request
        ThrifaServer server = ThrifaServer.getInstance(this.getContext());
        Call<List<Route>> call = server.getCityRouteInfo("99163");
        call.enqueue(new Callback<List<Route>>() {
            @Override
            public void onResponse(Call<List<Route>> call, Response<List<Route>> response) {
                Log.d(TAG, "Response:" + response.body());
                List<Route> trackerList = response.body();

                for (int i = 0; i < trackerList.size(); i++) {
                    final String routeName = trackerList.get(i).getRouteName();
                    final String routeID = trackerList.get(i).getRouteID();
                    final String opDays = trackerList.get(i).getOpDays();
                    final String opHours = trackerList.get(i).getOpHours();
                    final String color = trackerList.get(i).getColor();
                    routeList.add(new Route(Integer.toString(i),routeName, routeID,opDays,opHours,color));
                }
                Handler mainThread = new Handler(Looper.getMainLooper());
                // In your worker thread
                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        if (RoutesListFragment.routeList.size() != 0) {
                            RoutesListFragment.mRouteAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "size is o");
                        }

                    }
                });
            }

            @Override
            public void onFailure(Call<List<Route>> call, Throwable t) {
                Log.e(TAG, "Fail:" + t.getMessage());
                t.printStackTrace();
            }
        });

//        routeList.add(new Route("1", "E"));
//        routeList.add(new Route("2", "I"));
//        routeList.add(new Route("3", "North"));


        return rootView;
    }
}
