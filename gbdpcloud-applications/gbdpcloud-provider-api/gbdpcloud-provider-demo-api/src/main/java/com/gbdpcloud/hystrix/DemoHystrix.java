package com.gbdpcloud.hystrix;

import com.gbdpcloud.entity.DemoDemo;
import com.gbdpcloud.service.FeignDemoDemoService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.Result;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.ResultCode;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.ResultGenerator;
import gbdpcloudcommonbase.gbdpcloudcommonbase.page.PageRequest;
import org.springframework.stereotype.Component;

/**
 * demoservice 熔断器回调
 * @Author kongweichang
 */
@Component
public class DemoHystrix implements FeignDemoDemoService {

    @Override
    public Result save(DemoDemo demoDemo) {
        // 当服务请求失败时，返回自定义的内容
        return ResultGenerator.genFailResult(ResultCode.API_PROCESSING_FAILED);    }

    @Override
    public Result update(DemoDemo demoDemo) {
        // 当服务请求失败时，返回自定义的内容
        return ResultGenerator.genFailResult(ResultCode.API_PROCESSING_FAILED);    }

    @Override
    public Result delete(String id) {
        return ResultGenerator.genFailResult(ResultCode.API_PROCESSING_FAILED);
    }

    @Override
    public Result deleteMany(String ids) {
        return ResultGenerator.genFailResult(ResultCode.API_PROCESSING_FAILED);
    }

    @Override
    public Result get(String id) {
        return ResultGenerator.genFailResult(ResultCode.API_PROCESSING_FAILED);
    }

    @Override
    public Result list(PageRequest pageRequest, DemoDemo demoDemo) {
        return ResultGenerator.genFailResult(ResultCode.API_PROCESSING_FAILED);
    }
}
