package com.gbdpcloud.Report_Analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Report_Output {

	private String tool = "";
	private String date = "";
	private String tester = "";
	private String version = "";
	private String software = "";
	private String planid = "";

	public void Ensure(String tool, String date, String tester, String version, String software,String id) {
		this.tool = tool;
		this.date = date;
		this.tester = tester;
		this.version = version;
		this.software = software;
		this.planid = id;
	}

	public void Analysing_by_python() {

		String output_file = "";

		if(this.tool == "testbed") {
			output_file = "testbed_report_output.py";
		}else if(this.tool == "klocwork"){
			output_file = "testbed_report_output.py";
		}

		File f = new File(this.getClass().getResource("/").getPath());
		System.out.println(f);
		String pyfile = "";
		try {
			pyfile = new File("").getCanonicalPath()+"\\gbdpcloud-provider\\gbdpcloud-provider-demo\\src\\main\\java\\com\\gbdpcloud\\Report_Analysis\\";
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] arguments = new String[] {"python", pyfile + output_file, this.planid, this.tool, this.date, this.tester, this.version, this.software};

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

		Report_Output ra = new Report_Output();
		System.out.println(System.getProperty("user.dir"));
		ra.Ensure("testbed","2020.9.1",
				"ATMWho","2.5.0","TestDemo","cf6e7646-1bb1-4551-bcba-501e363dcfdd" );
		ra.testAnalyse();
	}

	public void testAnalyse(){
		String output_file = "";

		if(this.tool == "testbed") {
			output_file = "testbed_report_output.py";
		}else if(this.tool == "klocwork"){
			output_file = "testbed_report_output.py";
		}

		File f = new File(this.getClass().getResource("/").getPath());
		System.out.println(f);
		String pyfile = "";
		try {
			pyfile = new File("").getCanonicalPath()+"\\gbdpcloud-provider\\gbdpcloud-provider-demo\\src\\main\\java\\com\\gbdpcloud\\Report_Analysis\\";
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] arguments = new String[] {"python", pyfile+output_file,this.planid, this.tool, this.date, this.tester, this.version, this.software};
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
}
