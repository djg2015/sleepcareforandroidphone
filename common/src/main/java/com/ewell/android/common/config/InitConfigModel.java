package com.ewell.android.common.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    private String maincode;

    public String getMaincode() {
        return maincode;
    }
    public void setMaincode(String maincode){this.maincode = maincode;}


    //----------自定义方法-----------

    private Map<String, String> UserCodeEquipmentMap = new HashMap<String, String>();
    public  Map<String, String> getUserCodeEquipmentMap(){return UserCodeEquipmentMap;}
    public void setUserCodeEquipmentMap(Map<String, String> tempmap){UserCodeEquipmentMap = tempmap;}


   private Map<String, String> UserCodeNameMap = new HashMap<String, String>();
    public  Map<String, String> getUserCodeNameMap(){return UserCodeNameMap;}
    public void setUserCodeNameMap(Map<String, String> tempmap){UserCodeNameMap = tempmap;}

    private String curUserCode;
    public String getCurUserCode(){return curUserCode;}
    public void setCurUserCode(String curusercode){curUserCode = curusercode;}

    private String curUserName;
    public String getCurUserName(){return curUserName;}
    public void setCurUserName(String curusername){curUserName = curusername;}


private Integer unhandledAlarmCount=0;
    public Integer getUnhandledAlarmCount(){return unhandledAlarmCount;}
    public void setUnhandledAlarmCount(Integer count){unhandledAlarmCount = count;}

    private ArrayList<String> bedusercodeList=new ArrayList<String>();
    public ArrayList<String> getBedusercodeList(){return bedusercodeList;}
    public void setBedusercodeList(ArrayList<String> bedusercodelist){bedusercodeList = bedusercodelist;}


}
