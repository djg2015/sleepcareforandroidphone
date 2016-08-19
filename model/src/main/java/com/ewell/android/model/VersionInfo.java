package com.ewell.android.model;

/**
 * Created by lillix on 8/9/16.
 */
public class VersionInfo {
private String VersionCode;
    public String getVersionCode(){return VersionCode;}
    public void setVersionCode(String versioncode){VersionCode = versioncode;}

    private String VersionNum;
    public String getVersionNum(){return VersionNum;}
    public void setVersionNum(String versionnum){VersionNum = versionnum;}


    private String DownloadPath;
    public String getDownloadPath(){return DownloadPath;}
    public void setDownloadPath(String downloadpath){DownloadPath = downloadpath;}

    private String Status;
    public String getStatus(){return Status;}
    public void setStatus(String status){Status = status;}
}
