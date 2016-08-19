package com.ewell.android.model;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Created by lillix on 7/20/16.
 */
public class BedUserInfo extends BaseMessage {

    public BedUserInfo(XmppMsgSubject subject) {
        super(subject);
    }

    private String BedUserCode = "";

    public String getBedUserCode() {
        return BedUserCode;
    }

    private String BedUserName = "";

    public String getBedUserName() {
        return BedUserName;
    }

    //1 MALE; 2 FEMALE
    private String Sex = "";

    public String getSex() {
        return Sex;
    }

    private String MobilePhone = "";

    public String getMobilePhone() {
        return MobilePhone;
    }

    private String Address = "";

    public String getAddress() {
        return Address;
    }

    public static BedUserInfo XmlToMessage(String sujectXml, String bodyXml) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder(false);
        BedUserInfo result = new BedUserInfo(XmppMsgSubject.PareXmlToSubject(sujectXml));
        Reader returnQuote = new StringReader(bodyXml);
        Document doc = builder.build(returnQuote);
        Element root = doc.getRootElement();

        result.BedUserCode=root.getChildTextTrim("BedUserCode");
        result.BedUserName =root.getChildTextTrim("BedUserName");
        result.Sex =root.getChildTextTrim("Sex");
        result.MobilePhone =root.getChildTextTrim("MobilePhone");
        result.Address =root.getChildTextTrim("Address");

        returnQuote.close();
        return result;
    }

    @Override
    public String ToBodytXml() {
        return null;
    }
}
