package com.ewell.android.sleepcareforphone.viewmodels;

import android.databinding.Bindable;
import android.os.Environment;
import android.view.View;

import com.ewell.android.bll.DataFactory;
import com.ewell.android.common.Grobal;
import com.ewell.android.common.exception.EwellException;
import com.ewell.android.common.log.LogManager;
import com.ewell.android.common.xmpp.XmppMsgDelegate;
import com.ewell.android.ibll.SleepcareforPhoneManage;
import com.ewell.android.model.BaseMessage;
import com.ewell.android.model.EMLoginUser;
import com.ewell.android.model.EMRealTimeReport;
import com.ewell.android.sleepcareforphone.BR;

/**
 * Created by Dongjg on 2016-7-13.
 */
public class LoginViewModel extends BaseViewModel implements XmppMsgDelegate<EMRealTimeReport> {
    private String loginName;

    @Bindable
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    private String password;

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String status;

    @Bindable
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        notifyPropertyChanged(BR.status);
    }

    private String hr;

    @Bindable
    public String getHr() {
        return hr;
    }

    public void setHr(String hr) {
        this.hr = hr;
        notifyPropertyChanged(BR.hr);
    }

    private String rr;

    @Bindable
    public String getRr() {
        return rr;
    }

    public void setRr(String rr) {
        this.rr = rr;
        notifyPropertyChanged(BR.rr);
    }

    private String time;

    @Bindable
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
        notifyPropertyChanged(BR.time);
    }

    //-----------界面处理事件
    //事件
    public void btnCommand(View view) {
        SleepcareforPhoneManage sleepcareforPhoneManage = DataFactory.GetSleepcareforPhoneManage();
        try {
            Grobal.getInitConfigModel().setXmppUserName(this.loginName);
            if (Grobal.getXmppManager().Connect()) {
                EMLoginUser emLoginUser = sleepcareforPhoneManage.SingleLogin(this.loginName, this.password);
                this.setStatus("登录成功");
                //记录日志
                Grobal.getLogManager().LogInfo("登录成功！");
                //记住用户名和密码

                //设置消息代理
                Grobal.getXmppManager().SetXmppMsgDelegate(this);

            } else {
                //弹出连接失败消息
            }

        } catch (EwellException ex) {
            //做消息弹窗提醒
        } catch (Exception e) {
            //做消息弹窗提醒

        }
    }
    public void loginoutCommand(View view) {

            Grobal.getXmppManager().Close();

    }

    @Override
    public void ReciveMessage(EMRealTimeReport emRealTimeReport) {
        this.setHr(emRealTimeReport.getHR());
        this.setRr(emRealTimeReport.getRR());
        this.setTime(emRealTimeReport.getTime());
    }
}
