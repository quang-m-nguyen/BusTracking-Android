package com.thrifa.ruofei.bus_locator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.thrifa.ruofei.bus_locator.BusAlarm.BusAlarmItem;
import com.thrifa.ruofei.bus_locator.BusAlarm.BusAlarmListFragment;
import com.thrifa.ruofei.bus_locator.service.ThrifaBackgroundService;
import com.thrifa.ruofei.bus_locator.util.Constants;
import com.thrifa.ruofei.bus_locator.util.Server;
import com.thrifa.ruofei.bus_locator.util.ThrifaServer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ruofeixu on 7/3/16.
 */
public class SetBusAlarmFragment extends DialogFragment {
    private SeekBar seekBar;
    private TextView textView;
    private Context context;

    private final String TAG = this.getClass().getName();

    private String routeID, busstopID, token;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setTitle(getString(R.string.alarm_setting_title));
        final View view = inflater.inflate(R.layout.fragment_set_bus_alarm_dialog, null);
        builder.setView(view);

        context = inflater.getContext();

        initializeVariables(view);


        Bundle bundle = this.getArguments();
        if (bundle == null) {
            Log.d(TAG, "Can't get enough info to set alarm");
        } else {
            routeID = bundle.getString(Constants.ROUTE_ID_KEY, "N/A");
            busstopID = bundle.getString(Constants.BUSSTOP_ID_KEY, "N/A");
            token = bundle.getString(Constants.DEVICE_TOKEN_KEY, "N/A");
        }

        builder.setMessage(getString(R.string.alarm_setting_message))
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { // Ok, send request
                        // start a service that tracking alarm data
                        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.alarm_preference_key), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.alarm_flag_key), "true");

                        Intent alarmService = new Intent(context, ThrifaBackgroundService.class);
                        getActivity().startService(alarmService);

                        String textString = textView.getText().toString();
                        Double alarmSettingTime = Double.parseDouble(textString);
                        if (alarmSettingTime != null) {
                            editor.putInt(getString(R.string.alarm_setting_time_key), (int) (alarmSettingTime * 60));
                        }
                        editor.commit();

                        int alarmID = (int)BusAlarmListFragment.idPool.pop();
                        BusAlarmListFragment.busAlarmList.add(new BusAlarmItem(routeID,busstopID,routeID.substring(5), busstopID.substring(5), "time", "Set Time:" + seekBar.getProgress() + " Mins", alarmID,-1.0,(double)seekBar.getProgress(), true));
                        Handler mainThread = new Handler(Looper.getMainLooper());
                        // In your worker thread
                        mainThread.post(new Runnable() {
                            @Override
                            public void run() {
                                if (BusAlarmListFragment.busAlarmList.size() != 0) {
                                    BusAlarmListFragment.mBusAlarmAdapter.notifyDataSetChanged();

                                } else {
                                    Log.d(TAG, "size is o");
                                }
                            }
                        });

                        // send request
                        //set alarm add alarm to alarm list

//                        ThrifaServer server = (ThrifaServer)ThrifaServer.getInstance(context);
//                        Call<Void> call = server.subscribeBusAlarm(routeID, busstopID, token);
//                        Log.d(TAG, "send token to subscribe alarm");
//                        call.enqueue(new Callback<Void>() {
//                            @Override
//                            public void onResponse(Call<Void> call, Response<Void> response) {
//
//                            }
//
//                            @Override
//                            public void onFailure(Call<Void> call, Throwable t) {
//                                Log.e(TAG, "Fail to setup alarm:" + t.getMessage());
//                                t.printStackTrace();
//                            }
//                        });

                        Toast.makeText(context,"Check your alarm in alarm page", Toast.LENGTH_LONG);

//                        Intent intent = new Intent(context, MainActivity.class);
//                        intent.putExtra(Constants.INTENT_CALL_FROM_KEY,this.getClass().getName());
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Cancel
                    }
                });
        // Create the AlertDialog object and return it
        seekBar.setMax(60);

//        textView.setText("Covered: " + seekBar.getProgress() + "/" + seekBar.getMax());
        textView.setText("" + seekBar.getProgress() );

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
//                Toast.makeText(getActivity(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(getActivity(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                textView.setText("Covered: " + progress + "/" + seekBar.getMax());
                textView.setText("" + seekBar.getProgress());
                Toast.makeText(getActivity(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
            }
        });

        return builder.create();
    }

    // A private method to help us initialize our variables.
    private void initializeVariables(View view) {
        seekBar = (SeekBar) view.findViewById(R.id.timeSeekBar);
        textView = (TextView) view.findViewById(R.id.timeText);
    }

}
