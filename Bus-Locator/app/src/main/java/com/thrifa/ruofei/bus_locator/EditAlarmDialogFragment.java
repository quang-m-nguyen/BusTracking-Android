package com.thrifa.ruofei.bus_locator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.thrifa.ruofei.bus_locator.BusAlarm.BusAlarmItem;
import com.thrifa.ruofei.bus_locator.BusAlarm.BusAlarmListFragment;
import com.thrifa.ruofei.bus_locator.util.Constants;
import com.thrifa.ruofei.bus_locator.util.Server;
import com.google.firebase.iid.FirebaseInstanceId;
import com.thrifa.ruofei.bus_locator.util.ThrifaServer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ruofeixu on 8/9/16.
 */
public class EditAlarmDialogFragment extends DialogFragment {

    private Context context;

    private final String TAG = this.getClass().getName();

    String alarmID, routeID, stopID, token;

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setTitle(getString(R.string.alarm_remove_title));
        final View view = inflater.inflate(R.layout.fragment_remove_alarm_dialog, null);
        builder.setView(view);

        context = view.getContext();

        final Bundle bundle = this.getArguments();
//                        if (bundle != null) {
        alarmID = bundle.getString(Constants.AlarmList.ID, "unknown");
        routeID = bundle.getString(Constants.AlarmList.BUS_ROUTE, "unknown");
        stopID = bundle.getString(Constants.AlarmList.BUSSTOP, "unknown");

        token = FirebaseInstanceId.getInstance().getToken();

//                        }
        builder.setMessage(getString(R.string.alarm_remove_message))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { // Ok, send request
                        Log.d(TAG, "id is " + alarmID);

                        // TODO: using index for now, update to real id later
                        Integer alarmIDInt = Integer.parseInt(alarmID);
                        Log.d(TAG, "id is ind " + id);
                        if (alarmIDInt != null) {
                            for (int i = 0; i < BusAlarmListFragment.busAlarmList.size(); i++) {
                                final BusAlarmItem alarmItem = BusAlarmListFragment.busAlarmList.get(i);
                                Log.d(TAG, "loop remove target alarm" + alarmItem.getAlarmID());
                                if (alarmItem.getAlarmID() == alarmIDInt) {
                                    Log.d(TAG, "remove target alarm");
                                    BusAlarmListFragment.busAlarmList.remove(i);
                                    BusAlarmListFragment.idPool.push(i);
                                }
                            }
                            if (BusAlarmListFragment.busAlarmList.size() <= 0) {
                                BusAlarmListFragment.emptyAlarmTextView.setVisibility(View.VISIBLE);
                            } else {
                                BusAlarmListFragment.emptyAlarmTextView.setVisibility(View.INVISIBLE);
                            }
                            Handler mainThread = new Handler(Looper.getMainLooper());
                            mainThread.post(new Runnable() {
                                @Override
                                public void run() {
                                    BusAlarmListFragment.mBusAlarmAdapter.notifyDataSetChanged();
                                }
                            });
                            unsubscribeAlarm(routeID, stopID, token);
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Cancel
                    }
                });

        return builder.create();
    }

    private void unsubscribeAlarm(String routeID, String stopID, String token) {
        ThrifaServer server = (ThrifaServer) ThrifaServer.getInstance(context);
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
    }
}
