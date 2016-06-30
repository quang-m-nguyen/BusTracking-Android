package com.example.ruofei.bus_locator;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ruofei.bus_locator.busstop.BusStopPopupActivity;
import com.example.ruofei.bus_locator.pojo.BusStop;
import com.example.ruofei.bus_locator.pojo.GoogleMapDirection;
import com.example.ruofei.bus_locator.pojo.RouteInfo;
import com.example.ruofei.bus_locator.routes.RouteListActivity;
import com.example.ruofei.bus_locator.routes.RoutesAdapter;
import com.example.ruofei.bus_locator.util.Constants;
import com.example.ruofei.bus_locator.util.Server;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainTabFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    final String TAG = this.getClass().getName();
    static public String mCurrentRoute = "Unknown";
    //    static public double busLat = -1;
//    static public double busLng = -1;
    GoogleMap mMap;

    static public List<BusStop> mBusStops = new ArrayList<BusStop>();
    private List<List<LatLng>> mRoutes = new ArrayList<>();

    public MainTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View inflate = inflater.inflate(R.layout.fragment_main_tab, container, false);
        SupportMapFragment mapFragment =
                (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return inflate;
    }


    @Override
    public void onResume() {
        super.onResume();
        Intent intent = this.getActivity().getIntent();
        if (intent != null) {
            try {
                String callFrom = intent.getStringExtra(Constants.INTENT_CALL_FROM_KEY);
                String routeName = intent.getStringExtra(Constants.ROUTE_NAME_KEY);
                mCurrentRoute = routeName;
                if (callFrom.equals(RoutesAdapter.class.getName())) {
                    Server server = Server.getInstance(this.getContext());
                    Call<List<BusStop>> call = server.getBusStopsCall(routeName);
                    call.enqueue(new Callback<List<BusStop>>() {
                        @Override
                        public void onResponse(Call<List<BusStop>> call, Response<List<BusStop>> response) {
                            if (response != null) {
                                if (!response.body().equals(mBusStops)) {
                                    mBusStops = response.body();

                                    //create bus stop latlng list request route polyline
                                    if (mBusStops.size() > 1) {
                                        List<String> latLngList = new ArrayList<String>();
                                        mRoutes.clear();
                                        for (int i = 0; i < mBusStops.size(); i++) {
                                            latLngList.clear();
                                            if (i < mBusStops.size() - 1) {
                                                String ori = mBusStops.get(i).getLatitude() + "," + mBusStops.get(i).getLongtitude();
                                                String dest = mBusStops.get(i + 1).getLatitude() + "," + mBusStops.get(i + 1).getLongtitude();
                                                requestRoute(ori, dest);
//                                                requestRoute(latLngList);
                                            } else if (i == mBusStops.size() - 1) {
                                                latLngList.add(mBusStops.get(i).getLatitude() + "," + mBusStops.get(i).getLongtitude());
                                                latLngList.add(mBusStops.get(0).getLatitude() + "," + mBusStops.get(0).getLongtitude());
                                            }
                                        }
                                    }
                                    updateMap();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<BusStop>> call, Throwable t) {
                        }
                    });
                }
                TextView textView = (TextView) this.getActivity().findViewById(R.id.routeName);
                textView.setTextSize(20);
                textView.setText(mCurrentRoute);
            } catch (Exception e) {

            }
        }
    }

    public void changeRoute(View view) {
        Intent intent = new Intent(this.getContext(), RouteListActivity.class);
        startActivity(intent);
    }

    public void updateMap() {
        if (mMap != null) {
            List<Marker> markers = new ArrayList<Marker>();
            Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(),
                    R.drawable.bus_stop_icon);
            for (int i = 0; i < mBusStops.size(); i++) {
                BusStop busStop = mBusStops.get(i);
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(
                                new LatLng(busStop.getLatitude(),
                                        busStop.getLongtitude()
                                )).title(busStop.getStopName())
                        .snippet(Integer.toString(busStop.getStopNum())));
                markers.add(marker);
            }
//                if(busLat != -1 && busLng != -1){
//                     Marker marker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(icon))
//                            .position(
//                                    new LatLng(busLat,
//                                            busLng
//                                    )).title("Bus I Location")
//                            .snippet("lat:" + busLat + ",lng:"+ busLng));
//                    markers.add(marker);
//                }
            setMapMarker(mMap, markers);
        }
        for (int i = 0; i < mRoutes.size(); i++) {
            PolylineOptions newOpt = new PolylineOptions();
            newOpt.addAll(mRoutes.get(i))
                    .width(12)
                    .color(Color.parseColor("#05b1fb"))//Google maps blue color
                    .geodesic(true);
            mMap.addPolyline(newOpt);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this.getContext(), "Click busstop num:" + marker.getSnippet(),
                Toast.LENGTH_SHORT).show();
        // pop up a window
        Intent busStopPopUp = new Intent(this.getContext(), BusStopPopupActivity.class);
        busStopPopUp.putExtra(Constants.INTENT_EXTRA_BUS_STOP_NAME, marker.getTitle());
        startActivity(busStopPopUp);

        SharedPreferences sharedPref = getContext().getSharedPreferences(Constants.DISIRED_BUS_PREFFERNCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.DISIRED_BUS_Key, marker.getSnippet());
        editor.commit();
    }

    public void showNotification(String title, String detail, int id) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this.getContext());
        mBuilder.setSmallIcon(R.drawable.common_full_open_on_phone);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(detail);

        mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

        //LED
        mBuilder.setLights(Color.RED, 3000, 3000);

//        //Ton
//        mBuilder.setSound(Uri.parse("uri://sadfasdfasdf.mp3"));

        Intent resultIntent = new Intent(this.getContext(), RouteListActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this.getContext());
        stackBuilder.addParentStack(RouteListActivity.class);

// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
        mNotificationManager.notify(id, mBuilder.build());
        Log.e(TAG, "Notification");
    }

    private void requestRoute(String ori, String dest) {
        Server server = Server.getInstance(this.getContext());
        Call<GoogleMapDirection> call = server.getRouteCall(ori, dest);
        call.enqueue(new Callback<GoogleMapDirection>() {
            @Override
            public void onResponse(Call<GoogleMapDirection> call, Response<GoogleMapDirection> response) {
                if (response != null) {
                    List<List<LatLng>> list = new ArrayList<>();
                    List<RouteInfo> routeInfos = response.body().getRoute();
                    for (int i = 0; i < routeInfos.size(); i++) {
                        list.add(decodePoly(routeInfos.get(i).getPolyline().getPoints()));
                    }
                    mRoutes.addAll(list);
                }
                if (mBusStops.size() <= mRoutes.size() + 1) {
                    updateMap();
                }
            }

            @Override
            public void onFailure(Call<GoogleMapDirection> call, Throwable t) {
                Log.e(TAG, "Fail:" + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    public void setMapMarker(GoogleMap googleMap, List<Marker> markers) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (markers.size() == 1) {
            Marker marker = markers.get(0);
            builder.include(marker.getPosition());
            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15F);
            googleMap.animateCamera(cu);
        } else if (markers.size() > 1) {
            for (int i = 0; i < markers.size(); i++) {
                builder.include(markers.get(i).getPosition());
            }

            LatLngBounds bounds = builder.build();
            int padding = 0; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            // No animate
            googleMap.moveCamera(cu);

            // Animate
//            googleMap.animateCamera(cu);
        }
    }

}
