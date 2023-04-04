package org.apache.jmeter;

import java.net.URL;

public class NewDynamicClassloader extends DynamicClassLoader{
    public NewDynamicClassloader(URL[] urls) {
        super(urls);
    }

//    @Override
//    public Class<?> loadClass(String name) throws ClassNotFoundException {
//        try {
//            return super.loadClass(name);
//        }catch (Exception e) {
//            if (name.indexOf("java")<0 && name.indexOf("org.apache.jmeter")<0 && name.indexOf("bsh")<0 && name.indexOf("jdk")<0) {
//                throw e;
//            }
//        }
//        return null;
//    }
}
