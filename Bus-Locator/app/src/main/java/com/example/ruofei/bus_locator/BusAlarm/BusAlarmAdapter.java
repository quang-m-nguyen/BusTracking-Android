package com.example.ruofei.bus_locator.BusAlarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ruofei.bus_locator.R;
import com.example.ruofei.bus_locator.util.Constants;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;

/**
 * Created by ruofeixu on 7/10/16.
 */
public class BusAlarmAdapter extends RecyclerView.Adapter<BusAlarmAdapter.AlarmViewHolder> {

    public final String TAG = this.getClass().getName();
    private List<BusAlarmItem> busAlarmList;
    private Context context;
//    private String routeID, stopID, token;

    public class AlarmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView routeNameView, busstopNameView, remainingTimeView, alarmSettingTimeView;

        public  AlarmViewHolder(View view){
            super(view);
            view.setOnClickListener(this);
            context = view.getContext();
            routeNameView = (TextView) view.findViewById(R.id.alarmBusRoute);
            busstopNameView = (TextView) view.findViewById(R.id.alarmBusstop);
            remainingTimeView = (TextView) view.findViewById(R.id.alarmRemainingTime);
            alarmSettingTimeView = (TextView) view.findViewById(R.id.alarmSettingTime);
        }

        @Override
        public void onClick(View v) {
            // TODO: user shared preference
//            String routeID = trackedBusRoute.getText().toString();
//            String token  = FirebaseInstanceId.getInstance().getToken();
//            SharedPreferences sharedPref = context.getSharedPreferences(Constants.DISIRED_BUS_PREFFERNCE, Context.MODE_PRIVATE);
////            String defaultValue = context.getString(R.string.disired_bus_default);
//            String currentBusstopID = sharedPref.getString(context.getString(R.string.currenct_selected_busstop_key), "Unselect Current Busstop, ERROR");
//            Log.d(TAG, "RouteID:" + currentBusstopID);
//            setUpNotification(routeID,currentBusstopID,token);
        }
    }

    public void setUpNotification(String routeID, String stopID, String token) {

        Bundle bundle = new Bundle();
        bundle.putString(Constants.ROUTE_ID_KEY, routeID);
        bundle.putString(Constants.BUSSTOP_ID_KEY, stopID);
        bundle.putString(Constants.DEVICE_TOKEN_KEY, token);
//        DialogFragment newFragment = new SetBusAlarmFragment();
//        newFragment.setArguments(bundle);
//
//        newFragment.show(((AppCompatActivity) context).getFragmentManager(), "set up notification");
    }


    public BusAlarmAdapter(List<BusAlarmItem> busAlarmList){
        this.busAlarmList = busAlarmList;
    }

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itetmView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_bus_alarm,parent,false);
        return new AlarmViewHolder(itetmView);
    }

    @Override
    public void onBindViewHolder(AlarmViewHolder holder, int position) {
        BusAlarmItem alarmItem = busAlarmList.get(position);
        holder.routeNameView.setText(alarmItem.getRouteName());
        holder.busstopNameView.setText(alarmItem.getBusstopName());
        holder.remainingTimeView.setText(alarmItem.getRemainingTime());
        holder.alarmSettingTimeView.setText(alarmItem.getAlarmSettingTime());
    }

    @Override
    public int getItemCount() {
        return busAlarmList.size();
    }
}
