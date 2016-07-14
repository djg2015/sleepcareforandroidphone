package com.ewell.android.common.log;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;

import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * Created by Dongjg on 2016-7-12.
 */
public class LogManager {
    private LogManager(){}
    private static  LogManager _logManager = null;

    public static LogManager GetInstance(String logFileName)
    {
        if(_logManager == null){

            _logManager = new LogManager();
            final LogConfigurator logConfigurator = new LogConfigurator();

            logConfigurator.setFileName(logFileName);
            logConfigurator.setRootLevel(Level.DEBUG);
            logConfigurator.setLevel("sleepcare.debug", Level.DEBUG);
            logConfigurator.setLevel("sleepcare.info", Level.INFO);
            logConfigurator.setLevel("sleepcare.error", Level.ERROR);
            logConfigurator.setFilePattern("%L %d %-5p [%c{2}]-[%L] %m%n %n");

            logConfigurator.setMaxFileSize(1024 * 1024 * 5);
            logConfigurator.setImmediateFlush(true);
            logConfigurator.configure();
        }

        return  _logManager;
    }

    /*
    * 信息日志
    * */
    public void LogInfo(String msg){
        Logger log = Logger.getLogger("sleepcare.info");
        log.info(msg);
    }

    /*
  * 调试日志
  * */
    public void LogDebug(String error){
        Logger log = Logger.getLogger("sleepcare.debug");
        log.info(error);
    }

    /*
   * 错误日志
   * */
    public void LogError(String error){
        Logger log = Logger.getLogger("sleepcare.error");
        log.info(error);
    }
}
