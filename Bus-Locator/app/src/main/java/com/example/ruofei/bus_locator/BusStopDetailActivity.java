package com.example.ruofei.bus_locator;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

public class BusStopDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setContentView(R.layout.activity_bus_stop_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.BOTTOM | Gravity.CENTER;

        getWindow().setLayout(width,(int)(height*.6));
    }
}
