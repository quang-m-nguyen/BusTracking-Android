package com.example.ruofei.bus_locator.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.ruofei.bus_locator.MainActivity;
import com.example.ruofei.bus_locator.R;
import com.example.ruofei.bus_locator.TrackedBus;
import com.example.ruofei.bus_locator.TrackedBusFragment;
import com.example.ruofei.bus_locator.pojo.BusTracker;
import com.example.ruofei.bus_locator.util.MyDeserializer;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruofei on 6/4/2016.
 */
public class FirebaseBusMessagingService extends FirebaseMessagingService {

    private final String TAG = this.getClass().getName();


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
//        Log.d(TAG, "From: " + remoteMessage.getFrom());
//        Log.e(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        //Calling method to generate notification
//        sendNotification(remoteMessage.getNotification().getBody());
//        Log.d(TAG, "Notifcation Body:" + remoteMessage.getNotification().toString());


        String dataStr = remoteMessage.getData().toString();
        Log.e(TAG, "get data from firebase " + dataStr);
        String busArray = remoteMessage.getData().get("busArray");

        String busLat = remoteMessage.getData().get("lat");
        String busLng = remoteMessage.getData().get("long");
        Log.e(TAG, "get broad cast lat" +busLat + ", lng:" + busLng);
        if(busLat != null && busLng != null) {
//            MainActivity.busLng = Double.parseDouble(busLng);
//            MainActivity.busLat = Double.parseDouble(busLat);

            if (busLat != null && busLng != null) {
                Log.e(TAG, "broad cast lat" +busLat + ", lng:" + busLng);
                Intent i = new Intent("android.intent.action.MAIN").putExtra("BUS_LAT", busLat).putExtra("BUS_LNG", busLng);
                this.sendBroadcast(i);
            }
        }
        if (busArray != null) {

            Gson gson =
                    new GsonBuilder()
                            .registerTypeAdapter(BusTracker[].class, new MyDeserializer<BusTracker[]>())
                            .create();
            BusTracker[] busList = gson.fromJson(busArray, BusTracker[].class);

            if (busList != null) {
                Log.e(TAG, "Bus List:" + busList.toString());
//
                for (int i = 0; i < busList.length; i++) {
                    Log.e(TAG, "index:" + i + ", content:" + busList[i].getRouteID());

                    String routeID = busList[i].getRouteID();
                    String time = busList[i].getTime();
                    String busstopNum = busList[i].getStopNum();
                    String busstopID = busList[i].getStopID();

                    try {
                        Handler mainThread = new Handler(Looper.getMainLooper());
                        // In your worker thread
                        int index = TrackedBusFragment.trackedBusList.indexOf(new TrackedBus(routeID, "n/a", "n/a"));
                        Log.e(TAG, "tracker index:" + index + ", tracker route:" + routeID + ", traker time:" + time + ", stopNum:" + busstopNum);
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
}
