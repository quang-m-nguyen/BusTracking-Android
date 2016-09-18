package com.thrifa.ruofei.bus_locator.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.thrifa.ruofei.bus_locator.R;
import com.thrifa.ruofei.bus_locator.routes.RouteListActivity;
import com.thrifa.ruofei.bus_locator.util.Constants;
import com.thrifa.ruofei.bus_locator.util.Server;

/**
 * Created by ruofei on 5/31/2016.
 */
public class ThrifaBackgroundService extends Service {
    private String TAG = this.getClass().getName();
    private MyThread mythread;
    public boolean isRunning = false;
    private int lastUpdateTime, alarmTime;
    private volatile int currentCountingTime;
    private CountDownTimer mTimer;
    private BroadcastReceiver mReceiver;
    private volatile boolean timeUpdateFlag;
    private Context context;
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mythread = new MyThread();

        lastUpdateTime = -1;
        currentCountingTime = -1;
        timeUpdateFlag = false;

//        IntentFilter intentFilter = new IntentFilter(
//                "android.intent.action.MAIN");

        IntentFilter mStatusIntentFilter = new IntentFilter(
                Constants.BROADCAST_NEW_BUS_REMAINING_TIME);

        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e(TAG, "receive new time from fb message");
                Integer newRemaingTime = intent.getIntExtra(Constants.BUS_REMAINING_TIME, -1);

                //
                if (newRemaingTime != -1) {
                    currentCountingTime = newRemaingTime;
                    timeUpdateFlag = true;
                }
            }
        };
        //registering our receiver
        getApplicationContext().registerReceiver(mReceiver, mStatusIntentFilter);
    }

    @Override
    public synchronized void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if (isRunning) {
            mythread = null;
            isRunning = false;
        }
    }

    @Override
    public synchronized int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "onStart");
        if (!isRunning) {
            mythread.start();
            isRunning = true;
        }
        return START_NOT_STICKY;
    }


    public void showNotification(String title, String detail, int id) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.common_full_open_on_phone);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(detail);
//        mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        mBuilder.setVibrate(new long[]{1000});
        //LED
        mBuilder.setLights(Color.RED, 3000, 3000);
//        //Ton
//        mBuilder.setSound(Uri.parse("uri://sadfasdfasdf.mp3"));
        Intent resultIntent = new Intent(this, RouteListActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(RouteListActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
        mNotificationManager.notify(id, mBuilder.build());
        Log.e(TAG, "Notification");
    }

    public void checkingAlarmStatus() throws Exception {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.alarm_preference_key), Context.MODE_PRIVATE);
        // check if user set an alarm
        String flag = sharedPref.getString(getString(R.string.alarm_flag_key), "true");
        if (!flag.equals("true")) {
            currentCountingTime = -1;
            this.stopSelf();
        }
//        if(currentCountingTime == -1)
//            return;

//        Integer alarmTimeInSec = sharedPref.getInt(getString(R.string.alarm_remaining_time_key), currentCountingTime);
//        if (alarmTimeInMin != null && alarmTimeInMin >= 0) {
//
//        }
        Integer alarmSettingTime = sharedPref.getInt(getString(R.string.alarm_setting_time_key), -1);


        if (alarmSettingTime == -1) {
            // exception
            throw new Exception();
        } else {

            if (currentCountingTime <= alarmSettingTime && currentCountingTime != -1) {
                //send notification
                showNotification("bus is approaching", "remaning time:" + currentCountingTime, 100);

                // unsubscribe alarm
                Server server = Server.getInstance(context);
//                Call<Void> call = server.unsubscribeBusstop(routeID, busstopID, token);
//                Log.d(TAG, "send token to subscribe alarm");
//                call.enqueue(new Callback<Void>() {
//                    @Override
//                    public void onResponse(Call<Void> call, Response<Void> response) {
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<Void> call, Throwable t) {
//                        Log.e(TAG, "Fail to setup alarm:" + t.getMessage());
//                        t.printStackTrace();
//                    }
//                });
            } else {
                if (mTimer != null)
                    mTimer.cancel();

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mTimer = new CountDownTimer(currentCountingTime, 1000) {
                            public void onTick(long millisUntilFinished) {
//                mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                                if (currentCountingTime > 0)
                                    currentCountingTime -= 1;
                            }

                            public void onFinish() {
//                mTextField.setText("done!");

                            }
                        }.start();
                    }


                });


            }
        }
    }

    class MyThread extends Thread {
        static final long DELAY = 3000;

        @Override
        public void run() {

            Thread tmpThread = Thread.currentThread();
            Log.d(TAG, "Running1");
            while (isRunning && mythread == tmpThread) {
                Log.d(TAG, "Running");
                try {
                    Log.e(TAG, "alarm checking running");
                    checkingAlarmStatus();
                    Log.e(TAG, "alarm checking running2");
                    this.sleep(DELAY);
                } catch (Exception e) {
                    isRunning = false;
                    e.printStackTrace();
                }
            }
        }

    }
}
