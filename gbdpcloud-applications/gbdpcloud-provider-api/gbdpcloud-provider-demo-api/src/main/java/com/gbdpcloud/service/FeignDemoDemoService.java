package com.gbdpcloud.service;

import com.gbdpcloud.constants.DemoConstants;
import com.gbdpcloud.entity.DemoDemo;
import com.gbdpcloud.hystrix.DemoHystrix;
import gbdpcloudcommon.gbdpcloudsecurityfeign.feign.FeignConfig;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.Result;
import gbdpcloudcommonbase.gbdpcloudcommonbase.page.PageRequest;
import gbdpcloudcommonbase.gbdpcloudcommonbase.validation.CreateGroup;
import gbdpcloudcommonbase.gbdpcloudcommonbase.validation.UpdateGroup;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 *  DemoDemoService
 *  feign所定义的接口类，需要供其他项目访问的接口在这里定义
 *  @Author kongweichang
 */
@FeignClient(value= DemoConstants.BASE_SERVER, configuration = FeignConfig.class,fallback = DemoHystrix.class)
public interface FeignDemoDemoService {

    @PostMapping
    Result save(@RequestBody @Validated(CreateGroup.class) DemoDemo demoDemo);

    @PutMapping
    Result update(@RequestBody @Validated(UpdateGroup.class) DemoDemo demoDemo);

    @DeleteMapping("/{id}")
    Result delete(@PathVariable String id);

    @DeleteMapping
    Result deleteMany(@RequestParam(name = "ids") String ids);

    @GetMapping("/{id}")
    Result get(@PathVariable String id);

    @GetMapping
    Result list(PageRequest pageRequest, DemoDemo demoDemo);
}
