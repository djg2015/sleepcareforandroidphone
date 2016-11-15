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
        addUserCodeList = new HashMap<>();
        List<PartandPatientEntity> temppartandPatientDic = new ArrayList<>();

        if (Grobal.getXmppManager().Connect()) {
            MainInfo maininfo = sleepcareforPhoneManage.GetPartInfoWithoutFollowBedUser(mloginname,mmaincode);
            ArrayList<PartInfo> temppartinfolist =  maininfo.getPartInfoList();


for(int i=0;i<temppartinfolist.size();i++){

    PartandPatientEntity temppartandpatient = new PartandPatientEntity();
    temppartandpatient.setPartname(temppartinfolist.get(i).getPartName());
    List<patientEntity> temppatientlist = new ArrayList<>();

   // System.out.print(temppartinfolist.get(i).getPartName()+"===========\n");

    ArrayList<BedInfo> tempbedinfolist = temppartinfolist.get(i).getBedInfoList();
    for(int j=0;j<tempbedinfolist.size();j++){
      //  System.out.print(tempbedinfolist.get(j).getBedUserName()+"===========\n");

        patientEntity temppatient = new patientEntity();
        temppatient.setBedusercode(tempbedinfolist.get(j).getBedUserCode());
        temppatient.setBedusername(tempbedinfolist.get(j).getBedUserName());
        temppatient.setBednum(tempbedinfolist.get(j).getBedNumber());
        temppatient.setRoomnum(tempbedinfolist.get(j).getRoomNumber());
        temppatient.setEquipmentid(tempbedinfolist.get(j).getEquipmentID());
        temppatient.setBedcode(tempbedinfolist.get(j).getBedCode());
        temppatient.setSex(tempbedinfolist.get(j).getSex());
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


                //把要增加的mypatients列表元素记录在全局变量AddpatientitemList里
                //更新全局变量UserCodeEquipmentMap
                List<Map<String, String>> addpatientList = new ArrayList<>();
                Map<String, String> tempequipmentmap = Grobal.getInitConfigModel().getUserCodeEquipmentMap();

                for (PartandPatientEntity dicvalue:partandPatientDic) {
                    List<patientEntity> partpatientlist = dicvalue.getPatient();
                    for (patientEntity patient:partpatientlist) {
                        if(usercodelist.keySet().contains(patient.getBedusercode())){
                            Map<String,String> newpatient = new HashMap<>();
                            newpatient.put("bedusercode",patient.getBedusercode());
                            newpatient.put("sex",patient.getSex());
                            newpatient.put("roomnum",patient.getRoomnum());
                            newpatient.put("bednum",patient.getBednum());
                            newpatient.put("bedusername",patient.getBedusername());
                            newpatient.put("bedcode",patient.getBedcode());
                            newpatient.put("equipmentid",patient.getEquipmentid());

                            newpatient.put("hr","0");
                            newpatient.put("rr","0");
                            newpatient.put("onbedstatus","检测中");

                            addpatientList.add(newpatient);

                            tempequipmentmap.put(patient.getBedusercode(),patient.getEquipmentid());
                        }
                    }
                }
Grobal.getInitConfigModel().setAddpatientitemList(addpatientList);
Grobal.getInitConfigModel().setUserCodeEquipmentMap(tempequipmentmap);

                //返回我的病人列表
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
        private String sex="";
        public String getSex(){return sex;}
        public void setSex(String s){sex = s;}

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

        private String bedcode="";
        public String getBedcode(){return bedcode;}
        public void setBedcode(String mbedcode){bedcode = mbedcode;}


        private String equipmentid="";
        public String getEquipmentid(){return equipmentid;}
        public void setEquipmentid(String id){equipmentid = id;}


        private Boolean isChoosed=false;
        public Boolean getIsChoosed(){return isChoosed;}
        public void setIsChoosed(Boolean flag){isChoosed = flag;}
    }
}
