package com.thrifa.ruofei.bus_locator.BusAlarm;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.thrifa.ruofei.bus_locator.EditAlarmDialogFragment;
import com.thrifa.ruofei.bus_locator.R;
import com.thrifa.ruofei.bus_locator.util.Constants;
import com.thrifa.ruofei.bus_locator.util.Server;
import com.thrifa.ruofei.bus_locator.util.ThrifaServer;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ruofeixu on 7/10/16.
 */
public class BusAlarmAdapter extends RecyclerView.Adapter<BusAlarmAdapter.AlarmViewHolder> {

    public final String TAG = this.getClass().getName();
    private List<BusAlarmItem> busAlarmList;
    private Context context;
    private String routeID, stopID, token, routeName,stopName, alarmID;

    public class AlarmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView routeNameView, busstopNameView, remainingTimeView, alarmSettingTimeView, routeIDView,stopIDView,alarmIDView, alarmStatusView;

        public Switch alarmSwitchView;

        public AlarmViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            context = view.getContext();
            routeNameView = (TextView) view.findViewById(R.id.alarmBusRoute);
            busstopNameView = (TextView) view.findViewById(R.id.alarmBusstop);
            remainingTimeView = (TextView) view.findViewById(R.id.alarmRemainingTime);
            alarmSettingTimeView = (TextView) view.findViewById(R.id.alarmSettingTime);
            routeIDView = (TextView) view.findViewById(R.id.routeIDinAlarmItem);
            stopIDView = (TextView) view.findViewById(R.id.stopIDInAlarmItem);
            alarmIDView = (TextView) view.findViewById(R.id.alarmID);
            alarmSwitchView = (Switch) view.findViewById(R.id.alarm_switch);
            alarmStatusView = (TextView)view.findViewById(R.id.alarm_status);
        }


        @Override
        public void onClick(View v) {
            // TODO: user shared preference
            String routeID = routeIDView.getText().toString();
            String stopID = stopIDView.getText().toString();
            String alarmID = alarmIDView.getText().toString();

//            String token  = FirebaseInstanceId.getInstance().getToken();
//            SharedPreferences sharedPref = context.getSharedPreferences(Constants.DISIRED_BUS_PREFFERNCE, Context.MODE_PRIVATE);
////            String defaultValue = context.getString(R.string.disired_bus_default);
//            String currentBusstopID = sharedPref.getString(context.getString(R.string.currenct_selected_busstop_key), "Unselect Current Busstop, ERROR");
//            Log.d(TAG, "RouteID:" + currentBusstopID);
//            setUpNotification(routeID,currentBusstopID,token);

            Bundle bundle = new Bundle();
            bundle.putString(Constants.AlarmList.BUS_ROUTE, routeID);
            bundle.putString(Constants.AlarmList.BUSSTOP, stopID);
            bundle.putString(Constants.AlarmList.ID,  alarmID);

            DialogFragment newFragment = new EditAlarmDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(((AppCompatActivity) context).getFragmentManager(), "remove alarm");

        }
    }


    public BusAlarmAdapter(List<BusAlarmItem> busAlarmList) {
        this.busAlarmList = busAlarmList;
    }

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itetmView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_bus_alarm, parent, false);
        return new AlarmViewHolder(itetmView);
    }

    @Override
    public void onBindViewHolder(AlarmViewHolder holder, int position) {
        final BusAlarmItem alarmItem = busAlarmList.get(position);
        holder.routeNameView.setText(alarmItem.getRouteName());
        if(alarmItem.getBusstopName().length() > 20)
        holder.busstopNameView.setText(alarmItem.getBusstopName().substring(20) + "...");
        else
            holder.busstopNameView.setText(alarmItem.getBusstopName());

        holder.remainingTimeView.setText(alarmItem.getRemainingTime());
        holder.alarmSettingTimeView.setText(alarmItem.getAlarmSettingTime());

        holder.alarmIDView.setText(alarmItem.getAlarmID().toString());

        final Switch switchItem = holder.alarmSwitchView;

        routeID = alarmItem.getRouteID();
        stopID = alarmItem.getStopID();
        routeName = alarmItem.getRouteName();
        stopName = alarmItem.getBusstopName();
        token = FirebaseInstanceId.getInstance().getToken();

        if (alarmItem.isAlarmFlag()) {
            holder.alarmStatusView.setText("Alarm On");
            switchItem.setChecked(true);
        } else {
            holder.alarmStatusView.setText("Alarm Off");
            switchItem.setChecked(false);
        }

        switchItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d(TAG,"switch changed rid:" +routeID +",sid:" +stopID);
                int index = busAlarmList.indexOf(new BusAlarmItem(routeID, stopID,routeName,stopName, "n/a", "n/a", -1, -1.0, -1.0, true));
                Log.d(TAG,"switch changed i:"+index);
                Handler h = new Handler(context.getMainLooper());
                if (b) {
                    Log.d(TAG,"switch on changed");
                    // alarm on
                    if (index != -1) {
                        BusAlarmListFragment.busAlarmList.get(index).setAlarmFlag(true);
                    }
                    // Although you need to pass an appropriate context
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Alarm On", Toast.LENGTH_LONG);
                        }
                    });
                } else {
                    Log.d(TAG,"switch off changed");
                    // alarm off
                    if (index != -1) {
                        busAlarmList.get(index).setAlarmFlag(false);
                    }

                    // Although you need to pass an appropriate context
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Alarm Off", Toast.LENGTH_LONG);
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return busAlarmList.size();
    }
}
