package com.ewell.android.model;

import java.util.Random;

/**
 * Created by Dongjg on 2016-7-12.
 */
public class XmppMsgSubject {
    private String _postMethod = "Request";
    private String _authCode = "1234567890";
    private String _version = "1.0";
    private String _bizCode = "sleepcareforiphone";
    private String _operate = "";
public String get_operate(){return this._operate;}


    private String _requestID = null;

    public String getRequestID() {
        return _requestID;
    }

    private XmppMsgSubject() {
    }

    public XmppMsgSubject(String operate) {
        this(operate, "sleepcareforiphone");
    }

    public XmppMsgSubject(String operate, String bizCode) {
        this(operate, bizCode, "");
    }

    public XmppMsgSubject(String operate, String bizCode, String requestID) {
        this._operate = operate;
        this._bizCode = bizCode;
        this._requestID = requestID;
    }

    /*
    * 将subject xml字符串转换为subject对象
    * */
    public static XmppMsgSubject PareXmlToSubject(String sujectXml) {
        String[] subjects = sujectXml.split(":");
        String[] before = subjects[0].split("/");
        String[] end = subjects[1].split("/");
        return new XmppMsgSubject(end[0], end[1], before[3]);
    }

    /*
    * 获取对应的subjectXml
    * */
    public String ToSubjectXml() {
        //生成requestID
        this._requestID = getStringRandom(16);
        return this._postMethod + "/" + this._authCode + "/" + this._version + "/" + this._requestID + ":"
                + this._bizCode + "/" + this._operate;

    }


    //生成随机数字和字母,
    private String getStringRandom(int length) {

        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
}
