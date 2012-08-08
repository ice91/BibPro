//import parser.Parser;
import tpgen.TpGen;
import java.io.*;
import java.util.*;

public class testTpGen{
	public static void main(String[] args){
		Vector<String> TemplateDB = new Vector<String>(); //store template database
		TpGen tpGen;
		BufferedReader Readfile;
		PrintWriter Writefile;
		//System.out.println(parser.getOutput());
		int idx=0;
		int count=0;
		String temp;
		String Ref = "";
		String Author = "";
		String Title = "";
		String Journal = "";
		String Vol = "";
		String Issue = "";
		String Page = "";
		String Month = "";
		String Year = "";
		try{
			Readfile = new BufferedReader(new FileReader(args[0])); 
			while(Readfile.ready()){
				temp = Readfile.readLine();
				if(temp.indexOf("[Record]") != -1){
					temp = Readfile.readLine();
					count = 0;
					while(temp.indexOf("[/Record]") == -1 && count < 20){
						// Reference
						idx = temp.indexOf("[Reference]:");
						if (idx != -1){
							Ref = temp.substring(idx + "[Reference]:".length());
						}
						// Author
						idx = temp.indexOf("[Author]:");
						if (idx != -1){
							Author = temp.substring(idx + "[Author]:".length());
						}
						// Title
						idx = temp.indexOf("[Title]:");
						if (idx != -1){
							Title = temp.substring(idx + "[Title]:".length());
						}
						// Journal
						idx = temp.indexOf("[Journal]:");
						if (idx != -1){
							Journal = temp.substring(idx + "[Journal]:".length());
						}
						// Volume
						idx = temp.indexOf("[Volume]:");
						if (idx != -1){
							Vol = temp.substring(idx + "[Volume]:".length());
						}
						// Page
						idx = temp.indexOf("[Page]:");
						if (idx != -1){
							Page = temp.substring(idx + "[Page]:".length());
						}
						// Issue
						idx = temp.indexOf("[Issue]:");
						if (idx != -1){
							Issue = temp.substring(idx + "[Issue]:".length());
						}
						// Month
						idx = temp.indexOf("[Month]:");
						if (idx != -1){
							Month = temp.substring(idx + "[Month]:".length());
						}
						// Year
						idx = temp.indexOf("[Year]:");
						if (idx != -1){
							Year = temp.substring(idx + "[Year]:".length()).trim();
						}
						temp = Readfile.readLine();
						count = count + 1;
					}
					tpGen = new TpGen();
					tpGen.setParser(Ref, Author, Title, Journal, Vol, Page, Issue, Month, Year);
					if (tpGen.Filter){
						TemplateDB.add(">" + tpGen.StyleForm);
						TemplateDB.add(tpGen.IndexForm);
					}			
					
				}
			}	
			Readfile.close();
		}catch(Exception e){
			System.out.println("Input Format is illegal!!!");
			e.printStackTrace();
		}
		
		if (TemplateDB.size() > 0){
			try{
				Writefile = new PrintWriter(new FileWriter("C://BibPro//BibPro//TemplateDB//" + args[1] , false));
				for(int i=0; i < TemplateDB.size(); i++){
					System.out.println(TemplateDB.elementAt(i));
					Writefile.println(TemplateDB.elementAt(i));
				}
				Writefile.close();
			}catch(Exception e ){
				e.printStackTrace();
			}
		}
		
			// °õ¦æblast
		String[] cmds = {"formatdb", "-i", "c:\\BibPro\\BibPro\\TemplateDB\\" + args[1]};
		try{			
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(cmds);
			proc.waitFor();
			proc.destroy();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}