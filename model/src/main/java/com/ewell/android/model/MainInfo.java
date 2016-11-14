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
public class MainInfo extends BaseMessage {

    public MainInfo(XmppMsgSubject subject) {
        super(subject);
    }

    private ArrayList<PartInfo> partinfoList;
    public ArrayList<PartInfo> getPartInfoList() {
        return partinfoList;
    }

    public static MainInfo XmlToMessage(String sujectXml, String bodyXml) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder(false);
        MainInfo result = new MainInfo(XmppMsgSubject.PareXmlToSubject(sujectXml));
        Reader returnQuote = new StringReader(bodyXml);
        Document doc = builder.build(returnQuote);
        Element root = doc.getRootElement();

        Element list = root.getChild("PartInfoList");
        List partlist = list.getChildren();
        result.partinfoList = new ArrayList<PartInfo>();

        for (int i = 0; i < partlist.size(); i++) {
            PartInfo tempPartInfo = new PartInfo();

            Element part = (Element) partlist.get(i);
            tempPartInfo.setPartCode(part.getChildTextTrim("PartCode"));
            tempPartInfo.setPartName(part.getChildTextTrim("PartName"));
            tempPartInfo.setMainName(part.getChildTextTrim("MainName"));

           List bedlist = part.getChild("BedInfoList").getChildren();
            ArrayList<BedInfo> tempBedInfolist = new ArrayList<BedInfo>();


            for(int j = 0; j < bedlist.size(); j++){
                Element bed = (Element)bedlist.get(j);
                BedInfo tempBedInfo = new BedInfo();
                tempBedInfo.setRoomNumber(bed.getChildTextTrim("RoomName"));
                tempBedInfo.setBedCode(bed.getChildTextTrim("BedCode"));
                tempBedInfo.setBedNumber(bed.getChildTextTrim("BedNumber"));
                tempBedInfo.setBedUserCode(bed.getChildTextTrim("BedUserCode"));
                tempBedInfo.setBedUserName(bed.getChildTextTrim("BedUserName"));
               // tempBedInfo.setSex(bed.getChildTextTrim("Sex"));
                tempBedInfo.setSex("1");
                String id = bed.getChildTextTrim("EquipmentID") == null ? "未绑定" : bed.getChildTextTrim("EquipmentID");
                tempBedInfo.setEquipmentID(id);
               tempBedInfolist.add(tempBedInfo);
            }
            tempPartInfo.setBedInfoList(tempBedInfolist);


            result.partinfoList.add(tempPartInfo);
        }



        returnQuote.close();
        return result;
    }
    @Override
    public String ToBodytXml() {
        return null;
    }
}
