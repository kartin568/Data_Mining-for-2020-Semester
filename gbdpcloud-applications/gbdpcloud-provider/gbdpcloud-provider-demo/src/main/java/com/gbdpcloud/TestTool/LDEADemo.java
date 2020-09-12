package com.gbdpcloud.TestTool;

import com.gbdpcloud.entity.Code;

import java.util.ArrayList;

public class LDEADemo {
    private final static String path ="C:\\LDRA_Toolsuite";
    private final static String prefix = "start /wait "+ path+"\\LDRA_Toolsuite\\contestbed ";
    private final static String createSet = " /create_set=system ";
    private final  static String cmodel = " /cquality_model=";
    private final  static String cppmodel = " /cppquality_model=";
    private final  static String doAna = " /11q  /generate_code_review=HTML /generate_quality_review=HTML /noauto_macro";
    private final  static String doCreateSet = " /1q ";
    private final  static  String addfile = " /add_set_file=";

    private String cRule ;
    private String cppRule ;

    //private  String projectPath ; // auto bulid set
    private ArrayList<String> files;
    private String setName; // 手动创建的

    private boolean isSingleFile = false;
    private boolean isAutoBuildSet = true;

    private ArrayList<String> cmds= new ArrayList<String>();
    //start /wait contestbed C:\set.vcproj /create_set=system /cquality_model=MISRA-C:2012 /cppquality_model=GJB_8114 /11q
    // start /wait contestbed 集合名 /create_set=system /1q


    public String getAnaPackage(){
        return "C:\\LDRA_Workarea\\"+setName+"_tbwrkfls\\";
    }
    public void add(TestFiles testFiles){
        setName = testFiles.getTestSetName();
        ArrayList<String> file = new ArrayList<>();
        for(Code c:testFiles.getCodes()){
            file.add(c.getPath());
        }
        files = file;
    }

    private void init(){
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
    }

    private boolean check() {
        boolean r = true;
        if( !onlyNmae(setName)){
           // r = false;
        }
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

    private boolean onlyNmae(String setName) {
        return true;
    }

    private String addE(String s){
        return "\""+s+"\"";
    }

    public ArrayList<String> start(){
        init();
        return cmds;
    }


    public static void main(String[] args) {
        LDEADemo.TestFile();
    }

    public static void TestFiles(){
        LDEADemo demo = new LDEADemo();
        demo.setSetName("demo-auto");
        ArrayList<String> files = new ArrayList<>();
        files.add("E:\\LDRA\\demo\\test\\main.c");
        files.add("E:\\LDRA\\demo\\test\\library.c");
        files.add("E:\\LDRA\\demo\\test\\liberary.c");
        files.add("E:\\LDRA\\demo\\test\\history.c");
        files.add("E:\\LDRA\\demo\\test\\head1.h");
        demo.setFiles(files);
        demo.init();
        for(String s:demo.getCmds()){
            System.out.println(s);
        }
    }

    public static  void TestFile(){
        LDEADemo demo = new LDEADemo();
        demo.setSetName("demo-auto");
        ArrayList<String> files = new ArrayList<>();
        files.add("E:\\LDRA\\demo\\test\\main.c");
        demo.setFiles(files);
        demo.init();
        for(String s:demo.getCmds()){
            System.out.println(s);
        }
    }

    public ArrayList<String> getCmds() {
        return cmds;
    }

    public void setcRule(String cRule) {
        this.cRule = cRule;
    }

    public void setCppRule(String cppRule) {
        this.cppRule = cppRule;
    }


    public void setFiles(ArrayList<String> files) {
        this.files = files;
    }

    public void setSetName(String setName) {
        this.setName = setName;
        this.isAutoBuildSet = false;
    }

    public void setSingleFile(boolean singleFile) {
        isSingleFile = singleFile;
    }

    public void setAutoBuildSet(boolean autoBuildSet) {
        isAutoBuildSet = autoBuildSet;
    }

    public void setCmds(ArrayList<String> cmds) {
        this.cmds = cmds;
    }
}
