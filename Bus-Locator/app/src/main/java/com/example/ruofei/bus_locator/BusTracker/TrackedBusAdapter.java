package com.example.ruofei.bus_locator.BusTracker;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ruofei.bus_locator.R;
import com.example.ruofei.bus_locator.SetBusAlarmFragment;
import com.example.ruofei.bus_locator.util.Constants;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;

/**
 * Created by ruofei on 6/11/2016.
 */
public class TrackedBusAdapter extends RecyclerView.Adapter<TrackedBusAdapter.MyViewHolder> {

    public final String TAG = this.getClass().getName();
    private List<TrackedBus> trackedBusList;
    private Context context;
//    private String routeID, stopID, token;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView trackedBusRoute, trackedBusTime, trackedBusstopNum;

        public  MyViewHolder(View view){
            super(view);
            view.setOnClickListener(this);
            context = view.getContext();
            trackedBusRoute = (TextView) view.findViewById(R.id.trackedBusRoute);
            trackedBusTime = (TextView) view.findViewById(R.id.trackedBusTime);
            trackedBusstopNum = (TextView) view.findViewById(R.id.trackedBusstopNum);
        }

        @Override
        public void onClick(View v) {
            // TODO: user shared preference
            String routeID = trackedBusRoute.getText().toString();
            String token  = FirebaseInstanceId.getInstance().getToken();
            SharedPreferences sharedPref = context.getSharedPreferences(Constants.DISIRED_BUS_PREFFERNCE, Context.MODE_PRIVATE);
//            String defaultValue = context.getString(R.string.disired_bus_default);
            String currentBusstopID = sharedPref.getString(context.getString(R.string.currenct_selected_busstop_key), "Unselect Current Busstop, ERROR");
            Log.e(TAG, "RouteID:" + currentBusstopID);
            setUpNotification(routeID,currentBusstopID,token);
        }
    }

    public void setUpNotification(String routeID, String stopID, String token) {

        Bundle bundle = new Bundle();
        bundle.putString(Constants.ROUTE_ID_KEY, routeID);
        bundle.putString(Constants.BUSSTOP_ID_KEY, stopID);
        bundle.putString(Constants.DEVICE_TOKEN_KEY, token);
        DialogFragment newFragment = new SetBusAlarmFragment();
        newFragment.setArguments(bundle);

        newFragment.show(((AppCompatActivity) context).getFragmentManager(), "set up notification");
    }


    public TrackedBusAdapter(List<TrackedBus> trackedBusList){
        this.trackedBusList = trackedBusList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itetmView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_tracked_bus,parent,false);
        return new MyViewHolder(itetmView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TrackedBus trackedBus = trackedBusList.get(position);
        holder.trackedBusRoute.setText(trackedBus.getRouteName());
        holder.trackedBusTime.setText(trackedBus.getEstimatedTime());
//        holder.trackedBusstopNum.setText(trackedBus.getBusstopNum());
        holder.trackedBusstopNum.setText("click to set alarm");
    }

    @Override
    public int getItemCount() {
        return trackedBusList.size();
    }
}
