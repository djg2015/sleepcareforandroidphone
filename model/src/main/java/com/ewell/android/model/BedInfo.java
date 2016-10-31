package com.ewell.android.model;

/**
 * Created by lillix on 10/28/16.
 */
public class BedInfo {

    private String RoomNumber = "";
    public void setRoomNumber(String roomnumber){RoomNumber = roomnumber;}
    public String getRoomNumber(){return RoomNumber;}

    private String BedNumber = "";
    public void setBedNumber(String bednumber){BedNumber = bednumber;}
    public String getBedNumber(){return BedNumber;}

    private String BedCode = "";
    public void setBedCode(String bedcode){BedCode = bedcode;}
    public String getBedCode(){return BedCode;}

    private String BedUserCode = "";
    public void setBedUserCode(String bedusercode){BedUserCode = bedusercode;}
    public String getBedUserCode(){return BedUserCode;}

    private String BedUserName = "";
    public void setBedUserName(String bedusername){BedUserName = bedusername;}
    public String getBedUserName(){return BedUserName;}

    private String EquipmentID = "";
    public void setEquipmentID(String id){EquipmentID = id;}
    public String getEquipmentID(){return EquipmentID;}

}
