package com.ewell.android.common;

import com.ewell.android.common.config.InitConfigModel;
import com.ewell.android.common.log.LogManager;
import com.ewell.android.common.xmpp.XmppManager;

/**
 * 全局使用对象
 */
public class Grobal {
    private static XmppManager _xmppManager;
    public static XmppManager getXmppManager() {
        return _xmppManager;
    }
    public static void setXmppManager(XmppManager xmppManager) {
        _xmppManager = xmppManager;
    }

    private static InitConfigModel _initConfigModel;
    public static InitConfigModel getInitConfigModel() {
        return _initConfigModel;
    }
    public static void setInitConfigModel(InitConfigModel initConfigModel) {
        _initConfigModel = initConfigModel;
    }

    private static LogManager _logManager;
    public static LogManager getLogManager() {
        return _logManager;
    }
    public static void setLogManager(LogManager logManager) {
        _logManager = logManager;
    }

}
