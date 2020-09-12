package com.gbdpcloud.service.impl;

import com.gbdpcloud.TestTool.TestBedTask;
import com.gbdpcloud.TestTool.TestDetail;
import com.gbdpcloud.TestTool.TestDirector;
import com.gbdpcloud.TestTool.TestFiles;
import com.gbdpcloud.entity.*;
import com.gbdpcloud.mapper.CodeMapper;
import com.gbdpcloud.mapper.ResultErrMapper;
import com.gbdpcloud.mapper.TestMapper;
import com.gbdpcloud.service.TestService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.constant.GlobalConstant;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.security.UacUserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;

/**
 * Test service层
 *
 * @date 2020-07-29 18:16:17
 */
@Service
@Transactional(readOnly = true)
public class TestServiceImpl extends BaseService<Test> implements TestService {

    @Resource
    private TestMapper testMapper;
    @Resource
    private  ConfigurationServiceImpl configurationService;
    @Resource
    private CodeMapper codeMapper;
    @Resource
    private ResultErrServiceImpl resultErrService;
    @Resource
    private ResultErrMapper resultErrMapper;

    private BlockingQueue<TestDetail> testBed = new ArrayBlockingQueue<TestDetail>(1000);
    ExecutorService testBedSingleThreadExecutor = Executors.newSingleThreadExecutor();



    @Override
    public List<Test> getByProject(String id) {
        List<Test> list = testMapper.getByProject(id);
        return list;
    }


    public int anaTest(Test test){
        Configuration configuration = configurationService.getById(test.getConfiguration_ID());
        ArrayList<Code> codes = (ArrayList<Code>) codeMapper.getByProjectAndVersion(test.getProject_ID(),test.getCode_version());
        int r = 0;



        try {
            TestFiles files = new TestFiles();
            files.setCodes(codes);
            //files.setTestSetName("auto-test");
            String name = ""+test.getId();//""+System.currentTimeMillis()+"_"+test.getProject_ID()+"_"+test.getCode_version();
            files.setTestSetName(name);
            files.setTestSoftware(configuration.getTools());
            String[] rules = configuration.getRule().split("\\+");
            files.setCppRule(rules[0].toString());
            files.setCppRule(rules[1].toString());
            //
            TestDirector testDirector = TestDirector.get();
            testDirector.addTest(files);
            ArrayList<ResultErr> errs = testDirector.run1();

            for(ResultErr e:errs){
                e.setTest_ID(test.getId());
                try{
                    resultErrService.save(e);
                }catch (Exception ee){
                    ee.printStackTrace();
                }

            }
            //resultErrService.saveBatch(errs);
        }catch (Exception e){
            e.printStackTrace();
            r = 1;
        }
        return r;
    }

    public void getResult(){

    }

    public TestDetail getTestDetail(Test test){
        TestDetail r = new TestDetail();
        Configuration configuration = configurationService.getById(test.getConfiguration_ID());
        ArrayList<Code> codes = (ArrayList<Code>) codeMapper.getByProjectAndVersion(test.getProject_ID(),test.getCode_version());
        r.setCodes(codes);
        r.setConfiguration(configuration);
        r.setTest(test);
        r.setTestSetName(test.getId());
        //r.setUacUserDto(UacUserUtils.getUserInfoFromRequest());
        r.setUacUser(UacUserUtils.getUserInfoFromRequest());
        return r;
    }

    public void addTest2Q(Test test)  {
        System.out.print("add test");
        TestDetail t = getTestDetail(test);
        if(t.getConfiguration().getTools().toLowerCase().contains("testbed")){
            try {
                testBed.put(t);
                pList();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void pList(){
        for(TestDetail t:testBed){
            System.err.println(t.getTest().toString());
        }
    }

    public void  execTest() throws InterruptedException, ExecutionException {
        System.out.println("exec test");
        ArrayList<ResultErr> r = new ArrayList<ResultErr>();
        ArrayList<Test> testSet = new ArrayList<>();
        if (testBed.size()>0){
            TestDetail testDetail = testBed.take();
            Future future = testBedSingleThreadExecutor.submit(new TestBedTask(testDetail));
            while(!future.isDone()){

            }
            r.addAll( (ArrayList<ResultErr>)future.get());
            testDetail.getTest().setStatus("已完成");
            for(ResultErr err:r){
                resultErrService.save(err);
            }
            testSet.add(testDetail.getTest());
            System.out.println("queue size ="+testBed.size());
        }
       /*for(ResultErr err:r){
            resultErrService.save(err);
        }*/
        for(Test t:testSet){
            update(t);
        }
    }

    public void  execTest1() throws InterruptedException, ExecutionException {
        System.out.println("exec1 test");
        ArrayList<ResultErr> r = new ArrayList<ResultErr>();
        ArrayList<Test> testSet = new ArrayList<>();
        if (testBed.size()>0){
            TestDetail testDetail = testBed.take();
            Test t = testDetail.getTest();
            t.setStatus("分析中");
            update(t);
            Future future = testBedSingleThreadExecutor.submit(new TestBedTask(testDetail));
            while(!future.isDone()){

            }
            r.addAll( (ArrayList<ResultErr>)future.get());

            for(ResultErr err:r){
                resultErrService.save(err);
            }
            t.setStatus("已完成");
            update(t);
            testSet.add(testDetail.getTest());
            System.out.println("queue size ="+testBed.size());
        }
       /*for(ResultErr err:r){
            resultErrService.save(err);
        }*/
       /* for(Test t:testSet){
            update(t);
        }*/
    }
  /*  public int runTesst(ArrayList<Code> codes, Configuration configuration){
        int r = 0;
        try {
            TestDirector testDirector = TestDirector.get();
            TestFiles files = new TestFiles();
            //files.setCode(codes);
            files.setCodes(codes);
            files.setTestSoftware(configuration.getTools());
            //files.setTestSetName("auto-test");
            //files.setTestSetName();
            //files.setConfiguration(configuration);
            //files.setToolDeploymentInfo(toolDeploymentInfoService);
            testDirector.addTest(files);
            testDirector.run();
        }catch (Exception e){
            e.printStackTrace();
            r = 1;
        }
        return r;
    }*/
}