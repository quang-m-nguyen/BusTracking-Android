package com.example.ruofei.bus_locator.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.example.ruofei.bus_locator.R;
import com.example.ruofei.bus_locator.RouteListActivity;

/**
 * Created by ruofei on 5/31/2016.
 */
public class BusStatusUpdateService extends Service {
    private static String TAG = BusStatusUpdateService.class.getSimpleName();
    private MyThread mythread;
    public boolean isRunning = false;

    private int testCounter = 0;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mythread  = new MyThread();
    }

    @Override
    public synchronized void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if(!isRunning){
            mythread.interrupt();
            mythread.stop();
        }
    }

    @Override
    public synchronized void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "onStart");
        if(!isRunning){
            mythread.start();
            isRunning = true;
        }
    }

    public void readWebPage(){
//        HttpClient client = new DefaultHttpClient();
//        HttpGet request = new HttpGet("http://google.com");
//        // Get the response
//        ResponseHandler<String> responseHandler = new BasicResponseHandler();
//        String response_str = null;
//Toast.makeText(this, "Service Update:" ,
//                Toast.LENGTH_SHORT).show();

        //showNotification("service:" + testCounter,"service", testCounter++);
        try {
//            response_str = client.execute(request, responseHandler);
//            if(!response_str.equalsIgnoreCase("")){
//                Log.d(TAG, "Got Response");
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     public void showNotification(String title,String detail, int id) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.common_full_open_on_phone);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(detail);

        mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

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

    class MyThread extends Thread{
        static final long DELAY = 3000;
        @Override
        public void run(){
            while(isRunning){
                Log.d(TAG,"Running");
                try {
                    readWebPage();
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    isRunning = false;
                    e.printStackTrace();
                }
            }
        }

    }
}
