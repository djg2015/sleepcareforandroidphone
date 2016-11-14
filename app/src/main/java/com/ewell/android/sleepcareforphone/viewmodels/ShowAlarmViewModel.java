package com.ewell.android.sleepcareforphone.viewmodels;

import android.widget.Toast;

import com.ewell.android.bll.DataFactory;
import com.ewell.android.common.Grobal;
import com.ewell.android.common.exception.EwellException;
import com.ewell.android.ibll.SleepcareforPhoneManage;
import com.ewell.android.model.AlarmInfo;
import com.ewell.android.model.AlarmList;
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

    private String CurrentBedUserCode="";
    public void setCurrentBedUserCode(String code){CurrentBedUserCode = code;}

    private SleepcareforPhoneManage sleepcareforPhoneManage;
    private  String username="";
    private String maincode ="";



    public void InitData() {
        sleepcareforPhoneManage = DataFactory.GetSleepcareforPhoneManage();

        try {
            if (Grobal.getXmppManager().Connect()) {
                username = Grobal.getInitConfigModel().getLoginUserName();
                maincode = Grobal.getInitConfigModel().getMaincode();
                AlarmList tempAlarmlist = sleepcareforPhoneManage.GetAlarmByLoginUser(maincode,username,"","","","001","","");
                ArrayList<AlarmInfo> alarminfoList = tempAlarmlist.getAlarmInfoList();

                System.out.print(alarminfoList+"  获取服务器未处理报警============\n");
                Map<String,String> tempuserequipmentMap =  Grobal.getInitConfigModel().getUserCodeEquipmentMap();

                for(int i=0;i<alarminfoList.size();i++) {

                    if (CurrentBedUserCode.equals("") || alarminfoList.get(i).getUserCode().equals(CurrentBedUserCode)) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("alarmcode", alarminfoList.get(i).getAlarmCode());
                        map.put("timeanddate", alarminfoList.get(i).getAlarmTime());
                        map.put("username", alarminfoList.get(i).getUserName());
                        map.put("sex", alarminfoList.get(i).getUserSex());
                        map.put("bednumber", alarminfoList.get(i).getBedNumber());
                        map.put("alarmcontent", alarminfoList.get(i).getSchemaContent());
                        String type = "";
                        switch (alarminfoList.get(i).getSchemaCode()) {
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
                            case "ALM_FALLINGOUTOFBED":
                                type = "坠床风险";
                                break;
                            case "ALM_BEDSORE":
                                type = "褥疮风险";
                                break;
                            case "ALM_CALL":
                                type = "呼叫";
                                break;
                            default:
                                type = "";
                                break;

                        }
                        map.put("alarmtype", type);
                        //EQUIPEMENTID
                        String tempusercode = alarminfoList.get(i).getUserCode();
                        String tempid = "";
                        for (String key :
                                tempuserequipmentMap.keySet()) {
                            if (key.equals(tempusercode)) {
                                tempid = tempuserequipmentMap.get(key);

                                System.out.print(tempid + "  ============\n");
                                break;
                            }
                        }
                        map.put("equipmentid", tempid);
                        this.listItems.add(map);
                    }//end for
                }
            }
            else{
                Toast.makeText(parentactivity,"无法连接服务器!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (EwellException ex) {
            //做消息弹窗提醒
            Toast.makeText(parentactivity,ex.get_exceptionMsg(), Toast.LENGTH_SHORT).show();
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
                 sleepcareforPhoneManage.TransferAlarmMessage(id,"002");
                    flag = true;
            }
            else{
                Toast.makeText(parentactivity,"无法连接服务器!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (EwellException ex) {
            //做消息弹窗提醒
            Toast.makeText(parentactivity, ex.get_exceptionMsg(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //做消息弹窗提醒
            e.printStackTrace();
        }
        return flag;
    }

}
