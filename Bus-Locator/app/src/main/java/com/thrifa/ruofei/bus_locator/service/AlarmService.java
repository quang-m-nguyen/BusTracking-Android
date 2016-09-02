package com.thrifa.ruofei.bus_locator.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.thrifa.ruofei.bus_locator.BusAlarm.BusAlarmItem;
import com.thrifa.ruofei.bus_locator.BusAlarm.BusAlarmListFragment;
import com.thrifa.ruofei.bus_locator.MainActivity;
import com.thrifa.ruofei.bus_locator.R;
import com.thrifa.ruofei.bus_locator.pojo.BusTracker;
import com.thrifa.ruofei.bus_locator.util.ThrifaServer;

import java.sql.Time;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ruofeixu on 8/22/16.
 */

public class AlarmService extends IntentService {

    public String TAG = this.getClass().getName();
    private Context context;

    //    Time t = new Time();
    public AlarmService() {
        super("AlarmService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        context = getApplication().getApplicationContext();

        while (true) {
            try {
                final List<BusAlarmItem> busAlarmList = BusAlarmListFragment.busAlarmList;
                Thread.sleep(5000);

                ThrifaServer server = (ThrifaServer) ThrifaServer.getInstance(context);


                for (int i = 0; i < busAlarmList.size(); i++) {
                    final BusAlarmItem busAlarmItem = busAlarmList.get(i);
                    String routeID = busAlarmItem.getRouteID();
                    String stopID = busAlarmItem.getStopID();
                    Call<BusTracker> call = server.getTimeInfoForARoute(routeID, stopID);
                    Log.d(TAG, "send reqeust to update alarm");
                    call.enqueue(new Callback<BusTracker>() {
                        @Override
                        public void onResponse(Call<BusTracker> call, Response<BusTracker> response) {
                            final BusTracker body = response.body();
                            if (body == null) {
                                return;
                            }
                            // Set current remaining time
                            // TODO: change server side to send time in sec
//                            editor.putInt(getString(R.string.current_remaining_time_key), newRemainingTime * 60);
                            // TODO: notify alarm service the update

                            String routeID = body.getRouteID();
                            String stopID = body.getStopID();


                            String newRemainingTime = body.getTime();

                            Log.e(TAG, "update alarm time:" + newRemainingTime);
                            try {
                                Handler mainThread = new Handler(Looper.getMainLooper());
                                // In your worker thread
                                int index = BusAlarmListFragment.busAlarmList.indexOf(new BusAlarmItem(routeID, stopID, "", "", "n/a", "n/a", -1, -1.0, -1.0, true));
                                Log.e(TAG, "tracker index:" + index + ", tracker route:" + routeID + ", traker time:" + stopID);
                                if (index == -1)
                                    return;
                                final BusAlarmItem alarmItem = BusAlarmListFragment.busAlarmList.get(index);
                                //check if alarm off
                                if (alarmItem.isAlarmFlag()) {
                                    alarmItem.setRemainingTime("Arrive in:" + newRemainingTime + " Mins");
                                    alarmItem.setRemainTimeNum(Double.parseDouble(newRemainingTime));

                                    if (alarmItem.getSettingTimeNum() >= alarmItem.getRemainTimeNum() && alarmItem.getRemainTimeNum() >= 0) {
                                        sendNotification("Bus is about to arriving in " + Math.round(alarmItem.getRemainTimeNum()) + "minutes", "Bus About Arrive");
                                        alarmItem.setRemainingTime("alarm notified");
                                        alarmItem.setAlarmFlag(false);
                                        mainThread.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                BusAlarmListFragment.mBusAlarmAdapter.notifyDataSetChanged();
                                            }
                                        });

                                    }

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
                                }

                            } catch (Exception e) {
                                Log.e(TAG, e.toString());
                            }

                        }

                        @Override
                        public void onFailure(Call<BusTracker> call, Throwable t) {
                            Log.e(TAG, "Fail to update alarm:" + t.getMessage());
                            t.printStackTrace();
                        }
                    });
                }
            } catch (InterruptedException e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }
        }
    }

    private void sendNotification(String messageBody, String title) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
}
