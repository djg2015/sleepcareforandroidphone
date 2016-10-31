package com.ewell.android.model;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lillix on 10/28/16.
 */
public class BedUserList extends BaseMessage {

    public BedUserList(XmppMsgSubject subject) {
        super(subject);
    }

    private ArrayList<BedUserInfo> beduserinfoList;

    public ArrayList<BedUserInfo> getBeduserinfoInfoList() {
        return beduserinfoList;
    }

    public static BedUserList XmlToMessage(String sujectXml, String bodyXml) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder(false);
        BedUserList result = new BedUserList(XmppMsgSubject.PareXmlToSubject(sujectXml));
        Reader returnQuote = new StringReader(bodyXml);
        Document doc = builder.build(returnQuote);
        Element root = doc.getRootElement();

        Element list = root.getChild("BedUserList");
        List beduserlist = list.getChildren();
        result.beduserinfoList = new ArrayList<BedUserInfo>();


        for (int i = 0; i < beduserlist.size(); i++) {
           BedUserInfo tempBeduserInfo = new BedUserInfo();
            Element beduser = (Element) beduserlist.get(i);

            tempBeduserInfo.setBedUserCode(beduser.getChildTextTrim("BedUserCode"));
            tempBeduserInfo.setBedUserName(beduser.getChildTextTrim("BedUserName"));
            tempBeduserInfo.setRoomNum(beduser.getChildTextTrim("RoomNum"));
            tempBeduserInfo.setBedNum(beduser.getChildTextTrim("BedNum"));
            tempBeduserInfo.setPartCode(beduser.getChildTextTrim("PartCode"));
            tempBeduserInfo.setBedCode(beduser.getChildTextTrim("BedCode"));
            tempBeduserInfo.setEquipmentID(beduser.getChildTextTrim("EquipmentID"));
            tempBeduserInfo.setSex(beduser.getChildTextTrim("Sex"));

            result.beduserinfoList.add(tempBeduserInfo);
        }

        returnQuote.close();
        return result;
    }

    @Override
    public String ToBodytXml() {
        return null;
    }
}
