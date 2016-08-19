package com.ewell.android.sleepcareforphone.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ewell.android.common.Grobal;
import com.ewell.android.common.xmpp.XmppMsgDelegate;
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
import java.util.Iterator;
import java.util.Map;


public class FragmentSleep extends Fragment implements View.OnClickListener, XmppMsgDelegate<EMRealTimeReport> {
    private String BedUserCode = "";
    private String BedUserName = "";

    private static TextView txtdate;
    private ImageView onbedstatusimg;
    private String onbedstatus = "";

    private TextView patientname;
    private Button b1;

    private static TextView lightsleeptxt;
    private static TextView deepsleeptxt;
    private static TextView awakesleeptxt;

    private ArrayList<String> tempusercodelist = new ArrayList<String>();
    private ArrayList<String> tempusernamelist = new ArrayList<String>();

    //  private String[] areas = new String[]{"张奶奶", "王奶奶", "王爷爷", "李爷爷", "123"};
    private RadioOnClick radioOnClick = new RadioOnClick(0);
    private ListView areaRadioListView;

    private DatePickerFragment fragment;

    private static SleepProgressView mSleepProgressView;

    private static FancyChart chart;

    private SharedPreferences sp;
    // 获取到一个参数文件的编辑器。
    private SharedPreferences.Editor editor;

    private String reportDate = "";

    private static SleepViewModel sleepViewModel;


    private RelativeLayout view1;
    private LinearLayout view2;
    private RelativeLayout view3;
    private RelativeLayout view4;

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
        // 在我的设备里删除当前关注的老人,返回此页面,需要将bedusercode/name置空
        if(Grobal.getInitConfigModel().getCurUserCode().equals("")){
            BedUserCode="";
            BedUserName ="";
        }

        fragment = new DatePickerFragment();

        sp = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        editor = sp.edit();

        Date date = new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, -1);//把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        reportDate = formatter.format(date);


        sleepViewModel = new SleepViewModel();
        sleepViewModel.RefreshSleepData(reportDate);

        Grobal.getXmppManager().SetXmppMsgDelegate(this);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sleep, container, false);
//change patient button
        b1 = (Button) v.findViewById(R.id.btn1);
        b1.setOnClickListener(this);
        patientname = (TextView) v.findViewById(R.id.texttitle);
        patientname.setOnClickListener(this);
        if (!BedUserName.equals("")) {
            patientname.setText(BedUserName);
        }

        //change date
        txtdate = (TextView) v.findViewById(R.id.choosedate);
        txtdate.setText(reportDate);
        txtdate.setOnClickListener(this);

        onbedstatusimg = (ImageView) v.findViewById(R.id.onbedstatusimg);


        lightsleeptxt = (TextView) v.findViewById(R.id.lightsleep);
        lightsleeptxt.setText(sleepViewModel.getLightSleepTimespan());
        deepsleeptxt = (TextView) v.findViewById(R.id.deepsleep);
        deepsleeptxt.setText(sleepViewModel.getDeepSleepTimespan());
        awakesleeptxt = (TextView) v.findViewById(R.id.awakesleep);
        awakesleeptxt.setText(sleepViewModel.getAwakeningTimespan());


        mSleepProgressView = (SleepProgressView) v.findViewById(R.id.my_sleepprogress);
        mSleepProgressView.setMaxCount(24.0f);
        mSleepProgressView.setDeepsleepCount(sleepViewModel.getDeepSleepTimespanFloat());
        mSleepProgressView.setLightsleepCount(sleepViewModel.getLightSleepTimespanFloat());
        mSleepProgressView.setAwakeCount(sleepViewModel.getAwakeningTimespanFloat());
        mSleepProgressView.setScore(sleepViewModel.getSleepTimespan());

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
        //当前账户下没有设备,则隐藏子view,只显示背景图片
        view1 = (RelativeLayout) v.findViewById(R.id.topview1);
        view2 = (LinearLayout)v.findViewById(R.id.circleview);
        view3 = (RelativeLayout)v.findViewById(R.id.centerview);
        view4=(RelativeLayout)v.findViewById(R.id.chartview);
        if(Grobal.getInitConfigModel().getEquipmentcodeList().size()==0){
            view1.setVisibility(View.INVISIBLE);
            view2.setVisibility(View.INVISIBLE);
            view3.setVisibility(View.INVISIBLE);
            view4.setVisibility(View.INVISIBLE);
        }

        return v;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                ClickChangeBtn();
                break;
            case R.id.texttitle:
                ClickChangeBtn();
                break;
            case R.id.choosedate:
                showDatePickerFragemnt();
                break;

            default:
                System.out.print("============================none");
                break;
        }

    }

    //------------------------------------ 切换老人---------------------------
    public void ClickChangeBtn() {
        Map<String, String> tempmap = Grobal.getInitConfigModel().getUserCodeNameMap();
        tempusernamelist.clear();
        tempusercodelist.clear();

        // entrySet使用iterator遍历key和value
        Iterator<Map.Entry<String, String>> it = tempmap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            tempusercodelist.add(entry.getKey());
            tempusernamelist.add(entry.getValue());
            //   System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
        }

        int count = tempusercodelist.size();
        if (count > 0) {
            int i = 0;
            String[] usernamelist = new String[count];
            while (i < count) {
                usernamelist[i] = tempusernamelist.get(i);
                i++;
            }
            //弹窗
            AlertDialog ad = new AlertDialog.Builder(getActivity())
                    .setTitle("选择老人")
                    .setSingleChoiceItems(usernamelist, radioOnClick.getIndex(), radioOnClick)
                    .create();


            areaRadioListView = ad.getListView();
            ad.show();

            ad.getWindow().setLayout(500, 500);
        } else {
            Toast.makeText(getActivity(), "当前账户下没有关注老人!请先添加设备", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 点击单选框事件
     *
     * @author xmz
     */
    class RadioOnClick implements DialogInterface.OnClickListener {
        private int index;

        public RadioOnClick(int index) {
            this.index = index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public void onClick(DialogInterface dialog, int whichButton) {
            setIndex(whichButton);
            patientname.setText(tempusernamelist.get(index));
            BedUserCode = tempusercodelist.get(index);
            Grobal.getInitConfigModel().setCurUserCode(tempusercodelist.get(index));
            Grobal.getInitConfigModel().setCurUserName(tempusernamelist.get(index));
            editor.putString("curusercode", tempusercodelist.get(index));
            editor.putString("curusername", tempusernamelist.get(index));
            editor.commit();

            RefreshAfterSelectDate(reportDate);
            //   Toast.makeText(getActivity(), "您已经选择了： " + index + ":" + areas[index], Toast.LENGTH_SHORT).show();
            dialog.dismiss();
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

        mSleepProgressView.setDeepsleepCount(sleepViewModel.getDeepSleepTimespanFloat());
        mSleepProgressView.setLightsleepCount(sleepViewModel.getLightSleepTimespanFloat());
        mSleepProgressView.setAwakeCount(sleepViewModel.getAwakeningTimespanFloat());
        mSleepProgressView.setScore(sleepViewModel.getSleepTimespan());

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

    @Override
    public void ReciveMessage(EMRealTimeReport emRealTimeReport) {
        if (!BedUserCode.equals("") && BedUserCode.equals(emRealTimeReport.getBedUserCode())) {
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

        }
    }

}
