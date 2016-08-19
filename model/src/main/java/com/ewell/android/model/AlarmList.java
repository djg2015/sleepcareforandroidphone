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
 * Created by lillix on 7/25/16.
 */
public class AlarmList extends BaseMessage {
    public AlarmList(XmppMsgSubject subject) {
        super(subject);
    }

    private ArrayList<AlarmInfo> alarminfoList;

    public ArrayList<AlarmInfo> getAlarmInfoList() {
        return alarminfoList;
    }

    public static AlarmList XmlToMessage(String sujectXml, String bodyXml) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder(false);
        AlarmList result = new AlarmList(XmppMsgSubject.PareXmlToSubject(sujectXml));
        Reader returnQuote = new StringReader(bodyXml);
        Document doc = builder.build(returnQuote);
        Element root = doc.getRootElement();

        Element list = root.getChild("AlarmList");
        List alarmlist = list.getChildren();
        result.alarminfoList = new ArrayList<AlarmInfo>();

        for (int i = 0; i < alarmlist.size(); i++) {
            AlarmInfo tempAlarmInfo = new AlarmInfo();
            Element alarm = (Element) alarmlist.get(i);

            tempAlarmInfo.setUserSex(alarm.getChildTextTrim("UserSex"));
            tempAlarmInfo.setUserCode(alarm.getChildTextTrim("UserCode"));
            tempAlarmInfo.setUserName(alarm.getChildTextTrim("UserName"));
            tempAlarmInfo.setAlarmCode(alarm.getChildTextTrim("AlarmCode"));
            tempAlarmInfo.setSchemaCode(alarm.getChildTextTrim("SchemaCode"));
            tempAlarmInfo.setSchemaContent(alarm.getChildTextTrim("SchemaContent"));
            tempAlarmInfo.setAlarmTime(alarm.getChildTextTrim("AlarmTime"));
            tempAlarmInfo.setBedNumber(alarm.getChildTextTrim("BedNumber"));

            result.alarminfoList.add(tempAlarmInfo);
        }


        returnQuote.close();
        return result;
    }

    @Override
    public String ToBodytXml() {
        return null;
    }
}
