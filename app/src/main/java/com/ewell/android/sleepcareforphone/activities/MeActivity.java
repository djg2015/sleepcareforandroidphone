package com.ewell.android.sleepcareforphone.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ewell.android.common.Grobal;
import com.ewell.android.sleepcareforphone.R;

/**
 * Created by lillix on 10/28/16.
 */
public class MeActivity extends Activity implements View.OnClickListener{
    private TextView txtMyself;
    private TextView txtWeekreport;
    private TextView txtAlarms;
    private TextView txtSetting;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);

        txtMyself = (TextView)findViewById(R.id.myself);
        String accountname = Grobal.getInitConfigModel().getLoginUserName();
        txtMyself.setText(accountname);

txtAlarms = (TextView)findViewById(R.id.myalarms);
        txtAlarms.setOnClickListener(this);
        txtWeekreport = (TextView)findViewById(R.id.weekreport);
        txtWeekreport.setOnClickListener(this);
        txtSetting = (TextView)findViewById(R.id.accountsetting);
        txtSetting.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.myalarms:
                onClickAlarm();
                break;
            case R.id.weekreport:
                onClickWeekreport();
                break;

            case R.id.accountsetting:
                onClickSetting();
                break;
            default:
                System.out.print("============================none");
                break;
        }
    }

    public void onClickAlarm(){
        //  Toast.makeText(getActivity(), "点击了报警信息", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ShowAlarmActivity.class);
       this.startActivity(intent);
    }

    public void onClickWeekreport() {
        //  Toast.makeText(getActivity(), "点击了周报表", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ShowWeekReportActivity.class);
           this.startActivity(intent);

    }



    public void onClickSetting(){
        //  Toast.makeText(getActivity(), "点击了设置", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, AccountSettingActivity.class);
       this.startActivity(intent);
    }

    public void ClickClose(View view) {

        this.finish();
    }
}
