package com.ewell.android.sleepcareforphone.viewmodels;

import com.ewell.android.bll.DataFactory;
import com.ewell.android.common.Grobal;
import com.ewell.android.common.exception.EwellException;
import com.ewell.android.ibll.SleepcareforPhoneManage;
import com.ewell.android.model.HRRange;

import java.util.ArrayList;

/**
 * Created by lillix on 7/31/16.
 */
public class HRViewModel extends BaseViewModel {

    private ArrayList<String> hrDay_xValues;

    public ArrayList<String> getHrDay_xValues() {
        return hrDay_xValues;
    }

    private ArrayList<String> hrDay_yValues;

    public ArrayList<String> getHrDay_yValues() {
        return hrDay_yValues;
    }

    private ArrayList<String> hrWeek_xValues;

    public ArrayList<String> getHrWeek_xValues() {
        return hrWeek_xValues;
    }

    private ArrayList<String> hrWeek_yValues;

    public ArrayList<String> getHrWeek_yValues() {
        return hrWeek_yValues;
    }

    private ArrayList<String> hrMonth_xValues;

    public ArrayList<String> getHrMonth_xValues() {
        return hrMonth_xValues;
    }

    private ArrayList<String> hrMonth_yValues;

    public ArrayList<String> getHrMonth_yValues() {
        return hrMonth_yValues;
    }



    private SleepcareforPhoneManage sleepcareforPhoneManage = DataFactory.GetSleepcareforPhoneManage();
    private String bedusercode;


    public void RefreshHRData() {
        try {
            if (Grobal.getXmppManager().Connect()) {
                bedusercode = Grobal.getInitConfigModel().getCurUserCode();
                if (bedusercode.equals("")) {
                    hrDay_xValues = new ArrayList<String>();
                    hrDay_yValues = new ArrayList<String>();
                            hrWeek_xValues = new ArrayList<String>();
                    hrWeek_yValues = new ArrayList<String>();
                            hrMonth_xValues = new ArrayList<String>();
                    hrMonth_yValues = new ArrayList<String>();

                } else {


                    HRRange hrDayRange = sleepcareforPhoneManage.GetSingleHRTimeReport(bedusercode, "1");
                    HRRange hrWeekRange = sleepcareforPhoneManage.GetSingleHRTimeReport(bedusercode, "2");
                    HRRange hrMonthRange = sleepcareforPhoneManage.GetSingleHRTimeReport(bedusercode, "3");

                    hrDay_xValues = hrDayRange.getHrXValues();
                    hrDay_yValues = hrDayRange.getHrYValues();

                    hrWeek_xValues = hrWeekRange.getHrXValues();
                    hrWeek_yValues = hrWeekRange.getHrYValues();

                    hrMonth_xValues = hrMonthRange.getHrXValues();
                    hrMonth_yValues = hrMonthRange.getHrYValues();
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
