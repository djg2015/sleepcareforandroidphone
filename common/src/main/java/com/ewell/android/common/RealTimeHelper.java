package com.ewell.android.common;

import com.ewell.android.common.xmpp.XmppManager;
import com.ewell.android.common.xmpp.XmppMsgDelegate;
import com.ewell.android.model.EMRealTimeReport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lillix on 11/2/16.
 */
public class RealTimeHelper implements XmppMsgDelegate<EMRealTimeReport>{

    private static RealTimeHelper _realtimehelper = null;
private Map<String, EMRealTimeReport> realtimeCashes = null;
private Map<String, GetRealtimeDataDelegate> delegatelist = null;

    private  Runnable runnable ;


    private RealTimeHelper() {
    }

    public static RealTimeHelper GetInstance() {
        if (_realtimehelper == null) {
            _realtimehelper = new RealTimeHelper();
            _realtimehelper.realtimeCashes = new HashMap<String, EMRealTimeReport>();
            _realtimehelper.delegatelist = new HashMap<String, GetRealtimeDataDelegate>();

            XmppManager.GetInstance(Grobal.getInitConfigModel()).SetXmppMsgDelegate(_realtimehelper);
        }
        return _realtimehelper;
    }

    /*
    * 设置事件处理委托
    * */
    public void SetDelegate(String name, GetRealtimeDataDelegate delegate) {
        this.delegatelist.put( name, delegate);
    }

    //每隔2秒从缓冲区中取数据，让代理处理需要显示的数据
    public void startRealTimer(){


    }

   public void ShowRealTimeData(){
        for (String key: delegatelist.keySet()){
            if (delegatelist.get(key) != null){
                delegatelist.get(key).GetRealtimeData(realtimeCashes);

            }
        }
    }



    @Override
    public void ReciveMessage(EMRealTimeReport emRealTimeReport) {
        String reportkey = emRealTimeReport.getBedUserCode();
        ArrayList<String> tempbeduserlist = Grobal.getInitConfigModel().getBedusercodeList();
        if(tempbeduserlist.contains(reportkey)){
         realtimeCashes.put(reportkey,emRealTimeReport);

        }

    }


}

