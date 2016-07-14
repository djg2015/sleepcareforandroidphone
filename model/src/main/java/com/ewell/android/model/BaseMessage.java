package com.ewell.android.model;

import org.jdom.JDOMException;

import java.io.IOException;

/**
 * Created by Dongjg on 2016-7-12.
 */
public abstract class BaseMessage {
    private BaseMessage() {
    }

    private XmppMsgSubject _subject = null;

    public XmppMsgSubject getSubject() {
        return _subject;
    }

    /*
    * 初始消息对象
    * */
    public BaseMessage(String operate, String bizCode) {
        this._subject = new XmppMsgSubject(operate,bizCode);
    }

    /*
    * 初始消息对象
    * */
    public BaseMessage(String operate) {
        this._subject = new XmppMsgSubject(operate);
    }

    /*
   * 初始消息对象
   * */
    public BaseMessage(XmppMsgSubject subject) {
        this._subject =subject;
    }

    /*
   * 模型转为xml
   * */
    public   String ToSubjectXml()
    {
        return  this.getSubject().ToSubjectXml();
    }

    /*
   * 模型转为xml
   * */
    public abstract String ToBodytXml();

}
