package com.ewell.android.sleepcareforphone.viewmodels;

import android.widget.Toast;

import com.ewell.android.bll.DataFactory;
import com.ewell.android.common.Grobal;
import com.ewell.android.common.exception.EwellException;
import com.ewell.android.ibll.SleepcareforPhoneManage;
import com.ewell.android.model.ServerResult;
import com.ewell.android.sleepcareforphone.activities.RegistAccountActivity;

/**
 * Created by lillix on 7/18/16.
 */
public class RegistAccountViewModel extends BaseViewModel {

    private RegistAccountActivity registAccountActivity;

    public void SetParentActivity(RegistAccountActivity mregistActivity) {
        registAccountActivity = mregistActivity;
    }

    private String telephone="";

    public String getTelephone() {
        return telephone;
    }

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


    private String vcCode="";

    public void setVcCode(String vccode) {
        vcCode = vccode;
    }
    public String getVcCode(){return vcCode;}

    private boolean isCheckedProtocol = false;

    public void setIsCheckedProtocal(Boolean isCheckedFlag) {
        isCheckedProtocol = isCheckedFlag;
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
                Toast.makeText(registAccountActivity,"无法连接服务器!", Toast.LENGTH_SHORT).show();
            }
        } catch (EwellException ex) {
            //做消息弹窗提醒
            Toast.makeText(registAccountActivity, ex.getExceptionMsg(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //做消息弹窗提醒
            e.printStackTrace();
        }
    }


    public Boolean CheckEquipmentIDAfterScan(String equipementid) {

        SleepcareforPhoneManage sleepcareforPhoneManage = DataFactory.GetSleepcareforPhoneManage();
        Boolean flag = false;
        try {
            Grobal.getInitConfigModel().setXmppUserName("pad");

            if (Grobal.getXmppManager().Connect()) {
                ServerResult mresult = sleepcareforPhoneManage.CheckEquipmentID(equipementid);
                flag = mresult.getResult();
            } else {
                Toast.makeText(registAccountActivity,"无法连接服务器!", Toast.LENGTH_SHORT).show();
            }
        } catch (EwellException ex) {
            //做消息弹窗提醒
            Toast.makeText(registAccountActivity, ex.getExceptionMsg(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //做消息弹窗提醒
            e.printStackTrace();
        }
        return flag;
    }

    public Boolean RegistCommand() {
        SleepcareforPhoneManage sleepcareforPhoneManage = DataFactory.GetSleepcareforPhoneManage();
        Boolean flag = false;

        try {
            Grobal.getInitConfigModel().setXmppUserName("pad");
            if (Grobal.getXmppManager().Connect()) {
                //检查输入参数
                if ( this.telephone.equals("")) {
                    Toast.makeText(registAccountActivity, "手机号不能为空!", Toast.LENGTH_LONG).show();
                    return false;
                }
                if ( this.verifynum.equals("")) {
                    Toast.makeText(registAccountActivity, "手机验证码不能为空!", Toast.LENGTH_LONG).show();
                    return false;
                }
                if ( this.textnewpassword.equals("")) {
                    Toast.makeText(registAccountActivity, "密码不能为空!", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (this.vcCode.equals("")) {
                    Toast.makeText(registAccountActivity, "设备号不能为空!请点击扫一扫进行扫描", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (!this.isCheckedProtocol) {
                    Toast.makeText(registAccountActivity, "请勾选同意服务协议!", Toast.LENGTH_LONG).show();
                    return false;
                }

                //调用接口
                ServerResult mresult = sleepcareforPhoneManage.SingleRegist(this.telephone, this.textnewpassword, this.verifynum, this.vcCode);
                flag = mresult.getResult();
            } else {
                Toast.makeText(registAccountActivity,"无法连接服务器!", Toast.LENGTH_SHORT).show();
            }

        } catch (EwellException ex) {
            //做消息弹窗提醒
            Toast.makeText(registAccountActivity, ex.getExceptionMsg(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //做消息弹窗提醒
            e.printStackTrace();
        }
        return flag;
    }

}
