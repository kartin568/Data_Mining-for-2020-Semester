package com.gbdpcloud.Report_Analysis;

import com.gbdpcloud.entity.ResultErr;

import java.io.*;
import java.util.ArrayList;

public class Report_Analysis {
	
	public String chosen_tool = "";
	public String target_report = "";
	
	public void Ensure(String tool, String report) {
		chosen_tool = tool;
		target_report = report;
	}

	public void Analysing_by_python() {
		
		String analyse_file = "";
		
		if(this.chosen_tool == "testbed") {
			analyse_file = "testbed_code_analyse.py";
		}else if(this.chosen_tool == "klocwork"){
			analyse_file = "klocwork_code_analyse.py";
		}
		File f = new File(this.getClass().getResource("/").getPath());
		System.out.println(f);
		String pyfile = "";
		try {
			 pyfile = new File("").getCanonicalPath()+"\\gbdpcloud-provider\\gbdpcloud-provider-demo\\src\\main\\java\\com\\gbdpcloud\\Report_Analysis\\";
		} catch (IOException e) {
			e.printStackTrace();
		}
		//String[] arguments = new String[] {"python", "D:\\JavaToPython\\"+analyse_file, this.target_report};
		String[] arguments = new String[] {"python", pyfile+analyse_file, this.target_report};
		try {  
            ProcessBuilder pbuilder=new ProcessBuilder(arguments);  
            pbuilder.redirectErrorStream(true);  
            Process process=pbuilder.start();  
            BufferedReader reader=new BufferedReader(new InputStreamReader(process.getInputStream()));  
            String line=null;  
            while((line=reader.readLine())!=null){  
                System.out.println(line);  
            }     
            int result=process.waitFor();  
            System.out.println(result);  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (InterruptedException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
   }
	
	public static void main(String[] args) {
		String p = "E:\\LDRA\\LDRA_Workarea\\1598806615877_1234_1.0_tbwrkfls\\forTest_10.rpf";
		Report_Analysis ra = new Report_Analysis();
		ra.Ensure("testbed",p+".htm");
		ra.Analysing_by_python();
		ra.ReadCsv(p+".htm_detail.csv");

	}

	public ArrayList<ResultErr> ReadCsv(String filep){
		ArrayList<ResultErr> errs = new ArrayList<ResultErr>();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filep), "utf-8"));//GBK
			reader.readLine();//显示标题行,没有则注释掉
			System.out.println(reader.readLine());
			String line = null;
			while((line=reader.readLine())!=null){
				String item[] = line.split(",");//CSV格式文件时候的分割符,我使用的是,号
				String last = item[item.length-1];//CSV中的数据,如果有标题就不用-1
				System.out.println(line);
				ResultErr err = new ResultErr();
				err.setRule_type(item[0]);
				//err.setType(item[0]);// code M
				err.setErr_line(item[1]);
				//err.setLine(item[1]);// line,
				err.setRule(item[2]);//violation, char type not signed or unsigned.
				err.setCode(item[3]);// standard GJB_8114 R-1-13-14
				err.setSource(item[4]);//filename
				if(item.length>5){
					//err.setFunction(item[5]);// funname
					err.setErr_function(item[5]);
				}
				err.setMark("Y/N");
				errs.add(err);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errs;
	}
}

