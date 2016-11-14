package com.ewell.android.sleepcareforphone.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ewell.android.common.GetRealtimeDataDelegate;
import com.ewell.android.common.RealTimeHelper;
import com.ewell.android.model.EMRealTimeReport;
import com.ewell.android.sleepcareforphone.R;
import com.ewell.android.sleepcareforphone.common.SleepProgressView;
import com.ewell.android.sleepcareforphone.common.fancychart.ChartData;
import com.ewell.android.sleepcareforphone.common.fancychart.FancyChart;
import com.ewell.android.sleepcareforphone.common.fancychart.FancyChartPointListener;
import com.ewell.android.sleepcareforphone.common.fancychart.Point;
import com.ewell.android.sleepcareforphone.viewmodels.SleepViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;


public class FragmentSleep extends Fragment implements View.OnClickListener, GetRealtimeDataDelegate {
        //, XmppMsgDelegate<EMRealTimeReport> {
    private String BedUserCode = "";
    private String BedUserName = "";

    private static TextView txtdate;
    private ImageView onbedstatusimg;
    private String onbedstatus = "";
    private TextView patientname;

    private static TextView lightsleeptxt;
    private static TextView deepsleeptxt;
    private static TextView awakesleeptxt;
    private static TextView sleephourtxt;

    private DatePickerFragment fragment;
    private static SleepProgressView mSleepProgressView;
    private static FancyChart chart;

    private SharedPreferences sp;
    // 获取到一个参数文件的编辑器。
    private SharedPreferences.Editor editor;

    private String reportDate = "";
    private static SleepViewModel sleepViewModel;
    private Button back;
    private ImageView alarmImg;

    //从父activty传参
    public static FragmentSleep newInstance(String bedusercode, String bedusername) {
        FragmentSleep fragmentSleep = new FragmentSleep();
        Bundle args = new Bundle();
        args.putString("bedusercode", bedusercode);
        args.putString("bedusername", bedusername);
        fragmentSleep.setArguments(args);
        return fragmentSleep;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BedUserCode = getArguments().getString("bedusercode", "");
        //初始化数据
        BedUserName = getArguments().getString("bedusername", "");

        fragment = new DatePickerFragment();
        Date date = new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, -1);//把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        reportDate = formatter.format(date);


        sleepViewModel = new SleepViewModel();
        sleepViewModel.RefreshSleepData(reportDate);

      //  Grobal.getXmppManager().SetXmppMsgDelegate(this);
        RealTimeHelper.GetInstance().SetDelegate("realtimedelegate",this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sleep, container, false);
        //change patient button

        patientname = (TextView) v.findViewById(R.id.texttitle);
        patientname.setOnClickListener(this);
        patientname.setText(BedUserName);


        //change date
        txtdate = (TextView) v.findViewById(R.id.choosedate);
        txtdate.setText(reportDate);
        txtdate.setOnClickListener(this);

        onbedstatusimg = (ImageView) v.findViewById(R.id.onbedstatusimg);

        alarmImg = (ImageView) v.findViewById(R.id.alarmimg);
        alarmImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShowAlarmActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("currentusercode", BedUserCode);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });


        lightsleeptxt = (TextView) v.findViewById(R.id.lightsleep);
        lightsleeptxt.setText(sleepViewModel.getLightSleepTimespan());
        deepsleeptxt = (TextView) v.findViewById(R.id.deepsleep);
        deepsleeptxt.setText(sleepViewModel.getDeepSleepTimespan());
        awakesleeptxt = (TextView) v.findViewById(R.id.awakesleep);
        awakesleeptxt.setText(sleepViewModel.getAwakeningTimespan());

        sleephourtxt  = (TextView)v.findViewById(R.id.sleephour);
        sleephourtxt.setText(sleepViewModel.getSleepTimespan());

        mSleepProgressView = (SleepProgressView) v.findViewById(R.id.my_sleepprogress);
        mSleepProgressView.setMaxCount(24.0f);
        mSleepProgressView.setDeepsleepCount(sleepViewModel.getDeepSleepTimespanFloat());
        mSleepProgressView.setLightsleepCount(sleepViewModel.getLightSleepTimespanFloat());
        mSleepProgressView.setAwakeCount(sleepViewModel.getAwakeningTimespanFloat());
        mSleepProgressView.setScore(sleepViewModel.getSleepQuality());

        chart = (FancyChart) v.findViewById(R.id.my_sleepchart);
        chart.setOnPointClickListener(new FancyChartPointListener() {
            @Override
            public void onClick(Point point) {
                Toast.makeText(getActivity(), "时长: " + point.y + "小时", Toast.LENGTH_SHORT).show();

            }
        });

        RefreshChart();


//        ChartData data2 = new ChartData(ChartData.LINE_COLOR_GREEN);
//        int[] yValues2 = new int[]{2, 2, 4, 2,1, 5, 3};
//        for(int i = 0; i<yValues2.length;i++) {
//            data2.addPoint(i, yValues2[i]);
//        }
//        chart.addData(data2);
//
//        ChartData data3 = new ChartData(ChartData.LINE_COLOR_LIGHTGREEN);
//        int[] yValues3 = new int[]{1, 0, 3, 4,2, 3, 2};
//        for(int i = 0; i<yValues3.length;i++) {
//            data3.addPoint(i, yValues3[i]);
//        }
//        chart.addData(data3);


        back=(Button)v.findViewById(R.id.btnClose);
       back.setOnClickListener(this);

        return v;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClose:

                getActivity().finish();
                break;
            case R.id.choosedate:
                showDatePickerFragemnt();
                break;

            default:
                System.out.print("============================none");
                break;
        }

    }


    //----------------------选择日期--------------------------
    private void showDatePickerFragemnt() {

        fragment.show(getActivity().getFragmentManager(), "datePicker");

    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private int _year = 2016;
        private int _month = 1;
        private int _day = 1;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO 日期选择完成事件，取消时不会触发
            _year = year;
            _month = monthOfYear + 1;
            _day = dayOfMonth;
            //  Log.i(Constant.LOG_TAG, "year="+year+",monthOfYear="+monthOfYear+",dayOfMonth="+dayOfMonth);
            FragmentSleep.txtdate.setText(_year + "/" + _month + "/" + _day);

            //根据选择的日期,刷新整个页面
            FragmentSleep.RefreshAfterSelectDate(_year+"-"+_month+"-"+_day);
        }

        @Override
        public void show(FragmentManager manager, String tag) {
            super.show(manager, tag);
        }
//
//        public String getValue(){
//
//            return _year+"/"+_month+"/"+_day;
//        }

    }


    //--------------------根据选择的日期,刷新整个页面-----
    public static void RefreshAfterSelectDate(String reportdate) {
        sleepViewModel.RefreshSleepData(reportdate);

        lightsleeptxt.setText(sleepViewModel.getLightSleepTimespan());
        deepsleeptxt.setText(sleepViewModel.getDeepSleepTimespan());
        awakesleeptxt.setText(sleepViewModel.getAwakeningTimespan());

        sleephourtxt.setText(sleepViewModel.getSleepTimespan());

        mSleepProgressView.setDeepsleepCount(sleepViewModel.getDeepSleepTimespanFloat());
        mSleepProgressView.setLightsleepCount(sleepViewModel.getLightSleepTimespanFloat());
        mSleepProgressView.setAwakeCount(sleepViewModel.getAwakeningTimespanFloat());
        mSleepProgressView.setScore(sleepViewModel.getSleepQuality());

        chart.clearValues();
        ArrayList<Float> tempyvalues = sleepViewModel.getSleeptimeValues();
        ArrayList<String> tempxvalues = sleepViewModel.getWeekdayValues();
        //int[] yValues = new int[]{4, 3, 9, 5, 5, 3, 13};
        //  String[] yTitleValues = new String[]{"周三","周四","周五","周六","周日","周一","周二"};
        if (tempxvalues!=null && tempxvalues.size() > 0) {
            ChartData data = new ChartData(ChartData.LINE_COLOR_PURPLE);
            for (int i = 0; i < tempxvalues.size(); i++) {
                data.addPoint(i, tempyvalues.get(i).intValue());
                data.addXValue(i, tempxvalues.get(i));
            }
            chart.addData(data);
        }

    }

    public void RefreshChart(){

        chart.clearValues();
        ArrayList<Float> tempyvalues = sleepViewModel.getSleeptimeValues();
        ArrayList<String> tempxvalues = sleepViewModel.getWeekdayValues();
        //int[] yValues = new int[]{4, 3, 9, 5, 5, 3, 13};
        //  String[] yTitleValues = new String[]{"周三","周四","周五","周六","周日","周一","周二"};
        if (tempxvalues!=null && tempxvalues.size() > 0) {
            ChartData data = new ChartData(ChartData.LINE_COLOR_PURPLE);
            for (int i = 0; i < tempxvalues.size(); i++) {
                data.addPoint(i, tempyvalues.get(i).intValue());
                data.addXValue(i, tempxvalues.get(i));
            }
            chart.addData(data);
        }
    }

//    @Override
//    public void ReciveMessage(EMRealTimeReport emRealTimeReport) {
//        if (!BedUserCode.equals("") && BedUserCode.equals(emRealTimeReport.getBedUserCode())) {
//            onbedstatus = emRealTimeReport.getOnBedStatus();
//            onbedstatusimg.post(new Runnable() {
//
//                @Override
//                public void run() {
//                    switch (onbedstatus) {
//                        case "在床":
//                            onbedstatusimg.setBackgroundResource(R.drawable.icon_onbed);
//                            break;
//                        case "离床":
//                            onbedstatusimg.setBackgroundResource(R.drawable.icon_offbed);
//                            break;
//
//                        case "请假":
//                            onbedstatusimg.setBackgroundResource(R.drawable.icon_offduty);
//                            break;
//
//                        case "异常":
//                            onbedstatusimg.setBackgroundResource(R.drawable.icon_innormal);
//                            break;
//
//                        default:
//                            onbedstatusimg.setBackgroundResource(R.drawable.icon_offline);
//                            break;
//                    }
//                }
//            });
//
//        }
//    }

    @Override
    public void GetRealtimeData(Map<String, EMRealTimeReport> realtimeData) {
        for (EMRealTimeReport emRealTimeReport : realtimeData.values()) {

            if (BedUserCode.equals(emRealTimeReport.getBedUserCode())) {
            onbedstatus = emRealTimeReport.getOnBedStatus();
            onbedstatusimg.post(new Runnable() {

                @Override
                public void run() {
                    switch (onbedstatus) {
                        case "在床":
                            onbedstatusimg.setBackgroundResource(R.drawable.icon_onbed);
                            break;
                        case "离床":
                            onbedstatusimg.setBackgroundResource(R.drawable.icon_offbed);
                            break;

                        case "请假":
                            onbedstatusimg.setBackgroundResource(R.drawable.icon_offduty);
                            break;

                        case "异常":
                            onbedstatusimg.setBackgroundResource(R.drawable.icon_innormal);
                            break;

                        default:
                            onbedstatusimg.setBackgroundResource(R.drawable.icon_offline);
                            break;
                    }
                }
            });

                System.out.print(BedUserCode + "实时数据===============\n");
                break;
            }
        }
    }
}
