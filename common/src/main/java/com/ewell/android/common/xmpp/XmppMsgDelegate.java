package com.ewell.android.common.xmpp;

/**
 * Created by Dongjg on 2016-7-13.
 */


public interface XmppMsgDelegate<T> {
    void ReciveMessage(T t);

}
