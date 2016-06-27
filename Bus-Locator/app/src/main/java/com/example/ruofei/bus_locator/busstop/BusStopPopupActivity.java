package com.example.ruofei.bus_locator.busstop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ruofei.bus_locator.MainActivity;
import com.example.ruofei.bus_locator.R;
import com.example.ruofei.bus_locator.TrackedBusFragment;
import com.example.ruofei.bus_locator.util.Constants;
import com.example.ruofei.bus_locator.util.Server;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusStopPopupActivity extends AppCompatActivity {

    final String TAG = this.getClass().getName();

    //TODO:using shared preference  to store this flag
    boolean notificationFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.e(TAG, "start create");
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_title);
        TextView textView = (TextView) findViewById(R.id.busstop_popup_title);

        Intent intent = getIntent();
        String busstopName = intent.getStringExtra(Constants.INTENT_EXTRA_BUS_STOP_NAME);
        Log.e(TAG, "get busstop name");
        if (busstopName != null) {
            Log.e(TAG, busstopName);
            textView.setText(busstopName);
        }

//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);

        setContentView(R.layout.activity_bus_stop_popup);

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.BUSSTOP_ID_KEY, "99163"+busstopName);
            TrackedBusFragment newFragment = new TrackedBusFragment();
            newFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.arrivingBusContainer, newFragment)
                    .commit();
            Log.e(TAG, "CREATE LIST");
        }



        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.BOTTOM | Gravity.CENTER;

        getWindow().setLayout(width, (int) (height * .6));


        ImageView iw = (ImageView) findViewById(R.id.notificationImageView);
        if (notificationFlag == false)
            iw.setImageResource(R.drawable.ic_bell_outline_grey600_24dp);
        else
            iw.setImageResource(R.drawable.ic_bell_grey600_24dp);





    }


    public void onClickNotification(View view) {

        ImageView iw = (ImageView) findViewById(R.id.notificationImageView);
        Log.e(TAG, "Notification clicked, flag:" + notificationFlag);
        if (notificationFlag == false) {
            iw.setImageResource(R.drawable.ic_bell_outline_grey600_24dp);

            String token  = FirebaseInstanceId.getInstance().getToken();
            //send notification request
            Server server = Server.getInstance(this.getApplicationContext());
            // TODO: change route id and bus stop id to be dynamic
            Call<Void> call = server.sendNotification(token, 1, 41);
            Log.e(TAG, "send token");
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
        } else {
            final Vibrator vibrator;
            vibrator = (Vibrator) getSystemService(MainActivity.VIBRATOR_SERVICE);
            vibrator.cancel();
            iw.setImageResource(R.drawable.ic_bell_grey600_24dp);
        }
        notificationFlag = !notificationFlag;
        iw.invalidate();
    }

    public void onClickDetail(View view) {

    }
}
