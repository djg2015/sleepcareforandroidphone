package com.ewell.android.sleepcareforphone.viewmodels;

import android.widget.Toast;

import com.ewell.android.bll.DataFactory;
import com.ewell.android.common.Grobal;
import com.ewell.android.common.exception.EwellException;
import com.ewell.android.ibll.SleepcareforPhoneManage;
import com.ewell.android.model.BedUserInfo;
import com.ewell.android.model.BedUserList;
import com.ewell.android.sleepcareforphone.activities.MyPatientsActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lillix on 11/1/16.
 */
public class MyPatientsViewModel extends BaseViewModel {
    private MyPatientsActivity parentactivity;
    public void setParentactivity(MyPatientsActivity activity){parentactivity = activity;}

    private List<Map<String,Object>> listItems=new ArrayList<Map<String,Object>>();
    public List<Map<String, Object>> getList() {
        return listItems;
    }

    private SleepcareforPhoneManage sleepcareforPhoneManage;
    private  String username="";
    private  String maincode="";

    public void InitData() {
        sleepcareforPhoneManage = DataFactory.GetSleepcareforPhoneManage();
        try {
            if (Grobal.getXmppManager().Connect()) {
                username = Grobal.getInitConfigModel().getLoginUserName();
                maincode = Grobal.getInitConfigModel().getMaincode();
                BedUserList tempBeduserlist = sleepcareforPhoneManage.GetBedUsersByLoginName(username,maincode);
                ArrayList<BedUserInfo> beduserinfoList = tempBeduserlist.getBeduserinfoInfoList();
ArrayList<String> _bedusercodelist = new ArrayList<String>();

                for(int i=0;i<beduserinfoList.size();i++) {
                    Map<String, Object> map = new HashMap<String, Object>();

                    map.put("bedusercode", beduserinfoList.get(i).getBedUserCode());
                    map.put("bedusername", beduserinfoList.get(i).getBedUserName());
                    map.put("roomnum", beduserinfoList.get(i).getRoomNum());
                    map.put("bednum", beduserinfoList.get(i).getBedNum());
                    map.put("bedcode", beduserinfoList.get(i).getBedCode());
                    map.put("partcode", beduserinfoList.get(i).getPartCode());
                    map.put("equipmentid", beduserinfoList.get(i).getEquipmentID());
                    map.put("sex", beduserinfoList.get(i).getSex());

                    this.listItems.add(map);
                    _bedusercodelist.add(beduserinfoList.get(i).getBedUserCode());
                }
                Grobal.getInitConfigModel().setBedusercodeList(_bedusercodelist);

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

   public Boolean DeletePatient(int position){
       Boolean flag = false;

       return flag;
    }
}
