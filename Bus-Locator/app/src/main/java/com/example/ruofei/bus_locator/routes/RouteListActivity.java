package com.example.ruofei.bus_locator.routes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.ruofei.bus_locator.R;

public class RouteListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Choose Route");
        setContentView(R.layout.activity_route_list);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.routeContainer, new RoutesListFragment())
                    .commit();
        }
    }
}
