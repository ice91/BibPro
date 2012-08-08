import java.io.*;
import java.util.*;
//for regular expression
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ALIGNMENT{
	public ALIGNMENT(String Token, String Answer){
		TokenForm = new char[Token.length()];
		AnswerForm = new char[Answer.length()];
		for(int i=0; i < Token.length(); i++){
			TokenForm[i] = Token.charAt(i);
		}
		for(int i=0; i < Answer.length(); i++){
			AnswerForm[i] = Answer.charAt(i);
		}
	}
	public char[] TokenForm;
	public char[] AnswerForm;
	// A  R  N  D  C  Q  E  G  H  I  L  K  M  F  P  S  T  W  Y  V  B  Z  X
	public int[][] ScoreM = new int[23][23];
	
	public String ExtractSymbol(char Symbol){
		String temp = "";
		for(int i=0; i < AnswerForm.length; i++){
			if (AnswerForm[i] == Symbol){
				if (i > 1 && TokenForm[i-1] == 'B' && TokenForm[i] == 'B'){
				} else {
					temp = temp + TokenForm[i];
				}
			}
		}
		return temp;
	}
	
	public Vector<String> Regex_Processing(String term, String block_template){
		Vector<String> result = new Vector<String>();
		String inputStr = term;
		String temp="";
		//System.out.println("**" + inputStr);
		Pattern pattern = Pattern.compile(block_template);
		Matcher matcher = pattern.matcher(inputStr);
		boolean matchFound = matcher.find();
    while(matchFound) {
    	//System.out.println(matcher.start() + "-" + matcher.end());
    	temp = matcher.group();
    	result.add(temp);
    	//System.out.println("--" + temp);
    	if(matcher.end() + 1 <= inputStr.length()) {
        matchFound = matcher.find(matcher.end());
      }else{
        break;
      }
    }
    return result;
  }
  
  public Vector<String> A_rule1(){
  	String temp = ExtractSymbol('A');
  	return Regex_Processing(temp, "A[^LVWPYSF]+A");
  }
  
  public Vector<String> A_rule2(){
  	String temp = ExtractSymbol('A');
  	return Regex_Processing(temp, "A[^LVWPYSF]*[BX]");
  }
  
  public Vector<String> A_rule3(){
  	String temp = ExtractSymbol('A');
  	return Regex_Processing(temp, "[BX][^LVWPYSF]*A");
  }
  
  public Vector<String> L_rule1(){
  	String temp = ExtractSymbol('L');
  	return Regex_Processing(temp, "L[^AVWPYSF]+L");
  }
  
  public Vector<String> L_rule2(){
  	String temp = ExtractSymbol('L');
  	return Regex_Processing(temp, "L[^AVWPYSF]*[BX]");
  }
  
   public Vector<String> L_rule3(){
  	String temp = ExtractSymbol('L');
  	return Regex_Processing(temp, "[BX][^AVWPYSF]*L");
  }
  
  public Vector<String> V_rule(){
  	String temp = ExtractSymbol('V');
  	return Regex_Processing(temp, "V[^ALWPYSF]*N");
  }
  public Vector<String> W_rule(){
  	String temp = ExtractSymbol('W');
  	return Regex_Processing(temp, "W[^ALVPYSF]*N");
  }
  public Vector<String> P_rule(){
  	String temp = ExtractSymbol('P');
  	return Regex_Processing(temp, "P[^ALVWYSF]*N");
  }
  
	
	public void ComputeMatrix(){
		//initialize
		for(int i=0; i < 23; i++){
			for(int j=0; j < 23; j++){
				ScoreM[i][j] = 0;
			}
		}
		// scan整各alignment
		for(int i=0; i < TokenForm.length; i++){
			//計算每各符號的各數 紀錄matrix對角線的值
			// SYMBOL A
			if (TokenForm[i] == 'A'){
				ScoreM[0][0] = ScoreM[0][0] + 1;
			}
			if (AnswerForm[i] == 'A' ){
				ScoreM[0][0] = ScoreM[0][0] + 1;
			}
			// SYMBOL R
			if (TokenForm[i] == 'R'){
				ScoreM[1][1] = ScoreM[1][1] + 1;
			}
			if (AnswerForm[i] == 'R' ){
				ScoreM[1][1] = ScoreM[1][1] + 1;
			}
			// SYMBOL N
			if (TokenForm[i] == 'N'){
				ScoreM[2][2] = ScoreM[2][2] + 1;
			}
			if (AnswerForm[i] == 'N' ){
				ScoreM[2][2] = ScoreM[2][2] + 1;
			}
			// SYMBOL D
			if (TokenForm[i] == 'D'){
				ScoreM[3][3] = ScoreM[3][3] + 1;
			}
			if (AnswerForm[i] == 'D' ){
				ScoreM[3][3] = ScoreM[3][3] + 1;
			}
			// SYMBOL C
			if (TokenForm[i] == 'C'){
				ScoreM[4][4] = ScoreM[4][4] + 1;
			}
			if (AnswerForm[i] == 'C' ){
				ScoreM[4][4] = ScoreM[4][4] + 1;
			}
			// SYMBOL Q
			if (TokenForm[i] == 'Q'){
				ScoreM[5][5] = ScoreM[5][5] + 1;   // A  R  N  D  C  Q  E  G  H  I  L  K  M  F  P  S  T  W  Y  V  B  Z  X
			}
			if (AnswerForm[i] == 'Q' ){
				ScoreM[5][5] = ScoreM[5][5] + 1;
			}
			// SYMBOL E
			if (TokenForm[i] == 'E'){
				ScoreM[6][6] = ScoreM[6][6] + 1;
			}
			if (AnswerForm[i] == 'E' ){
				ScoreM[6][6] = ScoreM[6][6] + 1;
			}
			// SYMBOL G
			if (TokenForm[i] == 'G'){
				ScoreM[7][7] = ScoreM[7][7] + 1;
			}
			if (AnswerForm[i] == 'G' ){
				ScoreM[7][7] = ScoreM[7][7] + 1;
			}
			// SYMBOL H
			if (TokenForm[i] == 'H'){
				ScoreM[8][8] = ScoreM[8][8] + 1;
			}
			if (AnswerForm[i] == 'H' ){
				ScoreM[8][8] = ScoreM[8][8] + 1;
			}
			// SYMBOL I
			if (TokenForm[i] == 'I'){
				ScoreM[9][9] = ScoreM[9][9] + 1;
			}
			if (AnswerForm[i] == 'I' ){
				ScoreM[9][9] = ScoreM[9][9] + 1;
			}
			// SYMBOL L
			if (TokenForm[i] == 'L'){
				ScoreM[10][10] = ScoreM[10][10] + 1;
			}
			if (AnswerForm[i] == 'L' ){
				ScoreM[10][10] = ScoreM[10][10] + 1;
			}
			// SYMBOL K
			if (TokenForm[i] == 'K'){
				ScoreM[11][11] = ScoreM[11][11] + 1;
			}
			if (AnswerForm[i] == 'K' ){
				ScoreM[11][11] = ScoreM[11][11] + 1;
			}
			// SYMBOL M
			if (TokenForm[i] == 'M'){
				ScoreM[12][12] = ScoreM[12][12] + 1;
			}
			if (AnswerForm[i] == 'M' ){
				ScoreM[12][12] = ScoreM[12][12] + 1;   // A  R  N  D  C  Q  E  G  H  I  L  K  M  F  P  S  T  W  Y  V  B  Z  X
			}
			// SYMBOL F
			if (TokenForm[i] == 'F'){
				ScoreM[13][13] = ScoreM[13][13] + 1;
			}
			if (AnswerForm[i] == 'F' ){
				ScoreM[13][13] = ScoreM[13][13] + 1;
			}
			// SYMBOL P
			if (TokenForm[i] == 'P'){
				ScoreM[14][14] = ScoreM[14][14] + 1;
			}
			if (AnswerForm[i] == 'P' ){
				ScoreM[14][14] = ScoreM[14][14] + 1;
			}
			// SYMBOL S
			if (TokenForm[i] == 'S'){
				ScoreM[15][15] = ScoreM[15][15] + 1;
			}
			if (AnswerForm[i] == 'S' ){
				ScoreM[15][15] = ScoreM[15][15] + 1;   // A  R  N  D  C  Q  E  G  H  I  L  K  M  F  P  S  T  W  Y  V  B  Z  X
			}
			// SYMBOL T
			if (TokenForm[i] == 'T'){
				ScoreM[16][16] = ScoreM[16][16] + 1;
			}
			if (AnswerForm[i] == 'T' ){
				ScoreM[16][16] = ScoreM[16][16] + 1;
			}
			// SYMBOL W
			if (TokenForm[i] == 'W'){
				ScoreM[17][17] = ScoreM[17][17] + 1;
			}
			if (AnswerForm[i] == 'W' ){
				ScoreM[17][17] = ScoreM[17][17] + 1;
			}
			// SYMBOL Y
			if (TokenForm[i] == 'Y'){
				ScoreM[18][18] = ScoreM[18][18] + 1;
			}
			if (AnswerForm[i] == 'Y' ){
				ScoreM[18][18] = ScoreM[18][18] + 1;   // A  R  N  D  C  Q  E  G  H  I  L  K  M  F  P  S  T  W  Y  V  B  Z  X
			}
			// SYMBOL V
			if (TokenForm[i] == 'V'){
				ScoreM[19][19] = ScoreM[19][19] + 1;
			}
			if (AnswerForm[i] == 'V' ){
				ScoreM[19][19] = ScoreM[19][19] + 1;
			}
			// SYMBOL B
			if (TokenForm[i] == 'B'){
				ScoreM[20][20] = ScoreM[20][20] + 1;
			}
			if (AnswerForm[i] == 'B' ){
				ScoreM[20][20] = ScoreM[20][20] + 1;
			}
			// SYMBOL Z
			if (TokenForm[i] == 'Z'){
				ScoreM[21][21] = ScoreM[21][21] + 1;
			}
			if (AnswerForm[i] == 'Z' ){
				ScoreM[21][21] = ScoreM[21][21] + 1;   // A  R  N  D  C  Q  E  G  H  I  L  K  M  F  P  S  T  W  Y  V  B  Z  X
			}
			// SYMBOL X
			if (TokenForm[i] == 'X'){
				ScoreM[22][22] = ScoreM[22][22] + 1;
			}
			if (AnswerForm[i] == 'X' ){
				ScoreM[22][22] = ScoreM[22][22] + 1;
			}
			
			// 計算其他部分產生變異的值
			addScore(i, 'A', 'R', 0, 1);
			addScore(i, 'A', 'N', 0, 2);
			addScore(i, 'A', 'D', 0, 3);
			addScore(i, 'A', 'C', 0, 4);
			addScore(i, 'A', 'Q', 0, 5);
			addScore(i, 'A', 'E', 0, 6);
			addScore(i, 'A', 'G', 0, 7);
			addScore(i, 'A', 'H', 0, 8);
			addScore(i, 'A', 'I', 0, 9);
			addScore(i, 'A', 'L', 0, 10);
			addScore(i, 'A', 'K', 0, 11);
			addScore(i, 'A', 'M', 0, 12);
			addScore(i, 'A', 'F', 0, 13);
			addScore(i, 'A', 'P', 0, 14);
			addScore(i, 'A', 'S', 0, 15);
			addScore(i, 'A', 'T', 0, 16);
			addScore(i, 'A', 'W', 0, 17);
			addScore(i, 'A', 'Y', 0, 18);
			addScore(i, 'A', 'V', 0, 19);
			addScore(i, 'A', 'B', 0, 20);
			addScore(i, 'A', 'Z', 0, 21);
			addScore(i, 'A', 'X', 0, 22);
			addScore(i, 'R', 'N', 1, 2);
			addScore(i, 'R', 'D', 1, 3);
			addScore(i, 'R', 'C', 1, 4);
			addScore(i, 'R', 'Q', 1, 5);
			addScore(i, 'R', 'E', 1, 6);
			addScore(i, 'R', 'G', 1, 7);
			addScore(i, 'R', 'H', 1, 8);
			addScore(i, 'R', 'I', 1, 9);
			addScore(i, 'R', 'L', 1, 10);
			addScore(i, 'R', 'K', 1, 11);
			addScore(i, 'R', 'M', 1, 12);
			addScore(i, 'R', 'F', 1, 13);
			addScore(i, 'R', 'P', 1, 14);
			addScore(i, 'R', 'S', 1, 15);
			addScore(i, 'R', 'T', 1, 16);
			addScore(i, 'R', 'W', 1, 17);
			addScore(i, 'R', 'Y', 1, 18);
			addScore(i, 'R', 'V', 1, 19);
			addScore(i, 'R', 'B', 1, 20);
			addScore(i, 'R', 'Z', 1, 21);
			addScore(i, 'R', 'X', 1, 22);
			addScore(i, 'N', 'D', 2, 3);
			addScore(i, 'N', 'C', 2, 4);
			addScore(i, 'N', 'Q', 2, 5);
			addScore(i, 'N', 'E', 2, 6);
			addScore(i, 'N', 'G', 2, 7);
			addScore(i, 'N', 'H', 2, 8);
			addScore(i, 'N', 'I', 2, 9);
			addScore(i, 'N', 'L', 2, 10);
			addScore(i, 'N', 'K', 2, 11);
			addScore(i, 'N', 'M', 2, 12);
			addScore(i, 'N', 'F', 2, 13);
			addScore(i, 'N', 'P', 2, 14);
			addScore(i, 'N', 'S', 2, 15);
			addScore(i, 'N', 'T', 2, 16);
			addScore(i, 'N', 'W', 2, 17);
			addScore(i, 'N', 'Y', 2, 18);
			addScore(i, 'N', 'V', 2, 19);
			addScore(i, 'N', 'B', 2, 20);
			addScore(i, 'N', 'Z', 2, 21);
			addScore(i, 'N', 'X', 2, 22);
			addScore(i, 'D', 'C', 3, 4);
			addScore(i, 'D', 'Q', 3, 5);
			addScore(i, 'D', 'E', 3, 6);
			addScore(i, 'D', 'G', 3, 7);
			addScore(i, 'D', 'H', 3, 8);
			addScore(i, 'D', 'I', 3, 9);
			addScore(i, 'D', 'L', 3, 10);
			addScore(i, 'D', 'K', 3, 11);
			addScore(i, 'D', 'M', 3, 12);
			addScore(i, 'D', 'F', 3, 13);
			addScore(i, 'D', 'P', 3, 14);
			addScore(i, 'D', 'S', 3, 15);
			addScore(i, 'D', 'T', 3, 16);
			addScore(i, 'D', 'W', 3, 17);
			addScore(i, 'D', 'Y', 3, 18);
			addScore(i, 'D', 'V', 3, 19);
			addScore(i, 'D', 'B', 3, 20);
			addScore(i, 'D', 'Z', 3, 21);
			addScore(i, 'D', 'X', 3, 22);
			addScore(i, 'C', 'Q', 4, 5);
			addScore(i, 'C', 'E', 4, 6);
			addScore(i, 'C', 'G', 4, 7);
			addScore(i, 'C', 'H', 4, 8);
			addScore(i, 'C', 'I', 4, 9);
			addScore(i, 'C', 'L', 4, 10);
			addScore(i, 'C', 'K', 4, 11);
			addScore(i, 'C', 'M', 4, 12);
			addScore(i, 'C', 'F', 4, 13);
			addScore(i, 'C', 'P', 4, 14);
			addScore(i, 'C', 'S', 4, 15);
			addScore(i, 'C', 'T', 4, 16);
			addScore(i, 'C', 'W', 4, 17);
			addScore(i, 'C', 'Y', 4, 18);
			addScore(i, 'C', 'V', 4, 19);
			addScore(i, 'C', 'B', 4, 20);
			addScore(i, 'C', 'Z', 4, 21);
			addScore(i, 'C', 'X', 4, 22);
			addScore(i, 'Q', 'E', 5, 6);
			addScore(i, 'Q', 'G', 5, 7);
			addScore(i, 'Q', 'H', 5, 8);
			addScore(i, 'Q', 'I', 5, 9);
			addScore(i, 'Q', 'L', 5, 10);
			addScore(i, 'Q', 'K', 5, 11);
			addScore(i, 'Q', 'M', 5, 12);
			addScore(i, 'Q', 'F', 5, 13);
			addScore(i, 'Q', 'P', 5, 14);
			addScore(i, 'Q', 'S', 5, 15);
			addScore(i, 'Q', 'T', 5, 16);
			addScore(i, 'Q', 'W', 5, 17);
			addScore(i, 'Q', 'Y', 5, 18);
			addScore(i, 'Q', 'V', 5, 19);
			addScore(i, 'Q', 'B', 5, 20);
			addScore(i, 'Q', 'Z', 5, 21);
			addScore(i, 'Q', 'X', 5, 22);
			addScore(i, 'E', 'G', 6, 7);
			addScore(i, 'E', 'H', 6, 8);
			addScore(i, 'E', 'I', 6, 9);
			addScore(i, 'E', 'L', 6, 10);
			addScore(i, 'E', 'K', 6, 11);
			addScore(i, 'E', 'M', 6, 12);
			addScore(i, 'E', 'F', 6, 13);
			addScore(i, 'E', 'P', 6, 14);
			addScore(i, 'E', 'S', 6, 15);
			addScore(i, 'E', 'T', 6, 16);
			addScore(i, 'E', 'W', 6, 17);
			addScore(i, 'E', 'Y', 6, 18);
			addScore(i, 'E', 'V', 6, 19);
			addScore(i, 'E', 'B', 6, 20);
			addScore(i, 'E', 'Z', 6, 21);
			addScore(i, 'E', 'X', 6, 22);
			addScore(i, 'G', 'H', 7, 8);
			addScore(i, 'G', 'I', 7, 9);
			addScore(i, 'G', 'L', 7, 10);
			addScore(i, 'G', 'K', 7, 11);
			addScore(i, 'G', 'M', 7, 12);
			addScore(i, 'G', 'F', 7, 13);
			addScore(i, 'G', 'P', 7, 14);
			addScore(i, 'G', 'S', 7, 15);
			addScore(i, 'G', 'T', 7, 16);
			addScore(i, 'G', 'W', 7, 17);
			addScore(i, 'G', 'Y', 7, 18);
			addScore(i, 'G', 'V', 7, 19);
			addScore(i, 'G', 'B', 7, 20);
			addScore(i, 'G', 'Z', 7, 21);
			addScore(i, 'G', 'X', 7, 22);
			addScore(i, 'H', 'I', 8, 9);
			addScore(i, 'H', 'L', 8, 10);
			addScore(i, 'H', 'K', 8, 11);
			addScore(i, 'H', 'M', 8, 12);
			addScore(i, 'H', 'F', 8, 13);
			addScore(i, 'H', 'P', 8, 14);
			addScore(i, 'H', 'S', 8, 15);
			addScore(i, 'H', 'T', 8, 16);
			addScore(i, 'H', 'W', 8, 17);
			addScore(i, 'H', 'Y', 8, 18);
			addScore(i, 'H', 'V', 8, 19);
			addScore(i, 'H', 'B', 8, 20);
			addScore(i, 'H', 'Z', 8, 21);
			addScore(i, 'H', 'X', 8, 22);
			addScore(i, 'I', 'L', 9, 10);
			addScore(i, 'I', 'K', 9, 11);
			addScore(i, 'I', 'M', 9, 12);
			addScore(i, 'I', 'F', 9, 13);
			addScore(i, 'I', 'P', 9, 14);
			addScore(i, 'I', 'S', 9, 15);
			addScore(i, 'I', 'T', 9, 16);
			addScore(i, 'I', 'W', 9, 17);
			addScore(i, 'I', 'Y', 9, 18);
			addScore(i, 'I', 'V', 9, 19);
			addScore(i, 'I', 'B', 9, 20);
			addScore(i, 'I', 'Z', 9, 21);
			addScore(i, 'I', 'X', 9, 22);
			addScore(i, 'L', 'K', 10, 11);
			addScore(i, 'L', 'M', 10, 12);
			addScore(i, 'L', 'F', 10, 13);
			addScore(i, 'L', 'P', 10, 14);
			addScore(i, 'L', 'S', 10, 15);
			addScore(i, 'L', 'T', 10, 16);
			addScore(i, 'L', 'W', 10, 17);
			addScore(i, 'L', 'Y', 10, 18);
			addScore(i, 'L', 'V', 10, 19);
			addScore(i, 'L', 'B', 10, 20);
			addScore(i, 'L', 'Z', 10, 21);
			addScore(i, 'L', 'X', 10, 22);
			addScore(i, 'K', 'M', 11, 12);
			addScore(i, 'K', 'F', 11, 13);
			addScore(i, 'K', 'P', 11, 14);
			addScore(i, 'K', 'S', 11, 15);
			addScore(i, 'K', 'T', 11, 16);
			addScore(i, 'K', 'W', 11, 17);
			addScore(i, 'K', 'Y', 11, 18);
			addScore(i, 'K', 'V', 11, 19);
			addScore(i, 'K', 'B', 11, 20);
			addScore(i, 'K', 'Z', 11, 21);
			addScore(i, 'K', 'X', 11, 22);
			addScore(i, 'M', 'F', 12, 13);
			addScore(i, 'M', 'P', 12, 14);
			addScore(i, 'M', 'S', 12, 15);
			addScore(i, 'M', 'T', 12, 16);
			addScore(i, 'M', 'W', 12, 17);
			addScore(i, 'M', 'Y', 12, 18);
			addScore(i, 'M', 'V', 12, 19);
			addScore(i, 'M', 'B', 12, 20);
			addScore(i, 'M', 'Z', 12, 21);
			addScore(i, 'M', 'X', 12, 22);
			addScore(i, 'F', 'P', 13, 14);
			addScore(i, 'F', 'S', 13, 15);
			addScore(i, 'F', 'T', 13, 16);
			addScore(i, 'F', 'W', 13, 17);
			addScore(i, 'F', 'Y', 13, 18);
			addScore(i, 'F', 'V', 13, 19);
			addScore(i, 'F', 'B', 13, 20);
			addScore(i, 'F', 'Z', 13, 21);
			addScore(i, 'F', 'X', 13, 22);
			addScore(i, 'P', 'S', 14, 15);
			addScore(i, 'P', 'T', 14, 16);
			addScore(i, 'P', 'W', 14, 17);
			addScore(i, 'P', 'Y', 14, 18);
			addScore(i, 'P', 'V', 14, 19);
			addScore(i, 'P', 'B', 14, 20);
			addScore(i, 'P', 'Z', 14, 21);
			addScore(i, 'P', 'X', 14, 22);
			addScore(i, 'S', 'T', 15, 16);
			addScore(i, 'S', 'W', 15, 17);
			addScore(i, 'S', 'Y', 15, 18);
			addScore(i, 'S', 'V', 15, 19);
			addScore(i, 'S', 'B', 15, 20);
			addScore(i, 'S', 'Z', 15, 21);
			addScore(i, 'S', 'X', 15, 22);
			addScore(i, 'T', 'W', 16, 17);
			addScore(i, 'T', 'Y', 16, 18);
			addScore(i, 'T', 'V', 16, 19);
			addScore(i, 'T', 'B', 16, 20);
			addScore(i, 'T', 'Z', 16, 21);
			addScore(i, 'T', 'X', 16, 22);
			addScore(i, 'W', 'Y', 17, 18);
			addScore(i, 'W', 'V', 17, 19);
			addScore(i, 'W', 'B', 17, 20);
			addScore(i, 'W', 'Z', 17, 21);
			addScore(i, 'W', 'X', 17, 22);
			addScore(i, 'Y', 'V', 18, 19);
			addScore(i, 'Y', 'B', 18, 20);
			addScore(i, 'Y', 'Z', 18, 21);
			addScore(i, 'Y', 'X', 18, 22);
			addScore(i, 'V', 'B', 19, 20);
			addScore(i, 'V', 'Z', 19, 21);
			addScore(i, 'V', 'X', 19, 22);
			addScore(i, 'B', 'Z', 20, 21);
			addScore(i, 'B', 'X', 20, 22);
			addScore(i, 'Z', 'X', 21, 22);
		}
	}
	
	public void addScore(int idx, char S1, char S2, int i, int j ){
		if (TokenForm[idx] == S1 && AnswerForm[idx] == S2){
			ScoreM[i][j] = ScoreM[i][j] + 1;
			ScoreM[j][i] = ScoreM[j][i] + 1;
		}
		if (TokenForm[idx] == S2 && AnswerForm[idx] == S1){
			ScoreM[i][j] = ScoreM[i][j] + 1;
			ScoreM[j][i] = ScoreM[j][i] + 1;
		}
	}
	
	public int[][] AddMatrix(int[][] SUM){
		int[][] Total = new int[23][23];
		for(int i=0; i < 23; i++){
			for(int j=0; j < 23; j++){
				Total[i][j] = SUM[i][j] + ScoreM[i][j];
			}
		}
		return Total;
	}
	
	public void DisplayMatrix(){
		for(int i=0; i < 23; i++){
			for(int j=0; j < 23; j++){
				System.out.print(" " + ScoreM[i][j]);
			}
			System.out.println();
		}
	}
	
}