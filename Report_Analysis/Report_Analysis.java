import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
		
		String[] arguments = new String[] {"python", "D:\\JavaToPython\\"+analyse_file, this.target_report};
		
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
		
		Report_Analysis ra = new Report_Analysis();
		ra.Ensure("klocwork","report.xml");
		ra.Analysing_by_python();
	}
}

