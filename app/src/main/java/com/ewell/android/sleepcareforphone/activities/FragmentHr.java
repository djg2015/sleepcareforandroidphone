package com.ewell.android.sleepcareforphone.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ewell.android.common.Grobal;
import com.ewell.android.common.xmpp.XmppMsgDelegate;
import com.ewell.android.model.EMRealTimeReport;
import com.ewell.android.sleepcareforphone.R;
import com.ewell.android.sleepcareforphone.common.ProgressView;
import com.ewell.android.sleepcareforphone.common.fancychart.ChartData;
import com.ewell.android.sleepcareforphone.common.fancychart.FancyChart;
import com.ewell.android.sleepcareforphone.common.fancychart.FancyChartPointListener;
import com.ewell.android.sleepcareforphone.common.fancychart.Point;
import com.ewell.android.sleepcareforphone.viewmodels.HRViewModel;

import java.util.ArrayList;

public class FragmentHr extends Fragment implements View.OnClickListener, XmppMsgDelegate<EMRealTimeReport> {

    private String BedUserCode = "";
    private String BedUserName = "";

    private TextView patientname;

    private ProgressView mProgressView;

    private FancyChart chart;

    private TextView button1;
    private TextView button2;
    private TextView button3;
    private int button_select = Color.parseColor("#56a3fd");
    private int button_default = Color.parseColor("#929292");


    private SharedPreferences sp;
    // 获取到一个参数文件的编辑器。
    private SharedPreferences.Editor editor;


    private HRViewModel hrViewModel;
    private int SelectedIndex = 1;

    private String currentHR;
    private String avgHR;


    private String onbedstatus = "";
    private ImageView onbedstatusImg;
    private ImageView alarmImg;


    private RelativeLayout view1;
    private LinearLayout view2;
    private RelativeLayout view3;
    private RelativeLayout view4;
    private Button back;

    //从父activty传参
    public static FragmentHr newInstance(String bedusercode, String bedusername) {
        FragmentHr fragmentHr = new FragmentHr();
        Bundle args = new Bundle();
        args.putString("bedusercode", bedusercode);
        args.putString("bedusername", bedusername);
        fragmentHr.setArguments(args);
        return fragmentHr;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BedUserCode = getArguments().getString("bedusercode", "");
        BedUserName = getArguments().getString("bedusername", "");
        // 在我的设备里删除当前关注的老人,返回此页面,需要将bedusercode/name置空
        if (Grobal.getInitConfigModel().getCurUserCode().equals("")) {
            BedUserCode = "";
            BedUserName = "";
        }


        sp = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        editor = sp.edit();

        hrViewModel = new HRViewModel();
        hrViewModel.RefreshHRData();

        Grobal.getXmppManager().SetXmppMsgDelegate(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hr, container, false);

//change patient button

        patientname = (TextView) v.findViewById(R.id.texttitle);
        patientname.setOnClickListener(this);
        if (!BedUserName.equals("")) {
            patientname.setText(BedUserName);
        }

        onbedstatusImg = (ImageView) v.findViewById(R.id.onbedstatusimg);
        alarmImg = (ImageView) v.findViewById(R.id.alarmimg);



        //hr circle
        mProgressView = (ProgressView) v.findViewById(R.id.my_progress);
        mProgressView.setMaxCount(120.0f);
        mProgressView.setProgressviewType("心率");

        //hr chart
        chart = (FancyChart) v.findViewById(R.id.my_hrchart);
        chart.setOnPointClickListener(new FancyChartPointListener() {

            @Override
            public void onClick(Point point) {
                Toast.makeText(getActivity(), "平均心率" + point.y + "次/分", Toast.LENGTH_SHORT).show();

            }
        });
        ShowHourChart();


        //select chart button
        button1 = (TextView) v.findViewById(R.id.button1);
        button1.setOnClickListener(this);
        button2 = (TextView) v.findViewById(R.id.button2);
        button2.setOnClickListener(this);
        button3 = (TextView) v.findViewById(R.id.button3);
        button3.setOnClickListener(this);


//        view1 = (RelativeLayout) v.findViewById(R.id.topview1);
//        view2 = (LinearLayout) v.findViewById(R.id.circleview);
//        view3 = (RelativeLayout) v.findViewById(R.id.centerview);
//        view4 = (RelativeLayout) v.findViewById(R.id.chartview);
//
//
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
            case R.id.button1:
                button1.setTextColor(button_select);
                button2.setTextColor(button_default);
                button3.setTextColor(button_default);
                SelectedIndex = 1;
                ShowHourChart();
                break;
            case R.id.button2:
                button2.setTextColor(button_select);
                button1.setTextColor(button_default);
                button3.setTextColor(button_default);
                SelectedIndex = 2;
                ShowWeekChart();
                break;
            case R.id.button3:
                button3.setTextColor(button_select);
                button2.setTextColor(button_default);
                button1.setTextColor(button_default);
                SelectedIndex = 3;
                ShowMonthChart();
                break;

            default:
                System.out.print("============================none");
                break;
        }

    }

    //------------------------点击按钮选择查看chart类型
    public void ShowHourChart() {


        chart.clearValues();
        ChartData data = new ChartData(ChartData.LINE_COLOR_GREEN);
        ArrayList<String> tempxlist = hrViewModel.getHrDay_xValues();
        ArrayList<String> tempylist = hrViewModel.getHrDay_yValues();

        if (tempxlist != null && tempxlist.size() > 0) {

            //   int[] yValues = new int[]{4, 3, 9, 5, 35, 30, 33, 32, 46, 33, 56, 42};
            for (int i = 0; i < tempxlist.size(); i++) {
                data.addPoint(i, Integer.parseInt(tempylist.get(i)));
                data.addXValue(i, tempxlist.get(i));
            }
            chart.addData(data);
        }
    }

    public void ShowWeekChart() {


        chart.clearValues();
        ChartData data = new ChartData(ChartData.LINE_COLOR_GREEN);
        ArrayList<String> tempxlist = hrViewModel.getHrWeek_xValues();
        ArrayList<String> tempylist = hrViewModel.getHrWeek_yValues();

        //  int[] yValues = new int[]{41, 32, 19, 15, 25, 33, 23, 32, 46, 43, 56, 42};
        if (tempxlist != null && tempxlist.size() > 0) {
            for (int i = 0; i < tempxlist.size(); i++) {
                data.addPoint(i, Integer.parseInt(tempylist.get(i)));
                data.addXValue(i, tempxlist.get(i));
            }
            chart.addData(data);
        }
    }

    public void ShowMonthChart() {

        chart.clearValues();
        ChartData data = new ChartData(ChartData.LINE_COLOR_GREEN);

        ArrayList<String> tempxlist = hrViewModel.getHrMonth_xValues();
        ArrayList<String> tempylist = hrViewModel.getHrMonth_yValues();
        //  int[] yValues = new int[]{21, 22, 29, 25, 25, 43, 33, 32, 36, 43, 46, 42};

        if (tempxlist != null && tempxlist.size() > 0) {
            if (tempxlist.size() > 20) {
                for (int i = 0; i < tempxlist.size(); i += 2) {
                    data.addPoint(i, Integer.parseInt(tempylist.get(i)));
                    data.addXValue(i, tempxlist.get(i));
                }
            } else {
                for (int i = 0; i < tempxlist.size(); i++) {
                    data.addPoint(i, Integer.parseInt(tempylist.get(i)));
                    data.addXValue(i, tempxlist.get(i));
                }
            }
            chart.addData(data);
        }
    }

    @Override
    public void ReciveMessage(EMRealTimeReport emRealTimeReport) {
        if (!BedUserCode.equals("") && BedUserCode.equals(emRealTimeReport.getBedUserCode())) {

            currentHR = emRealTimeReport.getHR();
            avgHR = emRealTimeReport.getLastedAvgHR();
            onbedstatus = emRealTimeReport.getOnBedStatus();


            mProgressView.post(new Runnable() {

                @Override
                public void run() {
                    if (!avgHR.equals("")) {
                        mProgressView.setAvgCount(Float.parseFloat(avgHR));
                    }



                    if (!currentHR.equals("")) {
                        int score = Integer.parseInt(currentHR);
                        mProgressView.setCurrentCount(score);
                        mProgressView.setScore(score);
                        if (onbedstatus.equals("在床")) {
                            if (score > 80 || score < 20) {
                                alarmImg.setBackgroundResource(R.drawable.img_alarm);
                            } else {
                                alarmImg.setBackgroundResource(R.drawable.img_noalarm);
                            }
                        }
                    }


                    switch (onbedstatus) {
                        case "在床":
                            onbedstatusImg.setBackgroundResource(R.drawable.icon_onbed);
                            break;
                        case "离床":
                            onbedstatusImg.setBackgroundResource(R.drawable.icon_offbed);
                            break;

                        case "请假":
                            onbedstatusImg.setBackgroundResource(R.drawable.icon_offduty);
                            break;

                        case "异常":
                            onbedstatusImg.setBackgroundResource(R.drawable.icon_innormal);
                            break;

                        default:
                            onbedstatusImg.setBackgroundResource(R.drawable.icon_offline);
                            break;
                    }


                }

            });
        }
    }




}

