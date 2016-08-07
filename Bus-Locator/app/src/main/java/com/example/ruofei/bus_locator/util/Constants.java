package com.example.ruofei.bus_locator.util;

/**
 * Created by ruofei on 5/27/2016.
 */
public interface Constants {
    final static String INTENT_CALL_FROM_KEY = "com.example.ruofei.bus_locator.CALL_FROM";
    final static String ROUTE_NAME_KEY = "com.example.ruofei.bus_locator.ROUTE_NAME";
    final static String GOOGLE_MAP_API_KEY = "AIzaSyDCDYoiyBgsn8Imhscv417hwyW5sErNaKc";


    final static String BUS_LOCATOR_URL = "http://sudokit.com:3000/";
    final static String GOOGLE_MAP_URL = "https://maps.googleapis.com/maps/api/directions/";
    final static String FIRE_BASE_NOTIFICATION_URL = "http://sudokit.com:3000/";

    final static String DISIRED_BUS_PREFFERNCE = "disiredBusPreference";
    final static String DISIRED_BUS_Key = "disiredBusKey";
    final static String DISIRED_BUS_ID = "disiredBusId";

    final static String BUSSTOP_ID_KEY = "busstopIDKey";
    final static String ROUTE_ID_KEY = "routeIDKey";
    final static String TOKEN_ID_KEY = "tokenIDKey";


    final static String CURRENT_REMAINNING_TIME_KEY = "currentRemainingTime";


    final static String CURRENT_SELECTED_BUSSTOP = "currentSelectedBusstopPreference";

    final static String DEVICE_INFO_PREFFERNCE = "deviceInfo";
    final static String DEVICE_TOKEN_KEY = "deviceTokenKey";

    final static String INTENT_EXTRA_BUS_STOP_NAME = "com.example.ruofei.bus_locator.INTENT_EXTRA_BUS_STOP_NAME";


    final static String BROADCAST_NEW_BUS_REMAINING_TIME = "android.intent.action.BROADCAST_NEW_BUS_REMAINING_TIME";
    final static String BUS_REMAINING_TIME = "com.example.ruofei.bus_locator.BUS_REMAINING_TIME";

    final static String MAIN_ACTION = "android.intent.action.MAIN";

    public interface AlarmList{
       final static String ID = "alarmListID";
        final static String ALARM_SETTING_TIME = "alarmSettingTime";
        final static String REMAINING_TIME = "remainingTime";
        final static String BUS_ROUTE = "busRoute";
        final static String BUSSTOP = "busstop";
    }

}
