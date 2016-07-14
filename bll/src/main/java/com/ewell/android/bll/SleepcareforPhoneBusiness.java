package com.ewell.android.bll;

import com.ewell.android.common.Grobal;
import com.ewell.android.common.exception.EwellException;
import com.ewell.android.common.exception.ExceptionEnum;
import com.ewell.android.common.xmpp.XmppManager;
import com.ewell.android.ibll.SleepcareforPhoneManage;
import com.ewell.android.model.BaseMessage;
import com.ewell.android.model.EMLoginUser;
import com.ewell.android.model.EMProperties;
import com.ewell.android.model.EMServiceException;

/**
 * Created by Dongjg on 2016-7-13.
 */
public class SleepcareforPhoneBusiness implements SleepcareforPhoneManage {

    @Override
    public EMLoginUser SingleLogin(String loginName, String loginPwd) throws EwellException {
        try {
            EMProperties param = new EMProperties("SingleLogin", "sleepcareforiphone");
            param.AddKeyValue("loginName", loginName);
            param.AddKeyValue("loginPassword", loginPwd);
            BaseMessage result = Grobal.getXmppManager().Send(param);

            if(result.getClass().equals(EMServiceException.class)){
                throw  new EwellException(ExceptionEnum.XmppBusinessError,((EMServiceException)result).getMessage());
            }
            return  (EMLoginUser)result;
        }
        catch (EwellException ewellEx){
            throw  ewellEx;
        }
        catch (Exception  ex){
            throw new EwellException(ex);
        }
    }
}
