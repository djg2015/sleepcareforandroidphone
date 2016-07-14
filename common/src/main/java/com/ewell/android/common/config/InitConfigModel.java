package com.ewell.android.common.config;

/**
 * Created by Dongjg on 2016-7-12.
 */
public class InitConfigModel {
    //---------外部使用属性-------------
    private String xmppServerIP;

    public String getXmppServerIP() {
        return xmppServerIP;
    }

    public void setXmppServerIP(String xmppServerIP) {
        this.xmppServerIP = xmppServerIP;
    }

    private int xmppServerPort;

    public int getXmppServerPort() {
        return xmppServerPort;
    }

    public void setXmppServerPort(int xmppServerPort) {
        this.xmppServerPort = xmppServerPort;
    }

    private String xmppServerJID;

    public String getXmppServerJID() {
        return xmppServerJID;
    }

    public void setXmppServerJID(String xmppServerJID) {
        this.xmppServerJID = xmppServerJID;
    }

    private String xmppUserName;

    public String getXmppUserName() {
        return xmppUserName;
    }

    public void setXmppUserName(String xmppUserName) {
        this.xmppUserName = xmppUserName;
    }

    private String xmppUserPWD;

    public String getXmppUserPWD() {
        return xmppUserPWD;
    }

    public void setXmppUserPWD(String xmppUserPWD) {
        this.xmppUserPWD = xmppUserPWD;
    }

    private String loginUserName;

    public String getLoginUserName() {
        return loginUserName;
    }

    public void setLoginUserName(String loginUserName) {
        this.loginUserName = loginUserName;
    }

    private String loginUserPWD;

    public String getLoginUserPWD() {
        return loginUserPWD;
    }

    public void setLoginUserPWD(String loginUserPWD) {
        this.loginUserPWD = loginUserPWD;
    }
    //----------自定义方法-----------

}
