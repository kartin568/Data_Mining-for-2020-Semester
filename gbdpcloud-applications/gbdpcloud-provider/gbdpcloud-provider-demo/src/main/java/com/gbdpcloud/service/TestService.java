package com.gbdpcloud.service;

import com.gbdpcloud.entity.Test;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.Service;
import io.swagger.annotations.ApiOperation;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public interface TestService extends Service<Test> {

    @ApiOperation(value = "通过项目id获取测试计划")
    List<Test> getByProject(String id);


    public int anaTest(Test test);
    public void  execTest()throws InterruptedException, ExecutionException;
    public void  execTest1() throws InterruptedException, ExecutionException ;
    public void addTest2Q(Test test) ;
    //public Test get(String id);
}
