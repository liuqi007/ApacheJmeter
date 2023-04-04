package org.apache.jmeter;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class InitUserClassPath extends Thread {
    private String downLoadURL = null;
    private String callback = null;
    private Map<String, String> jars = null;
    private DynamicClassLoader loader = null;

    public InitUserClassPath(NewDynamicClassloader loader ,String cmd) {
        String[] tmps = cmd.split("##");
        this.loader = loader;
        this.downLoadURL = tmps[0].split("@")[0];
        this.callback = tmps[0].split("@")[1];

        if(tmps.length>1)
        this.jars = this.parseData(tmps[1]);
    }

    @Override
    public void run() {

        if (loader != null) {
            try {
                loader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String pluginsDir = NewDriver.JMETER_INSTALLATION_DIRECTORY + File.separator + "userlib";
        File dir = new File(pluginsDir);
        if(!dir.exists()) {
            dir.mkdir();
        }else {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        }

        //download jar
        if (jars != null && jars.keySet().size()>0) {
            Iterator<String> iterator = jars.keySet().iterator();
            while (iterator.hasNext()){
                String fileId = iterator.next();
                if ("jobId".equals(fileId) || "ip".equals(fileId) || "callbackUrl".equals(fileId)) {
                    continue;
                }

                String jarName = jars.get(fileId);
                try {
                    FileUtils.copyURLToFile(new URL(this.downLoadURL + "=" + fileId), new File(pluginsDir + File.separator + jarName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

//        NewDriver.reInitUserClasspath();
//
//        initUserClasspathDone();
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
