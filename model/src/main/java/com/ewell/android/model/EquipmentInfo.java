package com.ewell.android.model;

/**
 * Created by lillix on 7/24/16.
 */
public class EquipmentInfo {

        private String EquipmentID = "";
        public void setEquipmentID(String equipmentid){EquipmentID = equipmentid;}
        public String getEquipmentID(){return EquipmentID;}

        private String BedUserCode = "";
        public void setBedUserCode(String bedusercode){BedUserCode = bedusercode;}
         public String getBedUserCode(){return BedUserCode;}

        private String BedUserName = "";
        public void setBedUserName(String bedusername){BedUserName = bedusername;}
        public String getBedUserName(){return BedUserName;}

        private String Sex = "";
        public void setSex(String sex){Sex = sex;}
        public String getSex(){return Sex;}

        private String MobilePhone = "";
        public void setMobilePhone(String mobilephone){MobilePhone = mobilephone;}
        public String getMobilePhone(){return MobilePhone;}

        private String Address = "";
        public void setAddress(String address){Address = address;}
        public String getAddress(){return Address;}
}
