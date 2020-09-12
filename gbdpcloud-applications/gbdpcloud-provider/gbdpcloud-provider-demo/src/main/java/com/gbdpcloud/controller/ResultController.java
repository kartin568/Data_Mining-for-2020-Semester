package com.gbdpcloud.controller;

import com.gbdpcloud.service.ResultService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseController;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(value = "ResultController")
@RequestMapping("/ResultController")
@Validated
@RestController
public class ResultController extends BaseController {

    @Resource
    private ResultService resultService;
}
