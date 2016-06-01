package com.example.ruofei.bus_locator;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ruofei.bus_locator.pojo.BusStop;
import com.example.ruofei.bus_locator.pojo.GoogleMapDirection;
import com.example.ruofei.bus_locator.service.BusStatusUpdateService;
import com.example.ruofei.bus_locator.util.Constants;
import com.example.ruofei.bus_locator.util.Server;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    static final String TAG = "MAIN_ACTIVITY";
    static public String mCurrentRoute = "Unknown";
    //    static public double mCurrentLatitude = 46.73241830;
//    static public double mCurrentLongitude = -117.1658558;
//    static public String mBusStopMakerTitle = "Marker";
    GoogleMap mMap;
    int disiredBusStopId;

    public enum MapDisplayType {
        DISPLAY_BUSSTOP,
        DISPLAY_ROUTE,
        DISPLAY_NONE
    }

    static public MapDisplayType mMapDisplayType = MapDisplayType.DISPLAY_NONE;

    static public List<BusStop> mBusStops = new ArrayList<BusStop>();
    private List<List<LatLng>> mRoutes = new ArrayList<>();
    private List<String> mBustStopLatLngStr = new ArrayList<>();

    private int mRouteRequestCounter = 0;
    private boolean mUpdateMarkerFlag = false;
    private boolean mUpdateRouteFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapFragment mapFragment =
                (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

        return super.onCreateView(parent, name, context, attrs);
    }

    public void changeRoute(View view) {
        Intent intent = new Intent(this, RouteListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Intent intent = getIntent();
        if (intent != null) {
            try {
                String callFrom = intent.getStringExtra(Constants.INTENT_CALL_FROM_KEY);
                String routeName = intent.getStringExtra(Constants.ROUTE_NAME_KEY);

                if (callFrom.equals(Constants.ROUTE_LIST_FRAGMENT_TAG)) {
                    Server server = Server.getInstance(this.getApplicationContext());
                    Call<List<BusStop>> call = server.getBusStopsCall(routeName) ;
                    call.enqueue(new Callback<List<BusStop>>() {
                        @Override
                        public void onResponse(Call<List<BusStop>> call, Response<List<BusStop>> response) {
                            //TODO: error handling
                            if (response != null) {
                                if (!response.body().equals(mBusStops)) {
                                    mBusStops = response.body();
                                    mUpdateMarkerFlag = true;

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
//                                                latLngList.add(mBusStops.get(i).getLatitude() + "," + mBusStops.get(i).getLongtitude());
//                                                latLngList.add(mBusStops.get(i + 1).getLatitude() + "," + mBusStops.get(i + 1).getLongtitude());
//                                                requestRoute(latLngList);
                                            } else if (i == mBusStops.size() - 1) {
//                                                latLngList.add(mBusStops.get(i).getLatitude() + "," + mBusStops.get(i).getLongtitude());
//                                                latLngList.add(mBusStops.get(0).getLatitude() + "," + mBusStops.get(0).getLongtitude());
                                            }
                                        }
                                        mBustStopLatLngStr = latLngList;
                                    }
//                                    if (mBusStops.size() == 1) {
                                    updateMap();
//                                    }
                                } else {
                                    mUpdateMarkerFlag = false;
                                    return;
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<BusStop>> call, Throwable t) {

                        }
                    });

                } else if (callFrom.equals(Constants.BUSSTOP_LIST_FRAGMENT_TAG)) {
//                    Log.e(TAG, "POSTRESUME BUST STOP LIST");
                    TextView textView = (TextView) findViewById(R.id.routeName);
                    textView.setTextSize(20);
                    textView.setText(mCurrentRoute);
                }
            } catch (Exception e) {

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void updateMap() {
        if (mMap != null) {
            if (mUpdateMarkerFlag == true) {
                mUpdateRouteFlag = false;
                mUpdateRouteFlag = true;
                List<Marker> markers = new ArrayList<Marker>();
                for (int i = 0; i < mBusStops.size(); i++) {
                    BusStop busStop = mBusStops.get(i);
                    Marker marker = mMap.addMarker(new MarkerOptions().position(
                            new LatLng(busStop.getLatitude(),
                                    busStop.getLongtitude()
                            )).title(busStop.getStopName()).snippet(Integer.toString(busStop.getStopNum())));
                    markers.add(marker);
                }
                setMapMarker(mMap, markers);
            }
            mUpdateRouteFlag = true;
            if (mUpdateRouteFlag) {
                mUpdateRouteFlag = false;
                Log.e(TAG, "Draw route");

                for (int i = 0; i < mRoutes.size(); i++) {
                    PolylineOptions newOpt = new PolylineOptions();
                    newOpt.addAll(mRoutes.get(i))
                            .width(12)
                            .color(Color.parseColor("#05b1fb"))//Google maps blue color
                            .geodesic(true);
                    Polyline line = mMap.addPolyline(newOpt);
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Click busstop num:" + marker.getSnippet(),
                Toast.LENGTH_SHORT).show();
        disiredBusStopId = Integer.parseInt(marker.getSnippet());


        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(Constants.DISIRED_BUS_PREFFERNCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.putString(Constants.DISIRED_BUS_ID, marker.getSnippet());
        editor.commit();

        startService(new Intent(this, BusStatusUpdateService.class));

        showNotification("marker clicked", "detail", 0);
    }

    public void showNotification(String title,String detail, int id) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.common_full_open_on_phone);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(detail);

        mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

        //LED
        mBuilder.setLights(Color.RED, 3000, 3000);

//        //Ton
//        mBuilder.setSound(Uri.parse("uri://sadfasdfasdf.mp3"));

        Intent resultIntent = new Intent(this, RouteListActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(RouteListActivity.class);

// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
        mNotificationManager.notify(id, mBuilder.build());
        Log.e(TAG, "Notification");
    }

    //    private void requestRoute(List<String> waypoints) {
    private void requestRoute(String ori, String dest) {
//                    Server server = Server.getInstance(this.getApplicationContext());
//                    server.buildRetrofit(Constants.GOOGLE_MAP_URL);
//                    server.setApi(GoogleMapApi.class);
//                    GoogleMapApi service = (GoogleMapApi) server.getService();
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//        httpClient.addInterceptor(logging);
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Constants.GOOGLE_MAP_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(httpClient.build())
//                .build();
//        GoogleMapApi service = retrofit.create(GoogleMapApi.class);
////        String oriLatLng = waypoints.get(0);
//        String oriLatLng = ori;
////        String destLatLng = waypoints.get(waypoints.size() - 1);
//        String destLatLng = dest;
//
//        String waypointsStr = "";
//        if (waypoints.size() >= 3) {
//            waypointsStr = waypoints.get(1);
//            for (int i = 2; i < 22; i++) {
//                waypointsStr += ("|" + waypoints.get(i));
//            }
//        }
        Server server = Server.getInstance(this.getApplicationContext());
        Call<GoogleMapDirection> call = server.getRouteCall(ori,dest);

//        Call<GoogleMapDirection> call = service.getRoutePath(oriLatLng, destLatLng, false, "driving", false, Constants.GOOGLE_MAP_API_KEY);
        call.enqueue(new Callback<GoogleMapDirection>() {
            @Override
            public void onResponse(Call<GoogleMapDirection> call, Response<GoogleMapDirection> response) {
//                Log.e(TAG, "Route Response:");
                if (response != null) {
//                    Log.e(TAG, "Route body:" + response.body());
                    List<List<LatLng>> list = new ArrayList<>();

                    List<com.example.ruofei.bus_locator.pojo.Route> routes = response.body().getRoute();
                    for (int i = 0; i < routes.size(); i++) {
                        list.add(decodePoly(routes.get(i).getPolyline().getPoints()));
                    }
//                    for (int i = 0; i < list.size(); i++) {
//                        Log.e(TAG, "polyline:" + list.get(0));
//                    }
//                    Log.e(TAG, "Add new Polyline:" + list.get(0));
//                    mRoutes.addAll(list);
                    mRoutes.addAll(list);
                }
                Log.e(TAG, "size1:" +mBusStops.size()+" size2:" +mRoutes.size());
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
//        Log.e(TAG, "Start decodePoly");
//        Log.e(TAG, "encoded string:" + encoded);

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
//                Log.e(TAG, "multiple marker position:" + marker.getPosition().toString());
                builder.include(markers.get(i).getPosition());
            }

            LatLngBounds bounds = builder.build();
            int padding = 0; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            googleMap.animateCamera(cu);
        }
//        Log.e(TAG, "Error");

    }
}
