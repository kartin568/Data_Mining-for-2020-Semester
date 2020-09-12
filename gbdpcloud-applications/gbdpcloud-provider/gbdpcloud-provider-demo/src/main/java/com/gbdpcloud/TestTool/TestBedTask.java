package com.gbdpcloud.TestTool;

import com.gbdpcloud.Handler.SpringUtil;
import com.gbdpcloud.Report_Analysis.Report_Analysis;
import com.gbdpcloud.Report_Analysis.Report_Output;
import com.gbdpcloud.entity.ResultErr;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Callable;

import com.gbdpcloud.entity.Test;
import com.gbdpcloud.mapper.TestMapper;
import com.gbdpcloud.mapper.UacUserMapper;
import com.gbdpcloud.service.TestService;
import com.gbdpcloud.service.UacUserService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.dto.UacUserDto;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacUser;
import org.springframework.beans.factory.annotation.Autowired;


import javax.annotation.Resource;

public class TestBedTask implements Callable {

    private TestDetail testDetail;

    @Resource
    private TestMapper testMapper;

    @Resource
    @Autowired
    public TestService testService;

    @Resource
    private UacUserService uacUserService;

    @Resource
    private UacUserMapper uacuserMapper;

    public TestBedTask (TestDetail testDetail){
        this.testDetail = testDetail;
    }

    @Override
    public ArrayList<ResultErr> call() throws Exception {
        return exec();
    }

    public synchronized ArrayList<ResultErr> exec() throws InterruptedException {
        TestBed testBed = new TestBed();
        testBed.add(testDetail);
        ArrayList<String> cmds = testBed.getCMDs();
        String cmdN = "cmd.bat";
        writeBat(cmdN,cmds);

        int flag = 0;
        do{
            try{
                exeCmd(cmdN);
                flag = 2;
            }catch (IOException e){
                System.err.println("---------------------------------------------------------");
                //System.err.println("\n\nerr\n"+curr.getTestSetName());
                System.err.println("---------------------------------------------------------");
                writeBat(cmdN,cmds);
                //testbed.put(curr);
            }
        }while(flag==0);
       // Thread.sleep(10);
        wordFileOutput();
       return getResultErrs(testBed);
    }

    public ArrayList<ResultErr> getResultErrs(TestBed testBed){
        Report_Analysis ra = new Report_Analysis();
        String pre = testBed.getResultPath()+testDetail.getTestSetName();
        ra.Ensure("testbed",pre+".rps.htm");
        ra.Analysing_by_python();
        ArrayList<ResultErr> rr = ra.ReadCsv(pre+".rps.htm_detail.csv");

        for(ResultErr err:rr){
            err.setTest_ID(testDetail.getTest().getId());
        }
        return rr;
    }

    public String[] getWordInfo(String id){
        Test t =testDetail.getTest();
        UacUserDto u = testDetail.getUacUser();
        uacUserService = (UacUserService)SpringUtil.getBean(UacUserService.class);

        String project = t.getProject();//项目名
        Date date = t.getCreateDate();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);//测试日期

        String create_by = t.getCreateBy();//测试人ID

        UacUser uu = uacUserService.getInfoById(create_by);
        String cr = u.getLoginName();
        String fin_cr = cr.split(" ")[0];
        String version = t.getCode_version();//测试版本
        String creator = uu.getLoginName();
        String[] strs = {project, dateString, fin_cr, version};
        return strs;
    }

    public void wordFileOutput(){
        String id = testDetail.getTestSetName();;
        String[] info = getWordInfo(id);
        System.out.println(info);
        System.out.println(id);
        Report_Output ro = new Report_Output();
        ro.Ensure("testbed", info[1], info[2], info[3], info[0], id);
        ro.Analysing_by_python();
    }

    public void writeBat(String cmdN,ArrayList<String> cmds){
        File f = new File(cmdN);
        if(f.exists()){
            f.delete();
        }
        FileWriter writer = null;
        try {
            writer = new FileWriter(cmdN,false);
            BufferedWriter bw = new BufferedWriter(writer);
            for(String s:cmds){
                bw.write(s+"\n");
            }
            bw.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(!f.exists()){

        }
    }

    public synchronized static String exeCmd(String commandStr) throws IOException {
        System.out.print("todo:"+commandStr);
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        Process p =  Runtime.getRuntime().exec(""+commandStr);
        try {
            br = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.forName("GBK") ) );
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            System.out.println(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally
        {
            if (br != null)
            {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

}
