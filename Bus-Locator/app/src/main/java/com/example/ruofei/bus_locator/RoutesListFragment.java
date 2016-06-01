package com.example.ruofei.bus_locator;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ruofei.bus_locator.util.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RoutesListFragment extends Fragment {

    static final String TAG = Constants.ROUTE_LIST_FRAGMENT_TAG;
    public final static String EXTRA_MESSAGE = "com.example.ruofei.bus_locator.E";
    private ArrayAdapter<String> mRouteAdapter;

    // Using ListView for test
    // TODO: update to recycler view
    private ListView recyclerView;

    public RoutesListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // Don't have dynamic bus stop update from server
        // TODO: dynamic update after server updated
        String[] data = {
                "E",
                "Route B",
                "Route C"
        };

        List<String> routes = new ArrayList<String>(Arrays.asList(data));

        // Create a adapter for busStops
        mRouteAdapter =
                new ArrayAdapter<String>(
                        getActivity(),
                        R.layout.basic_list_item,
                        R.id.basic_list_item_textview,
                        routes
                );

        View rootView = inflater.inflate(R.layout.fragment_routes_list, container, false);

        recyclerView = (ListView) rootView.findViewById(R.id.recyclerview_route);
        recyclerView.setAdapter(mRouteAdapter);

        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent =  new Intent(getActivity(), BusStopListActivity.class);
                Intent intent =  new Intent(getActivity(), MainActivity.class);
                String routeName = recyclerView.getItemAtPosition(position).toString();
                MainActivity.mCurrentRoute = routeName;
                intent.putExtra(Constants.ROUTE_NAME_KEY,routeName);
                intent.putExtra(Constants.INTENT_CALL_FROM_KEY, TAG);
                startActivity(intent);
            }
        });

        return  rootView;
    }



}
