package com.gbdpcloud.controller;

import com.gbdpcloud.entity.ToolDeploymentInfo;
import com.gbdpcloud.service.ToolDeploymentInfoService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseController;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.Result;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

@Api(value = "ToolDeploymentInfoController")
@RequestMapping("/ToolDeploymentInfoController")
@Validated
@RestController
public class ToolDeploymentInfoController extends BaseController {

    @Resource
    private ToolDeploymentInfoService toolDeploymentInfoService;

    @Resource
    private UserLogController userLogController;

    @ApiOperation("辅助函数-测试连接")
    public ToolDeploymentInfo test_Connect(ToolDeploymentInfo toolDeploymentInfo){

        String hostname=toolDeploymentInfo.getHost();
        Integer port=toolDeploymentInfo.getPort();
        try{
            Socket connect = new Socket();
            connect.connect(new InetSocketAddress(hostname, port),100);
            boolean res = connect.isConnected();
            toolDeploymentInfo.setState("正常");
        }catch (IOException exception){
            toolDeploymentInfo.setState("连接异常");
        }
        return toolDeploymentInfo;
    }

    @ApiOperation(value = "添加服务器")
    @PostMapping("/add")
    public Result save(@RequestBody @Valid ToolDeploymentInfo toolDeploymentInfo) {
        log.info("toolDeploymentInfoController save [{}]", toolDeploymentInfo);
        ToolDeploymentInfo tool=test_Connect(toolDeploymentInfo);
        int i =toolDeploymentInfoService.saveOrUpdate(tool);
        if (i>0){
            userLogController.addLog("添加了服务器" + tool.getTool());
            return ResultGenerator.genSuccessResult();
        }else{
            return  ResultGenerator.genFailResult("添加失败！");
        }
    }

    @ApiOperation(value = "编辑服务器")
    @PutMapping("/update")
    public Result update(@RequestBody @Valid ToolDeploymentInfo toolDeploymentInfo) {
        log.info("toolDeploymentInfoController update [{}]", toolDeploymentInfo);
        ToolDeploymentInfo tool=test_Connect(toolDeploymentInfo);
        int i =toolDeploymentInfoService.saveOrUpdate(tool);
        if (i>0){
            userLogController.addLog("更新了服务器" + tool.getTool());
            return ResultGenerator.genSuccessResult();
        }else{
            return  ResultGenerator.genFailResult("更新失败！");
        }
    }

    @ApiOperation(value = "删除服务器")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable @Valid @NotBlank(message = "id不能为空") String id) {
        log.info("toolDeploymentInfoController delete [{}]", id);
        ToolDeploymentInfo tool=toolDeploymentInfoService.getById(id);
        int i =toolDeploymentInfoService.deleteById(id);
        if (i>0){
            userLogController.addLog("删除了服务器" + tool.getTool() );
            return ResultGenerator.genSuccessResult();
        }else{
            return  ResultGenerator.genFailResult("删除失败！");
        }
    }

    @ApiOperation(value = "测试连接")
    @PostMapping("/testConnect")
    public Result connect(@RequestBody @Valid ToolDeploymentInfo toolDeploymentInfo){
        ToolDeploymentInfo tool=test_Connect(toolDeploymentInfo);
        return ResultGenerator.genSuccessResult(tool.getState());

    }

}
