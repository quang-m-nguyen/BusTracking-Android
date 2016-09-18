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

import java.text.DecimalFormat;
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
    }

    private void updateAlarmTime(Map<String, String> data) {
        //UpdateAlarmTime
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.alarm_preference_key), Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPref.edit();
        //Check if the user set up an alarm
        String flag = sharedPref.getString(context.getString(R.string.alarm_flag_key), "false");
        if (flag.equals("true")) {
            // Set current remaining time
            DecimalFormat df = new DecimalFormat("#.#");
            Double newRemainingTimeDouble = Double.parseDouble(data.get("remain_time"));
            newRemainingTimeDouble = Double.valueOf(df.format(newRemainingTimeDouble));
            Integer newRemainingTime = (int) (newRemainingTimeDouble * 60); // convert sec
            if (newRemainingTime != null) {
                // Set current remaining time
                // TODO: change server side to send time in sec
//                            editor.putInt(getString(R.string.current_remaining_time_key), newRemainingTime * 60);
                // TODO: notify alarm service the update

                String routeID = data.get("route_ID").substring(5);
                String stopID = data.get("busstop_ID").substring(5);

                try {
                    Handler mainThread = new Handler(Looper.getMainLooper());
                    // In your worker thread
                    int index = BusAlarmListFragment.busAlarmList.indexOf(new BusAlarmItem(routeID, stopID,"","", "n/a", "n/a", -1, -1.0, -1.0, true));
                    if (index == -1)
                        return;


                    final BusAlarmItem alarmItem = BusAlarmListFragment.busAlarmList.get(index);
                    //check if alarm off
                    if(alarmItem.isAlarmFlag()) {
                    alarmItem.setRemainingTime("Arrive in:" + newRemainingTimeDouble.toString() + " Mins");
                    alarmItem.setRemainTimeNum(newRemainingTimeDouble);
//                        BusAlarmListFragment.busAlarmList.get(index).setAlarmSettingTime("Setting Time:" + );

                    if (alarmItem.getSettingTimeNum() >= alarmItem.getRemainTimeNum() && alarmItem.getRemainTimeNum() >= 0) {
                        sendNotification("Bus is about to arriving in " + Math.round(alarmItem.getRemainTimeNum()) + "minutes", "Bus About Arrive");

                        // unsubscribe
                        alarmItem.setAlarmFlag(false);
//                        alarmItem.setRemainingTime("Arriving in" + alarmItem.getRemainTimeNum() + " Min");
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
        String busLat = data.get("lat");
        String busLng = data.get("lng");
        if (busLat != null && busLng != null) {
//            MainActivity.busLng = Double.parseDouble(busLng);
//            MainActivity.busLat = Double.parseDouble(busLat);

            if (busLat != null && busLng != null) {
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
                for (int i = 0; i < busList.length; i++) {
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
                                    Log.d(TAG, "bus list size is o");
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

    private void sendNotification(String messageBody,String title) {
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

    private void unsubscribeAlarm(String routeID, String stopID, String token){
        ThrifaServer server =(ThrifaServer) ThrifaServer.getInstance(context);
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
