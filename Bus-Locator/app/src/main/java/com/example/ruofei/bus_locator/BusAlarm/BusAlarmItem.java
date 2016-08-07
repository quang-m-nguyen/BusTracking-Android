package com.example.ruofei.bus_locator.BusAlarm;

/**
 * Created by ruofeixu on 7/10/16.
 */
public class BusAlarmItem {

    private String routeName, busstopName;
    private String remainingTime, alarmSettingTime;
    private String alarmID;

    public BusAlarmItem(){

    }

    public BusAlarmItem(String routeName, String busstopName, String remainingTime, String alarmSettingTime, String alarmID){
        this.routeName = routeName;
        this.busstopName = busstopName;
        this.remainingTime = remainingTime;
        this.alarmSettingTime = alarmSettingTime;
        this.alarmID = alarmID;

    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getBusstopName() {
        return busstopName;
    }

    public void setBusstopName(String busstopName) {
        this.busstopName = busstopName;
    }

    public String getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(String remainingTime) {
        this.remainingTime = remainingTime;
    }

    public String getAlarmSettingTime() {
        return alarmSettingTime;
    }

    public void setAlarmSettingTime(String alarmSettingTime) {
        this.alarmSettingTime = alarmSettingTime;
    }

    public String getAlarmID() {
        return alarmID;
    }

    public void setAlarmID(String alarmID) {
        this.alarmID = alarmID;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null)
            return false;
        BusAlarmItem other = (BusAlarmItem) o;
        if(this.alarmID == null || other.alarmID == null) return false;
        if(this.alarmID.equals(other.alarmID))
            return true;
        return false;
    }
}
