package com.ewell.android.bll;

import com.ewell.android.common.Grobal;
import com.ewell.android.common.exception.EwellException;
import com.ewell.android.common.exception.ExceptionEnum;
import com.ewell.android.ibll.SleepcareforPhoneManage;
import com.ewell.android.model.AlarmList;
import com.ewell.android.model.BaseMessage;
import com.ewell.android.model.BedUserInfo;
import com.ewell.android.model.EMLoginUser;
import com.ewell.android.model.EMProperties;
import com.ewell.android.model.EMServiceException;
import com.ewell.android.model.EquipmentList;
import com.ewell.android.model.HRRange;
import com.ewell.android.model.RRRange;
import com.ewell.android.model.ServerResult;
import com.ewell.android.model.SleepQualityReport;
import com.ewell.android.model.VersionList;
import com.ewell.android.model.WeekSleep;

/**
 * Created by Dongjg on 2016-7-13.
 */
public class SleepcareforPhoneBusiness implements SleepcareforPhoneManage   {

    @Override
    public EMLoginUser SingleLogin(String loginName, String loginPwd) throws EwellException {
        try {
            EMProperties param = new EMProperties("SingleLogin", "sleepcareforiphone");
            param.AddKeyValue("loginName", loginName);
            param.AddKeyValue("loginPassword", loginPwd);
            BaseMessage result = Grobal.getXmppManager().Send(param);

            if (result.getClass().equals(EMServiceException.class)) {
                throw new EwellException(ExceptionEnum.XmppBusinessError, ((EMServiceException) result).getMessage());
            }
            return (EMLoginUser) result;
        } catch (EwellException ewellEx) {
            throw ewellEx;
        } catch (Exception ex) {
            throw new EwellException(ex);
        }
    }

    public ServerResult GetVerificationCode(String mobileNum) throws EwellException {
        try {

            EMProperties param = new EMProperties("GetVerificationCode", "sleepcareforiphone");
            param.AddKeyValue("mobileNum", mobileNum);
            BaseMessage result = Grobal.getXmppManager().Send(param);

            if (result.getClass().equals(EMServiceException.class)) {
                throw new EwellException(ExceptionEnum.XmppBusinessError, ((EMServiceException) result).getMessage());
            }
            return (ServerResult) result;
        } catch (EwellException ewellEx) {
            throw ewellEx;
        } catch (Exception ex) {
            throw new EwellException(ex);
        }
    }

    public ServerResult SingleRegist(String loginName, String loginPassword, String vcCode, String equipmentID) throws EwellException {

        try {

            EMProperties param = new EMProperties("SingleRegist", "sleepcareforiphone");
            param.AddKeyValue("loginName", loginName);
            param.AddKeyValue("loginPassword", loginPassword);
            param.AddKeyValue("vcCode", vcCode);
            param.AddKeyValue("equipmentID", equipmentID);
            BaseMessage result = Grobal.getXmppManager().Send(param);

            if (result.getClass().equals(EMServiceException.class)) {
                throw new EwellException(ExceptionEnum.XmppBusinessError, ((EMServiceException) result).getMessage());
            }
            return (ServerResult) result;

        } catch (EwellException ewellEx) {
            throw ewellEx;
        } catch (Exception ex) {
            throw new EwellException(ex);
        }
    }


    public ServerResult ModifyAccount(String loginName, String oldPassword, String newPassword) throws EwellException {
        try {

            EMProperties param = new EMProperties("ModifyAccount", "sleepcareforiphone");
            param.AddKeyValue("loginName", loginName);
            param.AddKeyValue("oldPassword", oldPassword);
            param.AddKeyValue("newPassword", newPassword);
            BaseMessage result = Grobal.getXmppManager().Send(param);

            if (result.getClass().equals(EMServiceException.class)) {
                throw new EwellException(ExceptionEnum.XmppBusinessError, ((EMServiceException) result).getMessage());
            }
            return (ServerResult) result;

        } catch (EwellException ewellEx) {
            throw ewellEx;
        } catch (Exception ex) {
            throw new EwellException(ex);
        }
    }


    public ServerResult CheckEquipmentID(String equipmentID) throws EwellException {
        try {

            EMProperties param = new EMProperties("CheckEquipmentID", "sleepcareforiphone");
            param.AddKeyValue("equipmentID", equipmentID);

            BaseMessage result = Grobal.getXmppManager().Send(param);

            if (result.getClass().equals(EMServiceException.class)) {
                throw new EwellException(ExceptionEnum.XmppBusinessError, ((EMServiceException) result).getMessage());
            }
            return (ServerResult) result;

        } catch (EwellException ewellEx) {
            throw ewellEx;
        } catch (Exception ex) {
            throw new EwellException(ex);
        }
    }

    public ServerResult ConfirmNewPassword(String loginName, String vcCode, String newPassword) throws EwellException {
        try {
            EMProperties param = new EMProperties("ConfirmNewPassword", "sleepcareforiphone");
            param.AddKeyValue("loginName", loginName);
            param.AddKeyValue("vcCode", vcCode);
            param.AddKeyValue("newPassword", newPassword);
            BaseMessage result = Grobal.getXmppManager().Send(param);

            if (result.getClass().equals(EMServiceException.class)) {
                throw new EwellException(ExceptionEnum.XmppBusinessError, ((EMServiceException) result).getMessage());
            }
            return (ServerResult) result;
        } catch (EwellException ewellEx) {
            throw ewellEx;
        } catch (Exception ex) {
            throw new EwellException(ex);
        }

    }

    public BedUserInfo GetBedUserInfoByEquipmentID(String equipmentID) throws EwellException {
        try {
            EMProperties param = new EMProperties("GetBedUserInfoByEquipmentID", "sleepcareforiphone");
            param.AddKeyValue("equipmentID", equipmentID);

            BaseMessage result = Grobal.getXmppManager().Send(param);

            if (result.getClass().equals(EMServiceException.class)) {
                throw new EwellException(ExceptionEnum.XmppBusinessError, ((EMServiceException) result).getMessage());
            }
            return (BedUserInfo) result;
        } catch (EwellException ewellEx) {
            throw ewellEx;
        } catch (Exception ex) {
            throw new EwellException(ex);
        }

    }

    public ServerResult ModifyBedUserInfo(String bedUserCode, String bedUserName, String sex, String mobilePhone, String address) throws EwellException {

        try {
            EMProperties param = new EMProperties("ModifyBedUserInfo", "sleepcareforiphone");
            param.AddKeyValue("bedUserCode", bedUserCode);
            param.AddKeyValue("bedUserName", bedUserName);
            param.AddKeyValue("sex", sex);
            param.AddKeyValue("mobilePhone", mobilePhone);
            param.AddKeyValue("address", address);

            BaseMessage result = Grobal.getXmppManager().Send(param);

            if (result.getClass().equals(EMServiceException.class)) {
                throw new EwellException(ExceptionEnum.XmppBusinessError, ((EMServiceException) result).getMessage());
            }
            return (ServerResult) result;
        } catch (EwellException ewellEx) {
            throw ewellEx;
        } catch (Exception ex) {
            throw new EwellException(ex);
        }

    }

    public ServerResult RemoveEquipment(String loginName, String equipmentIDs) throws EwellException {
        try {
            EMProperties param = new EMProperties("RemoveEquipment", "sleepcareforiphone");
            param.AddKeyValue("loginName", loginName);
            param.AddKeyValue("equipmentIDs", equipmentIDs);

            BaseMessage result = Grobal.getXmppManager().Send(param);

            if (result.getClass().equals(EMServiceException.class)) {
                throw new EwellException(ExceptionEnum.XmppBusinessError, ((EMServiceException) result).getMessage());
            }
            return (ServerResult) result;
        } catch (EwellException ewellEx) {
            throw ewellEx;
        } catch (Exception ex) {
            throw new EwellException(ex);
        }

    }


    public EquipmentList GetEquipmentsByLoginName(String loginName) throws EwellException {

        try {
            EMProperties param = new EMProperties("GetEquipmentsByLoginName", "sleepcareforiphone");
            param.AddKeyValue("loginName", loginName);
            BaseMessage result = Grobal.getXmppManager().Send(param);

            if (result.getClass().equals(EMServiceException.class)) {
                throw new EwellException(ExceptionEnum.XmppBusinessError, ((EMServiceException) result).getMessage());
            }
            return (EquipmentList) result;
        } catch (EwellException ewellEx) {
            throw ewellEx;
        } catch (Exception ex) {
            throw new EwellException(ex);
        }
    }



public HRRange GetSingleHRTimeReport(String bedUserCode, String searchType) throws EwellException{
    try {
        EMProperties param = new EMProperties("GetSingleHRTimeReport", "sleepcareforiphone");
        param.AddKeyValue("bedUserCode", bedUserCode);
        param.AddKeyValue("searchType", searchType);

        BaseMessage result = Grobal.getXmppManager().Send(param);

        if (result.getClass().equals(EMServiceException.class)) {
            throw new EwellException(ExceptionEnum.XmppBusinessError, ((EMServiceException) result).getMessage());
        }
        return (HRRange) result;
    } catch (EwellException ewellEx) {
        throw ewellEx;
    } catch (Exception ex) {
        throw new EwellException(ex);
    }

}

public RRRange GetSingleRRTimeReport(String bedUserCode, String searchType) throws EwellException{
    try {
        EMProperties param = new EMProperties("GetSingleRRTimeReport", "sleepcareforiphone");
        param.AddKeyValue("bedUserCode", bedUserCode);
        param.AddKeyValue("searchType", searchType);

        BaseMessage result = Grobal.getXmppManager().Send(param);

        if (result.getClass().equals(EMServiceException.class)) {
            throw new EwellException(ExceptionEnum.XmppBusinessError, ((EMServiceException) result).getMessage());
        }
        return (RRRange) result;
    } catch (EwellException ewellEx) {
        throw ewellEx;
    } catch (Exception ex) {
        throw new EwellException(ex);
    }

}

public SleepQualityReport GetSleepQualityofBedUser(String bedUserCode, String reportDate) throws EwellException{

    try {
        EMProperties param = new EMProperties("GetSleepQualityofBedUser", "sleepcareforiphone");
        param.AddKeyValue("bedUserCode", bedUserCode);
        param.AddKeyValue("reportDate", reportDate);


        BaseMessage result = Grobal.getXmppManager().Send(param);

        if (result.getClass().equals(EMServiceException.class)) {
            throw new EwellException(ExceptionEnum.XmppBusinessError, ((EMServiceException) result).getMessage());
        }
        return (SleepQualityReport) result;
    } catch (EwellException ewellEx) {
        throw ewellEx;
    } catch (Exception ex) {
        throw new EwellException(ex);
    }

}

    public ServerResult SendEmail(String bedUserCode,String sleepDate,String email) throws EwellException{
        try {
            EMProperties param = new EMProperties("SendEmail", "sleepcareforiphone");
            param.AddKeyValue("bedUserCode", bedUserCode);
            param.AddKeyValue("sleepDate", sleepDate);
            param.AddKeyValue("email", email);

            BaseMessage result = Grobal.getXmppManager().Send(param);

            if (result.getClass().equals(EMServiceException.class)) {
                throw new EwellException(ExceptionEnum.XmppBusinessError, ((EMServiceException) result).getMessage());
            }
            return (ServerResult) result;
        } catch (EwellException ewellEx) {
            throw ewellEx;
        } catch (Exception ex) {
            throw new EwellException(ex);
        }

    }

    public WeekSleep GetWeekSleepofBedUser(String bedUserCode, String reportDate) throws EwellException{

        try {
            EMProperties param = new EMProperties("GetWeekSleepofBedUser", "sleepcareforiphone");
            param.AddKeyValue("bedUserCode", bedUserCode);
            param.AddKeyValue("reportDate", reportDate);

            BaseMessage result = Grobal.getXmppManager().Send(param);

            if (result.getClass().equals(EMServiceException.class)) {
                throw new EwellException(ExceptionEnum.XmppBusinessError, ((EMServiceException) result).getMessage());
            }
            return (WeekSleep) result;
        } catch (EwellException ewellEx) {

            throw ewellEx;
        } catch (Exception ex) {
            throw new EwellException(ex);
        }
    }

public AlarmList GetSingleAlarmByLoginUser(String loginName, String schemaCode, String alarmTimeBegin, String alarmTimeEnd, String transferTypeCode, String from, String max) throws EwellException
    {
        try {
            EMProperties param = new EMProperties("GetSingleAlarmByLoginUser", "sleepcareforiphone");
            param.AddKeyValue("loginName", loginName);
            param.AddKeyValue("schemaCode", schemaCode);
            param.AddKeyValue("alarmTimeBegin", alarmTimeBegin);
            param.AddKeyValue("alarmTimeEnd", alarmTimeEnd);
            param.AddKeyValue("transferTypeCode", transferTypeCode);
            param.AddKeyValue("from", from);
            param.AddKeyValue("max",max);

            BaseMessage result = Grobal.getXmppManager().Send(param);

            if (result.getClass().equals(EMServiceException.class)) {
                throw new EwellException(ExceptionEnum.XmppBusinessError, ((EMServiceException) result).getMessage());
            }
            return (AlarmList) result;
        } catch (EwellException ewellEx) {
            throw ewellEx;
        } catch (Exception ex) {
            throw new EwellException(ex);
        }

    }


    public ServerResult DeleteAlarmMessage(String alarmCodes,String loginName) throws EwellException{
        try {
            EMProperties param = new EMProperties("DeleteAlarmMessage", "sleepcareforiphone");
            param.AddKeyValue("alarmCodes", alarmCodes);
            param.AddKeyValue("loginName", loginName);


            BaseMessage result = Grobal.getXmppManager().Send(param);

            if (result.getClass().equals(EMServiceException.class)) {
                throw new EwellException(ExceptionEnum.XmppBusinessError, ((EMServiceException) result).getMessage());
            }
            return (ServerResult) result;
        } catch (EwellException ewellEx) {
            throw ewellEx;
        } catch (Exception ex) {
            throw new EwellException(ex);
        }

    }

    public ServerResult BindEquipmentofUser(String equipmentID,String loginName) throws EwellException{
        try {
            EMProperties param = new EMProperties("BindEquipmentofUser", "sleepcareforiphone");
            param.AddKeyValue("equipmentID", equipmentID);
            param.AddKeyValue("loginName", loginName);


            BaseMessage result = Grobal.getXmppManager().Send(param);

            if (result.getClass().equals(EMServiceException.class)) {
                throw new EwellException(ExceptionEnum.XmppBusinessError, ((EMServiceException) result).getMessage());
            }
            return (ServerResult) result;
        } catch (EwellException ewellEx) {
            throw ewellEx;
        } catch (Exception ex) {
            throw new EwellException(ex);
        }
    }


    public VersionList GetVersionForPhone(String status, String type) throws EwellException{
        try {
            EMProperties param = new EMProperties("GetVersionForPhone", "sleepcareforiphone");
            param.AddKeyValue("status", status);
            param.AddKeyValue("type", type);


            BaseMessage result = Grobal.getXmppManager().Send(param);

            if (result.getClass().equals(EMServiceException.class)) {
                throw new EwellException(ExceptionEnum.XmppBusinessError, ((EMServiceException) result).getMessage());
            }
            return (VersionList) result;
        } catch (EwellException ewellEx) {
            throw ewellEx;
        } catch (Exception ex) {
            throw new EwellException(ex);
        }

    }


    public ServerResult OpenNotificationForAndroid(String deviceID,String loginName) throws EwellException{
        try {
            EMProperties param = new EMProperties("OpenNotificationForAndroid", "sleepcareforiphone");
            param.AddKeyValue("deviceID", deviceID);
            param.AddKeyValue("loginName", loginName);


            BaseMessage result = Grobal.getXmppManager().Send(param);

            if (result.getClass().equals(EMServiceException.class)) {
                throw new EwellException(ExceptionEnum.XmppBusinessError, ((EMServiceException) result).getMessage());
            }
            return (ServerResult) result;
        } catch (EwellException ewellEx) {
            throw ewellEx;
        } catch (Exception ex) {
            throw new EwellException(ex);
        }

    }




    public ServerResult CloseNotificationForAndroid(String deviceID,String loginName) throws EwellException{
        try {
            EMProperties param = new EMProperties("CloseNotificationForAndroid", "sleepcareforiphone");
            param.AddKeyValue("deviceID", deviceID);
            param.AddKeyValue("loginName", loginName);


            BaseMessage result = Grobal.getXmppManager().Send(param);

            if (result.getClass().equals(EMServiceException.class)) {
                throw new EwellException(ExceptionEnum.XmppBusinessError, ((EMServiceException) result).getMessage());
            }
            return (ServerResult) result;
        } catch (EwellException ewellEx) {
            throw ewellEx;
        } catch (Exception ex) {
            throw new EwellException(ex);
        }

    }
}

