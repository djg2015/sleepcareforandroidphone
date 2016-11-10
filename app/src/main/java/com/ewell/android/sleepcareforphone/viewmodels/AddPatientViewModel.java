package com.ewell.android.sleepcareforphone.viewmodels;

import android.widget.Toast;

import com.ewell.android.bll.DataFactory;
import com.ewell.android.common.Grobal;
import com.ewell.android.common.exception.EwellException;
import com.ewell.android.ibll.SleepcareforPhoneManage;
import com.ewell.android.model.BedInfo;
import com.ewell.android.model.MainInfo;
import com.ewell.android.model.PartInfo;
import com.ewell.android.sleepcareforphone.activities.AddPatientActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lillix on 11/4/16.
 */
public class AddPatientViewModel {
    private SleepcareforPhoneManage sleepcareforPhoneManage;

    private AddPatientActivity parentactivity;
    public void setParentactivity(AddPatientActivity activity){parentactivity = activity;}

    //要添加的病人code
    private Map<String,String> addUserCodeList;
    public void setAddUserCodeList(Map<String,String> templist){addUserCodeList = templist;}
    public Map<String,String> getAddUserCodeList(){return addUserCodeList;}

    //病区---病区下的所有未关注的病人
    private List<PartandPatientEntity> partandPatientDic;
    public void setPartandPatientDic(List<PartandPatientEntity> mpartandPatientDic) {
        this.partandPatientDic = mpartandPatientDic;
    }
    public List<PartandPatientEntity> getPartandPatientDic() {
        return partandPatientDic;
    }


private String mloginname="";
    private String mmaincode = "";

    public void InitData(){

    sleepcareforPhoneManage = DataFactory.GetSleepcareforPhoneManage();
        mloginname = Grobal.getInitConfigModel().getLoginUserName();
        mmaincode = Grobal.getInitConfigModel().getMaincode();

    try {
        addUserCodeList = new HashMap<String,String>();
        List<PartandPatientEntity> temppartandPatientDic = new ArrayList<PartandPatientEntity>();

        if (Grobal.getXmppManager().Connect()) {
            MainInfo maininfo = sleepcareforPhoneManage.GetPartInfoWithoutFollowBedUser(mloginname,mmaincode);
            ArrayList<PartInfo> temppartinfolist =  maininfo.getPartInfoList();


for(int i=0;i<temppartinfolist.size();i++){

    PartandPatientEntity temppartandpatient = new PartandPatientEntity();
    temppartandpatient.setPartname(temppartinfolist.get(i).getPartName());
    List<patientEntity> temppatientlist = new ArrayList<patientEntity>();

   // System.out.print(temppartinfolist.get(i).getPartName()+"===========\n");

    ArrayList<BedInfo> tempbedinfolist = temppartinfolist.get(i).getBedInfoList();
    for(int j=0;j<tempbedinfolist.size();j++){
      //  System.out.print(tempbedinfolist.get(j).getBedUserName()+"===========\n");

        patientEntity temppatient = new patientEntity();
        temppatient.setBedusercode(tempbedinfolist.get(j).getBedUserCode());
        temppatient.setBedusername("姓名:"+tempbedinfolist.get(j).getBedUserName());
        temppatient.setBednum("床号:"+tempbedinfolist.get(j).getBedNumber());
        temppatient.setRoomnum("房号:"+tempbedinfolist.get(j).getRoomNumber());
        temppatient.setEquipmentid( "设备编号:"+tempbedinfolist.get(j).getEquipmentID());
        temppatientlist.add(temppatient);
    }
    temppartandpatient.setPatientList(temppatientlist);
    temppartandPatientDic.add(temppartandpatient);
}

            partandPatientDic = temppartandPatientDic;

        }//end if
    }
    catch (EwellException ex) {
        //做消息弹窗提醒
        Toast.makeText(parentactivity,ex.get_exceptionMsg(), Toast.LENGTH_SHORT).show();
    } catch (Exception e) {
        //做消息弹窗提醒
        e.printStackTrace();
    }
}
    public void AddPatient(Map<String,String> usercodelist) {
        try {
            if (Grobal.getXmppManager().Connect()) {
                ArrayList<String> tempbedusercodes = Grobal.getInitConfigModel().getBedusercodeList();
               Map<String,String> tempmap = Grobal.getInitConfigModel().getUserCodeNameMap();

for(String code : usercodelist.keySet()) {
    // 更新服务端
    sleepcareforPhoneManage.FollowBedUser(mloginname, code,mmaincode);

    //更新bedusercodeList,UserCodeNameMap
    tempbedusercodes.add(code);
    tempmap.put(code,usercodelist.get(code));

}
                Grobal.getInitConfigModel().setBedusercodeList(tempbedusercodes);
                Grobal.getInitConfigModel().setUserCodeNameMap(tempmap);

                Toast.makeText(parentactivity,"添加成功!返回我的病人", Toast.LENGTH_SHORT).show();


                parentactivity.finish();
            }//end if

        }
        catch (EwellException ex) {
            //做消息弹窗提醒
            Toast.makeText(parentactivity,ex.get_exceptionMsg(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //做消息弹窗提醒
            e.printStackTrace();
        }

    }

    public class PartandPatientEntity {
        private String partname;
        private List<patientEntity> patient;

        public void setPartname(String name) {
            this.partname = name;
        }
        public void setPatientList(List<patientEntity> mpatient) {
            this.patient = mpatient;
        }

        public String getPartname() {
            return partname;
        }
        public List<patientEntity> getPatient() {
            return patient;
        }
    }

    public class patientEntity{
        private String bedusername="";
        public String getBedusername(){return bedusername;}
        public void setBedusername(String mbedusername){bedusername = mbedusername;}

        private String bedusercode="";
        public String getBedusercode(){return bedusercode;}
        public void setBedusercode(String mbedusercode){bedusercode = mbedusercode;}

        private String roomnum="";
        public String getRoomnum(){return roomnum;}
        public void setRoomnum(String mroomnum){roomnum = mroomnum;}

        private String bednum="";
        public String getBednum(){return bednum;}
        public void setBednum(String mbednum){bednum = mbednum;}

        private String equipmentid="";
        public String getEquipmentid(){return equipmentid;}
        public void setEquipmentid(String id){equipmentid = id;}


        private Boolean isChoosed=false;
        public Boolean getIsChoosed(){return isChoosed;}
        public void setIsChoosed(Boolean flag){isChoosed = flag;}
    }
}
