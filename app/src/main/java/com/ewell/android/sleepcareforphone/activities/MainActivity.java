package com.ewell.android.sleepcareforphone.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import com.ewell.android.common.Grobal;
import com.ewell.android.sleepcareforphone.R;
import com.ewell.android.sleepcareforphone.common.BadgeUtil;
import com.ewell.android.sleepcareforphone.common.CheckUpdate;
import com.ewell.android.sleepcareforphone.common.pushnotification.PushService;


/**
 * Created by lillix on 8/3/16.
 */
public class MainActivity extends Activity {

    private int count = 0;
    private Thread badgeThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取deviceid
        SharedPreferences prefs = getSharedPreferences(PushService.TAG, MODE_PRIVATE);
        if (prefs.getBoolean("isFirstlaunch", true)) {
            String id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("deviceID", id);
            editor.putBoolean("isFirstlaunch", false);
            editor.commit();
        }

        CheckUpdate ck = new CheckUpdate(this, true, MainActivity.this);
        ck.CheckVersionTask();

        badgeThread = new Thread(runbadge);
        badgeThread.start();
    }

    Runnable runbadge = new Runnable() {
        @Override
        public void run() {
            while (true) {
                RefreshBadgeNumber();
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    };

    private void RefreshBadgeNumber() {
        count = Grobal.getInitConfigModel().getUnhandledAlarmCount();
        if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
            BadgeUtil.setBadgeCount(MainActivity.this, count, BadgeUtil.Platform.mi);
        } else if (Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
            BadgeUtil.setBadgeCount(MainActivity.this, count, BadgeUtil.Platform.samsung);
        } else if (Build.MANUFACTURER.equalsIgnoreCase("htc")) {
            BadgeUtil.setBadgeCount(MainActivity.this, count, BadgeUtil.Platform.htc);
        } else if (Build.MANUFACTURER.equalsIgnoreCase("lg")) {
            BadgeUtil.setBadgeCount(MainActivity.this, count, BadgeUtil.Platform.lg);
        } else if (Build.MANUFACTURER.equalsIgnoreCase("sony")) {
            BadgeUtil.setBadgeCount(MainActivity.this, count, BadgeUtil.Platform.sony);
        }
        //else if (Build.MANUFACTURER.equalsIgnoreCase("huawei")) {
        //  BadgeUtil.setBadgeCount(MainActivity.this, count, BadgeUtil.Platform.huawei);
        //}
    }

}
