import java.io.*;
import java.util.*;
import tpgen.TpGen;

public class CiteSeerTpGen{
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
			String Book;
			String Tech;
			String Vol;
			String Issue;
			String M_Y;
			String Page;
			String Web;
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
				temp = Readfile.readLine(); //<Booktitle>
				Book = temp.substring(temp.indexOf("<Booktitle>")+"<Booktitle>".length(), temp.indexOf("</Booktitle>"));
				if (Book.length() == 0)
					Book = "X";
				temp = Readfile.readLine(); //<Techtitle>
				Tech = temp.substring(temp.indexOf("<Techtitle>")+"<Techtitle>".length(), temp.indexOf("</Techtitle>"));
				if (Tech.length() == 0)
					Tech = "X";
				temp = Readfile.readLine(); //<Vol>
				Vol = temp.substring(temp.indexOf("<Volume>")+"<Volume>".length(), temp.indexOf("</Volume>"));
				if (Vol.length() == 0)
					Vol = "X";
				temp = Readfile.readLine(); //<Issue>
				Issue = temp.substring(temp.indexOf("<Issue>")+"<Issue>".length(), temp.indexOf("</Issue>"));
				if (Issue.length() == 0)
					Issue = "X";
				temp = Readfile.readLine(); //<Date>
				M_Y = temp.substring(temp.indexOf("<Date>")+"<Date>".length(), temp.indexOf("</Date>"));
				if (M_Y.length() == 0)
					M_Y = "X";
				temp = Readfile.readLine(); //<Page>
				Page = temp.substring(temp.indexOf("<Page>")+"<Page>".length(), temp.indexOf("</Page>"));
				if (Page.length() == 0)
					Page = "X";
				temp = Readfile.readLine(); //<URL>
				Web = temp.substring(temp.indexOf("<URL>")+"<URL>".length(), temp.indexOf("</URL>"));
				if (Web.length() == 0)
					Web = "X";
			
				Readfile.readLine(); //</Record>
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
				tpGen.setParser(Ref, Author, Title, Journal, Book, Tech,  Vol, Page, Issue, M_Y, Web);
				if (tpGen.Filter){
					TemplateDB.add(">" + tpGen.StyleForm);
					TemplateDB.add(tpGen.IndexForm);
				}			
				//System.out.println("STYLE: " + tpGen.StyleForm);
				//System.out.println("INDEX: " + tpGen.IndexForm);	
				tpGen.parser.Alignment(tpGen.StyleForm);
				//System.err.println(tpGen.StyleForm);
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