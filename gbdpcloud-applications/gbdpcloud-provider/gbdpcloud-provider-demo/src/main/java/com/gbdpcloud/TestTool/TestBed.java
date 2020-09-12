package com.gbdpcloud.TestTool;

import com.gbdpcloud.entity.Code;


import java.util.ArrayList;

public class TestBed  {
    //private final static String path ="";
    private final static String prefix = "start /wait C:\\LDRA_Toolsuite\\contestbed ";
    private final static String createSet = " /create_set=system ";
    private final  static String cmodel = " /cquality_model=";
    private final  static String cppmodel = " /cppquality_model=";
    private final  static String doAna = " /112a35q  /generate_code_review=HTML /generate_quality_review=HTML /noauto_macro";//   /112a35q
    private final  static String doCreateSet = " /1q ";
    private final  static  String addfile = " /add_set_file=";
    private final static String resultPath ="C:\\LDRA_Workarea\\";

    private String cRule ;
    private String cppRule ;

    //private  String projectPath ; // auto bulid set
    private ArrayList<String> files;
    private String setName; // 手动创建的

    //private boolean isSingleFile = false;
    //private boolean isAutoBuildSet = true;

    private ArrayList<String> cmds= new ArrayList<String>();
    //start /wait contestbed C:\set.vcproj /create_set=system /cquality_model=MISRA-C:2012 /cppquality_model=GJB_8114 /11q
    // start /wait contestbed 集合名 /create_set=system /1q
    public void add(TestDetail testFiles){
        this.setName = testFiles.getTestSetName();
        ArrayList<String> file = new ArrayList<>();
        for(Code c:testFiles.getCodes()){
            file.add(c.getPath());
        }
        files = file;
    }

    public ArrayList<String> getCMDs(){
        if(check()){
            // create set
            String cmd1 = prefix + setName +createSet +doCreateSet;
            cmds.add(cmd1);

            // add files
            for(int i = 0;i<files.size();i++){
                String t = prefix + setName +addfile+addE(files.get(i))+doCreateSet;
                cmds.add(t);
            }
            // run
            String cmd2 = prefix+setName+doAna+ cmodel+cRule+" "+cppmodel+cppRule;
            cmds.add(cmd2);
        }
        return cmds;
    }

    private boolean check() {
        boolean r = true;
        if(null ==cRule){
            //r = false;
            cRule = "MISRA-C:2012";
        }
        if(null == cppRule){
            // r = false;
            cppRule="GJB_8114";
        }
        return true;
    }

    private String addE(String s){
        return "\""+s+"\"";
    }


    public String getResultPath(){
        return resultPath+setName+"_tbwrkfls\\";
    }

    public static void main(String[] args) {

    }


}
