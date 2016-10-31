package com.ewell.android.bll;

import com.ewell.android.common.Grobal;
import com.ewell.android.common.exception.EwellException;
import com.ewell.android.ibll.SleepcareforPhoneManage;
import com.ewell.android.model.AlarmList;
import com.ewell.android.model.BaseMessage;
import com.ewell.android.model.BedUserList;
import com.ewell.android.model.EMLoginUser;
import com.ewell.android.model.EMProperties;
import com.ewell.android.model.EMServiceException;
import com.ewell.android.model.HRRange;
import com.ewell.android.model.MainInfo;
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

    public ServerResult ConfirmNewPassword(String loginName, String vcCode, String newPassword) throws EwellException {

            EMProperties param = new EMProperties("ConfirmNewPassword", "sleepcareforiphone");
            param.AddKeyValue("loginName", loginName);
            param.AddKeyValue("vcCode", vcCode);
            param.AddKeyValue("newPassword", newPassword);
            BaseMessage result = Grobal.getXmppManager().Send(param);
            if (result.getClass().equals(EMServiceException.class)) {
                throw new EwellException(((EMServiceException) result).getMessage());
            }
            return (ServerResult) result;

    }


public HRRange GetSingleHRTimeReport(String bedUserCode, String searchType) throws EwellException{

        EMProperties param = new EMProperties("GetSingleHRTimeReport", "sleepcareforiphone");
        param.AddKeyValue("bedUserCode", bedUserCode);
        param.AddKeyValue("searchType", searchType);

        BaseMessage result = Grobal.getXmppManager().Send(param);

        if (result.getClass().equals(EMServiceException.class)) {
            throw new EwellException(((EMServiceException) result).getMessage());
        }
        return (HRRange) result;
}

public RRRange GetSingleRRTimeReport(String bedUserCode, String searchType) throws EwellException{

        EMProperties param = new EMProperties("GetSingleRRTimeReport", "sleepcareforiphone");
        param.AddKeyValue("bedUserCode", bedUserCode);
        param.AddKeyValue("searchType", searchType);

        BaseMessage result = Grobal.getXmppManager().Send(param);

        if (result.getClass().equals(EMServiceException.class)) {
            throw new EwellException(((EMServiceException) result).getMessage());
        }
        return (RRRange) result;
}

public SleepQualityReport GetSleepQualityofBedUser(String bedUserCode, String reportDate) throws EwellException{

        EMProperties param = new EMProperties("GetSleepQualityofBedUser", "sleepcareforiphone");
        param.AddKeyValue("bedUserCode", bedUserCode);
        param.AddKeyValue("reportDate", reportDate);


        BaseMessage result = Grobal.getXmppManager().Send(param);

        if (result.getClass().equals(EMServiceException.class)) {
            throw new EwellException(((EMServiceException) result).getMessage());
        }
        return (SleepQualityReport) result;

}

    public ServerResult SendEmail(String bedUserCode,String sleepDate,String email) throws EwellException{

            EMProperties param = new EMProperties("SendEmail", "sleepcareforiphone");
            param.AddKeyValue("bedUserCode", bedUserCode);
            param.AddKeyValue("sleepDate", sleepDate);
            param.AddKeyValue("email", email);

            BaseMessage result = Grobal.getXmppManager().Send(param);

            if (result.getClass().equals(EMServiceException.class)) {
                throw new EwellException(((EMServiceException) result).getMessage());
            }
            return (ServerResult) result;

    }

    public WeekSleep GetWeekSleepofBedUser(String bedUserCode, String reportDate) throws EwellException{

            EMProperties param = new EMProperties("GetWeekSleepofBedUser", "sleepcareforiphone");
            param.AddKeyValue("bedUserCode", bedUserCode);
            param.AddKeyValue("reportDate", reportDate);

            BaseMessage result = Grobal.getXmppManager().Send(param);

            if (result.getClass().equals(EMServiceException.class)) {
                throw new EwellException(((EMServiceException) result).getMessage());
            }
            return (WeekSleep) result;
    }





    public VersionList GetVersionForPhone(String status, String type) throws EwellException{

            EMProperties param = new EMProperties("GetVersionForPhone", "sleepcareforiphone");
            param.AddKeyValue("status", status);
            param.AddKeyValue("type", type);


            BaseMessage result = Grobal.getXmppManager().Send(param);

            if (result.getClass().equals(EMServiceException.class)) {
                throw new EwellException(((EMServiceException) result).getMessage());
            }
            return (VersionList) result;

    }


    public ServerResult OpenNotificationForAndroid(String deviceID,String loginName) throws EwellException{

            EMProperties param = new EMProperties("OpenNotificationForAndroid", "sleepcareforiphone");
            param.AddKeyValue("deviceID", deviceID);
            param.AddKeyValue("loginName", loginName);


            BaseMessage result = Grobal.getXmppManager().Send(param);

            if (result.getClass().equals(EMServiceException.class)) {
                throw new EwellException(((EMServiceException) result).getMessage());
            }
            return (ServerResult) result;
    }




    public ServerResult CloseNotificationForAndroid(String deviceID,String loginName) throws EwellException{

            EMProperties param = new EMProperties("CloseNotificationForAndroid", "sleepcareforiphone");
            param.AddKeyValue("deviceID", deviceID);
            param.AddKeyValue("loginName", loginName);


            BaseMessage result = Grobal.getXmppManager().Send(param);

        if (result.getClass().equals(EMServiceException.class)) {
            throw new EwellException(((EMServiceException) result).getMessage());
        }
        return (ServerResult) result;

    }


    //--------------------------------------------------
    public void TransferAlarmMessage(String alarmCode,String transferType) throws EwellException{

            EMProperties param = new EMProperties("TransferAlarmMessage", "sleepcareforiphone");
            param.AddKeyValue("alarmCode", alarmCode);
            param.AddKeyValue(" transferType",  transferType);


            BaseMessage result = Grobal.getXmppManager().Send(param);

        if (result.getClass().equals(EMServiceException.class)) {
            throw new EwellException(((EMServiceException) result).getMessage());
        }


    }

    public AlarmList GetAlarmByLoginUser(String mainCode, String loginName, String schemaCode, String alarmTimeBegin, String alarmTimeEnd, String transferTypeCode, String from, String max) throws EwellException
    {

            EMProperties param = new EMProperties("GetAlarmByLoginUser", "sleepcareforpad");
            param.AddKeyValue("mainCode", mainCode);

            param.AddKeyValue("loginName", loginName);
            param.AddKeyValue("schemaCode", schemaCode);
            param.AddKeyValue("alarmTimeBegin", alarmTimeBegin);
            param.AddKeyValue("alarmTimeEnd", alarmTimeEnd);
            param.AddKeyValue("transferTypeCode", transferTypeCode);
            param.AddKeyValue("from", from);
            param.AddKeyValue("max",max);

            BaseMessage result = Grobal.getXmppManager().Send(param);

        if (result.getClass().equals(EMServiceException.class)) {
            throw new EwellException(((EMServiceException) result).getMessage());
        }
        return (AlarmList) result;

    }

    public EMLoginUser Login(String loginName, String loginPwd) throws EwellException {

            EMProperties param = new EMProperties("Login", "sleepcareforiphone");
            param.AddKeyValue("loginName", loginName);
            param.AddKeyValue("loginPassword", loginPwd);
            BaseMessage result = Grobal.getXmppManager().Send(param);

        if (result.getClass().equals(EMServiceException.class)) {
            throw new EwellException(((EMServiceException) result).getMessage());
        }
        return ( EMLoginUser) result;
    }

    public MainInfo GetPartInfoWithoutFollowBedUser(String loginName, String mainCode) throws EwellException {
            EMProperties param = new EMProperties("GetPartInfoWithoutFollowBedUser", "sleepcareforiphone");
            param.AddKeyValue("loginName", loginName);
            param.AddKeyValue("mainCode", mainCode);
            BaseMessage result = Grobal.getXmppManager().Send(param);

        if (result.getClass().equals(EMServiceException.class)) {
            throw new EwellException(((EMServiceException) result).getMessage());
        }
        return (MainInfo) result;
    }

    public ServerResult FollowBedUser(String loginName,String bedUserCode,String mainCode) throws EwellException{

            EMProperties param = new EMProperties("FollowBedUser", "sleepcareforiphone");
            param.AddKeyValue("loginName", loginName);
            param.AddKeyValue("bedUserCode", bedUserCode);
            param.AddKeyValue("mainCode", mainCode);
            BaseMessage result = Grobal.getXmppManager().Send(param);

        if (result.getClass().equals(EMServiceException.class)) {
            throw new EwellException(((EMServiceException) result).getMessage());
        }
        return (ServerResult) result;
    }

public ServerResult RemoveFollowBedUser(String loginName,String bedUserCode) throws EwellException{

    EMProperties param = new EMProperties("RemoveFollowBedUser", "sleepcareforiphone");
    param.AddKeyValue("loginName", loginName);
    param.AddKeyValue("bedUserCode", bedUserCode);
    BaseMessage result = Grobal.getXmppManager().Send(param);

    if (result.getClass().equals(EMServiceException.class)) {
        throw new EwellException(((EMServiceException) result).getMessage());
    }
    return (ServerResult) result;
    }

    public BedUserList GetBedUsersByLoginName(String loginName, String mainCode) throws EwellException{
        EMProperties param = new EMProperties("GetBedUsersByLoginName", "sleepcareforiphone");
        param.AddKeyValue("loginName", loginName);
        param.AddKeyValue("mainCode", mainCode);
        BaseMessage result = Grobal.getXmppManager().Send(param);

        if (result.getClass().equals(EMServiceException.class)) {
            throw new EwellException(((EMServiceException) result).getMessage());
        }
        return (BedUserList) result;

    }

    public  ServerResult ModifyLoginUser(String loginName, String oldPassword, String newPassword,String mainCode) throws EwellException{
        EMProperties param = new EMProperties("ModifyLoginUser", "sleepcareforiphone");
        param.AddKeyValue("loginName", loginName);
        param.AddKeyValue("oldPassword", oldPassword);
        param.AddKeyValue("newPassword", newPassword);
        param.AddKeyValue("mainCode", mainCode);
        BaseMessage result = Grobal.getXmppManager().Send(param);

        if (result.getClass().equals(EMServiceException.class)) {
            throw new EwellException(((EMServiceException) result).getMessage());
        }
        return (ServerResult) result;

    }

}

