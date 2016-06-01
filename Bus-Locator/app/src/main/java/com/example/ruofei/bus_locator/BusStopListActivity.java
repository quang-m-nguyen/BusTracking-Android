package com.example.ruofei.bus_locator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class BusStopListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Choose Route");
        setContentView(R.layout.activity_bus_stop_list);

        if(savedInstanceState == null){

            Log.e("test retro", "activ start");
            BusStopListFragment newFragment = new BusStopListFragment();

            //Check Route Name
            Intent intent = getIntent();
            String message = intent.getStringExtra(RoutesListFragment.EXTRA_MESSAGE);
            if(message != null) {
                Bundle bundle = new Bundle();
                bundle.putString("RouteName",message);
                newFragment.setArguments(bundle);
            }
            Log.e("test retro act meesage", message);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.busStopContainer, newFragment, "busStopList")
                    .commit();
        }
    }
}
