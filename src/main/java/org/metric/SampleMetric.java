package org.metric;

import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.visualizers.backend.SamplerMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleMetric extends SamplerMetric {
    private static final Logger log = LoggerFactory.getLogger(SampleMetric.class);

    private static final String sepChar = " \n ";

    @Override
    public synchronized void add(SampleResult result) {
        log.error("接口名称："+result.getSampleLabel()+sepChar+"请求体："+result.getSamplerData()+sepChar+"响应："+result.getResponseDataAsString());
    }
}
