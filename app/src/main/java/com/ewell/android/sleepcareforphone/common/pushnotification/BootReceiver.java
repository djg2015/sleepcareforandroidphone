package com.ewell.android.sleepcareforphone.common.pushnotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by lillix on 8/4/16.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.example.lillix.pushnotification.destroy")) {

            //在这里写重新启动service的相关操作
              PushService.actionStart(context);
        }
//        else if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
//
////检查Service状态
//            boolean isServiceRunning = false;
//            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//                if ("com.example.lillix.pushnotificationdemo.PushService".equals(service.service.getClassName()))
//
//                {
//                    isServiceRunning = true;
//                }
//
//            }
//            if (!isServiceRunning) {
//                PushService.actionStart(context);
//            }
//        }
    }

}
