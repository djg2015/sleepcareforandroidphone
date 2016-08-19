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
public class RRRange extends BaseMessage {
    public RRRange(XmppMsgSubject subject) {
        super(subject);
    }

    private ArrayList<String> rrXValues;
    public ArrayList<String> getRrXValues(){return rrXValues;}

    private ArrayList<String> rrYValues;
    public ArrayList<String> getRrYValues(){return rrYValues;}


    public static RRRange XmlToMessage(String sujectXml, String bodyXml) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder(false);
        RRRange result = new RRRange(XmppMsgSubject.PareXmlToSubject(sujectXml));
        Reader returnQuote = new StringReader(bodyXml);
        Document doc = builder.build(returnQuote);
        Element root = doc.getRootElement();

        Element range = root.getChild("RRRange");
        List rrreportlist = range.getChildren();

        result.rrYValues = new ArrayList<String>();
        result.rrXValues = new ArrayList<String>();

        for (int i = 0; i < rrreportlist.size(); i++) {

            Element rrreport = (Element) rrreportlist.get(i);

            String temptime = rrreport.getChildTextTrim("ReportHour");
            if (temptime != null && !temptime.equals("")) {
                if (temptime.length() > 10) {
                    temptime = temptime.substring(11, 13);
                    if (temptime.substring(0,1).equals("0")) {
                        temptime = temptime.substring(1);
                    }
                    if(i==0){
                        temptime = temptime + "点";
                    }

                    result.rrXValues.add(temptime);
                } else {
                    temptime = temptime.substring(8, 10);
                    if (temptime.substring(0,1).equals("0")) {
                        temptime = temptime.substring(1);
                    }
                    if(i==0){
                        temptime = temptime + "号";
                    }

                    result.rrXValues.add(temptime);
                }
            } else {
                result.rrXValues.add("");
            }

            String tempavgrr = rrreport.getChildTextTrim("AvgRR");
            int indexdot = tempavgrr.indexOf(".");
            if(indexdot>=0){
                tempavgrr = tempavgrr.substring(0,indexdot);
            }
            result.rrYValues.add(tempavgrr);


        }
        returnQuote.close();

        Collections.reverse(result.rrXValues);
        Collections.reverse(result.rrYValues);
        return result;
    }

    @Override
    public String ToBodytXml() {
        return null;
    }
}
