package com.ewell.android.sleepcareforphone.viewmodels;

import com.ewell.android.bll.DataFactory;
import com.ewell.android.common.Grobal;
import com.ewell.android.common.exception.EwellException;
import com.ewell.android.ibll.SleepcareforPhoneManage;
import com.ewell.android.model.RRRange;

import java.util.ArrayList;

/**
 * Created by lillix on 8/1/16.
 */
public class RRViewModel extends BaseViewModel {

    private ArrayList<String> rrDay_xValues;

    public ArrayList<String> getRrDay_xValues() {
        return rrDay_xValues;
    }

    private ArrayList<String> rrDay_yValues;

    public ArrayList<String> getRrDay_yValues() {
        return rrDay_yValues;
    }

    private ArrayList<String> rrWeek_xValues;

    public ArrayList<String> getRrWeek_xValues() {
        return rrWeek_xValues;
    }

    private ArrayList<String> rrWeek_yValues;

    public ArrayList<String> getRrWeek_yValues() {
        return rrWeek_yValues;
    }

    private ArrayList<String> rrMonth_xValues;

    public ArrayList<String> getRrMonth_xValues() {
        return rrMonth_xValues;
    }

    private ArrayList<String> rrMonth_yValues;

    public ArrayList<String> getRrMonth_yValues() {
        return rrMonth_yValues;
    }
    private SleepcareforPhoneManage sleepcareforPhoneManage = DataFactory.GetSleepcareforPhoneManage();
    private String bedusercode;

    public void RefreshRRData() {
        try {
            if (Grobal.getXmppManager().Connect()) {
                bedusercode = Grobal.getInitConfigModel().getCurUserCode();
                if (bedusercode.equals("")) {
                    rrDay_xValues = new ArrayList<String>();
                    rrDay_yValues = new ArrayList<String>();
                    rrWeek_xValues = new ArrayList<String>();
                    rrWeek_yValues = new ArrayList<String>();
                    rrMonth_xValues = new ArrayList<String>();
                    rrMonth_yValues = new ArrayList<String>();
                } else {
                    RRRange rrDayRange = sleepcareforPhoneManage.GetSingleRRTimeReport(bedusercode, "1");
                    RRRange rrWeekRange = sleepcareforPhoneManage.GetSingleRRTimeReport(bedusercode, "2");
                    RRRange rrMonthRange = sleepcareforPhoneManage.GetSingleRRTimeReport(bedusercode, "3");

                    rrDay_xValues = rrDayRange.getRrXValues();
                    rrDay_yValues = rrDayRange.getRrYValues();

                    rrWeek_xValues = rrWeekRange.getRrXValues();
                    rrWeek_yValues = rrWeekRange.getRrYValues();

                    rrMonth_xValues = rrMonthRange.getRrXValues();
                    rrMonth_yValues = rrMonthRange.getRrYValues();
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
