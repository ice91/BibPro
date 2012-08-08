import parser.Parser;
import java.io.*;

public class testParser{
	public static void main(String[] args){
		BufferedReader Readfile;
		Parser parser = new Parser(); 
		parser.setCitation(args[0]);
		//System.out.println(parser.getOutput());
		try{
			Readfile = new BufferedReader(new FileReader(args[0])); 
			while(Readfile.ready()){
				parser.setCitation(Readfile.readLine());
				parser.Form_translation();
				//parser.TokenForm_Processing();
				parser.CallBlast("test","CiteDB_R");
				parser.Alignment();
				parser.PostProcessing();
				parser.AssignAnswer();
				parser.OutPut();
			}	
			Readfile.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}