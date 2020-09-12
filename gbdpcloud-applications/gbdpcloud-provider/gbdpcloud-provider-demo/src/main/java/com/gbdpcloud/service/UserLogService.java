package com.gbdpcloud.service;

import com.gbdpcloud.entity.UserLog;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.Service;
import io.swagger.annotations.ApiOperation;

import java.text.ParseException;
import java.util.List;

public interface UserLogService extends Service<UserLog> {

    @ApiOperation(value = "根据用户名查询日志")
    List<UserLog> selectByUsername(String username);

    @ApiOperation(value = "根据时间范围查询日志")
    List<UserLog> selectByDateRange(String startDate, String endDate) throws ParseException;

    @ApiOperation(value = "根据操作查询日志")
    List<UserLog> selectByOperate(String Operate);

    @ApiOperation(value = "查询所有日志记录")
    List<UserLog> selectAll();
}
