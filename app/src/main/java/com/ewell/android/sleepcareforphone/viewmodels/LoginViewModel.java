package com.ewell.android.sleepcareforphone.viewmodels;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.databinding.Bindable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.ewell.android.bll.DataFactory;
import com.ewell.android.common.Crypt;
import com.ewell.android.common.Grobal;
import com.ewell.android.common.exception.EwellException;
import com.ewell.android.ibll.SleepcareforPhoneManage;
import com.ewell.android.model.EMLoginUser;
import com.ewell.android.model.EquipmentInfo;
import com.ewell.android.model.EquipmentList;
import com.ewell.android.sleepcareforphone.activities.LoginActivity;
import com.ewell.android.sleepcareforphone.activities.TabbarActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import com.ewell.android.sleepcareforphone.BR;

/**
 * Created by Dongjg on 2016-7-13.
 */
public class LoginViewModel extends BaseViewModel {
    //implements XmppMsgDelegate<EMRealTimeReport> {

    private LoginActivity loginActivity;

    public void SetParentActivity(LoginActivity mloginActivity) {
        loginActivity = mloginActivity;
    }

    private String loginName = "";

    @Bindable
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    private String password = "";

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //  记住密码
    @Bindable
    private Boolean isSavepassword = false;

    public Boolean getIsSavepassword() {
        return isSavepassword;
    }

    public void setCheckbox(Boolean isSavepassword) {
        this.isSavepassword = isSavepassword;
    }

    //  纪录历史登录信息
    private SharedPreferences sp;

    public void setSharedPreferences(SharedPreferences sp) {
        this.sp = sp;
    }


    //-----------界面处理事件
    //记住密码
    public void checkboxCommand(View view) {

        this.isSavepassword = ((CheckBox) view).isChecked();
    }


    //登录
    public void btnCommand(View view) {
        SleepcareforPhoneManage sleepcareforPhoneManage = DataFactory.GetSleepcareforPhoneManage();
        try {
            Grobal.getInitConfigModel().setXmppUserName(this.loginName);
            if (Grobal.getXmppManager().Connect()) {
//检查输入
                if (this.loginName.equals("")) {
                    Toast.makeText(loginActivity, "登录名不能为空!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (this.password.equals("")) {
                    Toast.makeText(loginActivity, "登录密码不能为空!", Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(loginActivity, "正在登录...", Toast.LENGTH_SHORT).show();
                EMLoginUser emLoginUser = sleepcareforPhoneManage.SingleLogin(this.loginName, this.password);

                //记录在全局变量里
                Grobal.getInitConfigModel().setLoginUserName(this.loginName);
                Grobal.getInitConfigModel().setLoginUserPWD(this.password);
                // 获取到一个参数文件的编辑器。
                Editor editor = sp.edit();
                //记住用户名和密码
                if (isSavepassword) {
                    editor.putString("loginname", this.loginName);
                    editor.putString("password", Crypt.encrypt("key", this.password));
                } else {
                    editor.putString("loginname", "");
                    editor.putString("password", "");
                }
                editor.putBoolean("isSavePassword", this.isSavepassword);


                //记录日志
                Grobal.getLogManager().LogInfo("登录成功！");
                editor.putBoolean("isLogin",true);


                //获取当前账户下的设备列表,当前关注的老人code/name
                EquipmentList tempequipmentlist = sleepcareforPhoneManage.GetEquipmentsByLoginName(this.loginName);
                ArrayList<EquipmentInfo> tempequipmentinfoList = tempequipmentlist.getEquipmentInfoList();


                Map<String, String> map = new HashMap<String, String>();
                ArrayList<String> list = new ArrayList<String>();
                ArrayList<String> list2 = new ArrayList<String>();
                for (int i = 0; i < tempequipmentinfoList.size(); i++) {
                    map.put(tempequipmentinfoList.get(i).getBedUserCode(), tempequipmentinfoList.get(i).getBedUserName());
                    list.add(tempequipmentinfoList.get(i).getEquipmentID());
                    list2.add(tempequipmentinfoList.get(i).getBedUserCode());
                }
                Grobal.getInitConfigModel().setUserCodeNameMap(map);
                Grobal.getInitConfigModel().setEquipmentcodeList(list);
                Grobal.getInitConfigModel().setBedusercodeList(list2);

                //获取curusercode,curusername,进行验证
                String tempusername = sp.getString("curusername", "");
                String tempusercode = sp.getString("curusercode", "");
                //当前没有usercode,则置空
                if (list.size() == 0) {
                    tempusercode = "";
                    tempusername = "";
                }
                //当前只有一个usercode,则默认选中
                else if (list.size() == 1) {
                    for (String key : map.keySet()) {
                        tempusercode = key;
                        tempusername = map.get(key);
                    }
                }
                //当前有多个usercode,则验证纪录里的curusercode是否在当前usercode里
                else if (tempusercode != null && !tempusercode.equals("")) {
                    Boolean flag = false;
                    for (String s : map.keySet()) {
                        if (s.equals(tempusercode)) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        tempusercode = "";
                        tempusername = "";
                    }
                }
                Grobal.getInitConfigModel().setCurUserCode(tempusercode);
                Grobal.getInitConfigModel().setCurUserName(tempusername);
                editor.putString("curusercode", tempusercode);
                editor.putString("curusername", tempusername);


                // 把数据给保存到sp里面
                editor.commit();

                //跳转下一页面
                Intent intent = new Intent(this.loginActivity, TabbarActivity.class);
                this.loginActivity.startActivity(intent);

            } else {
                //弹出连接失败消息
                Toast.makeText(loginActivity,"无法连接服务器!", Toast.LENGTH_SHORT).show();
            }

        } catch (EwellException ex) {

            Toast.makeText(loginActivity, "登录失败!"+ex.getExceptionMsg(), Toast.LENGTH_SHORT).show();

            //做消息弹窗提醒
        } catch (Exception e) {
            //做消息弹窗提醒
            e.printStackTrace();
        }

    }




//    @Override
//    public void ReciveMessage(EMRealTimeReport emRealTimeReport) {
//        this.setHr(emRealTimeReport.getHR());
//        this.setRr(emRealTimeReport.getRR());
//        this.setTime(emRealTimeReport.getTime());
//    }
}
