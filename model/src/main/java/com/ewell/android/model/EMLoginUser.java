package com.ewell.android.model;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.parsers.SAXParser;

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

    private String jid = "";

    public String getJID() {
        return jid;
    }

    public static EMLoginUser XmlToMessage(String sujectXml, String bodyXml) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder(false);
        EMLoginUser result = new EMLoginUser(XmppMsgSubject.PareXmlToSubject(sujectXml));
        Reader returnQuote = new StringReader(bodyXml);
        Document doc = builder.build(returnQuote);
        Element root = doc.getRootElement();
//        Element segment= user.getChild("LoginName");
//        String email=book.getAttributeValue("email");
        result.loginName=root.getChildTextTrim("LoginName");
        result.jid =root.getChildTextTrim("JID");
        result.loginPassWord =root.getChildTextTrim("LoginPassword");
        returnQuote.close();
        return result;
    }

    @Override
    public String ToBodytXml() {
        return null;
    }

}
