package com.example.ruofei.bus_locator.BusAlarm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ruofei.bus_locator.R;
import com.example.ruofei.bus_locator.RecycleViewDividerItemDecoration;
import com.example.ruofei.bus_locator.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruofeixu on 7/10/16.
 */
public class BusAlarmListFragment extends Fragment {

    public static BusAlarmAdapter mBusAlarmAdapter;
    public static List<BusAlarmItem> busAlarmList = new ArrayList<>();
    public final String TAG = this.getClass().getName();
    String busstopID;
    String token;


    private RecyclerView recyclerView;

    public BusAlarmListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        // Don't have dynamic bus stop update from server
        // TODO: dynamic update after server updated

        View rootView = inflater.inflate(R.layout.fragment_bus_alarm_list, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_alarm_list);
        mBusAlarmAdapter = new BusAlarmAdapter(busAlarmList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.addItemDecoration(new RecycleViewDividerItemDecoration(this.getContext()));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mBusAlarmAdapter);

//        trackedBusList.add(new TrackedBus("E","unknown"));
//        trackedBusList.add(new TrackedBus("I","1:00"));
//        trackedBusList.add(new TrackedBus("I","1:00"));
//        trackedBusList.add(new TrackedBus("I", "1:00"));

//        mTrackedBusAdapter.notifyDataSetChanged();
//        String BussstopID = getArguments().getString(Constants.BUSSTOP_ID_KEY);

//                Bundle bundle = this.getArguments();
//                if (bundle != null) {
//                        busstopID = bundle.getString(Constants.BUSSTOP_ID_KEY, "unknown");
//                        Log.d(TAG, "id is " + busstopID);
//                        token = bundle.getString(Constants.DEVICE_TOKEN_KEY, "unknown");
//                } else {
//                        Log.e(TAG, "Can't get busstopID");
//                }

//        subscribeBusTrackerData();

        return rootView;
    }

}
