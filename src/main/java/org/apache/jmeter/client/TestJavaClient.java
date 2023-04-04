package org.apache.jmeter.client;

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestJavaClient extends AbstractJavaSamplerClient {
    private static final Logger log = LoggerFactory.getLogger(TestJavaClient.class);
    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        log.info("testJavaClient");
        return new SampleResult();
    }
}
