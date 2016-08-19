package com.ewell.android.ibll;

import com.ewell.android.common.exception.EwellException;
import com.ewell.android.model.AlarmList;
import com.ewell.android.model.BedUserInfo;
import com.ewell.android.model.EMLoginUser;
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
public interface SleepcareforPhoneManage {
    // 2登录认证
    // 参数：loginName->登录账户
    //      loginPassword->登录密码
    EMLoginUser SingleLogin(String loginName, String loginPwd) throws EwellException;

    //3.获取手机验证码
    //参数： mobileNum－> 11位手机号
    ServerResult GetVerificationCode(String mobileNum) throws EwellException;

    //4 注册个人用户信息
    //参数：loginName－>账户名（手机号）
    //     loginPassword->密码
    //     vcCode->验证码
    //     equipmentID->设备id
    ServerResult SingleRegist(String loginName, String loginPassword, String vcCode, String equipmentID) throws EwellException;

    //5 修改账户信息
    //参数：loginName－>账户名（手机号）
    //     oldPassword->旧密码
    //     newPassword->新密码
    ServerResult ModifyAccount(String loginName, String oldPassword, String newPassword) throws EwellException;


    //6设备编号认证
    //参数：equipmentID
    ServerResult CheckEquipmentID(String equipmentID) throws EwellException;

    //7 确认新密码（忘记密码）
    ServerResult ConfirmNewPassword(String loginName, String vcCode, String newPassword) throws EwellException;

    //8 根据设备编号查询对应老人信息
    //参数：equipmentID
    BedUserInfo GetBedUserInfoByEquipmentID(String equipmentID) throws EwellException;

    //9 完善老人信息
    //参数：bedUserCode,bedUserName,sex,mobilePhone,address
    ServerResult ModifyBedUserInfo(String bedUserCode, String bedUserName, String sex, String mobilePhone, String address) throws EwellException;

    //10移除设备
    //参数：loginName
    //     equipmentIDs->要移除的设备编码。（多个用半角“，”隔开）
    ServerResult RemoveEquipment(String loginName, String equipmentIDs) throws EwellException;

    //11 获取登录账户绑定的设备(包括设备对应的老人信息 )
    //参数：loginName
    EquipmentList GetEquipmentsByLoginName(String loginName) throws EwellException;


    //12获取心率曲线图
    //参数：bedUserCode
    //     searchType－>查询类型（1：近 24 小时 2：近一周  3：近一月）
    HRRange GetSingleHRTimeReport(String bedUserCode, String searchType) throws EwellException;

    //13获取呼吸曲线图
    //参数：bedUserCode
    //     searchType－>查询类型（1：近 24 小时 2：近一周  3：近一月）
    RRRange GetSingleRRTimeReport(String bedUserCode, String searchType) throws EwellException;

    //14查询老人睡眠质量
    //参数：bedUserCode
    //     reportDate ->查询时间yyyy-MM-dd
    SleepQualityReport GetSleepQualityofBedUser(String bedUserCode, String reportDate) throws EwellException;

    // 15根据床位用户编码和邮箱信息发送邮件(指定日期所在周的报表)
    // 参数：bedUserCode->床位用户编号
    //      sleepDate->分析日期
    //      email->要发送的邮箱地址
    ServerResult SendEmail(String bedUserCode, String sleepDate, String email) throws EwellException;

    //16查询指定日期所在周的老人睡眠报表
    //参数：bedUserCode
    //     reportDate ->查询时间yyyy-MM-dd
    WeekSleep GetWeekSleepofBedUser(String bedUserCode, String reportDate) throws EwellException;

    //17根据当前登录用户、报警类型、时间段、处理状态等多条件获取关注老人的报警信息
    //参数： loginName
    //      schemaCode->报警类型
    //      alarmTimeBegin,alarmTimeEnd->报警开始结束时间yyyy-MM-dd
    //      transferTypeCode->报警处理类型 001未处理  002已处理  003误报警
    //      from->开始记录序号 (为空表示查询全部）
    //      max->返回最大记录数量 (为空表示查询全部 )
    AlarmList GetSingleAlarmByLoginUser(String loginName, String schemaCode, String alarmTimeBegin, String alarmTimeEnd, String transferTypeCode, String from, String max) throws EwellException;


    //18删除报警信息
    ServerResult DeleteAlarmMessage(String alarmCodes, String loginName) throws EwellException;

    //19添加设备
    ServerResult BindEquipmentofUser(String equipmentID, String loginName) throws EwellException;


    //25 安卓版本更新
    //status 0.启用 1.禁用 注： value为“”代表所有
    //type 1：机构版 3.个人版
    VersionList GetVersionForPhone(String status, String type) throws EwellException;

    //27.	开启远程通知(安卓手机)
    ServerResult OpenNotificationForAndroid(String deviceID, String loginName) throws EwellException;

    //28.	关闭远程通知(安卓手机)
    ServerResult CloseNotificationForAndroid(String deviceID, String loginName) throws EwellException;

}
