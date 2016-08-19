package com.ewell.android.sleepcareforphone.viewmodels;

import com.ewell.android.bll.DataFactory;
import com.ewell.android.common.Grobal;
import com.ewell.android.common.exception.EwellException;
import com.ewell.android.ibll.SleepcareforPhoneManage;
import com.ewell.android.model.SleepQualityReport;

import java.util.ArrayList;

/**
 * Created by lillix on 8/1/16.
 */
public class SleepViewModel extends BaseViewModel {

    private String sleepTimespan="";

    public String getSleepTimespan() {
        return sleepTimespan;
    }

    private Float sleepTimespanFloat=0.0f;
    public Float getSleepTimespanFloat(){
        return sleepTimespanFloat;
    }

    private Float deepSleepTimespanFloat=0.0f;

    public Float getDeepSleepTimespanFloat() {
        return deepSleepTimespanFloat;
    }

    private String deepSleepTimespan="";

    public String getDeepSleepTimespan() {
        return deepSleepTimespan;
    }

    private Float lightSleepTimespanFloat=0.0f;

    public Float getLightSleepTimespanFloat() {
        return lightSleepTimespanFloat;
    }

    private String lightSleepTimespan="";

    public String getLightSleepTimespan() {
        return lightSleepTimespan;
    }

    private Float awakeningTimespanFloat=0.0f;

    public Float getAwakeningTimespanFloat() {
        return awakeningTimespanFloat;
    }

    private String awakeningTimespan="";

    public String getAwakeningTimespan() {
        return awakeningTimespan;
    }

    private ArrayList<String> weekdayValues=new ArrayList<String>();

    public ArrayList<String> getWeekdayValues() {
        return weekdayValues;
    }

    private ArrayList<Float> sleeptimeValues=new ArrayList<Float>();

    public ArrayList<Float> getSleeptimeValues() {
        return sleeptimeValues;
    }

    private SleepcareforPhoneManage sleepcareforPhoneManage = DataFactory.GetSleepcareforPhoneManage();
    private String bedusercode="";

    public void RefreshSleepData(String reportdate) {
        try {
            if (Grobal.getXmppManager().Connect()) {
                bedusercode = Grobal.getInitConfigModel().getCurUserCode();
                if (bedusercode.equals("")) {
                    sleepTimespan = "0";
                    sleepTimespanFloat = 0.0f;
                    deepSleepTimespan = "0";
                    deepSleepTimespanFloat = 0.0f;
                    lightSleepTimespan = "0";
                    lightSleepTimespanFloat = 0.0f;
                    awakeningTimespan = "0";
                    awakeningTimespanFloat = 0.0f;
                    weekdayValues = new ArrayList<String>();
                    sleeptimeValues = new ArrayList<Float>();

                } else {
                    SleepQualityReport sleepreport = sleepcareforPhoneManage.GetSleepQualityofBedUser(bedusercode, reportdate);


                    sleepTimespan = sleepreport.getSleepTimespan();
                    sleepTimespanFloat = sleepreport.getSleepTimespanFloat();
                    deepSleepTimespan = sleepreport.getDeepSleepTimespan();
                    deepSleepTimespanFloat = sleepreport.getDeepSleepTimespanFloat();
                    lightSleepTimespan = sleepreport.getLightSleepTimespan();
                    lightSleepTimespanFloat = sleepreport.getLightSleepTimespanFloat();
                    awakeningTimespan = sleepreport.getAwakeningTimespan();
                    awakeningTimespanFloat = sleepreport.getAwakeningTimespanFloat();
                    weekdayValues = sleepreport.getWeekdayValues();
                    sleeptimeValues = sleepreport.getSleeptimeValues();
                }
            } else {


            }
        } catch (EwellException ex) {
            //做消息弹窗提醒
        } catch (Exception e) {
            //做消息弹窗提醒

        }
    }
}
