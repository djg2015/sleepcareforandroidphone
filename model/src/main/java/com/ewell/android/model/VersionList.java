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
 * Created by lillix on 8/9/16.
 */
public class VersionList extends BaseMessage {
    public VersionList(XmppMsgSubject subject) {
        super(subject);
    }

    private ArrayList<VersionInfo> versioninfoList;

    public ArrayList<VersionInfo> getVersionInfoList() {
        return versioninfoList;
    }

    public static VersionList XmlToMessage(String sujectXml, String bodyXml) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder(false);
        VersionList result = new VersionList(XmppMsgSubject.PareXmlToSubject(sujectXml));
        Reader returnQuote = new StringReader(bodyXml);
        Document doc = builder.build(returnQuote);
        Element root = doc.getRootElement();

        Element list = root.getChild("VersionList");
        List versionlist = list.getChildren();
        result.versioninfoList = new ArrayList<VersionInfo>();

        for (int i = 0; i < versionlist.size(); i++) {
            VersionInfo tempVersionInfo = new VersionInfo();
            Element version = (Element) versionlist.get(i);

            tempVersionInfo.setVersionCode(version.getChildTextTrim("VersionCode"));
            tempVersionInfo.setVersionNum(version.getChildTextTrim("VersionNum"));
            tempVersionInfo.setDownloadPath(version.getChildTextTrim("DownloadPath"));
            tempVersionInfo.setStatus(version.getChildTextTrim("Status"));

            result.versioninfoList.add(tempVersionInfo);
        }


        returnQuote.close();
        return result;
    }

    @Override
    public String ToBodytXml() {
        return null;
    }
}