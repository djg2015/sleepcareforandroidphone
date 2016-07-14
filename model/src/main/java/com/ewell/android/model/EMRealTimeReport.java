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
public class EMRealTimeReport extends BaseMessage {

    private String HR = "";
    public String getHR() {
        return HR;
    }

    private String RR = "";
    public String getRR() {
        return RR;
    }

    private String time = "";
    public String getTime() {
        return time;
    }

    public EMRealTimeReport(XmppMsgSubject subject) {
        super(subject);
    }

    public static EMRealTimeReport XmlToMessage(String sujectXml, String bodyXml) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder(false);
        EMRealTimeReport result = new EMRealTimeReport(XmppMsgSubject.PareXmlToSubject(sujectXml));
        Reader returnQuote = new StringReader(bodyXml);
        Document doc = builder.build(returnQuote);
        Element root = doc.getRootElement();
//        Element segment= user.getChild("LoginName");
//        String email=book.getAttributeValue(“email”);
        result.HR =root.getChildTextTrim("HR");
        result.RR =root.getChildTextTrim("RR");
        result.time =root.getChildTextTrim("MsgTime");
        returnQuote.close();
        return result;
    }

    @Override
    public String ToBodytXml() {
        return null;
    }
}
