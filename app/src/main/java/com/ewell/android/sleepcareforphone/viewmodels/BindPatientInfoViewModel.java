package com.ewell.android.sleepcareforphone.viewmodels;

import android.databinding.Bindable;
import android.widget.Toast;

import com.ewell.android.bll.DataFactory;
import com.ewell.android.common.Grobal;
import com.ewell.android.common.exception.EwellException;
import com.ewell.android.ibll.SleepcareforPhoneManage;
import com.ewell.android.model.BedUserInfo;
import com.ewell.android.model.ServerResult;
import com.ewell.android.sleepcareforphone.activities.BindPatientInfoActivity;

/**
 * Created by lillix on 7/19/16.
 */
public class BindPatientInfoViewModel extends BaseViewModel {
    private BindPatientInfoActivity parentactivity;
    public void setParentactivity(BindPatientInfoActivity activity){parentactivity = activity;}

    private String textpatientname = "";

    @Bindable
    public String getPatientName() {
        return textpatientname;
    }


    private String textpatientcode = "";
    public String getPatientCode(){return textpatientcode;}

    private String sex = "";

    private String textpatienttelephone;

    @Bindable
    public String getPatientTelephone() {
        return textpatienttelephone;
    }

    public void setPatientTelephone(String telephone) {
        this.textpatienttelephone = telephone;
    }

    private String textpatientaddress = "";

    @Bindable
    public String getPatientAddress() {
        return textpatientaddress;
    }

    public void setPatientAddress(String address) {
        this.textpatientaddress = address;
    }

    private Boolean isFemale = false;

    public Boolean getIsFemale() {
        return isFemale;
    }

    public void setIsFemale(Boolean isfemale) {
        this.isFemale = isfemale;
    }


    private Boolean isMale = false;

    public Boolean getIsMale() {
        return isMale;
    }

    public void setIsMale(Boolean ismale) {
        this.isMale = ismale;
    }

    private SleepcareforPhoneManage sleepcareforPhoneManage = DataFactory.GetSleepcareforPhoneManage();

    private String equipmentid = "";

    public void setEquipmentid(String id) {
        equipmentid = id;
    }

    //-----------界面处理事件
    public void InitData() {
        if (!equipmentid.equals("")) {
            try {
                Grobal.getInitConfigModel().setXmppUserName("pad");
                if (Grobal.getXmppManager().Connect()) {
                    BedUserInfo tempBedUserInfo = sleepcareforPhoneManage.GetBedUserInfoByEquipmentID(equipmentid);
                    this.textpatientname = tempBedUserInfo.getBedUserName();
                    this.textpatientcode = tempBedUserInfo.getBedUserCode();
                    this.textpatienttelephone = tempBedUserInfo.getMobilePhone();
                    this.textpatientaddress = tempBedUserInfo.getAddress();
                    this.sex = tempBedUserInfo.getSex();
                    if (sex.equals("1")) {
                        this.isMale = true;
                    } else if (sex.equals("2")) {
                        this.isFemale = true;
                    }

                } else {
                    Toast.makeText(parentactivity,"无法连接服务器!", Toast.LENGTH_SHORT).show();
                }
            } catch (EwellException ex) {
                //做消息弹窗提醒
                Toast.makeText(parentactivity, ex.getExceptionMsg(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                //做消息弹窗提醒
                e.printStackTrace();
            }
        }
    }


    public Boolean ConfirmPatientInfoCommand() {
        Boolean flag = false;

        try {
            Grobal.getInitConfigModel().setXmppUserName("pad");
            if (Grobal.getXmppManager().Connect()) {
                if (isFemale) {
                    sex = "2";
                } else if (isMale) {
                    sex = "1";
                } else {
                    sex = "";
                }

                ServerResult result = sleepcareforPhoneManage.ModifyBedUserInfo(this.textpatientcode, this.textpatientname, this.sex, this.textpatienttelephone, this.textpatientaddress);
                flag = result.getResult();
            } else {
                Toast.makeText(parentactivity,"无法连接服务器!", Toast.LENGTH_SHORT).show();
            }
        } catch (EwellException ex) {
            //做消息弹窗提醒
            Toast.makeText(parentactivity, ex.getExceptionMsg(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //做消息弹窗提醒
            e.printStackTrace();
        }

        return flag;

    }
}
