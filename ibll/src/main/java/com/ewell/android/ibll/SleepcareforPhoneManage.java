package com.ewell.android.ibll;

import com.ewell.android.common.exception.EwellException;
import com.ewell.android.model.EMLoginUser;

/**
 * Created by Dongjg on 2016-7-13.
 */
public interface SleepcareforPhoneManage {
    EMLoginUser SingleLogin(String loginName,String loginPwd) throws EwellException;
}
