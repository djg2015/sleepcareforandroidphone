package com.ewell.android.common.xmpp;

import com.ewell.android.common.exception.EwellException;
import com.ewell.android.common.exception.ExceptionEnum;
import com.ewell.android.model.BaseMessage;
import com.ewell.android.model.EMLoginUser;
import com.ewell.android.model.EMRealTimeReport;
import com.ewell.android.model.EMServiceException;

/**
 * Created by Dongjg on 2016-7-12.
 */
public class MessageFactory {
    private static final String tag_loginUser = "<LoginUser";
    private static final String tag_realTimeReport = "<RealTimeReport";
    private static final String tag_serviceException = "<EMServiceException";

    public static BaseMessage GetMessage(String subjectXml, String bodyXml) throws EwellException {
        try {
            if (bodyXml.indexOf(tag_realTimeReport) == 0) {
                return EMRealTimeReport.XmlToMessage(subjectXml, bodyXml);

            } else if (bodyXml.indexOf(tag_loginUser) == 0) {
                return EMLoginUser.XmlToMessage(subjectXml, bodyXml);

            } else if (bodyXml.indexOf(tag_serviceException) == 0) {
                return EMServiceException.XmlToMessage(subjectXml, bodyXml);
            } else {
                throw new EwellException(ExceptionEnum.UnknowResponse);
            }
        } catch (Exception ex) {
            throw new EwellException(ex);
        }

    }
}
