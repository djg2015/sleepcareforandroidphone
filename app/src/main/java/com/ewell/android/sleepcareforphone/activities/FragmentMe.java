package com.ewell.android.sleepcareforphone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ewell.android.common.Grobal;
import com.ewell.android.sleepcareforphone.R;
import com.readystatesoftware.viewbadger.BadgeView;


public class FragmentMe extends Fragment implements View.OnClickListener {
    private TextView txtPatient;
    private TextView txtDevices;
    private TextView txtWeekreport;
    private TextView txtAbout;
    private TextView txtTips;
    private TextView txtAlarms;
    private TextView txtSetting;

private BadgeView badge;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.mypatient:
                onClickPatient();
                break;
            case R.id.mydevices:
                onClickDevice();
                break;

            case R.id.myalarms:
                onClickAlarm();
                break;
            case R.id.weekreport:
                onClickWeekreport();
                break;
            case R.id.about:
                onClickAbout();
                break;
            case R.id.tips:
                onClickTip();
                break;
            case R.id.accountsetting:
                onClickSetting();
                break;
            default:
                System.out.print("============================none");
                break;
        }
    }

    public static FragmentMe newInstance(String bedusercode) {
        FragmentMe fragment = new FragmentMe();
        Bundle args = new Bundle();
        args.putString("bedusercode", bedusercode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public  void onResume(){
        super.onResume();

        int count = Grobal.getInitConfigModel().getUnhandledAlarmcodeList().size();
        if(count>0) {
            badge.setText(Integer.toString(count));
            badge.show();
        }
        else{
            badge.hide();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_me, container, false);

        txtPatient = (TextView) v.findViewById(R.id.mypatient);
        txtPatient.setOnClickListener(this);
        txtDevices = (TextView) v.findViewById(R.id.mydevices);
        txtDevices.setOnClickListener(this);
        txtAlarms = (TextView) v.findViewById(R.id.myalarms);
        txtAlarms.setOnClickListener(this);
        txtAbout = (TextView) v.findViewById(R.id.about);
        txtAbout.setOnClickListener(this);
        txtTips = (TextView) v.findViewById(R.id.tips);
        txtTips.setOnClickListener(this);
        txtSetting = (TextView) v.findViewById(R.id.accountsetting);
        txtSetting.setOnClickListener(this);
        txtWeekreport = (TextView) v.findViewById(R.id.weekreport);
        txtWeekreport.setOnClickListener(this);


         badge = new BadgeView(getContext(), txtAlarms);
        badge.setTextSize(12);
        badge.setBadgePosition(BadgeView.POSITION_CENTER);


        return v;
    }


    public void onClickPatient() {
       // Toast.makeText(getActivity(), "点击了老人信息", Toast.LENGTH_SHORT).show();
        String tempusercode = Grobal.getInitConfigModel().getCurUserCode();
        if (tempusercode.equals("")) {
            Toast.makeText(getActivity(), "请先选择一个老人再进行查看!", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(getActivity(), ModifyPatientInfoActivity.class);
            getActivity().startActivity(intent);
        }
    }

    public void onClickDevice(){
       // Toast.makeText(getActivity(), "点击了设备", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), MyDevicesActivity.class);
        getActivity().startActivity(intent);
    }

    public void onClickAlarm(){
      //  Toast.makeText(getActivity(), "点击了报警信息", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), ShowAlarmActivity.class);
        getActivity().startActivity(intent);
    }

    public void onClickWeekreport() {
        //  Toast.makeText(getActivity(), "点击了周报表", Toast.LENGTH_SHORT).show();
        String tempusercode = Grobal.getInitConfigModel().getCurUserCode();
        if (tempusercode.equals("")) {
            Toast.makeText(getActivity(), "请先选择一个老人再进行查看!", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(getActivity(), ShowWeekReportActivity.class);
            getActivity().startActivity(intent);
        }
    }

    public void onClickAbout(){
      //  Toast.makeText(getActivity(), "点击了关于", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), ReadInfoActivity.class);
        getActivity().startActivity(intent);
    }

    public void onClickTip(){
      //  Toast.makeText(getActivity(), "点击了使用技巧", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), ReadTipsActivity.class);
        getActivity().startActivity(intent);
    }

    public void onClickSetting(){
      //  Toast.makeText(getActivity(), "点击了设置", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), AccountSettingActivity.class);
        getActivity().startActivity(intent);
    }
}
