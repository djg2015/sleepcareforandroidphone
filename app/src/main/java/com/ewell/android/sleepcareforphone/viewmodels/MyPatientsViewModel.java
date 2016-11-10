package com.ewell.android.sleepcareforphone.viewmodels;

import android.widget.Toast;

import com.ewell.android.bll.DataFactory;
import com.ewell.android.common.Grobal;
import com.ewell.android.common.exception.EwellException;
import com.ewell.android.ibll.SleepcareforPhoneManage;
import com.ewell.android.model.BedUserInfo;
import com.ewell.android.model.BedUserList;
import com.ewell.android.model.ServerResult;
import com.ewell.android.sleepcareforphone.activities.MyPatientsActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lillix on 11/1/16.
 */
public class MyPatientsViewModel extends BaseViewModel {
    private SleepcareforPhoneManage sleepcareforPhoneManage;

    private MyPatientsActivity parentactivity;
    public void setParentactivity(MyPatientsActivity activity){parentactivity = activity;}

    private List<Map<String,Object>> listItems;
    public List<Map<String, Object>> getList() {
        return listItems;
    }


    private  String username="";
    private  String maincode="";


    public void InitData() {

        sleepcareforPhoneManage = DataFactory.GetSleepcareforPhoneManage();
        username = Grobal.getInitConfigModel().getLoginUserName();
        maincode = Grobal.getInitConfigModel().getMaincode();
        this.listItems =new ArrayList<Map<String,Object>>();
        try {
            if (Grobal.getXmppManager().Connect()) {

                BedUserList tempBeduserlist = sleepcareforPhoneManage.GetBedUsersByLoginName(username,maincode);
                ArrayList<BedUserInfo> beduserinfoList = tempBeduserlist.getBeduserinfoInfoList();
                ArrayList<String> _bedusercodelist = new ArrayList<String>();
                Map<String, String> namecodemap = new HashMap<String, String>();

                for(int i=0;i<beduserinfoList.size();i++) {
                    Map<String, Object> map = new HashMap<String, Object>();

                    map.put("bedusercode", beduserinfoList.get(i).getBedUserCode());
                    map.put("bedusername", beduserinfoList.get(i).getBedUserName());
                    map.put("roomnum", beduserinfoList.get(i).getRoomNum());
                    map.put("bednum", beduserinfoList.get(i).getBedNum());
                    map.put("bedcode", beduserinfoList.get(i).getBedCode());
                    map.put("partcode", beduserinfoList.get(i).getPartCode());
                    map.put("equipmentid",beduserinfoList.get(i).getEquipmentID());
                    map.put("sex", beduserinfoList.get(i).getSex());

                    this.listItems.add(map);
                    _bedusercodelist.add(beduserinfoList.get(i).getBedUserCode());
                    namecodemap.put(beduserinfoList.get(i).getBedUserCode(),beduserinfoList.get(i).getBedUserName());
                }
                Grobal.getInitConfigModel().setBedusercodeList(_bedusercodelist);
                Grobal.getInitConfigModel().setUserCodeNameMap(namecodemap);
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

   public Boolean DeletePatient(String code){
       Boolean flag = false;
       try {
          ServerResult result =  sleepcareforPhoneManage.RemoveFollowBedUser(username, code);
           flag = result.getResult();
       }catch (EwellException ex) {
           //做消息弹窗提醒
           Toast.makeText(parentactivity,ex.get_exceptionMsg(), Toast.LENGTH_SHORT).show();
           return flag;
       } catch (Exception e) {
           //做消息弹窗提醒
           e.printStackTrace();
           return flag;
       }
       return flag;
    }
}
