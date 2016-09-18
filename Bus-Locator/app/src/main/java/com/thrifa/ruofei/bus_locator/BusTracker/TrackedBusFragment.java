package com.thrifa.ruofei.bus_locator.BusTracker;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thrifa.ruofei.bus_locator.R;
import com.thrifa.ruofei.bus_locator.RecycleViewDividerItemDecoration;
import com.thrifa.ruofei.bus_locator.busstop.BusStopPopupActivity;
import com.thrifa.ruofei.bus_locator.pojo.BusInfo;
import com.thrifa.ruofei.bus_locator.pojo.BusTracker;
import com.thrifa.ruofei.bus_locator.util.Constants;
import com.thrifa.ruofei.bus_locator.util.Server;
import com.thrifa.ruofei.bus_locator.util.ThrifaServer;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

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
    public static String busstopID= "N/A";
    String token;
    Context context;

    private GetBusTrackerInfo mUpdateTastk;


    private RecyclerView recyclerView;

    public TrackedBusFragment() {
        // Required empty public constructor
    }

    private class GetBusTrackerInfo extends AsyncTask<Pair<String, Integer>, Void, Integer> {
        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
//            updateBusLocation();
            updateBusTime();
        }

        protected Integer doInBackground(Pair<String, Integer>... params) {
            String stopID = params[0].first;
            Integer interval = params[0].second;

            ThrifaServer server = ThrifaServer.getInstance(context);
            if (busstopID == "N/A"){
                return -1;
            }
            Call<List<BusTracker>> call = server.getBusstopInfo(busstopID);
            call.enqueue(new Callback<List<BusTracker>>() {
                @Override
                public void onResponse(Call<List<BusTracker>> call, Response<List<BusTracker>> response) {
                    if (response != null) {
                        final List<BusTracker> body = response.body();
                        if (body != null) {
                            if (body.size() > 0) {
//                                mBusList.clear();
                                for (int i = 0; i < body.size(); i++) {
                                    final BusTracker busTracker = body.get(i);
                                    String routeID = busTracker.getRouteID();
                                    String routeName = busTracker.getRouteName();
                                    String time = busTracker.getTime();
                                    String busstopNum = busTracker.getStopNum();
                                    String busstopID = busTracker.getStopID();

                                    int index = trackedBusList.indexOf(new TrackedBus(routeID, routeName, "n/a", "n/a", busstopID));
                                    if (index == -1) {
                                        trackedBusList.add(new TrackedBus(routeID, routeName, time, busstopNum, busstopID));
                                    } else {
                                        trackedBusList.get(index).setEstimatedTime(time);
                                        trackedBusList.get(index).setBusstopNum(busstopNum);
                                    }

                                }
                                Handler mainThread = new Handler(Looper.getMainLooper());
                                // In your worker thread
                                mainThread.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        TrackedBusFragment.mTrackedBusAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }
                    }
                    return;
                }

                @Override
                public void onFailure(Call<List<BusTracker>> call, Throwable t) {
                    Log.e(TAG, "asyc update error:" + t.toString());
                }
            });
            return interval;
        }

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

        context = rootView.getContext();

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

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            busstopID = bundle.getString(Constants.BUSSTOP_ID_KEY, "unknown");
            Log.d(TAG, "id is " + busstopID);
            token = bundle.getString(Constants.DEVICE_TOKEN_KEY, "unknown");
        } else {
            Log.e(TAG, "Can't get busstopID");
        }
//        subscribeBusTrackerData();
        return rootView;
    }

//
//    private class GetBusInfoTask extends AsyncTask<Pair<String, Integer>, Void, Integer> {
//        protected Integer doInBackground(Pair<String, Integer>... params) {
//            String stopID = params[0].first;
//            Integer interval = params[0].second;
//            try {
//                this.wait(interval);
//            } catch (Exception e) {
//
//            }
//            ThrifaServer server = (ThrifaServer) Server.getInstance(context);
//            Call<BusInfo> call = server.getBusInfo(routeID);
//            call.enqueue(new Callback<BusInfo>() {
//                @Override
//                public void onResponse(Call<BusInfo> call, Response<BusInfo> response) {
//                    if (response != null) {
//                        mBus = response.body();
//                        updateBusLocation();
//                    }
//                    return;
//                }
//
//                @Override
//                public void onFailure(Call<BusInfo> call, Throwable t) {
//                    Log.e(TAG, "update error:" + t.toString());
//                }
//            });
//            return interval;
//        }
//    }

    private void subscribeBusTrackerData() {
        Log.d(TAG, "subscribe");
//        String token = FirebaseInstanceId.getInstance().getToken();
        //send notification request
        ThrifaServer server = (ThrifaServer) ThrifaServer.getInstance(this.getContext());
        Call<List<BusTracker>> call = server.getBusTrakerCall(busstopID, token);
        call.enqueue(new Callback<List<BusTracker>>() {
            @Override
            public void onResponse(Call<List<BusTracker>> call, Response<List<BusTracker>> response) {
                Log.d(TAG, "Response:" + response.body());
                List<BusTracker> trackerList = response.body();

                for (int i = 0; i < trackerList.size(); i++) {
                    final String routeID = trackerList.get(i).getRouteID();
                    final String routeName = trackerList.get(i).getRouteName();
                    final String stopID = trackerList.get(i).getStopID();
//                    Log.e(TAG, "test output: " + stopID + "route:" + routeID);
                    trackedBusList.add(new TrackedBus(routeID, routeName, "unknown", "unkonwn", stopID));
                }
                Handler mainThread = new Handler(Looper.getMainLooper());
                // In your worker thread
                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        if (TrackedBusFragment.trackedBusList.size() != 0) {
                            TrackedBusFragment.mTrackedBusAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "size is o");
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

    private void unsubscribeBusTrakerData() {

        ThrifaServer server = (ThrifaServer) ThrifaServer.getInstance(this.getContext());
        Call<Void> call = server.unsubscribeBusstop(busstopID, token);
        Log.d(TAG, "send token to unsubscribe");
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Fail:" + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();

        mUpdateTastk = null;
        busstopID = "N/A";
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
//            subscribeBusTrackerData();
            if(busstopID != "N/A") {
                if (mUpdateTastk == null)
                    mUpdateTastk = new GetBusTrackerInfo();
                mUpdateTastk.execute(new Pair<String, Integer>(busstopID, 2000));
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mUpdateTastk != null)
            mUpdateTastk.cancel(true);

        trackedBusList.clear();
//        unsubscribeBusTrakerData();
    }

    private void updateBusTime() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                if (mUpdateTastk == null)
                mUpdateTastk = new GetBusTrackerInfo();
                try {
                    mUpdateTastk.execute(new Pair<String, Integer>(busstopID, 2000));
                } catch (Exception e) {
                    updateBusTime();
                }
            }
        }, 5000);
    }
}
