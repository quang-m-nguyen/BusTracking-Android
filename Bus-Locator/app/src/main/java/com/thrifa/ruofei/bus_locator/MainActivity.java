package com.thrifa.ruofei.bus_locator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.thrifa.ruofei.bus_locator.BusAlarm.BusAlarmListFragment;
import com.thrifa.ruofei.bus_locator.customWidget.NonSwipeableViewPager;
import com.thrifa.ruofei.bus_locator.service.AlarmService;
import com.thrifa.ruofei.bus_locator.util.Constants;
import com.thrifa.ruofei.bus_locator.util.Parameters;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    final String TAG = this.getClass().getName();
//    static public String mCurrentRoute = "Unknown";
////    static public double busLat = -1;
////    static public double busLng = -1;
//    GoogleMap mMap;
//
//    static public List<BusStop> mBusStops = new ArrayList<BusStop>();
//    private List<List<LatLng>> mRoutes = new ArrayList<>();

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private NonSwipeableViewPager viewPager;

    private int[] tabIcons = {
            R.drawable.ic_home_white_48dp,
            R.drawable.ic_alarm_white_48dp,
            R.drawable.ic_information_outline_white_48dp
    };

    private void initialize() {
//         check build type
        if (BuildConfig.DEBUG) {
        Parameters.CURRENT_SERVER_PRODUCT_DOMAIN = Constants.THRIFA_TEST_PRODUCT_DOMAIN;
        } else {
            Parameters.CURRENT_SERVER_PRODUCT_DOMAIN = Constants.THRIFA_PRODUCT_DOMAIN;
        }

        Intent intent = new Intent(this, AlarmService.class);
        startService(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

//        FirebaseCrash.report(new Exception("My first Android non-fatal error"));

        viewPager = (NonSwipeableViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();


//        if (savedInstanceState == null) {
//            Bundle bundle = new Bundle();
//            TrackedBusFragment newFragment = new TrackedBusFragment();
//            newFragment.setArguments(bundle);
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.busstop_info_in_main, newFragment)
//                    .commit();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        Intent intent = this.getIntent();
//        if (intent != null) {
//            try {
//
//                String callFrom = intent.getStringExtra(Constants.INTENT_CALL_FROM_KEY);
//                if (callFrom.equals(SetBusAlarmFragment.class.getName())) {
//                    viewPager.setCurrentItem(1);
//                }
//            }catch (Exception e){
//
//            }
//        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MainTabFragment(), "Home");
        adapter.addFragment(new BusAlarmListFragment(), "Alarm");
        adapter.addFragment(new AboutTabFragment(), "About");
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);

//        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
//        tabOne.setText("Home");
//        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.bell, 0, 0);
//        tabLayout.getTabAt(0).setCustomView(tabOne);
//
//        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
//        tabTwo.setText("Alarm");
//        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.alarm, 0, 0);
//        tabLayout.getTabAt(1).setCustomView(tabTwo);
//
//        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
//        tabThree.setText("About");
//        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.bus_stop_icon, 0, 0);
//        tabLayout.getTabAt(2).setCustomView(tabThree);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}

