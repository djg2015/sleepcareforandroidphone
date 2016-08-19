package com.ewell.android.sleepcareforphone.viewmodels;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.ewell.android.bll.DataFactory;
import com.ewell.android.common.Grobal;
import com.ewell.android.common.exception.EwellException;
import com.ewell.android.ibll.SleepcareforPhoneManage;
import com.ewell.android.model.EquipmentInfo;
import com.ewell.android.model.EquipmentList;
import com.ewell.android.model.ServerResult;
import com.ewell.android.sleepcareforphone.activities.MyDevicesActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by lillix on 7/21/16.
 */
public class MyDevicesViewModel extends BaseViewModel {
    private MyDevicesActivity parentacticity;

    public void setParentacticity(MyDevicesActivity activity) {
        parentacticity = activity;
    }

    private List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

    public List<Map<String, Object>> getList() {
        return listItems;
    }

    private SleepcareforPhoneManage sleepcareforPhoneManage;
    private String username = "";


    private SharedPreferences sp;
    // 获取到一个参数文件的编辑器。
    private SharedPreferences.Editor editor;

    public void InitData() {
        sleepcareforPhoneManage = DataFactory.GetSleepcareforPhoneManage();
        sp = this.parentacticity.getSharedPreferences("config", Context.MODE_PRIVATE);
        editor = sp.edit();

        try {
            if (Grobal.getXmppManager().Connect()) {
                username = Grobal.getInitConfigModel().getLoginUserName();

                EquipmentList tempEquipmentList = sleepcareforPhoneManage.GetEquipmentsByLoginName(username);

                ArrayList<EquipmentInfo> equipmentinfoList = tempEquipmentList.getEquipmentInfoList();
                for (int i = 0; i < equipmentinfoList.size(); i++) {

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("code", equipmentinfoList.get(i).getBedUserCode());
                    map.put("name", equipmentinfoList.get(i).getBedUserName());
                    map.put("address", equipmentinfoList.get(i).getAddress());
                    map.put("equipmentid", equipmentinfoList.get(i).getEquipmentID());
                    map.put("sex", equipmentinfoList.get(i).getSex());

                    this.listItems.add(map);
                }
                System.out.print(listItems);
            } else {
                Toast.makeText(parentacticity,"无法连接服务器!", Toast.LENGTH_SHORT).show();
            }
        } catch (EwellException ex) {
            //做消息弹窗提醒
            Toast.makeText(parentacticity, ex.getExceptionMsg(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //做消息弹窗提醒
            e.printStackTrace();
        }
    }

    public Boolean DeleteDevice(int position) {
        Boolean flag = false;
        try {
            if (Grobal.getXmppManager().Connect()) {
                String id = listItems.get(position).get("equipmentid").toString();
                String usercode = listItems.get(position).get("code").toString();

                ServerResult result = sleepcareforPhoneManage.RemoveEquipment(username, id);
                if (result.getResult()) {
                    flag = true;
                    //  更新session里的equipmentcodelist,usercodeandnamemap,bedusercodelist
                    ArrayList<String> templist = Grobal.getInitConfigModel().getEquipmentcodeList();
                    Map<String, String> tempmap = Grobal.getInitConfigModel().getUserCodeNameMap();
                    for (int i = 0; i < templist.size(); i++) {
                        if (templist.get(i).equals(id)) {
                            templist.remove(i);
                            break;
                        }
                    }
                    Grobal.getInitConfigModel().setEquipmentcodeList(templist);

                    Iterator<Map.Entry<String, String>> it = tempmap.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<String, String> entry = it.next();
                        String key = entry.getKey();
                        if (key.equals(usercode)) {
                            it.remove();
                            break;
                        }
                    }
                    Grobal.getInitConfigModel().setUserCodeNameMap(tempmap);

                    ArrayList<String> templist2 = Grobal.getInitConfigModel().getBedusercodeList();
                    for (int i = 0; i < templist2.size(); i++) {
                        if (templist2.get(i).equals(usercode)) {
                            templist2.remove(i);
                            break;
                        }
                    }
                    Grobal.getInitConfigModel().setBedusercodeList(templist2);

                    //如果删除的是当前正在关注的老人,则更新curusercode/curusername为空,更新sp
                    String curusercode = Grobal.getInitConfigModel().getCurUserCode();
                    String curusername = Grobal.getInitConfigModel().getCurUserName();
                    if (curusercode.equals(usercode)) {
                        Grobal.getInitConfigModel().setCurUserName("");
                        Grobal.getInitConfigModel().setCurUserCode("");
                        editor.putString("curusercode", "");
                        editor.putString("curusername", "");
                        editor.commit();
                    }
                }

            } else {
                Toast.makeText(parentacticity,"无法连接服务器!", Toast.LENGTH_SHORT).show();
            }
        } catch (EwellException ex) {
            //做消息弹窗提醒
            Toast.makeText(parentacticity, ex.getExceptionMsg(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //做消息弹窗提醒
            e.printStackTrace();
        }
        return flag;
    }

    public Boolean BindDevice(String equipmentid) {
        Boolean flag = false;
        try {
            ServerResult result = sleepcareforPhoneManage.BindEquipmentofUser(equipmentid, username);
        } catch (EwellException ex) {
            //做消息弹窗提醒
            Toast.makeText(parentacticity, ex.getExceptionMsg(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //做消息弹窗提醒
            e.printStackTrace();
        }
        return flag;
    }
}