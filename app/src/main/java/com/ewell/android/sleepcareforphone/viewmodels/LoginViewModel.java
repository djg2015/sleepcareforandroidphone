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
import com.ewell.android.sleepcareforphone.activities.LoginActivity;
import com.ewell.android.sleepcareforphone.activities.MyPatientsActivity;
import com.ewell.android.sleepcareforphone.common.AlarmHelper;

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
    public void btnCommand(final View view) {
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


                EMLoginUser emLoginUser = sleepcareforPhoneManage.Login(this.loginName, this.password);
                Toast.makeText(loginActivity, "正在登录,请稍后...", Toast.LENGTH_SHORT).show();
                //记录在全局变量里
                Grobal.getInitConfigModel().setMaincode(emLoginUser.getMainCode());
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
                // 把数据给保存到sp里面
                editor.commit();


                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // 线程阻塞优化：post方法
                        view.post(new Runnable() {
                            @Override
                            public void run() {
                                //开报警
                                AlarmHelper.GetInstance().ReloadAlarms();
                                //跳转下一页面
                                Intent intent = new Intent(loginActivity, MyPatientsActivity.class);
                                loginActivity.startActivity(intent);

                                System.out.println("post执行---线程id = "+ Thread.currentThread().getId());
                            }
                        });

                    }
                }).start();




            } else {
                //弹出连接失败消息
                Toast.makeText(loginActivity,"无法连接服务器!", Toast.LENGTH_SHORT).show();
            }

        } catch (EwellException ex) {

            Toast.makeText(loginActivity, "登录失败!"+ex.get_exceptionMsg(), Toast.LENGTH_SHORT).show();

            //做消息弹窗提醒
        } catch (Exception e) {
            //做消息弹窗提醒
            e.printStackTrace();
        }

    }


}
