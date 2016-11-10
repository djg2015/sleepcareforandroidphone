package com.ewell.android.sleepcareforphone.activities;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ewell.android.bll.DataFactory;
import com.ewell.android.common.Grobal;
import com.ewell.android.common.exception.EwellException;
import com.ewell.android.sleepcareforphone.R;
import com.ewell.android.sleepcareforphone.common.pushnotification.PushService;

/**
 * Created by lillix on 7/21/16.
 */
public class AccountSettingActivity extends Activity {

    private ToggleButton alarmswitch;
private SharedPreferences.Editor editor;
    private String deviceid;
    private String loginname;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_accountsetting);

        deviceid = getSharedPreferences(PushService.TAG, MODE_PRIVATE).getString("deviceID","");
        loginname = Grobal.getInitConfigModel().getLoginUserName();

        editor = getSharedPreferences("config", MODE_PRIVATE).edit();

        alarmswitch = (ToggleButton)findViewById(R.id.switch_alarm);
        alarmswitch.setChecked(getSharedPreferences("config", MODE_PRIVATE).getBoolean("notificationflag",true));

        alarmswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                try{
                if(isChecked){
                    //选中
                    editor.putBoolean("notificationflag",true);
                    editor.commit();
                    DataFactory.GetSleepcareforPhoneManage().OpenNotificationForAndroid(deviceid, loginname);
                  //  PushService.actionStart(AccountSettingActivity.this);

                }else{
                    //未选中
                    editor.putBoolean("notificationflag",false);
                    editor.commit();
                    DataFactory.GetSleepcareforPhoneManage().CloseNotificationForAndroid(deviceid, loginname);
                  //  PushService.actionStop(AccountSettingActivity.this);
                }
                }catch (EwellException ex) {
                    Toast.makeText(AccountSettingActivity.this,ex.get_exceptionMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });// 添加监听事件
    }


    public void ClickModifyAccount(View view) {

        Intent intent = new Intent(this, ModifyAccountActivity.class);
        this.startActivity(intent);
    }

    public void ClickSoftwareUpdate(View view) {

        Intent intent = new Intent(this, UpdateActivity.class);
        this.startActivity(intent);
    }



    public void ClickClose(View view) {
if(getSharedPreferences("config", MODE_PRIVATE).getBoolean("notificationflag",true)){
    PushService.actionStart(AccountSettingActivity.this);
}
        this.finish();
    }

    public void ClickLogout(View view) {
        Grobal.getXmppManager().SetXmppMsgDelegate(null);
        editor.putBoolean("isLogin",false);
        editor.commit();

        NotificationManager nm =(NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancelAll();

        try{
        DataFactory.GetSleepcareforPhoneManage().CloseNotificationForAndroid(deviceid, loginname);
        PushService.actionStop(AccountSettingActivity.this);
        }catch (EwellException ex) {
            Toast.makeText(AccountSettingActivity.this,ex.get_exceptionMsg(), Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);

    }
}
