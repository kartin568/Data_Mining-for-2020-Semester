package com.gbdpcloud.service;

import com.gbdpcloud.entity.ResultErr;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.Service;
import io.swagger.annotations.ApiOperation;

import java.util.List;

public interface ResultErrService extends Service<ResultErr> {

    @ApiOperation(value = "通过测试计划id获取测试结果列表")
    List<ResultErr> getByTest(String id);
    @ApiOperation(value = "对结果进行排序")
    void sort(int order, int sortBy, List<ResultErr> list);
}
