package com.thrifa.ruofei.bus_locator.service;

import android.util.Log;

import com.thrifa.ruofei.bus_locator.R;
import com.thrifa.ruofei.bus_locator.util.SharedPreferenceUtils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by ruofei on 6/2/2016.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService{

    String TAG = this.getClass().getName();

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project

        SharedPreferenceUtils.getInstance(this).setString(getString(R.string.device_info_reference),
                getString(R.string.device_info_firebase_cloud_messaging_token),
                token);

//        Server server = Server.getInstance(this.getApplicationContext());
//        Call<Void> call = server.sendNotification(token);
//        Log.e(TAG,"send token");
//        call.request();

    }


}
