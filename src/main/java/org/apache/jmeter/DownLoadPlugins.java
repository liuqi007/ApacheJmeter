package org.apache.jmeter;

import org.apache.commons.io.FileUtils;
import org.apache.jmeter.NewDriver;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DownLoadPlugins extends Thread {
    private String downLoadURL = null;
    private Map<String, String> jars = null;

    public DownLoadPlugins(String cmd) {
        String[] tmps = cmd.split("##");
        this.downLoadURL = tmps[0];
        this.jars = this.parseData(tmps[1]);
    }

    @Override
    public void run() {
        String pluginsDir = NewDriver.JMETER_INSTALLATION_DIRECTORY + File.separator + "lib";
        File dir = new File(pluginsDir);
        if(!dir.exists()) {
            dir.mkdir();
        }

        try {
            Iterator<String> iterator = jars.keySet().iterator();
            while (iterator.hasNext()){
                String fileId = iterator.next();
                String jarName = jars.get(fileId);
                FileUtils.copyURLToFile(new URL(this.downLoadURL+"="+fileId), new File(pluginsDir + File.separator + jarName));
            }
        }catch (Exception e) {
            e.printStackTrace();
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
}
