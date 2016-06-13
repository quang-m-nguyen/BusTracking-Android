package com.example.ruofei.bus_locator.routes;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ruofei.bus_locator.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RoutesListFragment extends Fragment {

    public final String TAG = this.getClass().getName();
    public final static String EXTRA_MESSAGE = "com.example.ruofei.bus_locator.E";
    private RoutesAdapter mRouteAdapter;
    List<Route> routeList = new ArrayList<>();

    // Using ListView for test
    // TODO: update to recycler view
    private RecyclerView recyclerView;

    public RoutesListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // Don't have dynamic bus stop update from server
        // TODO: dynamic update after server updated

        View rootView = inflater.inflate(R.layout.fragment_routes_list, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_route);
        mRouteAdapter = new RoutesAdapter(routeList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mRouteAdapter);




                routeList.add(new Route("1", "E"));
                routeList.add(new Route("2", "I"));
                routeList.add(new Route("3", "North"));


//        recyclerView.setAdapter(mRouteAdapter);

//        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                Intent intent =  new Intent(getActivity(), BusStopListActivity.class);
//                Intent intent =  new Intent(getActivity(), MainActivity.class);
//                String routeName = recyclerView.getItemAtPosition(position).toString();
//                MainActivity.mCurrentRoute = routeName;
//                intent.putExtra(Constants.ROUTE_NAME_KEY,routeName);
//                intent.putExtra(Constants.INTENT_CALL_FROM_KEY, TAG);
//                startActivity(intent);
//            }
//        });

                return rootView;
            }


        }
