package com.example.ruofei.bus_locator.BusAlarm;

/**
 * Created by ruofeixu on 7/10/16.
 */
public class BusAlarmItem {

    private String routeName, busstopName;
    private String remainingTime, alarmSettingTime;
    private Integer alarmID;

    private Double remainTimeNum, settingTimeNum;
    private boolean alarmFlag;

    public BusAlarmItem(){

    }

    public BusAlarmItem(String routeName,
                        String busstopName,
                        String remainingTime,
                        String alarmSettingTime,
                        Integer alarmID,
                        Double remainTimeNum,
                        Double settingTimeNum,
                        boolean alarmFlag){
        this.routeName = routeName;
        this.busstopName = busstopName;
        this.remainingTime = remainingTime;
        this.alarmSettingTime = alarmSettingTime;
        this.alarmID = alarmID;

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

    public Integer getAlarmID() {
        return alarmID;
    }

    public void setAlarmID(Integer alarmID) {
        this.alarmID = alarmID;
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
         if(this.routeName == null || other.routeName == null || this.busstopName == null || other.busstopName == null) return false;
        if(this.routeName.equals(other.routeName) && this.busstopName.equals(other.busstopName))
            return true;
        return false;

    }
}
