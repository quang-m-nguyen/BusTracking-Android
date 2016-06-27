package com.example.ruofei.bus_locator;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ruofei.bus_locator.pojo.BusTracker;
import com.example.ruofei.bus_locator.pojo.Leg;
import com.example.ruofei.bus_locator.util.Constants;
import com.example.ruofei.bus_locator.util.Server;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class TrackedBusFragment extends Fragment {


    public static TrackedBusAdapter mTrackedBusAdapter;
    public static List<TrackedBus> trackedBusList = new ArrayList<>();
    public final String TAG = this.getClass().getName();


    private RecyclerView recyclerView;

    public TrackedBusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        // Don't have dynamic bus stop update from server
        // TODO: dynamic update after server updated

        View rootView = inflater.inflate(R.layout.fragment_tracked_bus, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_tracked_bus);
        mTrackedBusAdapter = new TrackedBusAdapter(trackedBusList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.addItemDecoration(new RecycleViewDividerItemDecoration(this.getContext()));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mTrackedBusAdapter);

//        trackedBusList.add(new TrackedBus("E","unknown"));
//        trackedBusList.add(new TrackedBus("I","1:00"));
//        trackedBusList.add(new TrackedBus("I","1:00"));
//        trackedBusList.add(new TrackedBus("I", "1:00"));

//        mTrackedBusAdapter.notifyDataSetChanged();
//        String BussstopID = getArguments().getString(Constants.BUSSTOP_ID_KEY);
        String busstopID = "unset";

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            busstopID = bundle.getString(Constants.BUSSTOP_ID_KEY, "unknown");
            Log.e(TAG, "id is " + busstopID);
        } else {
            Log.e(TAG, "Can't get busstopID");
        }
        subscribeBusTrackerData(busstopID);

        return rootView;

    }

    private void subscribeBusTrackerData(String busstopID) {
        Log.e(TAG, "subscribe");
        String token = FirebaseInstanceId.getInstance().getToken();
        //send notification request
        Server server = Server.getInstance(this.getContext());
        Call<List<BusTracker>> call = server.getBusTrakerCall(busstopID, token);
        call.enqueue(new Callback<List<BusTracker>>() {
            @Override
            public void onResponse(Call<List<BusTracker>> call, Response<List<BusTracker>> response) {
                Log.e(TAG, "Response:" + response.body());
                List<BusTracker> trackerList = response.body();

                for (int i = 0; i < trackerList.size(); i++) {
                    final String routeID = trackerList.get(i).getRouteID();
                    trackedBusList.add(new TrackedBus(routeID, "unknown", "unkonwn"));
                }
                Handler mainThread = new Handler(Looper.getMainLooper());
                // In your worker thread
                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        if (TrackedBusFragment.trackedBusList.size() != 0) {
                            TrackedBusFragment.mTrackedBusAdapter.notifyDataSetChanged();
                        } else {
                            Log.e(TAG, "size is o");
                        }

                    }
                });
            }

            @Override
            public void onFailure(Call<List<BusTracker>> call, Throwable t) {
                Log.e(TAG, "Fail:" + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void unsubcribeBusTrakerData() {

    }

    @Override
    public void onStop() {
        super.onStop();
        trackedBusList.clear();
    }
}
