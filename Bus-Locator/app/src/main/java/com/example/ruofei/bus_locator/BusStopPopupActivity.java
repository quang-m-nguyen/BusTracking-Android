package com.example.ruofei.bus_locator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ruofei.bus_locator.util.Constants;

public class BusStopPopupActivity extends AppCompatActivity {

    final String TAG = this.getClass().getName();

    //TODO:using shared preference  to store this flag
    boolean notificationFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.BOTTOM | Gravity.CENTER;

        getWindow().setLayout(width, (int) (height * .6));


        ImageView iw = (ImageView) findViewById(R.id.notificationImageView);
        if (notificationFlag = false)
            iw.setImageResource(R.drawable.ic_bell_outline_grey600_24dp);
        else
            iw.setImageResource(R.drawable.ic_bell_grey600_24dp);
    }


    public void onClickNotification(View view) {

        ImageView iw = (ImageView) findViewById(R.id.notificationImageView);
        notificationFlag = !notificationFlag;
        Log.e(TAG, "Notification clicked, flag:" + notificationFlag);
        if (notificationFlag ==false)
            iw.setImageResource(R.drawable.ic_bell_outline_grey600_24dp);
        else
            iw.setImageResource(R.drawable.ic_bell_grey600_24dp);
        iw.invalidate();
    }

    public void onClickDetail(View view) {

    }
}
