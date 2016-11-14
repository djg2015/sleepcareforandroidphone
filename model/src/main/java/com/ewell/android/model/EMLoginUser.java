package com.ewell.android.model;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Created by Dongjg on 2016-7-12.
 */
public class EMLoginUser extends BaseMessage {

    public EMLoginUser(XmppMsgSubject subject) {
        super(subject);
    }

    private String loginName = "";
    public String getLoginName() {
        return loginName;
    }

    private String loginPassWord = "";
    public String getLoginPassWord() {
        return loginPassWord;
    }

    private String mainCode = "";
    public String getMainCode() {
        return mainCode;
    }

    public static EMLoginUser XmlToMessage(String sujectXml, String bodyXml) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder(false);
        EMLoginUser result = new EMLoginUser(XmppMsgSubject.PareXmlToSubject(sujectXml));
        Reader returnQuote = new StringReader(bodyXml);
        Document doc = builder.build(returnQuote);
        Element root = doc.getRootElement();


        result.loginName=root.getChildTextTrim("LoginName");
        result.mainCode =root.getChildTextTrim("MainCode");
        result.loginPassWord =root.getChildTextTrim("LoginPassword");
       // System.out.print(result.mainCode +"   loginuser============\n");

        returnQuote.close();
        return result;
    }

    @Override
    public String ToBodytXml() {
        return null;
    }

}
