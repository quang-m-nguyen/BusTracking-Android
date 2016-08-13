package com.thrifa.ruofei.bus_locator.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.thrifa.ruofei.bus_locator.BusTracker.TrackedBus;
import com.thrifa.ruofei.bus_locator.BusTracker.TrackedBusFragment;
import com.thrifa.ruofei.bus_locator.pojo.BusTracker;
import com.thrifa.ruofei.bus_locator.util.Constants;
import com.thrifa.ruofei.bus_locator.util.MyDeserializer;
import com.thrifa.ruofei.bus_locator.util.Server;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thrifa.ruofei.bus_locator.util.ThrifaServer;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ruofei on 6/4/2016.
 */
public class FirebaseBusMessagingService extends FirebaseMessagingService {

    private final String TAG = this.getClass().getName();
    private Context context;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        //Calling method to generate notification
//        sendNotification(remoteMessage.getNotification().getBody());
        context = getApplicationContext();
        final Map<String, String> data = remoteMessage.getData();
        if (data != null) {
            String dataStr = data.toString();
            Log.e(TAG, "get data from firebase " + dataStr);
            String content_type = data.get("content_type");

            if (content_type != null) {
                if (content_type.equals("AlarmTimeUpdate")) {
                    updateAlarmTime(data);
                } else if (content_type.equals("BusstopTimeUpdate")) {
                    updateBusstopTime(data);
                } else if (content_type.equals("CoordinatesUpdate")) {
                    updateBusCoordinate(data);
                }
            }
        }


//        String timeStr = remoteMessage.getData().get("time");
//        if(timeStr != null) {
//            Double time = Double.parseDouble(timeStr);
//            String routeID = remoteMessage.getData().get("routeID");
//            String busstopNum = remoteMessage.getData().get("stopNum");
//            if (routeID == null)
//                return;
//            Log.e(TAG,"get data from firebase " +dataStr);
//            try {
//                Handler mainThread = new Handler(Looper.getMainLooper());
//                // In your worker thread
//                int index = TrackedBusFragment.trackedBusList.indexOf(new TrackedBus(routeID,"n/a","n/a"));
//                Log.e(TAG, "tracker index:" + index + ", tracker route:" + routeID + ", traker time:" + time + ", stopNum:"+busstopNum);
//                if (index == -1)
//                    return;
//
//                TrackedBusFragment.trackedBusList.get(index).setEstimatedTime(timeStr);
//                TrackedBusFragment.trackedBusList.get(index).setBusstopNum(busstopNum);
//                mainThread.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (TrackedBusFragment.trackedBusList.size() != 0) {
//                            TrackedBusFragment.mTrackedBusAdapter.notifyDataSetChanged();
//                        } else {
//                            Log.e(TAG, "size is o");
//                        }
//
//                    }
//                });
//
//
//                if (time <= 0) {
////                    final Vibrator vibrator;
////            vibrator = (Vibrator) getSystemService(MainActivity.VIBRATOR_SERVICE);
////            vibrator.vibrate(60000);
////                sendNotification(dataStr);
//                }
//            }catch (Exception e){
//                Log.e(TAG,e.toString());
//            }
//        }
    }

    private void updateAlarmTime(Map<String, String> data) {
        //UpdateAlarmTime
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.alarm_preference_key), Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPref.edit();
        //Check if the user set up an alarm
        String flag = sharedPref.getString(context.getString(R.string.alarm_flag_key), "false");
        if (flag.equals("true")) {
            // Set current remaining time
            Double newRemainingTimeDouble = Double.parseDouble(data.get("remain_time"));
            newRemainingTimeDouble =Double.longBitsToDouble(Math.round(newRemainingTimeDouble));
            Integer newRemainingTime = (int) (newRemainingTimeDouble * 60); // convert sec
            if (newRemainingTime != null) {
                // Set current remaining time
                // TODO: change server side to send time in sec
//                            editor.putInt(getString(R.string.current_remaining_time_key), newRemainingTime * 60);
                // TODO: notify alarm service the update
                Log.e(TAG, "update alarm time:" + newRemainingTime);

                String routeID = data.get("route_ID").substring(5);
                String stopID = data.get("busstop_ID").substring(5);

                try {
                    Handler mainThread = new Handler(Looper.getMainLooper());
                    // In your worker thread
                    int index = BusAlarmListFragment.busAlarmList.indexOf(new BusAlarmItem(routeID, stopID, "n/a", "n/a", -1, -1.0, -1.0, true));
                        Log.e(TAG, "tracker index:" + index + ", tracker route:" + routeID + ", traker time:" + stopID );
                    if (index == -1)
                        return;


                    final BusAlarmItem alarmItem = BusAlarmListFragment.busAlarmList.get(index);
                    //check if alarm off
                    if(alarmItem.isAlarmFlag()) {
                    alarmItem.setRemainingTime("Arrive in:" + newRemainingTimeDouble.toString() + " Mins");
                    alarmItem.setRemainTimeNum(newRemainingTimeDouble);
//                        BusAlarmListFragment.busAlarmList.get(index).setAlarmSettingTime("Setting Time:" + );

                    if (alarmItem.getSettingTimeNum() >= alarmItem.getRemainTimeNum() && alarmItem.getRemainTimeNum() >= 0) {
                        sendNotification("Bus is about to arriving in " + Math.round(alarmItem.getRemainTimeNum()) + "minutes");

                        // unsubscribe
                        alarmItem.setAlarmFlag(false);
                        alarmItem.setRemainingTime("Notified Bus Arriving in" + alarmItem.getRemainTimeNum() + " Min");
                        mainThread.post(new Runnable() {
                            @Override
                            public void run() {
                                BusAlarmListFragment.mBusAlarmAdapter.notifyDataSetChanged();
                            }
                        });

                        String token  = FirebaseInstanceId.getInstance().getToken();
                        unsubscribeAlarm(alarmItem.getRouteName(),alarmItem.getBusstopName(), token);
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
//                            Intent i = new Intent("android.intent.action.UpdateBusStatus").putExtra(Constants.BROADCAST_NEW_BUS_REMAINING_TIME, newRemainingTime);
//                            this.sendBroadcast(i);

//                            Intent localIntent =
//                                    new Intent(Constants.BROADCAST_NEW_BUS_REMAINING_TIME)
//                                            // Puts the status into the Intent
//                                            .putExtra(Constants.BUS_REMAINING_TIME, newRemainingTime);
//                            // Broadcasts the Intent to receivers in this app.
//                            LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

//                Intent intent = new Intent();
//                intent.setAction(Constants.BROADCAST_NEW_BUS_REMAINING_TIME);
//                intent.putExtra(Constants.BUS_REMAINING_TIME, newRemainingTime);
//                getApplicationContext().sendBroadcast(intent);

//                            editor.commit();
            }

        }
    }

    private void updateBusCoordinate(Map<String, String> data) {
        Log.e(TAG, "broadcast lat");
        String busLat = data.get("lat");
        String busLng = data.get("lng");
//        Log.e(TAG, "get broad cast lat" + busLat + ", lng:" + busLng);
        if (busLat != null && busLng != null) {
//            MainActivity.busLng = Double.parseDouble(busLng);
//            MainActivity.busLat = Double.parseDouble(busLat);

            if (busLat != null && busLng != null) {
                Log.e(TAG, "broadcast lat" + busLat + ", lng:" + busLng);
                Intent i = new Intent(Constants.MAIN_ACTION).putExtra("BUS_LAT", busLat).putExtra("BUS_LNG", busLng);
                this.sendBroadcast(i);
            }
        }
    }

    private void updateBusstopTime(Map<String, String> data) {

        String busArray = data.get("busArray");

        if (busArray != null) {
            Gson gson =
                    new GsonBuilder()
                            .registerTypeAdapter(BusTracker[].class, new MyDeserializer<BusTracker[]>())
                            .create();
            BusTracker[] busList = gson.fromJson(busArray, BusTracker[].class);

            if (busList != null) {
                Log.e(TAG, "Bus List:" + busList.toString());
                for (int i = 0; i < busList.length; i++) {
                    Log.e(TAG, "index:" + i + ", content:" + busList[i].getRouteID());

                    String routeID = busList[i].getRouteID();
                    String routeName = busList[i].getRouteName();
                    String time = busList[i].getTime();
                    String busstopNum = busList[i].getStopNum();
                    String busstopID = busList[i].getStopID();

                    try {
                        Handler mainThread = new Handler(Looper.getMainLooper());
                        // In your worker thread
                        if (TrackedBusFragment.trackedBusList == null || TrackedBusFragment.trackedBusList.size()<=0)
                            return;
                        int index = TrackedBusFragment.trackedBusList.indexOf(new TrackedBus(routeID, routeName, "n/a", "n/a",busstopID));
                        Log.e(TAG, "tracker index:" + index + ", stop id:" +busstopID + ", tracker route:" + routeID + ", route name:"+ routeName +", traker time:" + time + ", stopNum:" + busstopNum);
                        if (index == -1)
                            return;

                        TrackedBusFragment.trackedBusList.get(index).setEstimatedTime(time);
                        TrackedBusFragment.trackedBusList.get(index).setBusstopNum(busstopNum);
                        mainThread.post(new Runnable() {
                            @Override
                            public void run() {
                                if (TrackedBusFragment.trackedBusList.size() != 0) {
                                    TrackedBusFragment.mTrackedBusAdapter.notifyDataSetChanged();
                                } else {
                                    Log.e(TAG, "size is o");
                                }

                            }
                        });

                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }

                }
            }
        }
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Firebase Push Notification")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    private void unsubscribeAlarm(String routeID, String stopID, String token){
        ThrifaServer server =(ThrifaServer) Server.getInstance(context);
        Call<Void> call = server.unsubscribeBusAlarm(routeID,stopID, token);
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
