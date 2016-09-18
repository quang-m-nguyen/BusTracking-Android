package com.thrifa.ruofei.bus_locator.busstop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import com.thrifa.ruofei.bus_locator.R;
import com.thrifa.ruofei.bus_locator.BusTracker.TrackedBusFragment;
import com.thrifa.ruofei.bus_locator.util.Constants;
import com.google.firebase.iid.FirebaseInstanceId;

public class BusStopPopupActivity extends AppCompatActivity {

    final String TAG = this.getClass().getName();
    private String busstopID = "N/A";

    //TODO:using shared preference  to store this flag
    boolean notificationFlag = false;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        String token  = FirebaseInstanceId.getInstance().getToken();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_title);
        TextView textView = (TextView) findViewById(R.id.busstop_popup_title);

        Intent intent = getIntent();
        String busstopName = intent.getStringExtra(Constants.INTENT_EXTRA_BUS_STOP_NAME);

        //TODO: get ID INSTEAD
        busstopID = "99163"+busstopName;

        Log.d(TAG, "get busstop name");
        if (busstopName != null) {
            Log.d(TAG, busstopName);
            textView.setText(busstopName);
        }

        setContentView(R.layout.activity_bus_stop_popup);

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.BUSSTOP_ID_KEY, "99163"+busstopName);
            bundle.putString(Constants.DEVICE_TOKEN_KEY, token);
            TrackedBusFragment newFragment = new TrackedBusFragment();
            newFragment.busstopID = "99163" + busstopName;
            newFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.arrivingBusContainer, newFragment)
                    .commit();
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.BOTTOM | Gravity.CENTER;


        getWindow().setLayout(width, (int) (height * .6));


//        ImageView iw = (ImageView) findViewById(R.id.notificationImageView);
//        if (notificationFlag == false)
//            iw.setImageResource(R.drawable.ic_bell_outline_grey600_24dp);
//        else
//            iw.setImageResource(R.drawable.ic_bell_grey600_24dp);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
//
//        Server server = Server.getInstance(this.getApplicationContext());
//        Call<Void> call = server.unsubscribeBusstop(busstopID,token);
//        Log.e(TAG,"send token");
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//
//            }
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Log.e(TAG, "Fail:" + t.getMessage());
//                t.printStackTrace();
//            }
//        });
    }
}
