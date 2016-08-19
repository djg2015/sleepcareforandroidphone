package com.ewell.android.common.xmpp;

import com.ewell.android.common.config.InitConfigModel;
import com.ewell.android.common.exception.EwellException;
import com.ewell.android.common.exception.ExceptionEnum;
import com.ewell.android.model.AlarmList;
import com.ewell.android.model.BaseMessage;
import com.ewell.android.model.EMRealTimeReport;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Dongjg on 2016-7-12.
 */
public class XmppManager implements XmppMsgDelegate<String> {
    private static XmppManager _xmppManager = null;
    private XmppHelper xmppHelper;
    private HashMap requestQuene = null;
    private XmppMsgDelegate xmppMsgDelegate = null;

    private XmppManager() {
    }

    public static XmppManager GetInstance(InitConfigModel initConfigModel) {
        if (_xmppManager == null) {
            _xmppManager = new XmppManager();
            _xmppManager.xmppHelper = new XmppHelper(initConfigModel, _xmppManager);
            _xmppManager.requestQuene = new HashMap<String, Object>();
        }
        return _xmppManager;
    }

    /*
    * 设置事件处理委托
    * */
    public void SetXmppMsgDelegate(XmppMsgDelegate xmppMsgDelegate) {
        this.xmppMsgDelegate = xmppMsgDelegate;
    }


    /*
    * 连接服务器
    * */
    public boolean Connect() {
        try {
            this.xmppHelper.Connect();
        } catch (EwellException ex) {
            return false;
        }
        return true;
    }

    /*
    * 发送数据，并等待数据相应（相应超时为10秒）
    * */
    public BaseMessage Send(BaseMessage param) throws EwellException {
        Object result = null;
        try {

            this.xmppHelper.SendMessage(param.ToSubjectXml(), param.ToBodytXml());
            String key = param.getSubject().getRequestID();
            this.requestQuene.put(key, null);

            Date curNow = new Date();
            while (this.requestQuene.get(key) == null) {
                Date run = new Date();
                long between = (run.getTime() - curNow.getTime()) / 1000;
                if (between >= 10) {
                    throw new EwellException(ExceptionEnum.TimeOutError);
                }

               // Thread.sleep(10);
            }
            result = this.requestQuene.get(key);
            this.requestQuene.remove(key);
        } catch (Exception e) {
            throw new EwellException(ExceptionEnum.TimeOutError);
        }

        return (BaseMessage) result;
    }

    /*
   * 发送数据(异步)
   * */
    public void SendAsync(BaseMessage param) throws EwellException {
        this.xmppHelper.SendMessage(param.ToSubjectXml(), param.ToBodytXml());
    }

    /*
    * 关闭连接
    * */
    public void Close() {
        this.xmppHelper.CloseConnection();
    }

    /*
    * 监听到xmpp好友消息
    * */
    @Override
    public void ReciveMessage(String xml) {
        try {
            BaseMessage msg = ParseModel(xml);


            if (msg != null) {
                if (msg.getClass().isAssignableFrom(EMRealTimeReport.class)) {
                    if (this.xmppMsgDelegate != null) {
                        xmppMsgDelegate.ReciveMessage((EMRealTimeReport)msg);
                    }
                }
                else if(msg.getClass().isAssignableFrom(AlarmList.class) && !msg.getSubject().get_operate().equals("sleepcareforiphone")){
                    if (this.xmppMsgDelegate != null) {
                        xmppMsgDelegate.ReciveMessage((AlarmList)msg);
                    }
                }
                else {
                    this.requestQuene.put(msg.getSubject().getRequestID(), msg);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private BaseMessage ParseModel(String xml) {
        try {
            String subject = xml.split("</subject>")[0].split("<subject>")[1];
            String body = xml.split("</body>")[0].split("<body>")[1];
            return MessageFactory.GetMessage(decodeString(subject), decodeString(body));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 还原字符串中特殊字符
     */
    private String decodeString(String strData) {
        strData = replaceString(strData, "&lt;", "<");
        strData = replaceString(strData, "&gt;", ">");
        strData = replaceString(strData, "&apos;", "&apos;");
        strData = replaceString(strData, "&quot;", "\"");
        strData = replaceString(strData, "&amp;", "&");
        return strData;
    }

    /**
     * 替换一个字符串中的某些指定字符
     *
     * @param strData     String 原始字符串
     * @param regex       String 要替换的字符串
     * @param replacement String 替代字符串
     * @return String 替换后的字符串
     */
    private String replaceString(String strData, String regex,
                                 String replacement) {
        if (strData == null) {
            return null;
        }
        int index;
        index = strData.indexOf(regex);
        String strNew = "";
        if (index >= 0) {
            while (index >= 0) {
                strNew += strData.substring(0, index) + replacement;
                strData = strData.substring(index + regex.length());
                index = strData.indexOf(regex);
            }
            strNew += strData;
            return strNew;
        }
        return strData;
    }
}
