package com.ewell.android.bll;

import com.ewell.android.ibll.SleepcareforPhoneManage;

/**
 * Created by Dongjg on 2016-7-13.
 */
public class DataFactory {
    private static SleepcareforPhoneManage sleepcareforPhoneManage =null;
    public static SleepcareforPhoneManage GetSleepcareforPhoneManage(){
        if(sleepcareforPhoneManage == null) {
            sleepcareforPhoneManage =  new SleepcareforPhoneBusiness();
        }
        return  sleepcareforPhoneManage;
    }
}
