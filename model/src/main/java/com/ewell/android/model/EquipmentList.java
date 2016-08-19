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
 * Created by lillix on 7/20/16.
 */
public class EquipmentList extends BaseMessage {

    public EquipmentList(XmppMsgSubject subject) {
        super(subject);
    }

    private ArrayList<EquipmentInfo> equipmentinfoList;

    public ArrayList<EquipmentInfo> getEquipmentInfoList() {
        return equipmentinfoList;
    }


    public static EquipmentList XmlToMessage(String sujectXml, String bodyXml) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder(false);
        EquipmentList result = new EquipmentList(XmppMsgSubject.PareXmlToSubject(sujectXml));
        Reader returnQuote = new StringReader(bodyXml);
        Document doc = builder.build(returnQuote);
        Element root = doc.getRootElement();

        Element list = root.getChild("EquipmentList");
        List equipmentlist = list.getChildren();
        result.equipmentinfoList = new ArrayList<EquipmentInfo>();

        for (int i = 0; i < equipmentlist.size(); i++) {
            EquipmentInfo tempEquipmentInfo = new EquipmentInfo();
            Element equipment = (Element) equipmentlist.get(i);

            tempEquipmentInfo.setEquipmentID(equipment.getChildTextTrim("EquipmentID"));
            tempEquipmentInfo.setBedUserCode(equipment.getChildTextTrim("BedUserCode"));
            tempEquipmentInfo.setBedUserName(equipment.getChildTextTrim("BedUserName"));
            tempEquipmentInfo.setSex(equipment.getChildTextTrim("Sex"));
            tempEquipmentInfo.setMobilePhone(equipment.getChildTextTrim("MobilePhone"));
            tempEquipmentInfo.setAddress(equipment.getChildTextTrim("Address"));

            result.equipmentinfoList.add(tempEquipmentInfo);
        }

        returnQuote.close();
        return result;
    }

    @Override
    public String ToBodytXml() {
        return null;
    }
}


