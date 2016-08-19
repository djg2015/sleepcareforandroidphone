package com.ewell.android.model;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Created by lillix on 7/18/16.
 */
public class ServerResult extends BaseMessage{

    public ServerResult(XmppMsgSubject subject) {
        super(subject);
    }
    private Boolean result = false;

    public Boolean getResult() {
        return result;
    }


    public static ServerResult XmlToMessage(String sujectXml, String bodyXml) throws JDOMException, IOException {

        SAXBuilder builder = new SAXBuilder(false);
        ServerResult result = new ServerResult(XmppMsgSubject.PareXmlToSubject(sujectXml));
        Reader returnQuote = new StringReader(bodyXml);
        Document doc = builder.build(returnQuote);
        Element root = doc.getRootElement();
        result.result = root.getChildTextTrim("IsSuccessful").equals("true");
        returnQuote.close();
        return result;
    }
    @Override
    public String ToBodytXml() {
        return null;
    }
}
