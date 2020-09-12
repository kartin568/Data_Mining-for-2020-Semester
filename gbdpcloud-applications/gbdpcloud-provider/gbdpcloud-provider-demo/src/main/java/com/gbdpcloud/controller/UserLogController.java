package com.gbdpcloud.controller;

import com.gbdpcloud.entity.UserLog;
import com.gbdpcloud.service.UserLogService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.constant.GlobalConstant;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseController;
import gbdpcloudcommonbase.gbdpcloudcommonbase.dto.UacUserDto;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.Result;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.ResultGenerator;
import gbdpcloudcommonbase.gbdpcloudcommonbase.security.UacUserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

@Api(value = "UserLogController")
@RequestMapping("/UserlogController")
@Validated
@RestController
public class UserLogController extends BaseController {

    @Resource
    private UserLogService userLogService;

    public void addLog(String action) {
        UserLog userLog=new UserLog();
        UacUserDto user= UacUserUtils.getUserInfoFromRequest();
        userLog.setUser_id(user.getId());
        userLog.setUsername(user.getLoginName());
        userLog.setOperate(action);
        userLogService.save(userLog);
    }

    @ApiOperation(value = "按条件查找日志")
    @GetMapping("/log/find")
    public Result findLog(
            @RequestParam(required = false,value = "username")String username,
            @RequestParam(required = false,value = "startDate")String startDate,
            @RequestParam(required = false,value = "endDate")String endDate,
            @RequestParam(required = false,value = "operate")String operate){

        List<UserLog> list = userLogService.selectAll();
        if(username != null && username.length()>0){
            List<UserLog> list1 = userLogService.selectByUsername(username);
            list.retainAll(list1);
        }
        if(startDate == null || startDate.length()<=0){
            startDate = "1970-1-1";
        }
        if(endDate == null || endDate.length()<=0){
            endDate = "9999-12-31";
        }

        try{
            List<UserLog> list2 = userLogService.selectByDateRange(startDate,endDate);
            list.retainAll(list2);
        }catch (ParseException e){;}

        if(operate != null && operate.length()>0){
            List<UserLog> list3 = userLogService.selectByOperate(operate);
            list.retainAll(list3);
        }

        return ResultGenerator.genSuccessResult(list);

    }






}
