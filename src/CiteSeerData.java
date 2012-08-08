import java.io.*;
import java.util.*;
import tpgen.TpGen;

public class CiteSeerData{
	public static void main(String[] args){
		Vector<String> BWORD = new Vector<String>(Arrays.asList("Proceedings Proc Workshop Conf Conference Symposium Sympos Symp International Intern Annual Annu Proceedings Proc. Workshop. Conf. Symposium. Sympos. Symp. Intern. Annual. Annu.".split("\\s")));
		Vector<String> JWORD = new Vector<String>(Arrays.asList("Transactions Trans Journal J Transactions. Trans. Journal J.".split("\\s")));
		Vector<String> THESIS = new Vector<String>(Arrays.asList("Master Masters Ph PhD Thesis thesis Dissertation dissertation Master. Masters. Ph. PhD. Thesis. thesis. Dissertation. dissertation.".split("\\s")));
		Vector<String> TECH = new Vector<String>(Arrays.asList("Tech rep Rpt TR Technical Report Tech. rep. Rpt. TR. Technical. Report.".split("\\s")));
		Vector<String> TemplateDB = new Vector<String>(); //¦s¤Jtemplate database
		//Vector<Ans> Ans_list = new Vector<Ans>();
		BufferedReader Readfile;
		PrintWriter Writefile;		
		TpGen tpGen;
		int ID = 1;
		try{
			Readfile = new BufferedReader(new FileReader("..\\DataSet\\CiteSeer.txt"));
			String SN;
			String Ref;
			String Author;
			String Title;
			String Journal;
			String Vol;
			String Issue;
			String Month;
			String Year;
			String Page;
			String temp;
			while(Readfile.ready()){	
				Readfile.readLine(); //<Record>
				temp = Readfile.readLine(); //<SN>
				SN = temp.substring(temp.indexOf("<SN>")+"<SN>".length(), temp.indexOf("</SN>"));
				temp = Readfile.readLine(); //<Reference>
				Ref = temp.substring(temp.indexOf("<Reference>")+"<Reference>".length(), temp.indexOf("</Reference>"));
				temp = Readfile.readLine(); //<Author>
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
				temp = Readfile.readLine(); //<Month>
				Month = temp.substring(temp.indexOf("<Month>")+"<Month>".length(), temp.indexOf("</Month>"));
				if (Month.length() == 0)
					Month = "X";
				temp = Readfile.readLine(); //<Year>
				Year = temp.substring(temp.indexOf("<Year>")+"<Year>".length(), temp.indexOf("</Year>"));
				if (Year.length() == 0)
					Year = "X";
				temp = Readfile.readLine(); //<Page>
				Page = temp.substring(temp.indexOf("<Page>")+"<Page>".length(), temp.indexOf("</Page>"));
				if (Page.length() == 0)
					Page = "X";
				Readfile.readLine(); //<Type>		
				Readfile.readLine(); //<Style>
				Readfile.readLine(); //</Record>
			/*	System.out.println(SN);
				System.out.println(Ref);
				System.out.println(Author);
				System.out.println(Title);
				System.out.println(Journal);
				System.out.println(Vol);
				System.out.println(Issue);
				System.out.println(Year);
				System.out.println(Page);*/
			
				//Readfile.readLine(); //</Record>
				/*System.err.println(SN);
				System.err.println(Ref);
				System.err.println(Author);
				System.err.println(Title);
				System.err.println(Journal);
				System.err.println(Book);
				System.err.println(Tech);
				System.err.println(Vol);
				System.err.println(Issue);
				System.err.println(M_Y);
				System.err.println(Page);
				System.err.println(Web);*/

				tpGen = new TpGen();
				tpGen.setParser(Ref, Author, Title, Journal, "X", "X",  Vol, Page, Issue, Year, "X");
				if (tpGen.Filter){
					TemplateDB.add(">" + tpGen.StyleForm);
					TemplateDB.add(tpGen.IndexForm);
				}			
				//System.out.println("STYLE: " + tpGen.StyleForm);
				//System.out.println("INDEX: " + tpGen.IndexForm);	
				tpGen.parser.Alignment(tpGen.StyleForm);
				tpGen.parser.PostProcessing();
				tpGen.parser.AssignAnswer();
				//tpGen.parser.OutPut();
				//System.out.println();
				
				if (Author.compareTo("X") == 0){
					Author = "";
				}
				if (Title.compareTo("X") == 0){
					Title = "";
				}
				if (Journal.compareTo("X") == 0){
					Journal = "";
				}
				if (Vol.compareTo("X") == 0){
					Vol = "";
				} 
				if (Page.compareTo("X") == 0){
					Page = "";
				} 
				if (Issue.compareTo("X") == 0){
					Issue = "";
				} 
				if (Year.compareTo("X") == 0){
					Year = "";
				}
				
				System.out.println("<Record>");
				System.out.println("  <SN>" + ID + "</SN>");
				System.out.println("  <Reference>" + Ref + "</Reference>");
				System.out.println("  <Author>" + Author + "</Author>");
				System.out.println("  <Title>" + Title + "</Title>");
				if (Journal.compareTo("X") == 0){
					System.out.println("  <Journal>" + tpGen.parser.JournalAnswer + "</Journal>");
					System.out.println("  <Booktitle>" + tpGen.parser.BookAnswer + "</Booktitle>");
					System.out.println("  <Techtitle>" + tpGen.parser.TechAnswer + "</Techtitle>");
				} else {
					Vector<String> J_Array = new Vector<String>(Arrays.asList(Journal.split("\\s")));
					boolean journal = false;
					boolean book = false;
					boolean tech = false;
					for(int i=0; i < J_Array.size(); i++){
						if (JWORD.contains(J_Array.elementAt(i))){
							journal = true;
						}
						if (BWORD.contains(J_Array.elementAt(i))){
							book = true;
						}
						if (THESIS.contains(J_Array.elementAt(i))||TECH.contains(J_Array.elementAt(i))){
							tech = true;
						}
					}
					if (journal){
						System.out.println("  <Journal>" + Journal + "</Journal>");
						System.out.println("  <Booktitle></Booktitle>");
						System.out.println("  <Techtitle></Techtitle>");
					}else if (book){
						System.out.println("  <Journal></Journal>");
						System.out.println("  <Booktitle>" + Journal + "</Booktitle>");
						System.out.println("  <Techtitle></Techtitle>");
					}else if (tech){
						System.out.println("  <Journal></Journal>");
						System.out.println("  <Booktitle></Booktitle>");
						System.out.println("  <Techtitle>" + Journal + "</Techtitle>");
					}else {
						System.out.println("  <Journal>" + Journal + "</Journal>");
						System.out.println("  <Booktitle></Booktitle>");
						System.out.println("  <Techtitle></Techtitle>");
					}
				}
				System.out.println("  <Volume>" + Vol + "</Volume>");
				System.out.println("  <Issue>" + Issue + "</Issue>");
				System.out.println("  <Date>" + tpGen.parser.DateAnswer + "</Date>");
				System.out.println("  <Page>" + Page + "</Page>");
				System.out.println("  <URL>" + tpGen.parser.WEB + "</URL>");
				System.out.println("</Record>");
				ID = ID + 1;
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