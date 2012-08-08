// for INFOMAP DataSet
import java.io.*;
import java.util.*;
import tpgen.TpGen;

public class INFOTpGen{
	public INFOTpGen(){
	}
	
	public static void main(String[] args){
		Vector<String> TemplateDB = new Vector<String>(); //¦s¤Jtemplate database
		//Vector<Ans> Ans_list = new Vector<Ans>();
		BufferedReader Readfile;
		PrintWriter Writefile;		
		TpGen tpGen; 	
		try{
			Readfile = new BufferedReader(new FileReader("..\\DataSet\\" + args[0]));
			String SN;
			String Ref;
			String Author;
			String Title;
			String Journal;
			String Vol;
			String Issue;
			String Year;
			String Page;
			String temp;
			while(Readfile.ready()){	
				Readfile.readLine(); //<Record>
				temp = Readfile.readLine(); //<SN>
				SN = temp.substring(temp.indexOf("<SN>")+"<SN>".length(), temp.indexOf("</SN>"));
				Readfile.readLine(); //<BibID>
				temp = Readfile.readLine(); //<Reference>
				//System.out.println(temp + temp.indexOf("</Reference>"));
				
				Ref = temp.substring(temp.indexOf("<Reference>")+"<Reference>".length(), temp.indexOf("</Reference>"));
				temp = Readfile.readLine(); //<Author>
				//System.out.println(temp);
				Author = temp.substring(temp.indexOf("<Author>")+"<Author>".length(), temp.indexOf("</Author>"));
				if (Author.length() == 0)
					Author = "X";
				temp = Readfile.readLine(); //<Title>
				Title = temp.substring(temp.indexOf("<Title>")+"<Title>".length(), temp.indexOf("</Title>"));
				if (Title.length() == 0)
					Title = "X";
				temp = Readfile.readLine(); //<Journal>
				Journal = temp.substring(temp.indexOf("<Journal>")+"<Journal>".length(), temp.indexOf("</Journal>"));
				if (Journal.length() == 0)
					Journal = "X";
				temp = Readfile.readLine(); //<Vol>
				Vol = temp.substring(temp.indexOf("<Vol>")+"<Vol>".length(), temp.indexOf("</Vol>"));
				if (Vol.length() == 0)
					Vol = "X";
				temp = Readfile.readLine(); //<Issue>
				Issue = temp.substring(temp.indexOf("<Issue>")+"<Issue>".length(), temp.indexOf("</Issue>"));
				if (Issue.length() == 0)
					Issue = "X";
				temp = Readfile.readLine(); //<Year>
				Year = temp.substring(temp.indexOf("<Year>")+"<Year>".length(), temp.indexOf("</Year>"));
				if (Year.length() == 0)
					Year = "X";
				temp = Readfile.readLine(); //<Page>
				//System.out.println(temp);
				Page = temp.substring(temp.indexOf("<Page>")+"<Page>".length(), temp.indexOf("</Page>"));
				if (Page.length() == 0)
					Page = "X";
				Readfile.readLine(); //<Type>		
				Readfile.readLine(); //<Style>
				Readfile.readLine(); //</Record>
			//	System.out.println(SN);
			//	System.out.println(Ref);
				//System.out.println(Author);
				//System.out.println(Title);
				//System.err.println(Journal);
				//System.out.println(Vol);
				//System.out.println(Issue);
				//System.out.println(Year);
				//System.out.println(Page);

				tpGen = new TpGen();
				//tpGen.setParser(Ref, Author, Title, Journal, Vol, Page, Issue, "X", Year);
				tpGen.setParser(Ref, Author, Title, Journal, "X", "X",  Vol, Page, Issue, Year, "X");
				if (tpGen.Filter){
					TemplateDB.add(">" + tpGen.StyleForm);
					TemplateDB.add(tpGen.IndexForm);
					//System.out.println("Filter PASS!!");
				}			
				//System.out.println("STYLE: " + tpGen.StyleForm);
				//System.out.println("INDEX: " + tpGen.IndexForm);	
				tpGen.parser.Alignment(tpGen.StyleForm);
				tpGen.parser.PostProcessing();
				tpGen.parser.AssignAnswer();
				tpGen.parser.TOKEN_ANS();
				//tpGen.parser.OutPut();
				//System.out.println();
			}	
			Readfile.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			Writefile = new PrintWriter(new FileWriter(args[1]));
			for(int i=0; i < TemplateDB.size(); i++){
				Writefile.println(TemplateDB.elementAt(i));
			}
			Writefile.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}