package com.example.ruofei.bus_locator.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.example.ruofei.bus_locator.R;
import com.example.ruofei.bus_locator.RouteListActivity;
import com.example.ruofei.bus_locator.api.BusLocatorApi;
import com.example.ruofei.bus_locator.util.Constants;
import com.example.ruofei.bus_locator.util.Server;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        Server server = Server.getInstance(this.getApplicationContext());
        server.buildRetrofit(Constants.BUS_LOCATOR_URL);
        server.setApi(BusLocatorApi.class);
        BusLocatorApi service = (BusLocatorApi)server.getService();
        Call<String> call = service.getBusLocationIndicator();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null) {
                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(Constants.DISIRED_BUS_PREFFERNCE, Context.MODE_PRIVATE);
                    String defaultValue = getResources().getString(R.string.disired_bus_default);
                    String desiredBusLocation = sharedPref.getString(getString(R.string.disired_bus_key), defaultValue);
                    String updateLocation =  response.body();
                    Log.e(TAG, "response " + updateLocation + ", preference:" + desiredBusLocation);
                    if(updateLocation.equals(desiredBusLocation))
                    {
                        showNotification("Bus is coming at " + updateLocation, "Bus is comming", 3);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });






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
