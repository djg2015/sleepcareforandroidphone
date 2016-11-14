package com.ewell.android.sleepcareforphone.viewmodels;

import android.databinding.Bindable;
import android.widget.Toast;

import com.ewell.android.bll.DataFactory;
import com.ewell.android.common.exception.EwellException;
import com.ewell.android.ibll.SleepcareforPhoneManage;
import com.ewell.android.model.HRRRReport;
import com.ewell.android.model.LeaveBedReport;
import com.ewell.android.model.SleepReport;
import com.ewell.android.model.WeekSleep;
import com.ewell.android.sleepcareforphone.BR;
import com.ewell.android.sleepcareforphone.activities.ShowWeekReportActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by lillix on 7/29/16.
 */
public class ShowWeekReportViewModel extends BaseViewModel {
    private ShowWeekReportActivity parentactivity;
    public void setParentactivity(ShowWeekReportActivity activity){parentactivity = activity;}

    private String bedusercode = "";
    public String getBedusercode() {
        return bedusercode;
    }
    public void setBedusercode(String usercode){bedusercode = usercode;}


    private String bedusername = "";
    public String getBedusername() {
        return bedusername;
    }
    public void setBedusername(String username){bedusername = username;}


    private String endDate = "";
    private String beginDate = "";

    private String reportDate = "";
    public String getReportDate(){return reportDate;}


    private String titleDate = "";

    @Bindable
    public String getTitleDate() {
        return titleDate;
    }

    public void setTitleDate(String titledate) {
        this.titleDate = titledate;
        notifyPropertyChanged(BR.titleDate);
    }

    private String WeekMaxHR = "";

    @Bindable
    public String getWeekMaxHR() {
        return WeekMaxHR;
    }

    public void setWeekMaxHR(String maxhr) {
        this.WeekMaxHR = maxhr;
        notifyPropertyChanged(BR.weekMaxHR);
    }

    private String WeekMinHR = "";

    @Bindable
    public String getWeekMinHR() {
        return WeekMinHR;
    }

    public void setWeekMinHR(String minhr) {
        this.WeekMinHR = minhr;
        notifyPropertyChanged(BR.weekMinHR);
    }

    private String WeekAvgHR = "";

    @Bindable
    public String getWeekAvgHR() {
        return WeekAvgHR;
    }

    public void setWeekAvgHR(String avghr) {
        this.WeekAvgHR = avghr;
        notifyPropertyChanged(BR.weekAvgHR);
    }

    private String WeekMaxRR = "";

    @Bindable
    public String getWeekMaxRR() {
        return WeekMaxRR;
    }

    public void setWeekMaxRR(String maxrr) {
        this.WeekMaxRR = maxrr;
        notifyPropertyChanged(BR.weekMaxRR);
    }

    private String WeekMinRR = "";

    @Bindable
    public String getWeekMinRR() {
        return WeekMinRR;
    }

    public void setWeekMinRR(String minrr) {
        this.WeekMinRR = minrr;
        notifyPropertyChanged(BR.weekMinRR);
    }

    private String WeekAvgRR = "";

    @Bindable
    public String getWeekAvgRR() {
        return WeekAvgRR;
    }

    public void setWeekAvgRR(String avgrr) {
        this.WeekAvgRR = avgrr;
        notifyPropertyChanged(BR.weekAvgRR);
    }

    private ArrayList<HRRRReport> hrrrReports;

    public ArrayList<HRRRReport> getHrrrReports() {
        return hrrrReports;
    }

    private String LeaveBedSum = "";

    @Bindable
    public String getLeaveBedSum() {
        return LeaveBedSum;
    }

    public void setLeaveBedSum(String leavebedsum) {
        this.LeaveBedSum = leavebedsum;
        notifyPropertyChanged(BR.leaveBedSum);
    }

    private ArrayList<LeaveBedReport> leavebedReports;

    public ArrayList<LeaveBedReport> getLeavebedReports() {
        return leavebedReports;
    }

    private String WeekWakeHours = "";

    @Bindable
    public String getWeekWakeHours() {
        return WeekWakeHours;
    }

    public void setWeekWakeHours(String wakehours) {
        this.WeekWakeHours = wakehours;
        notifyPropertyChanged(BR.weekWakeHours);
    }


    private String WeekLightSleepHours = "";

    @Bindable
    public String getWeekLightSleepHours() {
        return WeekLightSleepHours;
    }

    public void setWeekLightSleepHours(String lightsleephours) {
        this.WeekLightSleepHours = lightsleephours;
        notifyPropertyChanged(BR.weekLightSleepHours);
    }

    private String WeekDeepSleepHours = "";

    @Bindable
    public String getWeekDeepSleepHours() {
        return WeekDeepSleepHours;
    }

    public void setWeekDeepSleepHours(String deepsleephours) {
        this.WeekDeepSleepHours = deepsleephours;
        notifyPropertyChanged(BR.weekDeepSleepHours);
    }


    private String WeekSleepHours = "";

    @Bindable
    public String getWeekSleepHours() {
        return WeekSleepHours;
    }

    public void setWeekSleepHours(String sleephours) {
        this.WeekSleepHours = sleephours;
        notifyPropertyChanged(BR.weekSleepHours);
    }


    private String OnbedBeginTime = "";

    @Bindable
    public String getOnbedBeginTime() {
        return OnbedBeginTime;
    }

    public void setOnbedBeginTime(String onbedbegin) {
        this.OnbedBeginTime = onbedbegin;
        notifyPropertyChanged(BR.onbedBeginTime);
    }

    private String OnbedEndTime = "";

    @Bindable
    public String getOnbedEndTime() {
        return OnbedEndTime;
    }

    public void setOnbedEndTime(String onbedend) {
        this.OnbedEndTime = onbedend;
        notifyPropertyChanged(BR.onbedEndTime);
    }

    private ArrayList<SleepReport> sleepReports;

    public ArrayList<SleepReport> getSleepReports() {
        return sleepReports;
    }


    private String AvgLeaveBedSum = "";

    @Bindable
    public String getAvgLeaveBedSum() {
        return AvgLeaveBedSum;
    }

    public void setAvgLeaveBedSum(String avgleavebed) {
        this.AvgLeaveBedSum = avgleavebed;
        notifyPropertyChanged(BR.avgLeaveBedSum);
    }


    private String AvgTurnTimes = "";

    @Bindable
    public String getAvgTurnTimes() {
        return AvgTurnTimes;
    }

    public void setAvgTurnTimes(String avgturn) {
        this.AvgTurnTimes = avgturn;
        notifyPropertyChanged(BR.avgTurnTimes);
    }

    private String MaxLeaveBedHours = "";

    @Bindable
    public String getMaxLeaveBedHours() {
        return MaxLeaveBedHours;
    }

    public void setMaxLeaveBedHours(String maxleavebed) {
        this.MaxLeaveBedHours = maxleavebed;
        notifyPropertyChanged(BR.maxLeaveBedHours);
    }

    private String TurnsRate = "";

    @Bindable
    public String getTurnsRate() {
        return TurnsRate;
    }

    public void setTurnsRate(String turnrates) {
        this.TurnsRate = turnrates;
        notifyPropertyChanged(BR.turnsRate);
    }

    private String SleepSuggest = "";

    @Bindable
    public String getSleepSuggest() {
        return SleepSuggest;
    }

    public void setSleepSuggest(String suggest) {
        this.SleepSuggest = suggest;
        notifyPropertyChanged(BR.sleepSuggest);
    }

    private ArrayList<String> hrrr_xValues = new ArrayList<String>();

    public ArrayList<String> getHrrr_xValues() {
        return hrrr_xValues;
    }

    private ArrayList<String> hr_yValues = new ArrayList<String>();

    public ArrayList<String> getHr_yValues() {
        return hr_yValues;
    }

    private ArrayList<String> rr_yValues = new ArrayList<String>();

    public ArrayList<String> getRr_yValues() {
        return rr_yValues;
    }

    private ArrayList<String> leavebed_xValues = new ArrayList<String>();

    public ArrayList<String> getLeavebed_xValues() {
        return leavebed_xValues;
    }

    private ArrayList<String> leavebed_yValues = new ArrayList<String>();

    public ArrayList<String> getLeavebed_yValues() {
        return leavebed_yValues;
    }

    private ArrayList<String> sleep_xValues = new ArrayList<String>();

    public ArrayList<String> getSleep_xValues() {
        return sleep_xValues;
    }

    private ArrayList<String> lightsleep_yValues = new ArrayList<String>();

    public ArrayList<String> getLightsleep_yValues() {
        return lightsleep_yValues;
    }

    private ArrayList<String> deepsleep_yValues = new ArrayList<String>();

    public ArrayList<String> getDeepsleep_yValues() {
        return deepsleep_yValues;
    }

    private ArrayList<String> awake_yValues = new ArrayList<String>();

    public ArrayList<String> getAwake_yValues() {
        return awake_yValues;
    }

    private SleepcareforPhoneManage sleepcareforPhoneManage = DataFactory.GetSleepcareforPhoneManage();

    public void InitData() {
        //获取当前时间的前一天
        Date date = new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, -1);//把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        this.reportDate = formatter.format(date);
    }


    public void RefreshWeekreport(String reportdate) {
        try {
            if(bedusercode.equals("")){
                //所有数据置空
                setTitleDate(reportDate);
                setWeekMaxHR("");
                setWeekMinHR("");
                setWeekAvgHR("");
                setWeekMaxRR("");
                setWeekMinRR("");
                setWeekAvgRR("");
                hrrrReports = new ArrayList<HRRRReport>();
                hrrr_xValues = new ArrayList<String>();
                hr_yValues = new ArrayList<String>();
                rr_yValues = new ArrayList<String>();
                setLeaveBedSum("");
                leavebedReports = new ArrayList<LeaveBedReport>();
                leavebed_xValues = new ArrayList<String>();
                leavebed_yValues = new ArrayList<String>();
                setWeekWakeHours("");
                setWeekLightSleepHours("");
                setWeekDeepSleepHours("");
                setWeekSleepHours("");
                setOnbedEndTime("");
                setOnbedBeginTime("");
                sleepReports = new ArrayList<SleepReport>();
                sleep_xValues = new ArrayList<String>();
                lightsleep_yValues = new ArrayList<String>();
                deepsleep_yValues = new ArrayList<String>();
                awake_yValues = new ArrayList<String>();
                setAvgLeaveBedSum("");
                setAvgTurnTimes("");
                setMaxLeaveBedHours("");
                setTurnsRate("");
                setSleepSuggest("");
            }
            else {
                this.reportDate = reportdate;
                WeekSleep weeksleep = sleepcareforPhoneManage.GetWeekSleepofBedUser(bedusercode, reportdate);
                beginDate = weeksleep.getBeginDate();
                endDate = weeksleep.getEndDate();
                setTitleDate(beginDate.replaceAll("-", "/") + "-" + endDate.replaceAll("-", "/"));
                setWeekMaxHR(weeksleep.getWeekMaxHR());
                setWeekMinHR(weeksleep.getWeekMinHR());
                setWeekAvgHR(weeksleep.getWeekAvgHR());
                setWeekMaxRR(weeksleep.getWeekMaxRR());
                setWeekMinRR(weeksleep.getWeekMinRR());
                setWeekAvgRR(weeksleep.getWeekAvgRR());

                hrrrReports = weeksleep.getHRRRRange();
                hrrr_xValues = new ArrayList<String>();
                hr_yValues = new ArrayList<String>();
                rr_yValues = new ArrayList<String>();
                for (int i = 0; i < hrrrReports.size(); i++) {
                    HRRRReport temphrrr = hrrrReports.get(i);
                    hrrr_xValues.add(temphrrr.getWeekDay());
                    hr_yValues.add(temphrrr.getHR());
                    rr_yValues.add(temphrrr.getRR());

                }

                setLeaveBedSum(weeksleep.getLeaveBedSum());
                leavebedReports = weeksleep.getLeaveBedRange();
                leavebed_xValues = new ArrayList<String>();
                leavebed_yValues = new ArrayList<String>();
                for (int i = 0; i < leavebedReports.size(); i++) {
                    LeaveBedReport templeavebed = leavebedReports.get(i);
                    leavebed_xValues.add(templeavebed.getWeekDay());
                    leavebed_yValues.add(templeavebed.getLeaveBedTimes());
                }

                setWeekWakeHours(weeksleep.getWeekWakeHours());
                setWeekLightSleepHours(weeksleep.getWeekLightSleepHours());
                setWeekDeepSleepHours(weeksleep.getWeekDeepSleepHours());
                setWeekSleepHours(weeksleep.getWeekSleepHours());

                String temponbedbegintime = weeksleep.getOnbedBeginTime();
                if (temponbedbegintime.length() > 11) {
                    setOnbedBeginTime(temponbedbegintime.substring(11));
                } else {
                    setOnbedBeginTime("");
                }

                String temponbedendtime = weeksleep.getOnbedEndTime();
                if (temponbedendtime.length() > 11) {
                    setOnbedEndTime(temponbedendtime.substring(11));
                } else {
                    setOnbedEndTime("");
                }


                sleepReports = weeksleep.getSleepRange();
                sleep_xValues = new ArrayList<String>();
                lightsleep_yValues = new ArrayList<String>();
                deepsleep_yValues = new ArrayList<String>();
                awake_yValues = new ArrayList<String>();
                for (int i = 0; i < sleepReports.size(); i++) {
                    SleepReport tempsleep = sleepReports.get(i);
                    sleep_xValues.add(tempsleep.getWeekDay());
                    lightsleep_yValues.add(tempsleep.getLightHours());
                    deepsleep_yValues.add(tempsleep.getDeepHours());
                    awake_yValues.add(tempsleep.getWakeHours());

                }

                setAvgLeaveBedSum(weeksleep.getAvgLeaveBedSum());
                setAvgTurnTimes(weeksleep.getAvgTurnTimes());
                setMaxLeaveBedHours(weeksleep.getMaxLeaveBedHours());
                setTurnsRate(weeksleep.getTurnsRate());
                setSleepSuggest(weeksleep.getSleepSuggest());
            }

        } catch (EwellException ex) {
            //做消息弹窗提醒
            Toast.makeText(parentactivity, ex.get_exceptionMsg(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //做消息弹窗提醒
            e.printStackTrace();
        }

    }

    public Boolean ClickSendEmail(String email) {
        Boolean result = false;
        try {

            result = sleepcareforPhoneManage.SendEmail(bedusercode, reportDate, email).getResult();

        } catch (EwellException ex) {
            //做消息弹窗提醒
            Toast.makeText(parentactivity, ex.get_exceptionMsg(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //做消息弹窗提醒
            e.printStackTrace();
        }
        return result;

    }

}
