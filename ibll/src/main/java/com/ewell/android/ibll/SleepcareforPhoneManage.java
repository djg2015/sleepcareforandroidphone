package com.ewell.android.ibll;

import com.ewell.android.common.exception.EwellException;
import com.ewell.android.model.AlarmList;
import com.ewell.android.model.BedUserList;
import com.ewell.android.model.EMLoginUser;
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
public interface SleepcareforPhoneManage {
    //7 确认新密码（忘记密码）
    ServerResult ConfirmNewPassword(String loginName, String vcCode, String newPassword) throws EwellException;

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


    //25 安卓版本更新
    //status 0.启用 1.禁用 注： value为“”代表所有
    //type 1：机构版 3.个人版
    VersionList GetVersionForPhone(String status, String type) throws EwellException;

    //27.	开启远程通知(安卓手机)
    ServerResult OpenNotificationForAndroid(String deviceID, String loginName) throws EwellException;

    //28.	关闭远程通知(安卓手机)
    ServerResult CloseNotificationForAndroid(String deviceID, String loginName) throws EwellException;


    //-----------------新增接口-----------------
    //处理报警信息 "002"处理，“003”误报警
    void TransferAlarmMessage(String alarmCode, String transferType) throws EwellException;


    //根据当前登录用户、报警类型、时间段、处理状态等多条件获取关注老人的报警信息
    //参数： loginName
    //      schemaCode->报警类型
    //      alarmTimeBegin,alarmTimeEnd->报警开始结束时间yyyy-MM-dd
    //      transferTypeCode->报警处理类型 001未处理  002已处理  003误报警
    //      from->开始记录序号 (为空表示查询全部）
    //      max->返回最大记录数量 (为空表示查询全部 )
    AlarmList GetAlarmByLoginUser(String mainCode, String loginName, String schemaCode, String alarmTimeBegin, String alarmTimeEnd, String transferTypeCode, String from, String max) throws EwellException;

    // 2登录认证
    // 参数：loginName->登录账户
    //      loginPassword->登录密码
    EMLoginUser Login(String loginName, String loginPwd) throws EwellException;


    //根据医院/养老院编号获取楼层以及床位用户信息（排除已经关注的老人）
    // 参数：loginName->登录账户
    //      mainCode->医院/养老院编号
    MainInfo GetPartInfoWithoutFollowBedUser(String loginName, String mainCode) throws EwellException;


    // 确认关注床位用户信息
    // 参数：loginName->登录账户
    //      bedUserCode->床位用户编码
    //      mainCode->医院/养老院编号
    ServerResult FollowBedUser(String loginName,String bedUserCode,String mainCode) throws EwellException;

    // 移除已关注的床位用户
    // 参数：loginName->登录账户
    //      bedUserCode->床位用户编码
    ServerResult RemoveFollowBedUser(String loginName,String bedUserCode) throws EwellException;

    // 根据当前登录用户获取所关注的床位用户
    // 参数：loginName->登录账户
    //      mainCode->医院/养老院编号
    BedUserList GetBedUsersByLoginName(String loginName, String mainCode) throws EwellException;

    // 修改登录用户密码和所属医院/养老院
    // 参数：loginName->登录账户
    //      oldPassword->旧的登录密码
    //      newPassword->新的登录密码
    //      mainCode->医院/养老院编码
    ServerResult ModifyLoginUser(String loginName, String oldPassword, String newPassword,String mainCode) throws EwellException;
}
