package parser;

import java.io.*;
import java.util.*;
//for regular expression
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//for jaligner
import jaligner.Alignment;
import jaligner.Sequence;
import jaligner.SmithWatermanGotoh;
import jaligner.formats.Pair;
import jaligner.matrix.Matrix;
import jaligner.matrix.MatrixLoader;
import jaligner.util.Commons;
import jaligner.util.SequenceParser;
//------------

public class Parser{
	private Vector<String> AUTHOR = new Vector<String>();
	private Vector<String> JOURNAL = new Vector<String>();
	private Vector<String> SWORD = new Vector<String>(Arrays.asList("a an the of and in on A AN THE OF AND IN ON".split("\\s")));
	private Vector<String> SYMBOL = new Vector<String>(Arrays.asList(", ; : \" \' ` . “ ” - ( ) [ ] / _ ! @ # $ % ^ & * + = \\ | { } < > ? 。 「 」 ~".split("\\s")));
	private Vector<String> SYMBOL_MARK = new Vector<String>(Arrays.asList("R Z C G E E D G G H I K I K Q Q Q Q Q Q Q Q Q Q Q Q Q Q I K I K Q Q I K Q".split("\\s")));
	private Vector<String> NUMBER = new Vector<String>(Arrays.asList("Number number Nr nr No no NO Nos".split("\\s")));
	private Vector<String> ROME_NUM = new Vector<String>(Arrays.asList("II III IV VI VII VIII IX XI XII XIII XIV XV XVI XVII XVIII XIX XX ii iii iv vi vii viii ix xi xii xiii xiv xv xvi xvii xviii xix xx".split("\\s"))); // 考慮單一字元會影響判斷
	private Vector<String> PAGE = new Vector<String>(Arrays.asList("pp page pages PP Page Pages pg PG".split("\\s")));
	private Vector<String> VOLUMN = new Vector<String>(Arrays.asList("Volume volume Vol vol Vo vo".split("\\s")));
	private Vector<String> MONTH = new Vector<String>(Arrays.asList("January February March April May June July August September October November December Jan Feb Mar Apr Jun Jul Aug Sep Oct Nov Dec Sept".split("\\s")));
	private Vector<String> EDITOR = new Vector<String>(Arrays.asList("Eds eds Editor editor Editors editors Ed ed ED edited".split("\\s")));
	private Vector<String> BWORD = new Vector<String>(Arrays.asList("Proceedings Proc Workshop Conf Conference Symposium Sympos Symp International Intern Annual Annu".split("\\s")));
	private Vector<String> JWORD = new Vector<String>(Arrays.asList("Transactions Trans Journal ".split("\\s")));
	private Vector<String> THESIS = new Vector<String>(Arrays.asList("Master Masters Ph PhD Thesis thesis Dissertation dissertation".split("\\s")));
	private Vector<String> TECH = new Vector<String>(Arrays.asList("Tech rep Rpt TR".split("\\s")));
	private Vector<String> INSTITUTION = new Vector<String>(Arrays.asList("University Univ Department Dept Corporation".split("\\s")));
	private Vector<String> PUBLISHER = new Vector<String>(Arrays.asList("Press Pub Publishers Inc Publications".split("\\s")));
  private String ID;
  
	public String Citation;
	public String[] MyArray;
	public char[] TokenForm;
	public char[] AnswerForm;
	public String IndexForm;
	public String AlignForm;
	public String Align_Result;
	public int[] Align_Start;
	public int[] Align_End;
	public Vector<String> StyleForm_Candidate = new Vector<String>();
	public String StyleForm;
	public char[] Result;
	public int[] Result_block;
	public String AuthorAnswer;
	public String TitleAnswer;
	public String VenueAnswer;
	public String JournalAnswer;
	public String BookAnswer;
	public String TechAnswer;
	public String VolumnAnswer;
	public String NumberAnswer;
	public String PageAnswer;
	public String DateAnswer;
	public String EditorAnswer;
	public String PublisherAnswer;
	public String InstitueAnswer;
	public String YearAnswer;
	public String MonthAnswer;
	public String WEB;
	public String ISBN;
	public String NOTE;

	public Parser(){
		//讀取author資料庫
		//ReadAuthor("C:\\BibPro\\BibPro\\KnowledgeDB\\author.txt");
		//讀取journal 資料庫
		//ReadJournal("C:\\BibPro\\BibPro\\KnowledgeDB\\journal.txt");
	}
	
	public void setCitation(String input){
		Citation = input;
		StyleForm_Candidate.removeAllElements();
		StyleForm = "";
		AuthorAnswer = "";
		TitleAnswer = "";
		VenueAnswer = "";
		JournalAnswer = "";
		BookAnswer = "";
		TechAnswer = "";
		VolumnAnswer = "";
		PageAnswer = "";
		NumberAnswer = "";
		DateAnswer = "";
		EditorAnswer = "";
		PublisherAnswer = "";
		InstitueAnswer = "";
		YearAnswer = "";
		MonthAnswer = "";
		WEB = "";
		ISBN = "";
		NOTE = "";
		input = Regex_Processing(input);
		MyArray = split_String(input);
	}
	
	public String Regex_Processing(String citation){
		String inputStr = citation;
		String url_pattern1 = "(((ht|f)tp(s?):\\/\\/)|(www\\.[^ \\[\\]\\(\\)\\n\\r\\t]+)|(([012]?[0-9]{1,2}\\.){3}[012]?[0-9]{1,2})\\/)([^ \\[\\]\\(\\),;&quot;'&lt;&gt;\\n\\r\\t]+)([^\\. \\[\\]\\(\\),;&quot;'&lt;&gt;\\n\\r\\t])|(([012]?[0-9]{1,2}\\.){3}[012]?[0-9]{1,2})";
		String url_pattern2 = "((https?|ftp)\\://((\\[?(\\d{1,3}\\.){3}\\d{1,3}\\]?)|(([-a-zA-Z0-9]+\\.)+[a-zA-Z]{2,4}))(\\:\\d+)?(/[-a-zA-Z0-9._?,'+&amp;%$#=~\\\\]+)*/?)";
		String isbn_pattern = "ISBN (?=.{13})\\d{1,5}( |\\-)\\d{1,7}\\1\\d{1,6}\\1(\\d|X)";
		String web="";
		String temp_web="";
		boolean first=true;
		// 第一階段 URL擷取
		Pattern pattern = Pattern.compile(url_pattern1);
		Matcher matcher = pattern.matcher(inputStr);
		boolean matchFound = matcher.find();
    while(matchFound) {
    	//System.out.println(matcher.start() + "-" + matcher.end());
    	temp_web = matcher.group();
    	if (temp_web.length()> web.length()){
    		web = temp_web;
    	}
    	if(matcher.end() + 1 <= inputStr.length()) {
        matchFound = matcher.find(matcher.end());
      }else{
        break;
      }
    }
		// 第二階段 URL擷取
		pattern = Pattern.compile(url_pattern2);
		matcher = pattern.matcher(inputStr);
		matchFound = matcher.find();
		while(matchFound) {
		//System.out.println(matcher.start() + "-" + matcher.end());
			temp_web = matcher.group();
    	if (temp_web.length()> web.length()){
    		web = temp_web;
    		first=false;
    	}
			if(matcher.end() + 1 <= inputStr.length()) {
				matchFound = matcher.find(matcher.end());
			}else{
			 	break;
			}
		}
		WEB=web;
		if (first){
			inputStr = inputStr.replaceAll(url_pattern1, "");
		} else {
			inputStr = inputStr.replaceAll(url_pattern2, "");
		}
		//ISBN擷取
		pattern = Pattern.compile(isbn_pattern);
		matcher = pattern.matcher(inputStr);
		matchFound = matcher.find();
		while(matchFound) {
		//System.out.println(matcher.start() + "-" + matcher.end());
		ISBN = matcher.group();
			if(matcher.end() + 1 <= inputStr.length()) {
				matchFound = matcher.find(matcher.end());
			}else{
			 	break;
			}
		}
		inputStr = inputStr.replaceAll(isbn_pattern, "");
		return inputStr;
	}
	
	public String[] split_String(String input){
		String TempString = input;
		if(TempString.compareTo("") != 0){
			StringTokenizer ST;
			for (int i=0; i < SYMBOL.size(); i++){
				ST = new StringTokenizer(TempString, SYMBOL.elementAt(i));		
				if (ST.countTokens() > 0 ){
					TempString = ST.nextToken();
					while (ST.hasMoreTokens()) {			
						TempString = TempString + " " + SYMBOL.elementAt(i) + " " + ST.nextToken();	
					}
				}		
				//System.out.println(input + ":" + input.length());
				if ( SYMBOL.elementAt(i).compareTo(""+input.charAt(input.length()-1)+"") == 0 ){
					TempString = TempString + " " + input.charAt(input.length()-1);
				}
			}
			ST = new StringTokenizer(TempString);
			String[] StrArray = new String[ST.countTokens()];
			int index = 0;
			while (ST.hasMoreTokens()) {			
				StrArray[index] = ST.nextToken();
				index = index + 1;
			}
			return StrArray;
		} else {
			String[] temp = new String[1];
			temp[0] = "";
			return temp;
		}
	}
	
	public void Form_translation(){
		TokenForm = new char[MyArray.length];
		Result = new char[MyArray.length];
		Result_block = new int[MyArray.length];
		for(int i=0; i < MyArray.length; i++){
			// 優先權越高越上面
			boolean parsed = false;
			if (!parsed && SYMBOL.contains(MyArray[i])){
				TokenForm[i] = SYMBOL_MARK.elementAt(SYMBOL.indexOf(MyArray[i])).charAt(0);
				parsed = true;			
			}
			if (!parsed && PAGE.contains(MyArray[i])){
				//lookahead~ 看後面接的是否為數字
				try{
					int temp_value = Integer.parseInt(MyArray[i+1]);
					TokenForm[i] = 'P';
					parsed = true;
				} catch(Exception e1){
					try{
						int temp_value2 = Integer.parseInt(MyArray[i+2]);
						TokenForm[i] = 'P';
						parsed = true;
					} catch(Exception e2){
					}
				}
			}
			if (!parsed && VOLUMN.contains(MyArray[i])){
				TokenForm[i] = 'v';
				parsed = true;
			}
			if (!parsed && NUMBER.contains(MyArray[i])){
				//lookahead~ 看後面接的是否為數字
				try{
					int temp_value = Integer.parseInt(MyArray[i+1]);
					TokenForm[i] = 'i';
					parsed = true;
				} catch(Exception e1){
					try{
						int temp_value2 = Integer.parseInt(MyArray[i+2]);
						TokenForm[i] = 'i';
						parsed = true;
					} catch(Exception e2){
					}
				}
			}
			if (!parsed && MONTH.contains(MyArray[i])){
				TokenForm[i] = 'Y';
				parsed = true;
			}
			if (!parsed && JWORD.contains(MyArray[i])){
				TokenForm[i] = 'L';
				parsed = true;
			}
			if (!parsed && BWORD.contains(MyArray[i])){
				TokenForm[i] = 'L';
				parsed = true;
			}
			/*if (!parsed && JOURNAL.contains(MyArray[i])){
				TokenForm[i] = 'L';
				parsed = true;
			}*/
			if (!parsed && THESIS.contains(MyArray[i])){
				TokenForm[i] = 'L';
				parsed = true;
			}
			if (!parsed && TECH.contains(MyArray[i])){
				TokenForm[i] = 'L';
				parsed = true;
			}
			if (i>=2 && !parsed && MyArray[i].toUpperCase().compareTo("REPORT") == 0){
				if (MyArray[i-1].toUpperCase().compareTo("TECHNICAL") == 0 || MyArray[i-2].toUpperCase().compareTo("TECHNICAL") == 0 || TokenForm[i-1]=='L' || TokenForm[i-2]=='L'){
					TokenForm[i] = 'L';
					parsed = true;
				}
			}
			//針對Proc. of
			if (i>=2 && !parsed && MyArray[i].compareTo("of") == 0){
				if (MyArray[i-1].toUpperCase().compareTo(".") == 0 && MyArray[i-2].toUpperCase().compareTo("PROC") == 0){
					TokenForm[i] = 'L';
					parsed = true;
				}
			} 
			//針對Conf. on
			if (i>=2 && !parsed && MyArray[i].compareTo("on") == 0){
				if (MyArray[i-1].toUpperCase().compareTo(".") == 0 && MyArray[i-2].toUpperCase().compareTo("CONF") == 0){
					TokenForm[i] = 'L';
					parsed = true;
				}
			} 
			if (!parsed && INSTITUTION.contains(MyArray[i])){
				TokenForm[i] = 'S';
				parsed = true;
			}
			if (!parsed && PUBLISHER.contains(MyArray[i])){
				TokenForm[i] = 'M';
				parsed = true;
			}
			//針對縮寫 設定Author
			if (i>=1 && MyArray[i-1].length() == 1 && MyArray[i-1].compareTo("J") != 0 && Character.isUpperCase(MyArray[i-1].charAt(0)) && MyArray[i].compareTo(".") == 0 ){
				if(MyArray[i-1].compareTo("D") == 0 && i+1 < MyArray.length && MyArray[i+1].toUpperCase().compareTo("THESIS") == 0 ){
				} else { 
					TokenForm[i-1] = 'A';
				}
				//TokenForm[i] = 'A';
			}
			
			if (i>=1 && i < MyArray.length - 1  && EDITOR.contains(MyArray[i]) && (MyArray[i-1].compareTo("(") == 0 || MyArray[i-1].compareTo(",") == 0 || MyArray[i+1].compareTo(",") == 0 || MyArray[i+1].compareTo(".") == 0 )){
				TokenForm[i] = 'F';
				parsed = true;
			}
			
			//針對"'" 做處理
			if (i>=1 && MyArray[i-1].compareTo("\'") == 0  && MyArray[i].compareTo("s") == 0 ){
				TokenForm[i-1] = 'X';
			}
			
			if (!parsed){
				try{
					int temp_value = Integer.parseInt(MyArray[i]);
					if (temp_value >= 1900 && temp_value <= 2010 ){
						TokenForm[i] = 'y';
					} else {
						TokenForm[i] = 'N';
					}
					parsed = true;
				} catch(Exception e1){
					//考慮開頭為英文字母的可能
					try{
						int temp_value = Integer.parseInt(MyArray[i].substring(1));
						TokenForm[i] = 'N';
					} catch(Exception e2){
					}
					//e.printStackTrace();
				}
			}
			if (!parsed && AUTHOR.contains(MyArray[i])){
				TokenForm[i] = 'A';
				parsed = true;
			}
			if (!parsed && ROME_NUM.contains(MyArray[i])){
				TokenForm[i] = 'N';
				parsed = true;
			}
			if (!parsed){
				TokenForm[i] = 'X';
			}
		}
		// 產生IndexForm
		IndexForm = "";
		int countX=0;
		
		//產生AlignForm
		int idx = 0;
		AlignForm = "";
		Align_Start = new int[MyArray.length];
		Align_End = new int[MyArray.length];
		for(int i=0; i < TokenForm.length; i++){
			if (TokenForm[i] != 'X'){
				if (countX >= 3){
					IndexForm = IndexForm + "B";
					AlignForm = AlignForm + "B";
					Align_Start[idx] = i - countX;
					Align_End[idx] = i-1;
					idx = idx + 1;
				} else if (countX == 2){
					AlignForm = AlignForm + "B";
					Align_Start[idx] = i-countX;
					Align_End[idx] = i-1;
					idx = idx + 1;
				} else if (countX == 1){
					AlignForm = AlignForm + "X";
					Align_Start[idx] = i-countX;
					Align_End[idx] = i-1;
					idx = idx + 1;
				}
				
				IndexForm = IndexForm + TokenForm[i];
				AlignForm = AlignForm + TokenForm[i];
				Align_Start[idx] = i;
				Align_End[idx] = i;
				idx = idx + 1;
				countX = 0;
			} else {
				countX=countX+1;
			}
		}	
		
		while(!(AlignForm.indexOf("NHN") == -1 && AlignForm.indexOf("XHN") == -1 && AlignForm.indexOf("NHX") == -1 && AlignForm.indexOf("BHN") == -1 && AlignForm.indexOf("NHB") == -1 ) ){
			Blocking_AlignForm("NHN", "N");
			Blocking_AlignForm("XHN", "B");
			Blocking_AlignForm("NHX", "B");
			Blocking_AlignForm("BHN", "B");
			Blocking_AlignForm("NHB", "B");
		}
		
		//Page blocking
		if (AlignForm.indexOf("PN") != -1){
			Blocking_AlignForm("PN", "P");
		} else if (AlignForm.indexOf("PDN") != -1){
			Blocking_AlignForm("PDN", "P");
		} else if (AlignForm.indexOf("PCN") != -1){
			Blocking_AlignForm("PCN", "P");
		}
		
		//Volumn Issue Blocking Stage1
		while(!(AlignForm.indexOf("vN") == -1 && AlignForm.indexOf("vX") == -1  && AlignForm.indexOf("vDX") == -1 && AlignForm.indexOf("vDN") == -1 && AlignForm.indexOf("vRN") == -1 && AlignForm.indexOf("vCN") == -1)){
			Blocking_AlignForm("vN", "V");
			Blocking_AlignForm("vX", "V");
			Blocking_AlignForm("vDX", "V");
			Blocking_AlignForm("vDN", "V");
			Blocking_AlignForm("vRN", "V");
			Blocking_AlignForm("vCN", "V");
		}
		
		while(!(AlignForm.indexOf("iN") == -1 && AlignForm.indexOf("iX") == -1  && AlignForm.indexOf("iDN") == -1 && AlignForm.indexOf("iRN") == -1 && AlignForm.indexOf("iCN") == -1)){
			Blocking_AlignForm("iN", "W");
			Blocking_AlignForm("iX", "W");
			Blocking_AlignForm("iDX", "W");
			Blocking_AlignForm("iDN", "W");
			Blocking_AlignForm("iRN", "W");
			Blocking_AlignForm("iCN", "W");
		}
		
		//Volumn Issue Blocking Stage2
		/*while(!(AlignForm.indexOf("v") == -1 && AlignForm.indexOf("VV") == -1 && AlignForm.indexOf("VDV") == -1 && AlignForm.indexOf("VRV") == -1 && AlignForm.indexOf("VCV") == -1)){
			Blocking_AlignForm("v", "X");
			Blocking_AlignForm("VV", "V");
			Blocking_AlignForm("VDV", "V");
			Blocking_AlignForm("VRV", "V");
			Blocking_AlignForm("VCV", "V");
		}
		
		
		//Volumn Issue Blocking Stage3
		if (AlignForm.indexOf("V") == -1){
			Blocking_AlignForm("NINK", "V");
			Blocking_AlignForm("NINRNRNK", "V");
		}*/
		
		// Date Blocking
		while(!(AlignForm.indexOf("yY") == -1 && AlignForm.indexOf("Yy") == -1 && AlignForm.indexOf("YDy") == -1 && AlignForm.indexOf("yDY") == -1 && AlignForm.indexOf("YQY") == -1 && AlignForm.indexOf("YN") == -1 && AlignForm.indexOf("NY") == -1 && AlignForm.indexOf("YDN") == -1)){
			Blocking_AlignForm("yY", "Y");
			Blocking_AlignForm("Yy", "Y");
			Blocking_AlignForm("YDy", "Y");
			Blocking_AlignForm("yDY", "Y");
			Blocking_AlignForm("YQY", "Y");
			Blocking_AlignForm("YN", "Y");
			Blocking_AlignForm("NY", "Y");
			Blocking_AlignForm("YDN", "Y");
		}
		
		
		while(!(AlignForm.indexOf("XX") == -1 && AlignForm.indexOf("BB") == -1 && AlignForm.indexOf("BX") == -1 && AlignForm.indexOf("XB") == -1 && AlignForm.indexOf("BCB") == -1 && AlignForm.indexOf("BHB") == -1 && AlignForm.indexOf("XHX") == -1 && AlignForm.indexOf("BHX") == -1 && AlignForm.indexOf("XHB") == -1 && AlignForm.indexOf("XCB") == -1 && AlignForm.indexOf("XQB") == -1) ){
			Blocking_AlignForm("XX", "B");
			Blocking_AlignForm("BB", "B");
			Blocking_AlignForm("BX", "B");
			Blocking_AlignForm("XB", "B");
			Blocking_AlignForm("BCB", "B");
			Blocking_AlignForm("BHB", "B");
			Blocking_AlignForm("XHX", "B");
			Blocking_AlignForm("BHX", "B");
			Blocking_AlignForm("XHB", "B");
			Blocking_AlignForm("XCB", "B");
			Blocking_AlignForm("XQB", "B");
		}
		
		
		//Author Blocking	Stage 1
		while(!(AlignForm.indexOf("AA") == -1 && AlignForm.indexOf("AB") == -1 && AlignForm.indexOf("BA") == -1 && AlignForm.indexOf("AX") == -1 && AlignForm.indexOf("XA") == -1 && AlignForm.indexOf("ARA") == -1 && AlignForm.indexOf("ADA") == -1 && AlignForm.indexOf("AHA") == -1 && AlignForm.indexOf("AZA") == -1 && AlignForm.indexOf("ADRA") == -1 && AlignForm.indexOf("ADHA") == -1 && AlignForm.indexOf("ADQA") == -1 && AlignForm.indexOf("ARQA") == -1 && AlignForm.indexOf("ARDQA") == -1 && AlignForm.indexOf("ADRQA") == -1  && AlignForm.indexOf("XRA") == -1 && AlignForm.indexOf("AHX") == -1 && AlignForm.indexOf("XHA") == -1 && AlignForm.indexOf("ADX") == -1 && AlignForm.indexOf("XDA") == -1 )){
			Blocking_AlignForm("AA","A");
			Blocking_AlignForm("AB","A");
			Blocking_AlignForm("BA","A");
			Blocking_AlignForm("AX","A");
			Blocking_AlignForm("XA","A");
			Blocking_AlignForm("ARA","A");
			Blocking_AlignForm("ADA","A");
			Blocking_AlignForm("AHA","A");
			Blocking_AlignForm("AZA","A");
			Blocking_AlignForm("ADRA","A");
			Blocking_AlignForm("ADHA","A");
			Blocking_AlignForm("ADQA","A");
			Blocking_AlignForm("ARQA","A");
			Blocking_AlignForm("ARDQA","A");
			Blocking_AlignForm("ADRQA","A");
			Blocking_AlignForm("XRA","A");
			Blocking_AlignForm("AHX","A");
			Blocking_AlignForm("XHA","A");
			Blocking_AlignForm("ADX","A");
			Blocking_AlignForm("XDA","A");
			//count = count + 1;
		}
		
		//Editor Blocking
		while(!(AlignForm.indexOf("RF") == -1 && AlignForm.indexOf("IFDK") == -1 && AlignForm.indexOf("AF") == -1 && AlignForm.indexOf("FA") == -1 && AlignForm.indexOf("ADF") == -1 && AlignForm.indexOf("AIFK") == -1)){
			Blocking_AlignForm("RF", "F");
			Blocking_AlignForm("IFDK", "F");
			Blocking_AlignForm("AF", "F");
			Blocking_AlignForm("FA", "F");
			Blocking_AlignForm("ADF", "F");
			Blocking_AlignForm("AIFK", "F");	
		}
		
		//Author Blocking Stage 2
		while(!(AlignForm.indexOf("ADBDA") == -1 && AlignForm.indexOf("ARBRA") == -1 && AlignForm.indexOf("ADBRA") == -1 &&  AlignForm.indexOf("ADRBRA") == -1 &&  AlignForm.indexOf("ADBDHA") == -1)){
			Blocking_AlignForm("ADBDA","A");
			Blocking_AlignForm("ARBRA","A");
			Blocking_AlignForm("ADBRA","A");
			Blocking_AlignForm("ADRBRA","A");
			Blocking_AlignForm("ADBDHA","A");
		}
			
		//Publish Blocking	
		while(!(AlignForm.indexOf("MM") == -1 && AlignForm.indexOf("MB") == -1 && AlignForm.indexOf("BM") == -1 && AlignForm.indexOf("MX") == -1 && AlignForm.indexOf("XM") == -1 && AlignForm.indexOf("MDM") == -1 && AlignForm.indexOf("MRM") == -1 && AlignForm.indexOf("MQM") == -1)){
			Blocking_AlignForm("MM","M");
			Blocking_AlignForm("MB","M");
			Blocking_AlignForm("BM","M");
			Blocking_AlignForm("MX","M");
			Blocking_AlignForm("XM","M");
			Blocking_AlignForm("MDM","M");
			Blocking_AlignForm("MRM","M");
			Blocking_AlignForm("MQM","M");	
		}
		
		//Instituation Blocking
		while(!(AlignForm.indexOf("SS") == -1 && AlignForm.indexOf("SB") == -1 && AlignForm.indexOf("BS") == -1 && AlignForm.indexOf("SX") == -1 && AlignForm.indexOf("XS") == -1 && AlignForm.indexOf("SDS") == -1 && AlignForm.indexOf("SRS") == -1 && AlignForm.indexOf("SQS") == -1)){
			Blocking_AlignForm("SS","S");
			Blocking_AlignForm("SB","S");
			Blocking_AlignForm("BS","S");
			Blocking_AlignForm("SX","S");
			Blocking_AlignForm("XS","S");
			Blocking_AlignForm("SDS","S");
			Blocking_AlignForm("SRS","S");
			Blocking_AlignForm("SQS","S");	
		}
		
		//Venue Blocking
		while(!(AlignForm.indexOf("LL") == -1 && AlignForm.indexOf("LB") == -1 && AlignForm.indexOf("BL") == -1 && AlignForm.indexOf("LX") == -1 && AlignForm.indexOf("XL") == -1 && AlignForm.indexOf("LDL") == -1 && AlignForm.indexOf("LRL") == -1 && AlignForm.indexOf("LHL") == -1 && AlignForm.indexOf("LyL") == -1 && AlignForm.indexOf("LNL") == -1  && AlignForm.indexOf("LQL") == -1 && AlignForm.indexOf("LEN") == -1 && AlignForm.indexOf("LDXDL") == -1 && AlignForm.indexOf("LDBDL") == -1 && AlignForm.indexOf("LDBENL") == -1)){
			Blocking_AlignForm("LL","L");
			Blocking_AlignForm("LB","L");
			Blocking_AlignForm("BL","L");
			Blocking_AlignForm("LX","L");
			Blocking_AlignForm("XL","L");
			Blocking_AlignForm("LDL","L");
			Blocking_AlignForm("LRL","L");
			Blocking_AlignForm("LHL","L");
			Blocking_AlignForm("LyL","L");
			Blocking_AlignForm("LNL","L");
			Blocking_AlignForm("LQL","L");
			Blocking_AlignForm("LEN","L");
			Blocking_AlignForm("LDXDL","L");
			Blocking_AlignForm("LDBDL","L");
			Blocking_AlignForm("LDBENL","L");
			//count = count + 1;
		}
		
		// 小寫修正
		while(!(AlignForm.indexOf("y") == -1 && AlignForm.indexOf("YRY") == -1 && AlignForm.indexOf("YDY") == -1)){
			Blocking_AlignForm("y","Y");
			Blocking_AlignForm("YRY","Y");
			Blocking_AlignForm("YDY","Y");
		}
		
		char[] temp_align = AlignForm.toCharArray();
		for(int i=0; i < AlignForm.length(); i++){
			if (AlignForm.charAt(i) == 'B' && Align_End[i] - Align_Start[i] > 4){
				temp_align[i] = 'T';
			}
		}
		AlignForm = new String(temp_align);		
		IndexForm = AlignForm;
	}
	
	// only used in Form_translation
	private void Blocking_AlignForm(String Target, String Replacement){
		//AlingForm做blocking
		int reduce_point;
		int[] temp_start;
		int[] temp_end;
		reduce_point = AlignForm.indexOf(Target);
		while(reduce_point != -1){
			AlignForm = AlignForm.replaceFirst(Target, Replacement);
			temp_start = new int[AlignForm.length()];
			temp_end = new int[AlignForm.length()];
			for(int i=0; i<reduce_point; i++){
				temp_start[i] = Align_Start[i];
				temp_end[i] = Align_End[i];
			}
			temp_start[reduce_point]=Align_Start[reduce_point];
			temp_end[reduce_point]=Align_End[reduce_point + Target.length()-1];
			for(int i=reduce_point+1; i<AlignForm.length();i++){
				if (i+Target.length()-1 < Align_Start.length){
					temp_start[i] = Align_Start[i+Target.length()-1];
					temp_end[i] = Align_End[i+Target.length()-1];
				}
			}
			Align_Start = temp_start;
			Align_End = temp_end;
			reduce_point = AlignForm.indexOf(Target);
		}
	}
	
	//有正確答案的情況下 給TemplateGen呼叫
	public void TForm_translation(String AuthorAns, String TitleAns, String JournalAns, String BookAns, String TechAns, String VolumnAns, String PageAns, String NumAns, String M_YAns, String WebAns){
		//TokenForm = new char[MyArray.length];
		//Result = new char[MyArray.length];
		//Result_block = new int[MyArray.length];
		String[] A_Array = split_String( AuthorAns);
		String[] T_Array = split_String(TitleAns);
		String[] J_Array = split_String(JournalAns);
		String[] B_Array = split_String(BookAns);
		String[] Tech_Array = split_String(TechAns);
		String[] V_Array = split_String(VolumnAns);
		String[] P_Array = split_String(PageAns);
		String[] N_Array = split_String(NumAns);
		String[] Y_Array = split_String(M_YAns);
		String[] W_Array = split_String(WebAns);

		//用來預防Vol,Num,page彼此的衝突
		int V_count = 0;
		int P_count = 0;
		int N_count = 0;
		boolean V_parsed;
		boolean P_parsed;
		boolean N_parsed;
		//\\//
		boolean[] assign = new boolean[MyArray.length]; 
		AnswerForm = new char[MyArray.length];
		//\\//
		//int skip = 0; //遇到縮寫時修正 
		//處理AnswerForm
		for(int i=0; i < MyArray.length; i++){	
			if (SYMBOL.contains(MyArray[i])){
				AnswerForm[i] = SYMBOL_MARK.elementAt(SYMBOL.indexOf(MyArray[i])).charAt(0);
			//只考慮不是標點符號的token
			} else if (MyArray[i].toUpperCase().compareTo("AND") == 0){
				AnswerForm[i] = 'X';
			} else {
				//Title
				if(i+T_Array.length <= MyArray.length && assign[i] == false){
			    boolean testing = false;
			    int skip_T = 0;
			    int end_T = 0;
					for(int j=0; j+skip_T  < T_Array.length; j++){
						//System.out.println(T_Array[j+skip_T].toUpperCase() + " " + MyArray[i+j].toUpperCase());
						if (T_Array[j+skip_T].toUpperCase().compareTo(MyArray[i+j].toUpperCase()) == 0){
							testing = true;
							end_T = i+j;
						}else{
							if (!SYMBOL.contains(T_Array[j+skip_T]) && !SYMBOL.contains(MyArray[i+j])){
								testing = false;
								break;
							} else if (SYMBOL.contains(T_Array[j+skip_T]) && !SYMBOL.contains(MyArray[i+j])) {
								skip_T = skip_T + 1;
								j = j - 1;
							} else if (!SYMBOL.contains(T_Array[j+skip_T]) && SYMBOL.contains(MyArray[i+j])) {
								skip_T = skip_T - 1;
								//j = j+1;
							}
						}
					}	
					//System.out.println("START: " + i + " END: " + end_T + " " + testing);
					if (testing){
						for(int j=i; j <= end_T; j++){
							assign[j] = true;
							if (!SYMBOL.contains(MyArray[j])){
								AnswerForm[j] = 'T';
							}
						}
					}
				}
				//Author
				if(i+A_Array.length <= MyArray.length && assign[i] == false){
			    boolean testing = false;
			    //答案欄縮寫處理
			    int skip_A = 0;
			    int end_A = 0;
					for(int j=0; j+skip_A < A_Array.length; j++){
						if (A_Array[j+skip_A].toUpperCase().compareTo(MyArray[i+j].toUpperCase()) == 0){
							testing = true;
							end_A = i+j;
						} else if (MyArray[i+j].length() == 1 && A_Array[j+skip_A].toUpperCase().charAt(0) == MyArray[i+j].charAt(0)){
							if (i+j+1 < MyArray.length && MyArray[i+j+1].compareTo(".") == 0){
								testing = true;
								end_A = i+j;
								j = j + 1; //跳過MyArray的.
								skip_A = skip_A-1; //回覆A_Array的位置
							} else {
								testing = false;
								break;
							}
						}else if (A_Array[j+skip_A].length() == 1 && A_Array[j+skip_A].charAt(0) == MyArray[i+j].toUpperCase().charAt(0)){
							if (j+skip_A+1 < A_Array.length && A_Array[j+skip_A+1].compareTo(".") == 0){
								testing = true;
								end_A = i+j;
								skip_A = skip_A+1; //跳過A_Array的.
							} else {
								testing = false;
								break;
							}
						}else{
							if (!SYMBOL.contains(MyArray[i+j]) && !SYMBOL.contains(A_Array[j+skip_A]) && MyArray[i+j].toUpperCase().compareTo("AND") != 0 && A_Array[j+skip_A].toUpperCase().compareTo("AND") != 0){
								testing = false;
								break;
							} else if ((!SYMBOL.contains(MyArray[i+j]) && MyArray[i+j].toUpperCase().compareTo("AND") != 0) && (SYMBOL.contains(A_Array[j+skip_A]) || A_Array[j+skip_A].toUpperCase().compareTo("AND") == 0)){
								skip_A = skip_A + 1;
								j = j-1;
							} else if ((SYMBOL.contains(MyArray[i+j]) || MyArray[i+j].toUpperCase().compareTo("AND") == 0) && (!SYMBOL.contains(A_Array[j+skip_A]) && A_Array[j+skip_A].toUpperCase().compareTo("AND") != 0)){
								skip_A = skip_A - 1;
								//j = j+1;
							}
						}
					}	
					if (testing){
						for(int j=i; j <= end_A; j++){
							assign[j] = true;
							if (!SYMBOL.contains(MyArray[j])){
								AnswerForm[j] = 'A';
							}
						}
					} 
					
				}
				//Journal
				if(i+J_Array.length <= MyArray.length && assign[i] == false){
			    boolean testing = false;
			    int skip_J = 0;
			    int end_J = 0;
					for(int j=0; j+skip_J < J_Array.length; j++){
						if (J_Array[j+skip_J].toUpperCase().compareTo(MyArray[i+j].toUpperCase()) == 0){
							testing = true;
							end_J = i+j;
						}else{
							if (!SYMBOL.contains(J_Array[j+skip_J]) && !SYMBOL.contains(MyArray[i+j])){
								testing = false;
								break;
							} else if (SYMBOL.contains(J_Array[j+skip_J]) && !SYMBOL.contains(MyArray[i+j])) {
								skip_J = skip_J + 1;
								j = j - 1;
							} else if (!SYMBOL.contains(J_Array[j+skip_J]) && SYMBOL.contains(MyArray[i+j])) {
								skip_J = skip_J - 1;
								//j = j+1;
							}
						}
					}	
					if (testing){
						for(int j=i; j <= end_J; j++){
							assign[j] = true;
							if (!SYMBOL.contains(MyArray[j])){
								AnswerForm[j] = 'j';
							}
						}
					}
				}
				//Book
				if(i+B_Array.length <= MyArray.length && assign[i] == false){
			    boolean testing = false;
			    int skip_B = 0;
			    int end_B = 0;
					for(int j=0; j+skip_B < B_Array.length; j++){
						if (B_Array[j+skip_B].toUpperCase().compareTo(MyArray[i+j].toUpperCase()) == 0){
							testing = true;
							end_B = i+j;
						}else{
							if (!SYMBOL.contains(B_Array[j+skip_B]) && !SYMBOL.contains(MyArray[i+j])){
								testing = false;
								break;
							} else if (SYMBOL.contains(B_Array[j+skip_B]) && !SYMBOL.contains(MyArray[i+j])) {
								skip_B = skip_B + 1;
								j = j - 1;
							} else if (!SYMBOL.contains(B_Array[j+skip_B]) && SYMBOL.contains(MyArray[i+j])) {
								skip_B = skip_B - 1;
								//j = j+1;
							}
						}
					}	
					if (testing){
						for(int j=i; j <= end_B; j++){
							assign[j] = true;
							if (!SYMBOL.contains(MyArray[j])){
								AnswerForm[j] = 'b';
							}
						}
					}
				}
				//Tech
				if(i+Tech_Array.length <= MyArray.length && assign[i] == false){
			    boolean testing = false;
			    int skip_T = 0;
			    int end_T = 0;
					for(int j=0; j+skip_T < Tech_Array.length; j++){
						if (Tech_Array[j+skip_T].toUpperCase().compareTo(MyArray[i+j].toUpperCase()) == 0){
							testing = true;
							end_T = i+j;
						}else{
							if (!SYMBOL.contains(Tech_Array[j+skip_T]) && !SYMBOL.contains(MyArray[i+j])){
								testing = false;
								break;
							} else if (SYMBOL.contains(Tech_Array[j+skip_T]) && !SYMBOL.contains(MyArray[i+j])) {
								skip_T = skip_T + 1;
								j = j - 1;
							} else if (!SYMBOL.contains(Tech_Array[j+skip_T]) && SYMBOL.contains(MyArray[i+j])) {
								skip_T = skip_T - 1;
								//j = j+1;
							}
						}
					}	
					if (testing){
						for(int j=i; j <= end_T; j++){
							assign[j] = true;
							if (!SYMBOL.contains(MyArray[j])){
								AnswerForm[j] = 't';
							}
						}
					}
				}
				//Page
				if(i+P_Array.length <= MyArray.length && assign[i] == false){
			    boolean testing = false;
			    int skip_P = 0;
			    int end_P = 0;
					for(int j=0; j+skip_P< P_Array.length; j++){
						if (P_Array[j+skip_P].toUpperCase().compareTo(MyArray[i+j].toUpperCase()) == 0){
							testing = true;
							end_P = i+j;
						}else{
							if (!SYMBOL.contains(P_Array[j+skip_P]) && !SYMBOL.contains(MyArray[i+j])){
								testing = false;
								break;
							} else if (SYMBOL.contains(P_Array[j+skip_P]) && !SYMBOL.contains(MyArray[i+j])) {
								skip_P = skip_P + 1;
								j = j - 1;
							} else if (!SYMBOL.contains(P_Array[j+skip_P]) && SYMBOL.contains(MyArray[i+j])) {
								skip_P = skip_P - 1;
								//j = j+1;
							}
						}
					}	
					if (testing){
						for(int j=i; j <= end_P; j++){
							assign[j] = true;
							if (!SYMBOL.contains(MyArray[j])){
								AnswerForm[j] = 'P';
							}
						}
					}
				}
				//Issue
				if(i+N_Array.length <= MyArray.length && assign[i] == false){
			    boolean testing = false;
			    int skip_i = 0;
			    int end_i = 0;
					for(int j=0; j+skip_i < N_Array.length; j++){
						if (N_Array[j+skip_i].toUpperCase().compareTo(MyArray[i+j].toUpperCase()) == 0){
							testing = true;
							end_i = i+j;
						}else{
							if (!SYMBOL.contains(N_Array[j+skip_i]) && !SYMBOL.contains(MyArray[i+j])){
								testing = false;
								break;
							} else if (SYMBOL.contains(N_Array[j+skip_i]) && !SYMBOL.contains(MyArray[i+j])) {
								skip_i = skip_i + 1;
								j = j - 1;
							} else if (!SYMBOL.contains(N_Array[j+skip_i]) && SYMBOL.contains(MyArray[i+j])) {
								skip_i = skip_i - 1;
								//j = j+1;
							}
						}
					}	
					if (testing){
						for(int j=i; j <= end_i; j++){
							assign[j] = true;
							if (!SYMBOL.contains(MyArray[j])){
								AnswerForm[j] = 'W';
							}
						}
					}
				}
				//Volume
				if(i+V_Array.length <= MyArray.length && assign[i] == false){
			    boolean testing = false;
			    int skip_v = 0;
			    int end_v = 0;
					for(int j=0; j+skip_v < V_Array.length; j++){
						if (V_Array[j+skip_v].toUpperCase().compareTo(MyArray[i+j].toUpperCase()) == 0){
							testing = true;
							end_v = i+j;
						}else{
							if (!SYMBOL.contains(V_Array[j+skip_v]) && !SYMBOL.contains(MyArray[i+j])){
								testing = false;
								break;
							} else if (SYMBOL.contains(V_Array[j+skip_v]) && !SYMBOL.contains(MyArray[i+j])) {
								skip_v = skip_v + 1;
								j = j - 1;
							} else if (!SYMBOL.contains(V_Array[j+skip_v]) && SYMBOL.contains(MyArray[i+j])) {
								skip_v = skip_v - 1;
								//j = j+1;
							}
						}
					}	
					if (testing){
						for(int j=i; j <= end_v; j++){
							assign[j] = true;
							AnswerForm[j] = 'V';
						}
					}
				}
				//Month
				/*if(i+M_Array.length <= MyArray.length && assign[i] == false){
			    boolean testing = false;
			    int skip_Y=0;
			    int end_Y=0;
					for(int j=0; j+skip_Y < M_Array.length; j++){
						if (M_Array[j].toUpperCase().compareTo(MyArray[i+j].toUpperCase()) == 0){
							testing = true;
							end_Y = i+j;
						}else{
							if (!SYMBOL.contains(M_Array[j+skip_Y]) ){
								testing = false;
								break;
							} else {
								skip_Y = skip_Y + 1;
								j = j - 1;
							}
						}
					}	
					if (testing){
						for(int j=i; j <= end_Y; j++){
							assign[j] = true;
							AnswerForm[j] = 'm';
						}
					}
				}*/
				//Month Year
				if(i+Y_Array.length <= MyArray.length && assign[i] == false){
			    boolean testing = false;
			    int skip_y=0;
			    int end_y=0;
					for(int j=0; j+skip_y < Y_Array.length; j++){
						if (Y_Array[j+skip_y].toUpperCase().compareTo(MyArray[i+j].toUpperCase()) == 0){
							testing = true;
							end_y = i+j;
						}else{
							if (!SYMBOL.contains(Y_Array[j+skip_y]) && !SYMBOL.contains(MyArray[i+j])){
								testing = false;
								break;
							} else if (SYMBOL.contains(Y_Array[j+skip_y]) && !SYMBOL.contains(MyArray[i+j])) {
								skip_y = skip_y + 1;
								j = j - 1;
							} else if (!SYMBOL.contains(Y_Array[j+skip_y]) && SYMBOL.contains(MyArray[i+j])) {
								skip_y = skip_y - 1;
								//j = j+1;
							}
						}
					}	
					if (testing){
						for(int j=i; j <= end_y; j++){
							assign[j] = true;
							AnswerForm[j] = 'Y';
						}
					}
				}
				//Web
				if(i+W_Array.length <= MyArray.length && assign[i] == false){
			    boolean testing = false;
			    int skip_W=0;
			    int end_W=0;
					for(int j=0; j+skip_W < W_Array.length; j++){
						if (W_Array[j+skip_W].toUpperCase().compareTo(MyArray[i+j].toUpperCase()) == 0){
							testing = true;
							end_W = i+j;
						}else{
							if (!SYMBOL.contains(W_Array[j+skip_W]) && !SYMBOL.contains(MyArray[i+j])){
								testing = false;
								break;
							} else if (SYMBOL.contains(W_Array[j+skip_W]) && !SYMBOL.contains(MyArray[i+j])) {
								skip_W = skip_W + 1;
								j = j - 1;
							} else if (!SYMBOL.contains(W_Array[j+skip_W]) && SYMBOL.contains(MyArray[i+j])) {
								skip_W = skip_W - 1;
								//j = j+1;
							}
						}
					}	
					if (testing){
						for(int j=i; j <= end_W; j++){
							assign[j] = true;
							AnswerForm[j] = 'u';
						}
					}
				}
				// 填入標點符號
				if (!assign[i] && SYMBOL.contains(MyArray[i])){
					AnswerForm[i] = SYMBOL_MARK.elementAt(SYMBOL.indexOf(MyArray[i])).charAt(0);
					assign[i] = true;			
				}
				// 填入保留字
				if (!assign[i] && PAGE.contains(MyArray[i])){
					//lookahead~ 看後面接的是否為數字
					try{
						int temp_value = Integer.parseInt(MyArray[i+1]);
						AnswerForm[i] = 'P';
						assign[i] = true;
					} catch(Exception e1){
						try{
							int temp_value2 = Integer.parseInt(MyArray[i+2]);
							AnswerForm[i] = 'P';
							assign[i] = true;
						} catch(Exception e2){
						}
					}
				}
				if (!assign[i] && VOLUMN.contains(MyArray[i])){
					AnswerForm[i] = 'v';
					assign[i] = true;
				}
				if (!assign[i] && NUMBER.contains(MyArray[i])){
					//lookahead~ 看後面接的是否為數字
					try{
						int temp_value = Integer.parseInt(MyArray[i+1]);
						AnswerForm[i] = 'i';
						assign[i] = true;
					} catch(Exception e1){
						try{
							int temp_value2 = Integer.parseInt(MyArray[i+2]);
							AnswerForm[i] = 'i';
							assign[i] = true;
						} catch(Exception e2){
						}
					}
				}
				//填入無法辨認
				if (!assign[i]){
					AnswerForm[i] = 'X';
				}
			}			
		}
		//加入AlignForm Information
		if (AlignForm != null){
			for(int i=0; i < AlignForm.length(); i++){
				for(int j=Align_Start[i]; j<=Align_End[i];j++){
					if (assign[j] == false && (AlignForm.charAt(i) == 'F' || AlignForm.charAt(i) == 'S' || AlignForm.charAt(i) == 'M')){
						AnswerForm[j] = AlignForm.charAt(i);
					}
				}
			}
		}

		// 連接相同欄位
		// 將Author 區域合併
		String temp = new String(AnswerForm);
		int Author1 = temp.indexOf("A");
		int Author2 = temp.lastIndexOf("A");
		for(int i=Author1+1; i <= Author2; i++){
			AnswerForm[i] = 'A';
		}		
		// 將Title 區域合併
		int Title1 = temp.indexOf("T");
		int Title2 = temp.lastIndexOf("T");
		for(int i=Title1+1; i <= Title2; i++){
			AnswerForm[i] = 'T';
		}		
		// 將Venue 區域合併
		int journal1 = temp.indexOf("j");
		int journal2 = temp.lastIndexOf("j");
		for(int i=journal1+1; i <= journal2; i++){
			AnswerForm[i] = 'j';
		}		
		int book1 = temp.indexOf("b");
		int book2 = temp.lastIndexOf("b");
		for(int i=book1+1; i <= book2; i++){
			AnswerForm[i] = 'b';
		}		
		int tech1 = temp.indexOf("t");
		int tech2 = temp.lastIndexOf("t");
		for(int i=tech1+1; i <= tech2; i++){
			AnswerForm[i] = 't';
		}		
		// 將Volume 區域合併
		/*int Volume1 = temp.indexOf("V");
		int Volume2 = temp.lastIndexOf("V");
		for(int i=Volume1+1; i <= Volume2; i++){
			AnswerForm[i] = 'V';
		}		*/
		// 將Issue 區域合併
		/*int Issue1 = temp.indexOf("W");
		int Issue2 = temp.lastIndexOf("W");
		for(int i=Issue1+1; i <= Issue2; i++){
			AnswerForm[i] = 'W';
		}		*/
		// 將Page 區域合併
		/*int Page1 = temp.indexOf("P");
		int Page2 = temp.lastIndexOf("P");
		for(int i=Page1+1; i <= Page2; i++){
			AnswerForm[i] = 'P';
		}		*/
	}

	//處理AnswerForm 給TemplateGen呼叫
	public void TPreProcessing(){	
		// 產生StyleForm
		StyleForm = "";
		int count_A = 0;
		int count_T = 0;
		int count_L = 0;
		for(int i=0; i < AnswerForm.length; i++){
			if (AnswerForm[i] == 'A'){
				count_A = count_A + 1;
			} else if (AnswerForm[i] == 'T'){
				count_T = count_T + 1;
			} else if (AnswerForm[i] == 'j' || AnswerForm[i] == 'b' || AnswerForm[i] == 't'){
				count_L = count_L + 1;
			} else if (AnswerForm[i] != 'X' && count_A > 0) {
				StyleForm = StyleForm + "A" + AnswerForm[i];
				count_A = 0;
			} else if (AnswerForm[i] != 'X' && count_T > 0) {
				StyleForm = StyleForm + "T" + AnswerForm[i];
				count_T = 0;
			} else if (AnswerForm[i] != 'X' && count_L > 0) {
				StyleForm = StyleForm + "L" + AnswerForm[i];
				count_L = 0;
			} else if (AnswerForm[i] != 'X'){
				StyleForm = StyleForm + AnswerForm[i];
			}
		}	
		if (count_A > 0) {
			StyleForm = StyleForm + "A";
		} else if (count_T > 0){
			StyleForm = StyleForm + "T";
		} else if (count_L > 0){
			StyleForm = StyleForm + "L";
		}
		
		//修正Volume
		/*while(!(StyleForm.indexOf("VV") == -1 && StyleForm.indexOf("vv") == -1 &&  StyleForm.indexOf("ii") == -1 && StyleForm.indexOf("vi") == -1 && StyleForm.indexOf("vRi") == -1 && StyleForm.indexOf("vDi") == -1 && StyleForm.indexOf("vIiK") == -1)){
			StyleForm = StyleForm.replaceFirst("vv", "v");
			StyleForm = StyleForm.replaceFirst("ii", "i");
			StyleForm = StyleForm.replaceFirst("vi", "V");
			StyleForm = StyleForm.replaceFirst("vRi", "V");
			StyleForm = StyleForm.replaceFirst("vDi", "V");
			StyleForm = StyleForm.replaceFirst("vIiK", "V");
		}
		
		while(!(StyleForm.indexOf("v") == -1 && StyleForm.indexOf("i") == -1 &&  StyleForm.indexOf("VV") == -1)){
			StyleForm = StyleForm.replaceFirst("v", "V");
			StyleForm = StyleForm.replaceFirst("i", "V");
			StyleForm = StyleForm.replaceFirst("VV", "V");
		}*/
		while(!(StyleForm.indexOf("VDV") == -1 && StyleForm.indexOf("VCV") == -1 && StyleForm.indexOf("VHV") == -1 && StyleForm.indexOf("VV") == -1)){
			StyleForm = StyleForm.replaceFirst("VDV", "V");
			StyleForm = StyleForm.replaceFirst("VCV", "V");
			StyleForm = StyleForm.replaceFirst("VHV", "V");
			StyleForm = StyleForm.replaceFirst("VV", "V");
		}
		
		while(!(StyleForm.indexOf("WDW") == -1 && StyleForm.indexOf("WCW") == -1 && StyleForm.indexOf("WHW") == -1 && StyleForm.indexOf("WW") == -1)){
			StyleForm = StyleForm.replaceFirst("WDW", "W");
			StyleForm = StyleForm.replaceFirst("WCW", "W");
			StyleForm = StyleForm.replaceFirst("WHW", "W");
			StyleForm = StyleForm.replaceFirst("WW", "W");
		}
		
		
		//修正Page
		while(!(StyleForm.indexOf("PDP") == -1 && StyleForm.indexOf("PCP") == -1 && StyleForm.indexOf("PHP") == -1 && StyleForm.indexOf("PP") == -1)){
			StyleForm = StyleForm.replaceFirst("PDP", "P");
			StyleForm = StyleForm.replaceFirst("PCP", "P");
			StyleForm = StyleForm.replaceFirst("PHP", "P");
			StyleForm = StyleForm.replaceFirst("PP", "P");
		}
		
		
		//修正Month Year
		while(!(StyleForm.indexOf("YY") == -1 && StyleForm.indexOf("y") == -1 && StyleForm.indexOf("YRY") == -1 && StyleForm.indexOf("YDY") == -1 )){
			StyleForm = StyleForm.replaceFirst("y", "Y");
			StyleForm = StyleForm.replaceFirst("YY", "Y");
			StyleForm = StyleForm.replaceFirst("YRY", "Y");
			StyleForm = StyleForm.replaceFirst("YDY", "Y");
		}
		
		//修正隱藏欄位
		while(!(StyleForm.indexOf("FF") == -1 && StyleForm.indexOf("MM") == -1 && StyleForm.indexOf("SS") == -1)){
			StyleForm = StyleForm.replaceFirst("FF", "F");
			StyleForm = StyleForm.replaceFirst("MM", "M");
			StyleForm = StyleForm.replaceFirst("SS", "S");
		}
	}
	

	//將Month Year, Volume Issue分別取出, 判定venue
	public void PostProcessing(){
		if ( Align_Result !=null){
			AlignForm = Align_Result;
			while(!(AlignForm.indexOf("AA") == -1 && AlignForm.indexOf("TT") == -1 && AlignForm.indexOf("LL") == -1 && AlignForm.indexOf("VV") == -1  && AlignForm.indexOf("WW") == -1 && AlignForm.indexOf("PP") == -1 && AlignForm.indexOf("YY") == -1) ){
				Blocking_AlignForm("AA", "A");
				Blocking_AlignForm("TT", "T");
				Blocking_AlignForm("LL", "L");
				Blocking_AlignForm("VV", "V");
				Blocking_AlignForm("WW", "W");
				Blocking_AlignForm("PP", "P");
				Blocking_AlignForm("YY", "Y");
			}
			while(!(AlignForm.indexOf("LYL") == -1) ){
				Blocking_AlignForm("LYL", "L");
			}
		}
		char[] temp_align = new String(AlignForm).toCharArray();
		Result = new String(TokenForm).toCharArray();
		//System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXx");
		//System.out.print(AlignFormat);
		//System.out.println(temp_Result.length() + " " + temp_Result);
		//System.out.println(AlignForm.length() + " " + AlignForm);
		
		for(int i=0; i < AlignForm.length(); i++){
			if (AlignForm.charAt(i) == 'L'){
				boolean journal = false;
				boolean book = false;
				boolean thesis = false;
				boolean tech = false;
				if (i-1 >=0 && temp_align[i-1] == 'j'){
					temp_align[i] = 'j';
				} else if (i-1 >=0 && temp_align[i-1] == 'b'){
					temp_align[i] = 'b';
				} else if (i-1 >=0 && temp_align[i-1] == 't'){
					temp_align[i] = 't';
				} else {
					for(int j=Align_Start[i]; j <= Align_End[i]; j++){
						if (THESIS.contains(MyArray[j])){
							thesis = true;
						}
						if (TECH.contains(MyArray[j])|| MyArray[j].toUpperCase().compareTo("TECHNICAL") == 0 || MyArray[j].toUpperCase().compareTo("REPORT") == 0  ){
							tech = true;
						}
						if (BWORD.contains(MyArray[j])){
							book = true;
						}
						if (JWORD.contains(MyArray[j])){
							journal = true;
						}
					}
					if (journal){
						temp_align[i] = 'j';
					} else if (book){
						temp_align[i] = 'b';
					} else if (tech || thesis){
						temp_align[i] = 't';
					} else {
						temp_align[i] = 'j';
					} 
				}
			}
		}
		//\\// 由temp_align 填滿Result
		if (temp_align.length > 1  ){
			for(int i=0; i < temp_align.length; i++){
				for(int j=Align_Start[i]; j <= Align_End[i]; j++ ){
					Result[j] = temp_align[i]; 
					// Assign block weight
					if ((Align_End[i] - Align_Start[i]) >=1 && (Align_End[i] - Align_Start[i]) <= 3){
						Result_block[j] = 1;
					} else if ((Align_End[i] - Align_Start[i]) >3){
						Result_block[j] = 2;
					} else{
						Result_block[j] = 0;
					}
				}
			}		
		}
		//\\//
		Vector<String> temp_year = new Vector<String>();
		for(int i=0; i < MyArray.length; i++){
			if (Result[i] == 'Y'){
				if (MONTH.contains(MyArray[i])){
					MonthAnswer = MonthAnswer + " " + MyArray[i];
					//Result[i] = 'm';
				} else {
					try{
						int temp_value = Integer.parseInt(MyArray[i]);
						if (temp_value >= 1900 && temp_value <= 2010 ){
							if (!temp_year.contains(MyArray[i])){
								YearAnswer = YearAnswer + " " + MyArray[i];
								temp_year.add(MyArray[i]);
							}
							//Result[i] = 'y';
						} else {
							//Result[i] = 'Y';
						}
					} catch(Exception e1){
						//考慮結尾為英文字母的可能
						try{
							int temp_value = Integer.parseInt(MyArray[i].substring(0,3));
							//Result[i] = 'y';
							if (!temp_year.contains(MyArray[i])){
								YearAnswer = YearAnswer + " " + MyArray[i];
								temp_year.add(MyArray[i]);
							}
						} catch(Exception e2){
							//Result[i] = 'X';
						}
					}
				}
			}
			
		}
		
	}
	
	public void CallBlast(String identifier, String TPDB){
		// 產生input檔
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter("c:\\BibPro\\UPDATE\\tmp\\" + identifier + ".txt"));
			out.write(">" + identifier + "\n");
			out.write(IndexForm);
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		// 執行blast
		String[] cmds = {"bash", "c:\\BibPro\\UPDATE\\TemplateDB\\BibPro.sh", identifier, TPDB};
		try{			
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(cmds);
			proc.waitFor();
			proc.destroy();
		}catch(Exception e){
			e.printStackTrace();
		}
		//讀取output檔
		try{
			BufferedReader Readfile = new BufferedReader(new FileReader("c:\\BibPro\\UPDATE\\tmp\\" + identifier + ".out"));
			String temp;
			while(Readfile.ready()){
				temp = Readfile.readLine();
				if (temp.compareTo("Sequences producing significant alignments:                      (bits) Value") == 0){
					Readfile.readLine();
					int judge_score = 0;
					temp = Readfile.readLine();
					while( temp.length()> 0){
						StringTokenizer ST = new StringTokenizer(temp);
						String temp_template = ST.nextToken();
						int temp_score = Integer.parseInt(ST.nextToken());
						if (temp_score > judge_score && StyleForm_Candidate.size() <= 10){
							StyleForm_Candidate.add(temp_template);
							judge_score = temp_score;
						} else if (temp_score >= judge_score-1 && temp_score <= judge_score && StyleForm_Candidate.size() <= 10){
							StyleForm_Candidate.add(temp_template);
						} else if (StyleForm_Candidate.size() <= 5){
							StyleForm_Candidate.add(temp_template);
						}
						temp = Readfile.readLine();
					}
					break;
				}
			/*	if ( temp.length() > 0 && temp.charAt(0) == '>'){
					//System.out.println(temp);
					StyleForm_Candidate.add(temp.substring(1));
					// 取最高分的10各template
					if (StyleForm_Candidate.size() > 1){
						break;
					}
				}*/
			}	
			Readfile.close();
		}catch(Exception e){
			StyleForm = "";
			e.printStackTrace();
		}
		//選出template 包函 T 的為優先考慮
		/*for(int i=0; i < StyleForm_Candidate.size(); i++){
			if (StyleForm_Candidate.elementAt(i).indexOf("T") != -1){
				StyleForm = StyleForm_Candidate.elementAt(i);
				break;
			}
		}
		if (StyleForm == "" && StyleForm_Candidate.size() > 0 ){
			StyleForm = StyleForm_Candidate.elementAt(0);
		}*/
	}
	
	public String Alignment(){
		String AlignFormat ="";
		try{
			String m = "new-define";						// scoring matrix id or file name user-defined scoring matrix
			float o = 1;	// open gap penalty
			float e = (float)0.1;	// extend gap penalty			
			/*if (StyleForm.compareTo("") == 0){
				StyleForm = "X";
			}*/
			Sequence s1;
			Sequence temp_s1;
			Sequence s2 = SequenceParser.parse(AlignForm);
			float SCORE = 0;
			Matrix matrix = MatrixLoader.load(m);
			if (StyleForm_Candidate.size() == 0 && StyleForm == ""){
				StyleForm = "X";
			} else {
				Alignment temp_alignment;
				for(int i=0; i < StyleForm_Candidate.size(); i++){
					temp_s1 = SequenceParser.parse(StyleForm_Candidate.elementAt(i));
					temp_alignment = SmithWatermanGotoh.align (temp_s1, s2, matrix, o, e);
					if (temp_alignment.getScore() > SCORE){
						StyleForm = StyleForm_Candidate.elementAt(i);
						SCORE = temp_alignment.getScore();
					}
				}
			}
			s1 = SequenceParser.parse(StyleForm);
			Alignment alignment = SmithWatermanGotoh.align (s1, s2, matrix, o, e);
			/*System.out.println("STYLE:" + StyleForm);
			System.out.println("ALIGN:" + AlignForm);
			System.out.println(alignment.getScore());
			System.out.println("1: " + new String(alignment.getSequence1()));
			System.out.println("2: " + new String(alignment.getSequence2()));*/
			AlignFormat =new String(new Pair().format(alignment));
			//System.out.println( AlignFormat );
			Align_Result = do_align(alignment, s2);
		}catch(Exception e){
			e.printStackTrace();
		}
		return AlignFormat;
	}
	
	public void Alignment(String STYLE){
		try{
			String m = "IDENTITY";						// scoring matrix id or file name user-defined scoring matrix
			float o = 1;	// open gap penalty
			float e = (float)0.1;	// extend gap penalty			
			/*if (StyleForm.compareTo("") == 0){
				StyleForm = "X";
			}*/
			Sequence s1;
			Sequence temp_s1;
			Sequence s2 = SequenceParser.parse(new String(AlignForm));
			float SCORE = 0;
			Matrix matrix = MatrixLoader.load(m);
			StyleForm = STYLE;
			s1 = SequenceParser.parse(StyleForm);
			Alignment alignment = SmithWatermanGotoh.align (s1, s2, matrix, o, e);
			//System.out.println(alignment.getScore());
			//System.out.print (new Pair().format(alignment));
			//System.out.println(do_align(alignment, s2));
			Align_Result = do_align(alignment, s2);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Process the alignment for citation
	 */
	public String do_align(Alignment alignment, Sequence s2) {
		char[] extend_form = new String(alignment.getSequence2()).toCharArray() ;
		char[] temp_s2 = new String(alignment.getSequence2()).toCharArray() ;
		// 將T轉回B
		for(int i=0; i < extend_form.length; i++){
			if (extend_form[i] == 'T'){
				extend_form[i] = 'B';
			}
		}
		String ORIGINAL = new String(s2.getSequence());
		ORIGINAL = ORIGINAL.replaceAll("T", "B");
		// 填滿A,T,L 從前到後
		char change_black = 'X';
		for( int i = 0; i < alignment.getSequence1().length; i++ ){
			if (alignment.getSequence1()[i] == 'A'){
				change_black = 'A';
				if (extend_form[i] == 'B' || extend_form[i] == 'X'){
					extend_form[i] = 'A';
				}
			} else if (alignment.getSequence1()[i] == 'T'){
				change_black = 'T';
				if (extend_form[i] == 'B' || extend_form[i] == 'X' || extend_form[i] == 'N'){
					extend_form[i] = 'T';
				}
			} else if (alignment.getSequence1()[i] == 'L'){
				change_black = 'L';
				if (extend_form[i] == 'B' || extend_form[i] == 'X' || extend_form[i] == 'N'|| extend_form[i] == 'A'){
					extend_form[i] = 'L';
				}
			} else if (alignment.getSequence1()[i] == 'V'){
				change_black = 'V';
				if ( extend_form[i] == 'N'){
					extend_form[i] = 'V';	
				}
			} else if (alignment.getSequence1()[i] == '-' ){
				if ((change_black == 'A') && (temp_s2[i] != 'V' && temp_s2[i] != 'P' && temp_s2[i] != 'F' && temp_s2[i] != 'W' && temp_s2[i] != 'S' && temp_s2[i] != 'M'&& (temp_s2[i] != 'Y' || (Align_End[i] - Align_Start[i] == 0 && (MyArray[Align_Start[i]].compareTo("Jan") == 0 || MyArray[Align_Start[i]].compareTo("May") == 0 || MyArray[Align_Start[i]].compareTo("June") == 0 || MyArray[Align_Start[i]].compareTo("Augest") == 0)) ))){
					extend_form[i] = change_black;
				} else if ((change_black == 'L' || change_black == 'T') && (temp_s2[i] != 'V' && temp_s2[i] != 'P' && temp_s2[i] != 'F' && temp_s2[i] != 'W' && temp_s2[i] != 'S' && temp_s2[i] != 'M')){
					extend_form[i] = change_black;
				} else if (change_black == 'V' && (temp_s2[i] == 'N' || temp_s2[i] == 'R' || temp_s2[i] == 'D')){
					extend_form[i] = change_black;
				}
			} else{
				change_black = 'X';
				if (alignment.getSequence1()[i] == 'V'){
					if (extend_form[i] == 'N'){
						extend_form[i] = alignment.getSequence1()[i];
					}
				} else if (alignment.getSequence1()[i] == 'W'){
					if (extend_form[i] == 'N'){
						extend_form[i] = alignment.getSequence1()[i];
					}
				} else if (alignment.getSequence1()[i] == 'P'){
					if (extend_form[i] == 'N'){
						extend_form[i] = alignment.getSequence1()[i];
					}
				} else if ( alignment.getSequence1()[i] == 'Y'){
					if (extend_form[i] == 'N'){
						extend_form[i] = alignment.getSequence1()[i];
					}
				}
			}
		}
		// 填滿A,T,L 從後到前
		change_black = 'X';
		for( int i = alignment.getSequence1().length - 1; i >= 0; i-- ){
			if (alignment.getSequence1()[i] == 'A'){
				change_black = 'A';
				if (extend_form[i] == 'B' || extend_form[i] == 'X'){
					extend_form[i] = 'A';
				}
			} else if (alignment.getSequence1()[i] == 'T'){
				change_black = 'T';
				if (extend_form[i] == 'B' || extend_form[i] == 'X' || extend_form[i] == 'N'){
					extend_form[i] = 'T';
				}
			} else if (alignment.getSequence1()[i] == 'L'){
				change_black = 'L';
				if (extend_form[i] == 'B' || extend_form[i] == 'X' || extend_form[i] == 'N'){
					extend_form[i] = 'L';
				}
			} else if (alignment.getSequence1()[i] == 'V'){
				change_black = 'V';
				if ( extend_form[i] == 'N'){
					extend_form[i] = 'V';
				}
			} else if (alignment.getSequence1()[i] == '-' ){
				if ((change_black == 'A') && (temp_s2[i] != 'V' && temp_s2[i] != 'P' && temp_s2[i] != 'F' && temp_s2[i] != 'W' && temp_s2[i] != 'S' && temp_s2[i] != 'M'&& (temp_s2[i] != 'Y' || (Align_End[i] - Align_Start[i] == 0 && (MyArray[Align_Start[i]].compareTo("Jan") == 0 || MyArray[Align_Start[i]].compareTo("May") == 0 || MyArray[Align_Start[i]].compareTo("June") == 0 || MyArray[Align_Start[i]].compareTo("Augest") == 0)) ))){
					extend_form[i] = change_black;
				} else if ((change_black == 'L' || change_black == 'T') && (temp_s2[i] != 'V' && temp_s2[i] != 'P' && temp_s2[i] != 'F' && temp_s2[i] != 'W' && temp_s2[i] != 'S' && temp_s2[i] != 'M')){
					extend_form[i] = change_black;
				} else if (change_black == 'V' && (temp_s2[i] == 'N' || temp_s2[i] == 'R' || temp_s2[i] == 'D')){
					extend_form[i] = change_black;
				}
			} else{
				change_black = 'X';
			}		
		}
		// 去除original中 因alignment 所產生的空白
		String temp = "";
		for (int i = 0; i < alignment.getSequence2().length; i++ ){
			if (alignment.getSequence2()[i] != '-') {
				temp = temp + extend_form[i];
			}
		}
		// 延伸之前可能因alignment 所去掉的部分
		String extend = "";
		int extend_count = alignment.getStart2();
		if ( extend_count > 0 ) {
			for (int i = 0; i < extend_count; i++ ) {
				extend = extend +  extend_form[0];
			}
		}
		extend = extend + temp;
		// 延伸之後可能因alignment 所去掉的部分
		if (extend.length() < ORIGINAL.length()){
			char[] tail = new String(ORIGINAL.substring(extend.length())).toCharArray();
			change_black = extend.charAt(extend.length()-1);
			for(int i=0; i < tail.length; i++ ){
				if ((change_black == 'A' || change_black == 'T' || change_black == 'L') && (tail[i] != 'Y' && tail[i] != 'V' && tail[i] != 'P' && tail[i] != 'F' && tail[i] != 'W' && tail[i] != 'S' && tail[i] != 'M')){
					tail[i] = change_black;
				} else if (change_black == 'V' && (tail[i] == 'N' || tail[i] == 'R' || tail[i] == 'D' || tail[i] == 'L')){
					tail[i] = change_black;
				}
				extend = extend + tail[i];
			}
			//extend = extend + temp;
		}
		return extend;
	}

	public void AssignAnswer(){
		// Assign Answer
		for(int i=0; i < Result.length; i++){
			switch(Result[i]){
				case 'A':
					if (MyArray[i].compareTo(".") == 0 || MyArray[i].compareTo("-") == 0 || MyArray[i].compareTo(",") == 0){
						AuthorAnswer = AuthorAnswer + MyArray[i];	
					} else {
						AuthorAnswer = AuthorAnswer + " " + MyArray[i];
					}			
					break;
				case 'T':
					if (MyArray[i].compareTo(".") == 0 || MyArray[i].compareTo("-") == 0 || MyArray[i].compareTo(",") == 0 || MyArray[i].compareTo("-") == 0 || MyArray[i].compareTo("/") == 0){
						TitleAnswer = TitleAnswer + MyArray[i];	
					} else {
						TitleAnswer = TitleAnswer + " " + MyArray[i];
					}			
					break;
				case 'j':
					if (MyArray[i].compareTo(".") == 0 || MyArray[i].compareTo("-") == 0 || MyArray[i].compareTo(",") == 0 || MyArray[i].compareTo("-") == 0 || MyArray[i].compareTo("/") == 0){
						JournalAnswer = JournalAnswer + MyArray[i];	
						VenueAnswer = VenueAnswer + MyArray[i];
					} else {
						JournalAnswer = JournalAnswer + " " + MyArray[i];
						VenueAnswer = VenueAnswer + " " + MyArray[i];
					}						
					break;
				case 'b':
					if (MyArray[i].compareTo(".") == 0 || MyArray[i].compareTo("-") == 0 || MyArray[i].compareTo(",") == 0 || MyArray[i].compareTo("-") == 0 || MyArray[i].compareTo("/") == 0){
						BookAnswer = BookAnswer + MyArray[i];	
						VenueAnswer = VenueAnswer + MyArray[i];
					} else {
						BookAnswer = BookAnswer + " " + MyArray[i];
						VenueAnswer = VenueAnswer + " " + MyArray[i];
						
					}						
					break;
				case 't':
					if (MyArray[i].compareTo(".") == 0 || MyArray[i].compareTo("-") == 0 || MyArray[i].compareTo(",") == 0 || MyArray[i].compareTo("-") == 0 || MyArray[i].compareTo("/") == 0){
						TechAnswer = TechAnswer + MyArray[i];	
						VenueAnswer = VenueAnswer + MyArray[i];
					} else {
						TechAnswer = TechAnswer + " " + MyArray[i];
						VenueAnswer = VenueAnswer + " " + MyArray[i];
					}						
					break;
				case 'V':
					if (!SYMBOL.contains(MyArray[i]) && !VOLUMN.contains(MyArray[i])) {
						if (VolumnAnswer.compareTo("") == 0){
							VolumnAnswer = MyArray[i];
						} else { 
							VolumnAnswer = VolumnAnswer + " " + MyArray[i];
						}
					}
					break;
				case 'W':
					if (!SYMBOL.contains(MyArray[i]) && !NUMBER.contains(MyArray[i])) {
						if (NumberAnswer.compareTo("") == 0){
							NumberAnswer = MyArray[i];
						} else { 
							NumberAnswer = NumberAnswer + "-" + MyArray[i];
						}
					}
					break;
				case 'P':
					if (!SYMBOL.contains(MyArray[i]) && !PAGE.contains(MyArray[i])) {
						if (PageAnswer.compareTo("") == 0){
							PageAnswer = MyArray[i];
						} else{ 
							PageAnswer = PageAnswer + "-" + MyArray[i];
						}
					}
					break;
				case 'Y':
					if (DateAnswer.compareTo("") == 0){
						DateAnswer = MyArray[i];
					} else { 
						DateAnswer = DateAnswer + " " + MyArray[i];
					}
					break;
				case 'F':
					if (MyArray[i].compareTo(".") == 0 || MyArray[i].compareTo("-") == 0 || MyArray[i].compareTo(",") == 0){
						EditorAnswer = EditorAnswer + MyArray[i];	
					} else {
						EditorAnswer = EditorAnswer + " " + MyArray[i];
					}			
					break;
				case 'M':
					if (MyArray[i].compareTo(".") == 0 || MyArray[i].compareTo("-") == 0 || MyArray[i].compareTo(",") == 0){
						PublisherAnswer = PublisherAnswer + MyArray[i];	
					} else {
						PublisherAnswer = PublisherAnswer + " " + MyArray[i];
					}			
					break;
				case 'S':
					if (MyArray[i].compareTo(".") == 0 || MyArray[i].compareTo("-") == 0 || MyArray[i].compareTo(",") == 0){
						InstitueAnswer = InstitueAnswer + MyArray[i];	
					} else {
						InstitueAnswer = InstitueAnswer + " " + MyArray[i];
					}			
					break;
				case 'N':
					if (NOTE.compareTo("") == 0){
						NOTE = MyArray[i];
					} else { 
						NOTE = NOTE + " " + MyArray[i];
					}
					break;
				case 'X':
					if (NOTE.compareTo("") == 0){
						NOTE = MyArray[i];
					} else { 
						NOTE = NOTE + " " + MyArray[i];
					}
					break;
				case 'B':
					if (NOTE.compareTo("") == 0){
						NOTE = MyArray[i];
					} else { 
						NOTE = NOTE + " " + MyArray[i];
					}
					break;
				default: // 標點符號
					break;
			}
		}
	}
	
	public void ReadAuthor(String input){
		BufferedReader Readfile;
		try{
			Readfile = new BufferedReader(new FileReader(input));
			String temp; 
			while(Readfile.ready()){
				temp = Readfile.readLine();
				AUTHOR.add(temp.substring(0,1).toUpperCase() + temp.substring(1).toLowerCase());
				//System.out.println(temp.substring(0,1).toUpperCase() + temp.substring(1).toLowerCase());
			}	
			Readfile.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void ReadJournal(String input){
		BufferedReader Readfile;
		try{
			Readfile = new BufferedReader(new FileReader(input)); 
			while(Readfile.ready()){
				JOURNAL.add(Readfile.readLine());
			}	
			Readfile.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//用來產生BLOCK RULE 跟 SCORE MATRIX
	public void TOKEN_ANS(){
		for(int i=0 ; i < TokenForm.length; i++){
			if ( TokenForm[i] == 'b' || TokenForm[i] == 'j' || TokenForm[i] == 't')
				TokenForm[i] = 'L';
			if ( AnswerForm[i] == 'b' || AnswerForm[i] == 'j' || AnswerForm[i] == 't')
				AnswerForm[i] = 'L';
			if ( TokenForm[i] == 'y')
				TokenForm[i] = 'Y';
			if ( AnswerForm[i] == 'y')
				AnswerForm[i] = 'Y';
			if ( i >= 1 && (TokenForm[i-1] == 'X' || TokenForm[i-1] == 'B') && TokenForm[i] == 'X'){
				TokenForm[i-1] = 'B';
				TokenForm[i] = 'B';
			}
		}
		System.out.println("TOKENFORM : " + new String(TokenForm));
		System.out.println("ANSWERFORM: " + new String(AnswerForm));
	}
	public void OutPut(){
		System.out.println("Original:" + Citation);
		System.out.println("MyArray:");
		for (int i=0; i < MyArray.length; i++){
			//System.out.println(i + " -[" + MyArray[i] + "]-[" + TokenForm[i] + "]");
			System.out.print("[" + MyArray[i] + "]");
		}
		System.out.println("");
		if (AnswerForm != null && AlignForm != null) {
			for (int i=0; i < MyArray.length; i++){
				//System.out.println(i + " -[" + MyArray[i] + "]-[" + TokenForm[i] + "]");
				System.out.print("[" + TokenForm[i]);
				for (int j=0; j < MyArray[i].length() - 1; j++){
					System.out.print(" ");
				}
				System.out.print("]");
			}
			System.out.println("");
			for (int i=0; i < MyArray.length; i++){
				//System.out.println(i + " -[" + MyArray[i] + "]-[" + TokenForm[i] + "]");
				System.out.print("[" + AnswerForm[i]);
				for (int j=0; j < MyArray[i].length() - 1; j++){
					System.out.print(" ");
				}
				System.out.print("]");
			}
			System.out.println("");
			
			//System.out.println("INDEXFORM: " + AlignForm);
			System.out.print("INDEX----: ");
			for(int i=0; i<AlignForm.length(); i++){
				System.out.print("[" + Align_Start[i] + "]");
			}
			System.out.println("");
			System.out.print("INDEX----: ");
			for(int i=0; i<AlignForm.length(); i++){
				System.out.print("[" + Align_End[i] + "]");
			}
			System.out.println("");
			System.out.println("INDEXFORM : " + IndexForm);
			System.out.println("ALIGNFORM : " + AlignForm);
			if (StyleForm.compareTo("") == 0 ){
				System.out.println("Nothing Match!!");
				System.out.println("----------------------------------------------------------\n");
			} else {
				System.out.println("STYLEFORM : " + StyleForm);
				System.out.println("TOKENFORM : " + new String(TokenForm));
				System.out.println("ALIGNMENT : " + new String(Result));
				System.out.println("ANSWERFORM: " + new String(AnswerForm));
				System.out.println("Author: " + AuthorAnswer);
				System.out.println("Title: " + TitleAnswer);
				System.out.println("Journal: " + JournalAnswer);
				System.out.println("BookTitle: " + BookAnswer);
				System.out.println("TechTitle: " + TechAnswer);
				System.out.println("Volumn: " + VolumnAnswer);
				System.out.println("Issue: " + NumberAnswer);
				System.out.println("Page: " + PageAnswer);
				System.out.println("Date: " + DateAnswer);
				System.out.println("----------------------------------------------------------\n");
			}
		} else if (AlignForm != null){
			for (int i=0; i < MyArray.length; i++){
				//System.out.println(i + " -[" + MyArray[i] + "]-[" + TokenForm[i] + "]");
				System.out.print("[" + TokenForm[i]);
				for (int j=0; j < MyArray[i].length() - 1; j++){
					System.out.print(" ");
				}
				System.out.print("]");
			}
			System.out.println("");
			System.out.print("INDEX----: ");
			for(int i=0; i<AlignForm.length(); i++){
				System.out.print("[" + Align_Start[i] + "]");
			}
			System.out.println("");
			System.out.print("INDEX----: ");
			for(int i=0; i<AlignForm.length(); i++){
				System.out.print("[" + Align_End[i] + "]");
			}
			System.out.println("");
			System.out.println("INDEXFORM : " + IndexForm);
			if (StyleForm.compareTo("") == 0 ){
				System.out.println("Nothing Match!!");
				System.out.println("----------------------------------------------------------\n");
			} else {
				System.out.println("STYLEFORM : " + StyleForm);
				System.out.println("TOKENFORM : " + new String(TokenForm));
				System.out.println("ALIGNMENT : " + new String(Result));
				System.out.println("Author: " + AuthorAnswer);
				System.out.println("Title: " + TitleAnswer);
				System.out.println("Journal: " + JournalAnswer);
				System.out.println("BookTitle: " + BookAnswer);
				System.out.println("TechTitle: " + TechAnswer);
				System.out.println("Volumn: " + VolumnAnswer);
				System.out.println("Issue: " + NumberAnswer);
				System.out.println("Page: " + PageAnswer);
				System.out.println("Date: " + DateAnswer);
				System.out.println("----------------------------------------------------------\n");
			}
		}
	}
	
}
