package com.gbdpcloud.controller;

import com.gbdpcloud.entity.Test;
import com.gbdpcloud.service.TestService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseController;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.Result;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Api(value = "TestController")
@RequestMapping("/TestController")
@Validated
@RestController
public class TestController extends BaseController {

    @Resource
    private TestService testService;
    
    @Resource
    private UserLogController userLogController;

    @ApiOperation(value = "添加测试计划")
    @PostMapping("/add")
    public Result save(@RequestBody @Valid Test test) {
        log.info("TestController save [{}]", test);
        //TODO:执行计划状态码更新
        test.setStatus("等待中");
        //int i = testService.saveOrUpdate(test);
        int i = testService.save(test);




        if(i>0){
            userLogController.addLog("为项目"+ test.getProject() +"的"+ test.getCode_version()+"版本添加了测试计划,使用的配置方案为"+test.getConfiguration());
            int rr = 0;//testService.anaTest(test);
            testService.addTest2Q(test);
            return ResultGenerator.genSuccessResult(test.getId());
        }else{
            return ResultGenerator.genFailResult("添加失败！");
        }
    }



    @ApiOperation(value = "执行测试计划")
    @PostMapping("/runTest")
    public Result runTest(@RequestBody @Valid Test test){
        int i = 0;
        try{
            i = testService.anaTest(test);
        }catch (CannotCreateTransactionException e){
            i = testService.anaTest(test);
        }catch (Exception ee){
            ee.printStackTrace();
        }

        test.setStatus("已完成");
        testService.update(test);
        return i==0?ResultGenerator.genSuccessResult() :ResultGenerator.genFailResult("测试失败");
    }

    @ApiOperation(value = "执行测试计划Queue")
    @PostMapping("/runTestQ")
    public Result runTestq(@RequestBody @Valid Test test){
        int i = 0;
        try {

            testService.execTest1();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return ResultGenerator.genSuccessResult();
    }


    /*
    @PostMapping("/async01")
    public Callable<String> async01(){
        System.out.println("主线程开始..."+Thread.currentThread()+"==>"+System.currentTimeMillis());
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("副线程开始..."+Thread.currentThread()+"==>"+System.currentTimeMillis());
                Thread.sleep(2000);
                System.out.println("副线程开始..."+Thread.currentThread()+"==>"+System.currentTimeMillis());
                return "Callable<String> async01()";
            }
        };
        System.out.println("主线程结束..."+Thread.currentThread()+"==>"+System.currentTimeMillis());
        return callable;
    }

*/

}
