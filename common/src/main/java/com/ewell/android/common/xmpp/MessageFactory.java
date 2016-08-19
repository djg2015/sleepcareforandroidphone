package com.ewell.android.common.xmpp;

import com.ewell.android.common.exception.EwellException;
import com.ewell.android.common.exception.ExceptionEnum;
import com.ewell.android.model.AlarmList;
import com.ewell.android.model.BaseMessage;
import com.ewell.android.model.BedUserInfo;
import com.ewell.android.model.EMLoginUser;
import com.ewell.android.model.EMRealTimeReport;
import com.ewell.android.model.EMServiceException;
import com.ewell.android.model.EquipmentList;
import com.ewell.android.model.HRRange;
import com.ewell.android.model.RRRange;
import com.ewell.android.model.ServerResult;
import com.ewell.android.model.SleepQualityReport;
import com.ewell.android.model.VersionList;
import com.ewell.android.model.WeekSleep;

/**
 * Created by Dongjg on 2016-7-12.
 */
public class MessageFactory {
    private static final String tag_loginUser = "<LoginUser";
    private static final String tag_realTimeReport = "<RealTimeReport";
    private static final String tag_serviceException = "<EMServiceException";
    private static final String tag_serverresult = "<ServerResult";
private static  final String tag_alarminfoList = "<EPAlarmInfoList";
    private static final String tag_equipmentinfolist="<EPEquipmentInfoList";
    private static final String tag_beduserinfo="<BedUserInfo";
private static final String tag_weekreport="<WeekSleep";
    private static final String tag_hrrange = "<EPHRRange";
    private static final String tag_rrrange = "<EPRRRange";
    private static final String tag_sleepreport = "<SleepQualityReport";
    private static final String tag_versionlist = "<EPAndroidVersion";

    public static BaseMessage GetMessage(String subjectXml, String bodyXml) throws EwellException {
        try {
            if (bodyXml.indexOf(tag_realTimeReport) == 0) {
                return EMRealTimeReport.XmlToMessage(subjectXml, bodyXml);

            } else if (bodyXml.indexOf(tag_loginUser) == 0) {
                return EMLoginUser.XmlToMessage(subjectXml, bodyXml);
            }
            else if(bodyXml.indexOf(tag_beduserinfo) == 0){
                return BedUserInfo.XmlToMessage(subjectXml, bodyXml);
            }
            else if(bodyXml.indexOf(tag_equipmentinfolist) == 0){
             return EquipmentList.XmlToMessage(subjectXml, bodyXml);
            }
            else if (bodyXml.indexOf(tag_serverresult) == 0){
                return ServerResult.XmlToMessage(subjectXml, bodyXml);
            }
            else if(bodyXml.indexOf(tag_alarminfoList) == 0){
                return AlarmList.XmlToMessage(subjectXml, bodyXml);
            }
            else if(bodyXml.indexOf(tag_weekreport) == 0){
                return WeekSleep.XmlToMessage(subjectXml, bodyXml);
            }
            else if(bodyXml.indexOf(tag_hrrange) == 0){
                return HRRange.XmlToMessage(subjectXml, bodyXml);
            }
            else if(bodyXml.indexOf(tag_rrrange) == 0){
                return RRRange.XmlToMessage(subjectXml, bodyXml);
            }
            else if(bodyXml.indexOf(tag_sleepreport) == 0){
                return SleepQualityReport.XmlToMessage(subjectXml, bodyXml);
            }
            else if(bodyXml.indexOf(tag_versionlist) == 0){
                return VersionList.XmlToMessage(subjectXml, bodyXml);
            }
            else if (bodyXml.indexOf(tag_serviceException) == 0) {
                return EMServiceException.XmlToMessage(subjectXml, bodyXml);
            }
            else {
                throw new EwellException(ExceptionEnum.UnknowResponse);
            }
        } catch (Exception ex) {
            throw new EwellException(ex);
        }

    }
}
