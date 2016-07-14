package com.ewell.android.model;

import org.jdom.JDOMException;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Dongjg on 2016-7-12.
 */
public class EMProperties extends BaseMessage {
    private Map params = new HashMap();

    public EMProperties(String operate, String bizCode) {
        super(operate, bizCode);
    }

    /*
    *设置键值
    */
    public void AddKeyValue(String key, String value) {
        this.params.put(key, value);

    }


    @Override
    public String ToBodytXml() {
        String bodyXml = "<EMProperties>";
        Set ss=params.entrySet() ;//返回Map.Entry 接口实现

        Iterator i=ss.iterator() ;   //通过 Map.Entry静态接口 获取元素
        while(i.hasNext())
        {
            Map.Entry me=(Map.Entry)i.next() ;//强制转换
            System.out.println(me.getKey()+":"+me.getValue());
            bodyXml +="<EMProperty name=\""+ me.getKey() +"\" value=\""+ me.getValue() +"\" />";
        }
        bodyXml += "</EMProperties>";
        return bodyXml;
    }
}
