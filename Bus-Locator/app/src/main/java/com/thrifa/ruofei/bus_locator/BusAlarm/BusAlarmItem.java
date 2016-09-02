package com.thrifa.ruofei.bus_locator.BusAlarm;

/**
 * Created by ruofeixu on 7/10/16.
 */
public class BusAlarmItem {

    private String routeID, stopID;
    private Integer alarmID;
    private String routeName, busstopName;
    private String remainingTime, alarmSettingTime;

    private Double remainTimeNum, settingTimeNum;
    private boolean alarmFlag;

    public BusAlarmItem(){

    }

    public BusAlarmItem(String routeID,
                        String stopID,
                        String routeName,
                        String busstopName,
                        String remainingTime,
                        String alarmSettingTime,
                        Integer alarmID,
                        Double remainTimeNum,
                        Double settingTimeNum,
                        boolean alarmFlag){
        this.routeID = routeID;
        this.stopID = stopID;
        this.alarmID = alarmID;
        this.routeName = routeName;
        this.busstopName = busstopName;
        this.remainingTime = remainingTime;
        this.alarmSettingTime = alarmSettingTime;

        this.remainTimeNum = remainTimeNum;
        this.settingTimeNum = settingTimeNum;
        this.alarmFlag = alarmFlag;

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


    public Double getRemainTimeNum() {
        return remainTimeNum;
    }

    public void setRemainTimeNum(Double remainTimeNum) {
        this.remainTimeNum = remainTimeNum;
    }

    public Double getSettingTimeNum() {
        return settingTimeNum;
    }

    public void setSettingTimeNum(Double settingTimeNum) {
        this.settingTimeNum = settingTimeNum;
    }

    public boolean isAlarmFlag() {
        return alarmFlag;
    }

    public void setAlarmFlag(boolean alarmFlag) {
        this.alarmFlag = alarmFlag;
    }

    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    public String getStopID() {
        return stopID;
    }

    public void setStopID(String stopID) {
        this.stopID = stopID;
    }

    public Integer getAlarmID() {
        return alarmID;
    }

    public void setAlarmID(Integer alarmID) {
        this.alarmID = alarmID;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null)
            return false;
        BusAlarmItem other = (BusAlarmItem) o;
        // TODO: update this
//        if(this.alarmID == null || other.alarmID == null) return false;
//        if(this.alarmID.equals(other.alarmID))
//            return true;
//        return false;
         if(this.routeID == null || other.routeID == null || this.stopID == null || other.stopID == null) return false;
        if(this.routeID.equals(other.routeID) && this.stopID.equals(other.stopID))
            return true;
        return false;

    }
}
