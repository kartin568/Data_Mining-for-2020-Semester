import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Report_Output {

	private String tool = "";
	private String date = "";
	private String tester = "";
	private String version = "";
	private String software = "";
	private String mts_file = "";
	private String rps_file = "";
	
	public void Ensure(String tool, String date, String tester, String version, String software, String mts, String rps) {
		this.tool = tool;
		this.date = date;
		this.tester = tester;
		this.version = version;
		this.software = software;
		this.mts_file = mts;
		this.rps_file = rps;
	}
	
public void Analysing_by_python() {
		
		String output_file = "";
		
		if(this.tool == "testbed") {
			output_file = "testbed_report_output.py";
		}else if(this.tool == "klocwork"){
			output_file = "testbed_report_output.py";
		}
		
		String[] arguments = new String[] {"python", output_file, this.mts_file, this.rps_file, this.tool, this.date, this.tester, this.version, this.software};
		
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
		ra.Ensure("testbed","2020.9.1","ATMWho","2.5.0","TestDemo","testDemo.mts.htm","testDemo.rps.htm");
		ra.Analysing_by_python();
	}
			
}
