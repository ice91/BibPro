package tpgen;

import java.io.*;
import java.util.*;
import parser.Parser;

public class TpGen{
	public TpGen(){
	}
	public Parser parser;
	public Parser parser2;
	public String IndexForm = "";
	public String StyleForm = "";
	public boolean Filter = true;
	
	public void setParser(String input, String Author, String Title, String Journal, String Booktitle, String Techtitle, String Volume, String Page, String Num, String M_Y, String Web){
		parser = new Parser(); 
		parser2 = new Parser(); 
		try{
			parser.setCitation(input);	
			parser.Form_translation();			
			parser.TForm_translation(Author, Title, Journal, Booktitle, Techtitle, Volume, Page, Num, M_Y, Web);
			parser.TPreProcessing();
			StyleForm = parser.StyleForm;
			//parser2.setCitation(input);
			//parser2.Form_translation();
			//parser2.TokenForm_Processing();
			IndexForm = parser.IndexForm;
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		int count_A = 0;
		int count_T = 0;
		int count_L = 0;
		for(int i = 0; i < StyleForm.length(); i++){
			if (StyleForm.charAt(i) == 'A'){
				count_A = count_A + 1;
			}
			if (StyleForm.charAt(i) == 'T'){
				count_T = count_T + 1;
			}
			if (StyleForm.charAt(i) == 'L'){
				count_L = count_L + 1;
			}
		}
		Filter = true;
		//若欄位有相同的答案 則不可採用
		if (Volume.trim().compareTo("") != 0 && Volume.trim().compareTo("X") != 0 && (Volume.trim().compareTo(Num.trim()) == 0 || Volume.trim().compareTo(Page.trim()) == 0 || Volume.trim().compareTo(M_Y.trim()) == 0)){
			Filter = false;
		} 
		if (Num.trim().compareTo("") != 0 && Num.trim().compareTo("X") != 0 && (Num.trim().compareTo(Page.trim()) == 0 || Num.trim().compareTo(M_Y.trim()) == 0)){
			Filter = false;
		} 
		
		if (!(StyleForm.indexOf("A") == -1)  && !(StyleForm.indexOf("T") == -1) && count_A <= 1 && count_T <= 1 && count_L <= 1 && StyleForm.indexOf("AT") == -1 && StyleForm.indexOf("TA") == -1 && StyleForm.indexOf("TL") == -1 && StyleForm.indexOf("LT") == -1 && StyleForm.indexOf("AL") == -1 && StyleForm.indexOf("LA") == -1) {
		} else {
			Filter = false;
		}
	}
	
}