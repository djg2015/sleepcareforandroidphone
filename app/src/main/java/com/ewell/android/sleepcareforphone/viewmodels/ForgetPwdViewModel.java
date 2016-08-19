package com.ewell.android.sleepcareforphone.viewmodels;

import android.view.View;
import android.widget.Toast;

import com.ewell.android.bll.DataFactory;
import com.ewell.android.common.Grobal;
import com.ewell.android.common.exception.EwellException;
import com.ewell.android.ibll.SleepcareforPhoneManage;
import com.ewell.android.model.ServerResult;
import com.ewell.android.sleepcareforphone.activities.ForgetPwdActivity;

/**
 * Created by lillix on 7/18/16.
 */
public class ForgetPwdViewModel extends BaseViewModel {

    private ForgetPwdActivity forgetPwdActivity;

    public void SetParentActivity(ForgetPwdActivity mforgetpwdActivity) {
        forgetPwdActivity = mforgetpwdActivity;
    }

    private String telephone="";
public String getTelephone(){return telephone;}
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    private String verifynum="";

    public void setVerifynum(String verifynum) {
        this.verifynum = verifynum;
    }

    private String textnewpassword="";

    public void setNewpassword(String newpassword) {
        this.textnewpassword = newpassword;

    }


    //-----------界面处理事件
    //发送验证码
    public void SendVerifyCode() {

        SleepcareforPhoneManage sleepcareforPhoneManage = DataFactory.GetSleepcareforPhoneManage();
        try {
            Grobal.getInitConfigModel().setXmppUserName("pad");

            if (Grobal.getXmppManager().Connect()) {
                ServerResult mresult = sleepcareforPhoneManage.GetVerificationCode(this.telephone);
                if (mresult.getResult()) {

                    Grobal.getLogManager().LogInfo("发送校验码成功！");
                } else {
                    Grobal.getLogManager().LogInfo("发送校验码失败！");
                }

            } else {
                Toast.makeText(forgetPwdActivity,"无法连接服务器!", Toast.LENGTH_SHORT).show();
            }
        } catch (EwellException ex) {
            //做消息弹窗提醒
            Toast.makeText(forgetPwdActivity, ex.getExceptionMsg(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //做消息弹窗提醒
            e.printStackTrace();
        }
    }

    //确认修改密码
    public void ConfirmCommand(View view) {
        SleepcareforPhoneManage sleepcareforPhoneManage = DataFactory.GetSleepcareforPhoneManage();
        try {
            Grobal.getInitConfigModel().setXmppUserName("pad");
            if (Grobal.getXmppManager().Connect()) {
                 //检查输入

                if ( this.telephone.equals("")) {
                    Toast.makeText(forgetPwdActivity, "手机号不能为空!", Toast.LENGTH_LONG).show();
                    return ;
                }
                if ( this.verifynum.equals("")) {
                    Toast.makeText(forgetPwdActivity, "手机验证码不能为空!", Toast.LENGTH_LONG).show();
                    return ;
                }
                if ( this.textnewpassword.equals("")) {
                    Toast.makeText(forgetPwdActivity, "密码不能为空!", Toast.LENGTH_LONG).show();
                    return ;
                }

                ServerResult mresult = sleepcareforPhoneManage.ConfirmNewPassword(this.telephone, this.verifynum, this.textnewpassword);
                if (mresult.getResult()) {
                    Grobal.getLogManager().LogInfo("修改密码成功！");
                } else {
                    Grobal.getLogManager().LogInfo("修改密码失败！");
                }

            } else {
                Toast.makeText(forgetPwdActivity,"无法连接服务器!", Toast.LENGTH_SHORT).show();
            }
        } catch (EwellException ex) {
            //做消息弹窗提醒
            Toast.makeText(forgetPwdActivity, ex.getExceptionMsg(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //做消息弹窗提醒
            e.printStackTrace();
        }
    }
}
