package com.gbdpcloud.TestTool;

import com.gbdpcloud.Report_Analysis.Report_Analysis;
import com.gbdpcloud.entity.ResultErr;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class TestDirector {
    private  Queue<TestFiles> queue = new LinkedList<TestFiles>();
    private static   TestDirector testDirector = new TestDirector();

    private LDEADemo ldeaDemo = new LDEADemo();
    private boolean run = false;

    private TestDirector(){}

    public static  TestDirector get(){
        return testDirector;
    }

    public void addTest(TestFiles testFiles){
        queue.add(testFiles);
    }

    public String run(){
        String r ="";
        ArrayList<String> cmds = null;
        String packageP = "";
        while(!queue.isEmpty()){
            TestFiles curr = queue.peek();
            String type = queue.peek().getTestSoftware();
            if(type.equals("")){
                cmds = null;
            }else if(type.equals("testBed")){
                ldeaDemo.add(queue.poll());
                cmds = ldeaDemo.start();
                packageP  = ldeaDemo.getAnaPackage();
            }

            FileWriter writer = null;
            String cmdN = "cmd.bat";
            try {
                writer = new FileWriter(cmdN);
                BufferedWriter bw = new BufferedWriter(writer);
                for(String s:cmds){
                    bw.write(s+"\n");
                }
                bw.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            r = Command.exeCmd(cmdN);

            //String tp = "E:\\LDRA\\LDRA_Workarea\\1598806615877_1234_1.0_tbwrkfls\\forTest_10.rpf";
            Report_Analysis ra = new Report_Analysis();
            ra.Ensure("testbed",packageP+curr.getTestSetName()+".rps.htm");
            ra.Analysing_by_python();
            ArrayList<ResultErr> rr = ra.ReadCsv(packageP+curr.getTestSetName()+".rps.htm_detail.csv");
            //resultErrService.saveBatch(rr);
            }
        return r;
    }

    public ArrayList<ResultErr> run1(){
        String r ="";
        ArrayList<String> cmds = null;
        String packageP = "";
        TestFiles curr = queue.peek();
        String type = queue.peek().getTestSoftware();
        if(type.equals("")){
            cmds = null;
        }else if(type.equals("testBed")){
            ldeaDemo.add(queue.poll());
            cmds = ldeaDemo.start();
            packageP  = ldeaDemo.getAnaPackage();
        }

        FileWriter writer = null;
        String cmdN = "cmd.bat";
        try {
            writer = new FileWriter(cmdN);
            BufferedWriter bw = new BufferedWriter(writer);
            for(String s:cmds){
                bw.write(s+"\n");
            }
            bw.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        r = Command.exeCmd(cmdN);

        //String tp = "E:\\LDRA\\LDRA_Workarea\\1598806615877_1234_1.0_tbwrkfls\\forTest_10.rpf";
        Report_Analysis ra = new Report_Analysis();
        ra.Ensure("testbed",packageP+curr.getTestSetName()+".rps.htm");
        ra.Analysing_by_python();
        ArrayList<ResultErr> rr = ra.ReadCsv(packageP+curr.getTestSetName()+".rps.htm_detail.csv");
        //resultErrService.saveBatch(rr);
        return rr;
    }

    public static void main(String[] args) {
        TestDirector testDirector = TestDirector.get();
        TestFiles files = new TestFiles();
        /*
                demo.setSetName("demo-auto");
        ArrayList<String> files = new ArrayList<>();
        files.add("E:\\LDRA\\demo\\test\\main.c");

        Code code = new Code();
        code.setPath("E:\\LDRA\\demo\\test\\main.c");
        ArrayList<Code> codes = new ArrayList<>();
        codes.add(code);
        files.setCode(codes);
        files.setTestSetName("auto-test");
        testDirector.addTest(files);
        testDirector.run();*/
    }
}
