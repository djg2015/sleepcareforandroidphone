package com.ewell.android.model;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lillix on 7/29/16.
 */
public class WeekSleep extends BaseMessage {
    public WeekSleep(XmppMsgSubject subject) {
        super(subject);
    }

    private String BeginDate = "";

    public String getBeginDate() {
        return BeginDate;
    }

    private String EndDate = "";

    public String getEndDate() {
        return EndDate;
    }

    private String WeekMaxRR = "";

    public String getWeekMaxRR() {
        return WeekMaxRR;
    }

    private String WeekMinRR = "";

    public String getWeekMinRR() {
        return WeekMinRR;
    }

    private String WeekAvgRR = "";

    public String getWeekAvgRR() {
        return WeekAvgRR;
    }

    private String WeekMaxHR = "";

    public String getWeekMaxHR() {
        return WeekMaxHR;
    }

    private String WeekMinHR = "";

    public String getWeekMinHR() {
        return WeekMinHR;
    }

    private String WeekAvgHR = "";

    public String getWeekAvgHR() {
        return WeekAvgHR;
    }

    // 当前周心率呼吸报表
    private ArrayList<HRRRReport> HRRRRange = new ArrayList<HRRRReport>();

    public ArrayList<HRRRReport> getHRRRRange() {
        return HRRRRange;
    }

    private String LeaveBedSum = "";

    public String getLeaveBedSum() {
        return LeaveBedSum;
    }

    //当前周离床报表
    private ArrayList<LeaveBedReport> LeaveBedRange = new ArrayList<LeaveBedReport>();

    public ArrayList<LeaveBedReport> getLeaveBedRange() {
        return LeaveBedRange;
    }

    private String WeekWakeHours = "";

    public String getWeekWakeHours() {
        return WeekWakeHours;
    }

    private String WeekLightSleepHours = "";

    public String getWeekLightSleepHours() {
        return WeekLightSleepHours;
    }

    private String WeekDeepSleepHours = "";

    public String getWeekDeepSleepHours() {
        return WeekDeepSleepHours;
    }

    private String WeekSleepHours = "";

    public String getWeekSleepHours() {
        return WeekSleepHours;
    }

    private String OnbedBeginTime = "";

    public String getOnbedBeginTime() {
        return OnbedBeginTime;
    }

    private String OnbedEndTime = "";

    public String getOnbedEndTime() {
        return OnbedEndTime;
    }

    //当前周睡眠报表
    private ArrayList<SleepReport> SleepRange = new ArrayList<SleepReport>();

    public ArrayList<SleepReport> getSleepRange() {
        return SleepRange;
    }

    private String AvgLeaveBedSum = "";

    public String getAvgLeaveBedSum() {
        return AvgLeaveBedSum;
    }

    private String AvgTurnTimes = "";

    public String getAvgTurnTimes() {
        return AvgTurnTimes;
    }

    private String MaxLeaveBedHours = "";

    public String getMaxLeaveBedHours() {
        return MaxLeaveBedHours;
    }

    private String TurnsRate = "";

    public String getTurnsRate() {
        return TurnsRate;
    }

    private String SleepSuggest = "";

    public String getSleepSuggest() {
        return SleepSuggest;
    }

    public static WeekSleep XmlToMessage(String sujectXml, String bodyXml) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder(false);
        WeekSleep result = new WeekSleep(XmppMsgSubject.PareXmlToSubject(sujectXml));
        Reader returnQuote = new StringReader(bodyXml);
        Document doc = builder.build(returnQuote);
        Element root = doc.getRootElement();

        result.BeginDate = root.getChildTextTrim("BeginDate");
        result.EndDate = root.getChildTextTrim("EndDate");
        result.WeekAvgHR = root.getChildTextTrim("WeekAvgHR");
        result.WeekMinHR = root.getChildTextTrim("WeekMinHR");
        result.WeekMaxHR = root.getChildTextTrim("WeekMaxHR");
        result.WeekAvgRR = root.getChildTextTrim("WeekAvgRR");
        result.WeekMinRR = root.getChildTextTrim("WeekMinRR");
        result.WeekMaxRR = root.getChildTextTrim("WeekMaxRR");

        Element list = root.getChild("HRRRRange");
        List hrrrlist = list.getChildren();
        result.HRRRRange = new ArrayList<HRRRReport>();
        for (int i = 0; i < hrrrlist.size(); i++) {
            HRRRReport hrrrreport = new HRRRReport();
            Element hrrr = (Element) hrrrlist.get(i);
            hrrrreport.setReportDate(hrrr.getChildTextTrim("ReportDate"));
            String tempweekday = hrrr.getChildTextTrim("WeekDay");
            hrrrreport.setWeekDay(tempweekday.replace("星期","周"));
            String temphr = hrrr.getChildTextTrim("HR");
            if(temphr==null || temphr.equals("")){
                temphr = "0";
            }
            hrrrreport.setHR(temphr);

            String temprr = hrrr.getChildTextTrim("RR");
            if(temprr==null || temprr.equals("")){
                temprr = "0";
            }
            hrrrreport.setRR(temprr);
            result.HRRRRange.add(hrrrreport);
        }

        result.LeaveBedSum = root.getChildTextTrim("LeaveBedSum");

        Element list2 = root.getChild("LeaveBedRange");
        List leavebedlist = list2.getChildren();
        result.LeaveBedRange = new ArrayList<LeaveBedReport>();
        for (int i = 0; i < leavebedlist.size(); i++) {
            LeaveBedReport leavebedreport = new LeaveBedReport();
            Element leavebed = (Element) leavebedlist.get(i);
            leavebedreport.setReportDate(leavebed.getChildTextTrim("ReportDate"));

            String tempweekday = leavebed.getChildTextTrim("WeekDay");
            leavebedreport.setWeekDay(tempweekday.replace("星期","周"));
            String templeavebed = leavebed.getChildTextTrim("LeaveBedTimes");
            if(templeavebed==null || templeavebed.equals("")){
                templeavebed="0";
            }
            leavebedreport.setLeaveBedTimes(templeavebed);

            result.LeaveBedRange.add(leavebedreport);
        }

        result.WeekWakeHours = root.getChildTextTrim("WeekWakeHours");
        result.WeekLightSleepHours= root.getChildTextTrim("WeekLightSleepHours");
        result.WeekDeepSleepHours = root.getChildTextTrim("WeekDeepSleepHours");
        result.WeekSleepHours = root.getChildTextTrim("WeekSleepHours");
        result.OnbedBeginTime = root.getChildTextTrim("OnbedBeginTime");
result.OnbedEndTime = root.getChildTextTrim("OnbedEndTime");

        Element list3 = root.getChild("SleepRange");
        List sleeplist = list3.getChildren();
        result.SleepRange = new ArrayList<SleepReport>();
        for (int i = 0; i < sleeplist.size(); i++) {
            SleepReport sleepreport = new SleepReport();
            Element sleep = (Element) sleeplist.get(i);
            sleepreport.setReportDate(sleep.getChildTextTrim("ReportDate"));
            String tempweekday = sleep.getChildTextTrim("WeekDay");
            sleepreport.setWeekDay(tempweekday.replace("星期","周"));
            String tempwake = sleep.getChildTextTrim("WakeHours");
            if(tempwake!=null && tempwake.length()>3){
               tempwake = tempwake.substring(0,2);
                if(tempwake.substring(0,1).equals("0")){
                    tempwake=tempwake.substring(1,2);
                }
            }
            sleepreport.setWakeHours(tempwake);
            String tempdeep = sleep.getChildTextTrim("DeepHours");
            if(tempdeep!=null && tempdeep.length()>3){
                tempdeep = tempdeep.substring(0,2);
                if(tempdeep.substring(0,1).equals("0")){
                    tempdeep=tempdeep.substring(1,2);
                }
            }
            sleepreport.setDeepHours(tempdeep);

            String templight = sleep.getChildTextTrim("LightHours");
            if(templight!=null && templight.length()>3){
                templight = templight.substring(0,2);
                if(templight.substring(0,1).equals("0")){
                    templight=templight.substring(1,2);
                }
            }
            sleepreport.setLightHours(templight);

            result.SleepRange.add(sleepreport);
        }

        result.AvgLeaveBedSum = root.getChildTextTrim("AvgLeaveBedSum");
        result.AvgTurnTimes = root.getChildTextTrim("AvgTurnTimes");
        result.MaxLeaveBedHours = root.getChildTextTrim("MaxLeaveBedHours");
        result.TurnsRate = root.getChildTextTrim("TurnsRate");
        result.SleepSuggest = root.getChildTextTrim("SleepSuggest");


        returnQuote.close();
        return result;
    }

    @Override
    public String ToBodytXml() {
        return null;
    }
}
