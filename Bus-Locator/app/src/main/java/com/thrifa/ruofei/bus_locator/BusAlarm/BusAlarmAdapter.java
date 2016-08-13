package com.thrifa.ruofei.bus_locator.BusAlarm;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
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
    private String routeID, stopID, token;

    public class AlarmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView routeNameView, busstopNameView, remainingTimeView, alarmSettingTimeView, alarmIDView;

        public Switch alarmSwitchView;

        public AlarmViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            context = view.getContext();
            routeNameView = (TextView) view.findViewById(R.id.alarmBusRoute);
            busstopNameView = (TextView) view.findViewById(R.id.alarmBusstop);
            remainingTimeView = (TextView) view.findViewById(R.id.alarmRemainingTime);
            alarmSettingTimeView = (TextView) view.findViewById(R.id.alarmSettingTime);
            alarmIDView = (TextView) view.findViewById(R.id.alarmID);

            alarmSwitchView = (Switch) view.findViewById(R.id.alarm_switch);
        }


        @Override
        public void onClick(View v) {
            // TODO: user shared preference
            String routeID = routeNameView.getText().toString();
            String stopID = busstopNameView.getText().toString();
            String ID = alarmIDView.getText().toString();
//            String token  = FirebaseInstanceId.getInstance().getToken();
//            SharedPreferences sharedPref = context.getSharedPreferences(Constants.DISIRED_BUS_PREFFERNCE, Context.MODE_PRIVATE);
////            String defaultValue = context.getString(R.string.disired_bus_default);
//            String currentBusstopID = sharedPref.getString(context.getString(R.string.currenct_selected_busstop_key), "Unselect Current Busstop, ERROR");
//            Log.d(TAG, "RouteID:" + currentBusstopID);
//            setUpNotification(routeID,currentBusstopID,token);

            Bundle bundle = new Bundle();
            bundle.putString(Constants.AlarmList.BUS_ROUTE, routeID);
            bundle.putString(Constants.AlarmList.BUSSTOP, stopID);
            bundle.putString(Constants.AlarmList.ID, ID);

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
        holder.busstopNameView.setText(alarmItem.getBusstopName());
        holder.remainingTimeView.setText(alarmItem.getRemainingTime());
        holder.alarmSettingTimeView.setText(alarmItem.getAlarmSettingTime());
        holder.alarmIDView.setText(alarmItem.getAlarmID().toString());

        final Switch switchItem = holder.alarmSwitchView;

        routeID = alarmItem.getRouteName();
        stopID = alarmItem.getBusstopName();
        token = FirebaseInstanceId.getInstance().getToken();

        if (alarmItem.isAlarmFlag()) {
            switchItem.setChecked(true);
        } else {
            switchItem.setChecked(false);
        }

        switchItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                final List<BusAlarmItem> busAlarmList = BusAlarmListFragment.busAlarmList;
                int index = busAlarmList.indexOf(new BusAlarmItem(routeID, stopID, "n/a", "n/a", -1, -1.0, -1.0, true));

                if (b) {
                    // alarm on
                    if (index != -1) {
                        final BusAlarmItem busAlarmItem = busAlarmList.get(index);
                        busAlarmItem.setAlarmFlag(true);
                    }

                    // sub
                    ThrifaServer server = (ThrifaServer) Server.getInstance(context);
                    Call<Void> call = server.subscribeBusAlarm(routeID, stopID, token);
                    Log.d(TAG, "send token to subscribe alarm");
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {

                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e(TAG, "Fail to setup alarm:" + t.getMessage());
                            t.printStackTrace();
                        }
                    });

                    Toast.makeText(context, "Alarm On", Toast.LENGTH_LONG);
                } else {
                    // alarm off
                    if (index != -1) {
                        final BusAlarmItem busAlarmItem = busAlarmList.get(index);
                        busAlarmItem.setAlarmFlag(false);
                    }

                    // unsub
                    ThrifaServer server = (ThrifaServer) Server.getInstance(context);
                    Call<Void> call = server.unsubscribeBusAlarm(routeID, stopID, token);
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


                    Toast.makeText(context, "Alarm Off", Toast.LENGTH_LONG);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return busAlarmList.size();
    }
}
