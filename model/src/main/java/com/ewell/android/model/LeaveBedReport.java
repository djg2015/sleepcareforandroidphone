package com.ewell.android.model;

/**
 * Created by lillix on 7/29/16.
 */
public class LeaveBedReport {
    private String ReportDate = "";
    public void setReportDate(String reportdate){ReportDate = reportdate;}
    public String getReportDate(){return ReportDate;}

    private String WeekDay = "";
    public void setWeekDay(String weekday){WeekDay = weekday;}
    public String getWeekDay(){return WeekDay;}

    private String LeaveBedTimes = "";
    public void setLeaveBedTimes(String leavebedtimes){LeaveBedTimes = leavebedtimes;}
    public String getLeaveBedTimes(){return LeaveBedTimes;}

}
