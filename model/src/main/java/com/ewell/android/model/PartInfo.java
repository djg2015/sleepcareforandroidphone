package com.ewell.android.model;

import java.util.ArrayList;

/**
 * Created by lillix on 10/28/16.
 */
public class PartInfo {
    private String PartCode = "";
    public void setPartCode(String partcode){PartCode = partcode;}
    public String getPartCode(){return PartCode;}

    private String PartName = "";
    public void setPartName(String partname){PartName = partname;}
    public String getPartName(){return PartName;}

    private String MainName = "";
    public void setMainName(String mainname){MainName = mainname;}
    public String getMainName(){return MainName;}

    private ArrayList<BedInfo> BedInfoList = new ArrayList<BedInfo>();
    public void setBedInfoList(ArrayList<BedInfo> bedinfolist){BedInfoList = bedinfolist;}
    public ArrayList<BedInfo> getBedInfoList(){return BedInfoList;}


}
