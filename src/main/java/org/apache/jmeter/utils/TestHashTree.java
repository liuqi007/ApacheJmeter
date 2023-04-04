package org.apache.jmeter.utils;

import org.apache.jmeter.client.TestJavaClient;
import org.apache.jmeter.save.SaveService;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class TestHashTree {
    private static final Logger log = LoggerFactory.getLogger(TestHashTree.class);
    public static void main(String[] args) throws IOException {
        HashTree tree = SaveService.loadTree(new File("d://a.jmx"));
        for(Iterator it = tree.keySet().iterator(); it.hasNext();) {
            Object key = it.next();
            log.info( "key val: "+ key + tree.get(key));
        }
    }
}
