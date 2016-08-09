package com.example.ruofei.bus_locator;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ruofei.bus_locator.busstop.BusStopPopupActivity;
import com.example.ruofei.bus_locator.pojo.BusStop;
import com.example.ruofei.bus_locator.pojo.GoogleMapDirection;
import com.example.ruofei.bus_locator.pojo.RouteInfo;
import com.example.ruofei.bus_locator.routes.RouteListActivity;
import com.example.ruofei.bus_locator.routes.RoutesAdapter;
import com.example.ruofei.bus_locator.util.Constants;
import com.example.ruofei.bus_locator.util.PermissionUtils;
import com.example.ruofei.bus_locator.util.Server;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
//import com.mypopsy.drawable.SearchArrowDrawable;
//import com.mypopsy.drawable.ToggleDrawable;
//import com.mypopsy.drawable.model.CrossModel;
//import com.mypopsy.drawable.util.Bezier;
//import com.mypopsy.widget.FloatingSearchView;
//import com.mypopsy.widget.internal.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainTabFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, ActivityCompat.OnRequestPermissionsResultCallback {
    final String TAG = this.getClass().getName();
    static public String mCurrentRoute = "N/A";
    private Context context;
    //    static public double busLat = -1;
//    static public double busLng = -1;
    GoogleMap mMap;


    static public List<BusStop> mBusStops = new ArrayList<BusStop>();
    private List<List<LatLng>> mRoutes = new ArrayList<>();

//    private FloatingSearchView mSearchView;
    //    private SearchAdapter mAdapter;
    private static View view;

    private BroadcastReceiver mReceiver;
    private double busLat;
    private double busLng;
    private boolean updateBusMarkerFlag = false;


    public MainTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        Log.d(TAG, "onCreate set option menu to true");
        setHasOptionsMenu(true);


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_main_tab, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }


        Button clickButton = (Button) view.findViewById(R.id.get_route_button_on_map);
//        clickButton.setBackground(ContextCompat.getDrawable(context, R.drawable.places_ic_search));
        clickButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RouteListActivity.class);
                startActivity(intent);
            }
        });

        SupportMapFragment mapFragment =
                (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        View locationButton = ((View) view.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//        rlp.setMargins(0, 0, 30, 105);
        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mReceiver != null)
            getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public void onResume() {

        super.onResume();
        Log.e(TAG, "resume receiver");
        IntentFilter intentFilter = new IntentFilter(
                Constants.MAIN_ACTION);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //extract our message from intent
                busLat = Double.parseDouble(intent.getStringExtra("BUS_LAT"));
                busLng = Double.parseDouble(intent.getStringExtra("BUS_LNG"));
                //log our message value
                Log.e("receiver", "latlng:" + busLat + busLng);

                updateBusMarkerFlag = true;
                updateMap();
            }
        };
        //registering our receiver
        getActivity().registerReceiver(mReceiver, intentFilter);

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
//                                    if (mBusStops.size() > 1) {
//                                        List<String> latLngList = new ArrayList<String>();
//                                        mRoutes.clear();
//                                        for (int i = 0; i < mBusStops.size(); i++) {
//                                            latLngList.clear();
//                                            if (i < mBusStops.size() - 1) {
//                                                String ori = mBusStops.get(i).getLatitude() + "," + mBusStops.get(i).getLongtitude();
//                                                String dest = mBusStops.get(i + 1).getLatitude() + "," + mBusStops.get(i + 1).getLongtitude();
//                                                requestRoute(ori, dest);
////                                                requestRoute(latLngList);
//                                            } else if (i == mBusStops.size() - 1) {
//                                                latLngList.add(mBusStops.get(i).getLatitude() + "," + mBusStops.get(i).getLongtitude());
//                                                latLngList.add(mBusStops.get(0).getLatitude() + "," + mBusStops.get(0).getLongtitude());
//                                            }
//                                        }
//                                    }
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


    public void updateMap() {
        if (mMap != null) {
            List<Marker> markers = new ArrayList<Marker>();
            Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(),
                    R.drawable.bus_stop_icon);

            Bitmap bmp = Bitmap.createBitmap(50,50,Bitmap.Config.ARGB_8888);
            bmp.eraseColor(Color.argb(0,0,0,0));

            Canvas c = new Canvas(bmp);
            c.drawColor(0, PorterDuff.Mode.CLEAR);

            Paint p = new Paint();

            p.setColor(ContextCompat.getColor(context, R.color.colorAccent));
            c.drawCircle(bmp.getHeight()/2, bmp.getWidth()/2 , bmp.getHeight()/2, p);

            p.setColor(ContextCompat.getColor(context, R.color.materialColorRed));
            c.drawCircle(bmp.getHeight()/2, bmp.getWidth()/2 , bmp.getHeight()/2 -2, p);

            for (int i = 0; i < mBusStops.size(); i++) {
                BusStop busStop = mBusStops.get(i);

                double radiusInMeters = 100.0;
                //red outline
                int strokeColor = 0xffff0000;
                //opaque red fill
                int shadeColor = 0x44ff0000;

                LatLng position =new LatLng(busStop.getLatitude(),
                        busStop.getLongtitude()
                );
//
//                CircleOptions circleOptions = new CircleOptions().center(position).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(2);
//                mMap.addCircle(circleOptions);
//                MarkerOptions markerOptions = new MarkerOptions().position(position);
//                mMap.addMarker(markerOptions);


                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(
                                new LatLng(busStop.getLatitude(),
                                        busStop.getLongtitude()
                                )).title(busStop.getStopName())
                        .snippet(Integer.toString(busStop.getStopNum()) + " click for detail")
                        .icon(BitmapDescriptorFactory.fromBitmap(bmp)));
                markers.add(marker);
            }
            if (updateBusMarkerFlag) {
                Marker marker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(icon))
                        .position(
                                new LatLng(busLat,
                                        busLng
                                )).title("Bus I Location")
                        .snippet("lat:" + busLat + ",lng:" + busLng));
                markers.add(marker);
            }
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

        mMap.setPadding(0, 0, 30, 180);

        enableMyLocation();
        Criteria criteria = new Criteria();
        LocationManager locationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            enableMyLocation();
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
//        googleMap.addMarker(new MarkerOptions().position(latLng));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }

//
//        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            mMap.setMyLocationEnabled(true);
//        } else {
//            // Show rationale and request permission.
//                    Toast.makeText(this.getContext(), "Dont have the permission to access current location",
//                Toast.LENGTH_SHORT).show();
//        }



    }

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission((AppCompatActivity) this.getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    private boolean mPermissionDenied = false;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.

            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this.getContext(), "Click busstop num:" + marker.getSnippet(),
                Toast.LENGTH_SHORT).show();
        // pop up a window
        Intent busStopPopUp = new Intent(this.getContext(), BusStopPopupActivity.class);
        busStopPopUp.putExtra(Constants.INTENT_EXTRA_BUS_STOP_NAME, marker.getTitle());
        startActivity(busStopPopUp);

        // TODO:fix this part
        SharedPreferences sharedPref = getContext().getSharedPreferences(Constants.DISIRED_BUS_PREFFERNCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.DISIRED_BUS_Key, marker.getSnippet());
        editor.putString(getString(R.string.currenct_selected_busstop_key), "99163" + marker.getTitle());
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
        Log.d(TAG, "Notification");
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
                Log.d(TAG, "Fail:" + t.getMessage());
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
