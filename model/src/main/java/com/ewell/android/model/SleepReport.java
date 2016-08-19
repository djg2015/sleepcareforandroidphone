package com.ewell.android.model;

/**
 * Created by lillix on 7/29/16.
 */
public class SleepReport {

    private String ReportDate = "";
    public void setReportDate(String reportdate){ReportDate = reportdate;}
    public String getReportDate(){return ReportDate;}

    private String WeekDay = "";
    public void setWeekDay(String weekday){WeekDay = weekday;}
    public String getWeekDay(){return WeekDay;}

    private String WakeHours = "";
    public void setWakeHours(String wakehours){WakeHours = wakehours;}
    public String getWakeHours(){return WakeHours;}

    private String DeepHours = "";
    public void setDeepHours(String deephours){DeepHours = deephours;}
    public String getDeepHours(){return DeepHours;}

    private String LightHours = "";
    public void setLightHours(String lighthours){LightHours = lighthours;}
    public String getLightHours(){return LightHours;}

}
