package com.ewell.android.sleepcareforphone.viewmodels;

import android.content.SharedPreferences;
import android.databinding.Bindable;
import android.view.View;
import android.widget.Toast;

import com.ewell.android.common.Grobal;
import com.ewell.android.sleepcareforphone.activities.ServerSettingActivity;

/**
 * Created by lillix on 10/31/16.
 */
public class ServerSettingViewModel extends BaseViewModel {
    private ServerSettingActivity serversettingActivity;

    public void SetParentActivity(ServerSettingActivity serversetting) {
        serversettingActivity = serversetting;
    }

    private String ServerJID="";
    @Bindable

    public String getServerJID() {
        return ServerJID;
    }
    public void setServerJID(String jid){this.ServerJID = jid;}

    private String ServerIP="";
    @Bindable
    public String getServerIP() {
        return ServerIP;
    }
    public void setServerIP(String ip){this.ServerIP = ip;}

    private String ServerPort="";
    @Bindable
    public String getServerPort() {
        return ServerPort;
    }
    public void setServerPort(String port){this.ServerPort = port;}

    private String ServerUser="";
    @Bindable
    public String getServerUser() {
        return ServerUser;
    }
    public void setServerUser(String user){this.ServerUser = user;}

    private String ServerPwd="";
    @Bindable
    public String getServerPwd() {
        return ServerPwd;
    }
    public void setServerPwd(String pwd){this.ServerPwd = pwd;}

    //
    private SharedPreferences sp;

    public void setSharedPreferences(SharedPreferences sp) {
        this.sp = sp;
    }


    public void InitData(){
        ServerIP = Grobal.getInitConfigModel().getXmppServerIP();
ServerJID = Grobal.getInitConfigModel().getXmppServerJID();
 Integer t = Grobal.getInitConfigModel().getXmppServerPort();
      ServerPort = t.toString();
        ServerUser = Grobal.getInitConfigModel().getXmppUserName();
        ServerPwd = Grobal.getInitConfigModel().getXmppUserPWD();
    }

    public void ConfirmCommand(View view) {

        //更新global内容
        Grobal.getInitConfigModel().setXmppServerIP(ServerIP);
        Grobal.getInitConfigModel().setXmppServerJID(ServerJID);
        Grobal.getInitConfigModel().setXmppServerPort(Integer.parseInt(ServerPort));
        Grobal.getInitConfigModel().setXmppUserName(ServerUser);
        Grobal.getInitConfigModel().setXmppUserPWD(ServerPwd);
        //更新config文件内容
        // 获取到一个参数文件的编辑器。
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("XmppServerIP", ServerIP);
        editor.putString("XmppServerJID", ServerJID);
        editor.putInt("XmppServerPort", Integer.parseInt(ServerPort));
        editor.putString("XmppUserName", ServerUser);
        editor.putString("XmppUserPWD", ServerPwd);
        editor.commit();

        //
        Toast.makeText(serversettingActivity, "修改成功!继续登录", Toast.LENGTH_SHORT).show();
       serversettingActivity.finish();
    }
}
