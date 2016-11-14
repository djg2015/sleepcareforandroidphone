package com.ewell.android.model;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Created by Dongjg on 2016-7-13.
 */
public class EMServiceException extends  BaseMessage {
    private String code = "";

    public EMServiceException(XmppMsgSubject subject) {
        super(subject);
    }

    public String getCode() {
        return code;
    }

    private String message = "";

    public String getMessage() {
        return message;
    }

    public static EMServiceException XmlToMessage(String sujectXml, String bodyXml) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder(false);
        EMServiceException result = new EMServiceException(XmppMsgSubject.PareXmlToSubject(sujectXml));
        Reader returnQuote = new StringReader(bodyXml);
        Document doc = builder.build(returnQuote);
        Element user = doc.getRootElement();

       System.out.print(bodyXml+"========== error\n");
//        Element segment= user.getChild("LoginName");
//        String email=book.getAttributeValue(“email”);
        result.code=user.getAttributeValue("code");
        result.message =user.getAttributeValue("message");
        returnQuote.close();
        return result;
    }

    @Override
    public String ToBodytXml() {
        return null;
    }
}
