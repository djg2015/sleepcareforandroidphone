package com.ewell.android.sleepcareforphone;

import android.app.Application;
import android.os.Environment;

import com.ewell.android.common.Grobal;
import com.ewell.android.common.config.InitConfigModel;
import com.ewell.android.common.log.LogManager;
import com.ewell.android.common.xmpp.XmppManager;

import java.io.File;

/**
 * Created by Dongjg on 2016-7-13.
 */
public class application extends Application {
    @Override
    public void onCreate()
    {
        super.onCreate();

        //此处初始化程序用的所有对象
        //此处应该从配置文件加载相关初始值，这里只是示例，请自己冲配置文件中读取
        InitConfigModel initConfigModel = new InitConfigModel();
        initConfigModel.setXmppServerIP("122.224.242.241");
        initConfigModel.setXmppServerPort(7018);
        initConfigModel.setXmppServerJID("ewell@122.224.242.241");
        initConfigModel.setXmppUserPWD("123");

        Grobal.setInitConfigModel(initConfigModel);
        Grobal.setXmppManager(XmppManager.GetInstance(initConfigModel));

        //初始化日志文件
        LogManager logManager = LogManager.GetInstance(Environment.getExternalStorageDirectory().getPath() + File.separator
                    + "sleepcareforphone" + File.separator + "logs" + File.separator + "log.txt");
        Grobal.setLogManager(logManager);
    }
}
