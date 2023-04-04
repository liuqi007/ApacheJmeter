package org.apache.jmeter;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StartJmeter extends Thread {
    String[] args = null;
    String cmd = null;
    Object instance = null;
    Class<?> initialClass = null;
    Map<String, String> testData = null;
    NewDynamicClassloader loader = null;

    public StartJmeter(NewDynamicClassloader loader , String cmd) {

        this.loader = loader;
        this.cmd = cmd;

        String[] tmps = cmd.split("##");
        for (int i = 0; i < tmps.length; i++) {
            System.out.println("StartJmeter.tmps:"+tmps[i]);
        }

        testData = parseData(tmps[1]);
        System.out.println("testData:"+testData);

        String jobId = testData.get("jobId");
        System.out.println("jobId:"+jobId);

        if (jobId != null) {
            //set error log dir
            String logPath = NewDriver.JMETER_INSTALLATION_DIRECTORY + File.separator + "logs";
            File logDir = new File(logPath);

            if (!logDir.exists()) {
                logDir.mkdir();
            }

            String jmeterLogFile = logPath + File.separator + "JOB_" + jobId + ".log";
            String assertFailLogFile = logPath + File.separator + "ASSET_ERR_" + jobId + ".log";

            System.setProperty("jmeter.logfile", jmeterLogFile);
            System.setProperty("assert.err.logfile", assertFailLogFile);
        }

        String scriptPath = testData.get("fullJmxFileName");

        args = (tmps[0] + scriptPath).split(" ");
        for (int i = 0; i < args.length; i++) {
            System.out.println("#####StartJmeter.args"+ args[i]);
        }

        NewDriver.setLoggingProperties(args);

        try {
            initialClass = loader.loadClass("org.apache.jmeter.NewJMeter");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Jmeter home dir was detected as:" + NewDriver.JMETER_INSTALLATION_DIRECTORY);
        }
    }


    public Map<String, String> parseData(String keyValue) {
        if(null != keyValue) {
            String[] kvs = keyValue.split(";");

            Map<String, String> map = new HashMap<>();

            for(int i = 0;i<kvs.length ;i++){
                String item = kvs[i];
                String[] itemKV = item.split("=");
                map.put(itemKV[0], itemKV[1]);
            }
            return map;
        }
        return null;
    }

    public Object getInstance() {
         return  instance;
    }
}
