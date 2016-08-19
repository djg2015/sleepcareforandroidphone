package com.ewell.android.sleepcareforphone.viewmodels;

import android.databinding.Bindable;
import android.view.View;
import android.widget.Toast;

import com.ewell.android.bll.DataFactory;
import com.ewell.android.common.Grobal;
import com.ewell.android.common.exception.EwellException;
import com.ewell.android.ibll.SleepcareforPhoneManage;
import com.ewell.android.model.ServerResult;
import com.ewell.android.sleepcareforphone.activities.ModifyAccountActivity;

/**
 * Created by lillix on 7/18/16.
 */
public class ModifyAccountViewModel extends BaseViewModel{
    private ModifyAccountActivity modifyaccountActivity;

    public void SetParentActivity(ModifyAccountActivity mmodifyaccountActivity) {
        modifyaccountActivity = mmodifyaccountActivity;
    }


    private String username="15716199610";
    @Bindable
    public String getUsername() {
        return username;
    }

   private String oldpassword="";

    public void setOldpassword(String oldpassword){this.oldpassword = oldpassword;}

    private String newpassword="";
    public void setNewpassword(String newpassword){this.newpassword = newpassword;}

    private  String confirmnewpwd="";
    public void setConfirmnewpwd(String confirmnewpwd){this.confirmnewpwd = confirmnewpwd;}




    public void ConfirmCommand(View view) {

        SleepcareforPhoneManage sleepcareforPhoneManage = DataFactory.GetSleepcareforPhoneManage();
        try {
            Grobal.getInitConfigModel().setXmppUserName("15716199610");
            if (Grobal.getXmppManager().Connect()) {
                //检查输入
                if(this.oldpassword.equals("")){
                    Toast.makeText(modifyaccountActivity, "旧密码不能为空!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(this.newpassword.equals("")){
                    Toast.makeText(modifyaccountActivity, "新密码不能为空!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(this.confirmnewpwd.equals("")){
                    Toast.makeText(modifyaccountActivity, "确认新密码不能为空!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!this.newpassword.equals(this.confirmnewpwd)){
                    Toast.makeText(modifyaccountActivity, "两次新密码不一致!", Toast.LENGTH_LONG).show();
                    return;
                }


                ServerResult mresult = sleepcareforPhoneManage.ModifyAccount(this.username, this.oldpassword, this.newpassword);
                if (mresult.getResult()) {
                    Grobal.getLogManager().LogInfo("修改账户密码成功！");
                } else {
                    Grobal.getLogManager().LogInfo("修改账户密码失败！");
                }

            } else {
                Toast.makeText(modifyaccountActivity,"无法连接服务器!", Toast.LENGTH_SHORT).show();
            }
        } catch (EwellException ex) {
            //做消息弹窗提醒
            Toast.makeText(modifyaccountActivity, ex.getExceptionMsg(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //做消息弹窗提醒
            e.printStackTrace();
        }
    }

}
