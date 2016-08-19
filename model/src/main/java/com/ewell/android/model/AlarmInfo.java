package com.ewell.android.model;

/**
 * Created by lillix on 7/25/16.
 */
public class AlarmInfo {
    private String AlarmCode = "";
    public void setAlarmCode(String alarmcode){AlarmCode = alarmcode;}
    public String getAlarmCode(){return AlarmCode;}

    private String UserCode = "";
    public void setUserCode(String usercode){UserCode = usercode;}
    public String getUserCode(){return UserCode;}

    private String UserName = "";
    public void setUserName(String username){UserName = username;}
    public String getUserName(){return UserName;}

    private String UserSex = "";
    public void setUserSex(String usersex){UserSex = usersex;}
    public String getUserSex(){return UserSex;}

    private String BedCode = "";
    public void setBedCode(String bedcode){BedCode = bedcode;}
    public String getBedCode(){return BedCode;}

    private String BedNumber = "";
    public void setBedNumber(String bednumber){BedNumber = bednumber;}
    public String getBedNumber(){return BedNumber;}


    private String SchemaCode = "";
    public void setSchemaCode(String schemacode){SchemaCode = schemacode;}
    public String getSchemaCode(){return SchemaCode;}

    private String SchemaContent = "";
    public void setSchemaContent(String schemacontent){SchemaContent = schemacontent;}
    public String getSchemaContent(){return SchemaContent;}

    private String AlarmDate = "";
    public void setAlarmDate(String alarmdate){AlarmDate = alarmdate;}
    public String getAlarmDate(){return AlarmDate;}

    private String AlarmTime = "";
    public void setAlarmTime(String alarmtime){AlarmTime = alarmtime;}
    public String getAlarmTime(){return AlarmTime;}

    private String HandleFlag = "";
    public void setHandleFlag(String handleflag){HandleFlag = handleflag;}
    public String getHandleFlag(){return HandleFlag;}

}
