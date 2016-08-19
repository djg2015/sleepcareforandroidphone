package com.ewell.android.model;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lillix on 7/30/16.
 */
public class HRRange extends BaseMessage {
    public HRRange(XmppMsgSubject subject) {
        super(subject);
    }

    private ArrayList<HRTimeReport> hrTimeReportList;
    public ArrayList<HRTimeReport> getHrTimeReportList() {
        return hrTimeReportList;
    }

    private ArrayList<String> hrXValues;
    public ArrayList<String> getHrXValues(){return hrXValues;}

    private ArrayList<String> hrYValues;
    public ArrayList<String> getHrYValues(){return hrYValues;}


    public static HRRange XmlToMessage(String sujectXml, String bodyXml) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder(false);
        HRRange result = new HRRange(XmppMsgSubject.PareXmlToSubject(sujectXml));
        Reader returnQuote = new StringReader(bodyXml);
        Document doc = builder.build(returnQuote);
        Element root = doc.getRootElement();

        Element range = root.getChild("HRRange");
        List hrreportlist = range.getChildren();
        result.hrTimeReportList = new ArrayList<HRTimeReport>();
result.hrYValues = new ArrayList<String>();
        result.hrXValues = new ArrayList<String>();

        for (int i = 0; i < hrreportlist.size(); i++) {
            HRTimeReport temphrreport = new HRTimeReport();


            Element hrreport = (Element) hrreportlist.get(i);

            String temptime = hrreport.getChildTextTrim("ReportHour");
            if (temptime != null && !temptime.equals("")) {
                if (temptime.length() > 10) {
                    temptime = temptime.substring(11, 13);
                    if (temptime.substring(0,1).equals("0")) {
                        temptime = temptime.substring(1);
                    }
                    if(i==0){
                        temptime = temptime + "点";
                    }
                    temphrreport.setReportHour(temptime);
                    result.hrXValues.add(temptime);
                } else {
                    temptime = temptime.substring(8, 10);
                    if (temptime.substring(0,1).equals("0")) {
                        temptime = temptime.substring(1);
                    }
                    if(i==0){
                        temptime = temptime + "号";
                    }
                    temphrreport.setReportHour(temptime);
                   result.hrXValues.add(temptime);
                }
            } else {
                temphrreport.setReportHour("");
            }

            temphrreport.setAvgHR(hrreport.getChildTextTrim("AvgHR"));

            String tempavghr = hrreport.getChildTextTrim("AvgHR");
            int indexdot = tempavghr.indexOf(".");
            if(indexdot>=0){
                tempavghr = tempavghr.substring(0,indexdot);
            }
            result.hrYValues.add(tempavghr);

            result.hrTimeReportList.add(temphrreport);



        }
        returnQuote.close();

        Collections.reverse(result.hrTimeReportList);
        Collections.reverse(result.hrXValues);
        Collections.reverse(result.hrYValues);
        return result;
    }

    @Override
    public String ToBodytXml() {
        return null;
    }
}
