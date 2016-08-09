package com.thrifa.ruofei.bus_locator.busstop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.thrifa.ruofei.bus_locator.R;
import com.thrifa.ruofei.bus_locator.routes.RoutesListFragment;

public class BusStopListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Choose Route");
        setContentView(R.layout.activity_bus_stop_list);

        if(savedInstanceState == null){

            BusStopListFragment newFragment = new BusStopListFragment();

            //Check Route Name
            Intent intent = getIntent();
            String message = intent.getStringExtra(RoutesListFragment.EXTRA_MESSAGE);
            if(message != null) {
                Bundle bundle = new Bundle();
                bundle.putString("RouteName",message);
                newFragment.setArguments(bundle);
            }
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.busStopContainer, newFragment, "busStopList")
                    .commit();
        }
    }
}
