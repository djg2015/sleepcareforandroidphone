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
 * Created by lillix on 7/30/16.
 */
public class SleepQualityReport extends BaseMessage {

    public SleepQualityReport(XmppMsgSubject subject) {
        super(subject);
    }

    private String SleepQuality="";

    public String getSleepQuality() {
        return SleepQuality;
    }

    private Float SleepTimespanFloat=0.0f;

    public Float getSleepTimespanFloat() {
        return SleepTimespanFloat;
    }

    private String SleepTimespan="";

    public String getSleepTimespan() {
        return SleepTimespan;
    }

    private Float DeepSleepTimespanFloat=0.0f;

    public Float getDeepSleepTimespanFloat() {
        return DeepSleepTimespanFloat;
    }

    private String DeepSleepTimespan="";

    public String getDeepSleepTimespan() {
        return DeepSleepTimespan;
    }

    private Float LightSleepTimespanFloat=0.0f;

    public Float getLightSleepTimespanFloat() {
        return LightSleepTimespanFloat;
    }

    private String LightSleepTimespan="";

    public String getLightSleepTimespan() {
        return LightSleepTimespan;
    }

    private Float AwakeningTimespanFloat=0.0f;

    public Float getAwakeningTimespanFloat() {
        return AwakeningTimespanFloat;
    }


    private String AwakeningTimespan="";

    public String getAwakeningTimespan() {
        return AwakeningTimespan;
    }

    private ArrayList<String> weekdayValues;

    public ArrayList<String> getWeekdayValues() {
        return weekdayValues;
    }

    private ArrayList<Float> sleeptimeValues;

    public ArrayList<Float> getSleeptimeValues() {
        return sleeptimeValues;
    }


    public static SleepQualityReport XmlToMessage(String sujectXml, String bodyXml) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder(false);
        SleepQualityReport result = new SleepQualityReport(XmppMsgSubject.PareXmlToSubject(sujectXml));
        Reader returnQuote = new StringReader(bodyXml);
        Document doc = builder.build(returnQuote);
        Element root = doc.getRootElement();



        result.SleepQuality = root.getChildTextTrim("SleepQuality")==null?"无":root.getChildTextTrim("SleepQuality");
System.out.print(result.SleepQuality+"睡眠得分==========\n");
        String tempsleeptime = root.getChildTextTrim("SleepTimespan");
        if (tempsleeptime != null && !tempsleeptime.equals("")) {
            String hour = tempsleeptime.substring(0, 2);
            String min = tempsleeptime.substring(3, 5);
            Float tempfloatvalue = Float.parseFloat(hour) + Float.parseFloat(min) / 60;

            result.SleepTimespan = hour + "时" + min + "分";
            result.SleepTimespanFloat = tempfloatvalue;

        } else {
            result.SleepTimespan = "0时0分";
            result.SleepTimespanFloat = 0.0f;
        }

        String tempdeepsleep = root.getChildTextTrim("DeepSleepTimespan");
        if (tempdeepsleep != null && !tempdeepsleep.equals("")) {
            String hour = tempdeepsleep.substring(0, 2);
            String min = tempdeepsleep.substring(3, 5);
            Float tempfloatvalue = Float.parseFloat(hour) + Float.parseFloat(min) / 60;

            result.DeepSleepTimespan = hour + "时" + min + "分";
            result.DeepSleepTimespanFloat = tempfloatvalue;
        } else {
            result.DeepSleepTimespan = "0时0分";
            result.DeepSleepTimespanFloat = 0.0f;
        }

        String templightsleep = root.getChildTextTrim("LightSleepTimespan");
        if (templightsleep != null && !templightsleep.equals("")) {
            String hour = templightsleep.substring(0, 2);
            String min = templightsleep.substring(3, 5);
            Float tempfloatvalue = Float.parseFloat(hour) + Float.parseFloat(min) / 60;

            result.LightSleepTimespan = hour + "时" + min + "分";
            result.LightSleepTimespanFloat = tempfloatvalue;

        } else {
            result.LightSleepTimespan = "0时0分";
            result.LightSleepTimespanFloat = 0.0f;
        }

        String tempawake = root.getChildTextTrim("AwakeningTimespan");
        if (tempawake != null && !tempawake.equals("")) {
            String hour = tempawake.substring(0, 2);
            String min = tempawake.substring(3, 5);
            Float tempfloatvalue = Float.parseFloat(hour) + Float.parseFloat(min) / 60;

            result.AwakeningTimespan = hour + "时" + min + "分";
            result.AwakeningTimespanFloat = tempfloatvalue;
        } else {
            result.AwakeningTimespan = "0时0分";
            result.AwakeningTimespanFloat = 0.0f;
        }

        Element range = root.getChild("EPSleepRange");
        List reportlist = range.getChildren();

        result.weekdayValues = new ArrayList<String>();
        result.sleeptimeValues = new ArrayList<Float>();
        for (int i = 0; i < reportlist.size(); i++) {
            Element report = (Element) reportlist.get(i);

            String tempweekday = report.getChildTextTrim("WeekDay");
            tempweekday = tempweekday.replace("星期", "周");
            result.weekdayValues.add(tempweekday);

            String tempsleeptimespan = report.getChildTextTrim("SleepTimespan");
            if (tempsleeptimespan.length() > 5) {
                Float temphour = Float.parseFloat(tempsleeptimespan.substring(0, 2));
                Float tempmin = Float.parseFloat(tempsleeptimespan.substring(3, 5));
                result.sleeptimeValues.add(temphour + tempmin / 60);
            } else {
                result.sleeptimeValues.add(0.0f);
            }
        }
        returnQuote.close();

        return result;
    }

    @Override
    public String ToBodytXml() {
        return null;
    }
}
