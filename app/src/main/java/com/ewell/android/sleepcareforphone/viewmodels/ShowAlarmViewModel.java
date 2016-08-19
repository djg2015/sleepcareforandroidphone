package com.ewell.android.sleepcareforphone.viewmodels;

import android.widget.Toast;

import com.ewell.android.bll.DataFactory;
import com.ewell.android.common.Grobal;
import com.ewell.android.common.exception.EwellException;
import com.ewell.android.ibll.SleepcareforPhoneManage;
import com.ewell.android.model.AlarmInfo;
import com.ewell.android.model.AlarmList;
import com.ewell.android.model.EquipmentInfo;
import com.ewell.android.model.EquipmentList;
import com.ewell.android.model.ServerResult;
import com.ewell.android.sleepcareforphone.activities.ShowAlarmActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lillix on 7/25/16.
 */
public class ShowAlarmViewModel extends BaseViewModel {
    private ShowAlarmActivity parentactivity;
    public void setParentactivity(ShowAlarmActivity activity){parentactivity = activity;}

    private List<Map<String,Object>> listItems=new ArrayList<Map<String,Object>>();

    public List<Map<String, Object>> getList() {
        return listItems;
    }

    private SleepcareforPhoneManage sleepcareforPhoneManage;
    private  String username="";

    public void InitData() {
        sleepcareforPhoneManage = DataFactory.GetSleepcareforPhoneManage();

        try {
            if (Grobal.getXmppManager().Connect()) {
                username = Grobal.getInitConfigModel().getLoginUserName();

                AlarmList tempAlarmlist = sleepcareforPhoneManage.GetSingleAlarmByLoginUser(username,"","","","001","","");
                EquipmentList tempequipmentlist = sleepcareforPhoneManage.GetEquipmentsByLoginName(username);
                ArrayList<EquipmentInfo> tempequipmentinfoList = tempequipmentlist.getEquipmentInfoList();

                ArrayList<AlarmInfo> alarminfoList = tempAlarmlist.getAlarmInfoList();
                for(int i=0;i<alarminfoList.size();i++){

                    Map<String, Object> map = new HashMap<String,Object>();
                    map.put("alarmcode", alarminfoList.get(i).getAlarmCode());
                    map.put("timeanddate",alarminfoList.get(i).getAlarmTime());
                    map.put("username", alarminfoList.get(i).getUserName());
                    map.put("sex", alarminfoList.get(i).getUserSex());
                    map.put("bednumber", alarminfoList.get(i).getBedNumber());
                    map.put("alarmcontent", alarminfoList.get(i).getSchemaContent());
                    String type="";
                    switch(alarminfoList.get(i).getSchemaCode()){
                        case "ALM_TEMPERATURE":
                            type = "体温";
                            break;
                        case "ALM_HEARTBEAT":
                            type = "心率";
                            break;
                        case "ALM_BREATH":
                            type = "呼吸";
                            break;
                        case "ALM_BEDSTATUS":
                            type = "在离床";
                            break;
                        case  "ALM_FALLINGOUTOFBED":
                            type = "坠床风险";
                            break;
                        case  "ALM_BEDSORE":
                            type = "褥疮风险";
                            break;
                        case  "ALM_CALL":
                            type = "呼叫";
                            break;
                        default:
                            type = "";
                            break;

                    }
                    map.put("alarmtype",type);


                    //EQUIPEMENTID
                    String tempusercode=alarminfoList.get(i).getUserCode();
                    String tempid = "";

                    for(int ii = 0;i<tempequipmentinfoList.size();ii++){
                        if (tempequipmentinfoList.get(ii).getBedUserCode().equals(tempusercode)){
                            tempid = tempequipmentinfoList.get(ii).getEquipmentID();
                            break;
                        }

                    }
                    map.put("equipmentid",tempid);


                    this.listItems.add(map);
                }
                System.out.print(listItems);
            }
            else{
                Toast.makeText(parentactivity,"无法连接服务器!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (EwellException ex) {
            //做消息弹窗提醒
            Toast.makeText(parentactivity, ex.getExceptionMsg(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //做消息弹窗提醒
            e.printStackTrace();
        }
    }


    public Boolean DeleteAlarm(int position){
        Boolean flag = false;
        try {
            if (Grobal.getXmppManager().Connect()) {
                String id = listItems.get(position).get("alarmcode").toString();

                ServerResult result =  sleepcareforPhoneManage.DeleteAlarmMessage(id,username);
                if (result.getResult()){
                    //从unhandledcodes里删除
ArrayList<String> tempalarmcodes=Grobal.getInitConfigModel().getUnhandledAlarmcodeList();
                    for(int i=0;i<tempalarmcodes.size();i++){
                        if(tempalarmcodes.get(i).equals(id)){
                            tempalarmcodes.remove(i);
                            Grobal.getInitConfigModel().setUnhandledAlarmcodeList(tempalarmcodes);
                            break;
                        }
                    }
                    flag = true;
                }

            }
            else{
                Toast.makeText(parentactivity,"无法连接服务器!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (EwellException ex) {
            //做消息弹窗提醒
            Toast.makeText(parentactivity, ex.getExceptionMsg(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //做消息弹窗提醒
            e.printStackTrace();
        }
        return flag;
    }

}
