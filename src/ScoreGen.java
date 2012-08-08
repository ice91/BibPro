import java.io.*;
import java.util.*;

public class ScoreGen{
	public static void main(String[] args){
		Vector<ALIGNMENT> align_list = new Vector<ALIGNMENT>();
		BufferedReader Readfile;
		try{
			Readfile = new BufferedReader(new FileReader(args[0]));
			String temp;
			String token;
			String ans;
			while(Readfile.ready()){	
				temp = Readfile.readLine();
				token = temp.substring(temp.indexOf(":")+2);
				temp = Readfile.readLine();
				ans = temp.substring(temp.indexOf(":")+2);
				ALIGNMENT tmp_align = new ALIGNMENT(token, ans); 
				align_list.add(tmp_align);
			}	
			Readfile.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		//align_list.get(0).ComputeMatrix();
		//align_list.get(0).DisplayMatrix();
		int[][] ScoreMatrix = new int[23][23];
		float[][] FinalMatrix = new float[23][23];
		for(int i=0; i < align_list.size(); i++){
			align_list.get(i).ComputeMatrix();
			ScoreMatrix = align_list.get(i).AddMatrix(ScoreMatrix);
			//System.out.println(new String(align_list.get(i).TokenForm));
			//System.out.println(new String(align_list.get(i).AnswerForm));
		}
		
		long D_SUM = 0;
		for(int i=0; i < 23; i++){
			D_SUM = D_SUM + ScoreMatrix[i][i];
		} 
		// Compute FinalMatrix
		for(int i=0; i < 23; i++){
			for(int j=0; j < 23; j++){
			  long child = (long)D_SUM*ScoreMatrix[i][j];
			  long mother = (long)((long)ScoreMatrix[i][i]*(long)ScoreMatrix[j][j]+1);
			  float child_f = (float)child/100000000;
			  float mother_f = (float)mother/100000000;
			  //System.out.println(i + " " + j + " " + child + " " + mother + " " + child_f/mother_f);
				FinalMatrix[i][j] =(float)Math.log(0.5+2*child_f/mother_f);
				//FinalMatrix[i][j] = (float)((long)D_SUM*ScoreMatrix[i][j]/10000)/(long)((ScoreMatrix[i][i]*ScoreMatrix[j][j]+1)/10000);
				//FinalMatrix[i][j] = (float)Math.log(0.5+2*D_SUM*((float)ScoreMatrix[i][j]/(float)((long)ScoreMatrix[i][i]*(long)ScoreMatrix[j][j]+1)));
			}
		}
		
		Vector<String> SYMBOL = new Vector<String>(Arrays.asList("A R N D C Q E G H I L K M F P S T W Y V B Z X *".split("\\s")));
		//System.out.println(D_SUM);
		
	  // DEBUG
		/*System.out.println("   A  R  N  D  C  Q  E  G  H  I  L  K  M  F  P  S  T  W  Y  V  B  Z  X  *");
		for(int i=0; i < 23; i++){
			System.out.print(SYMBOL.get(i));
			for(int j=0; j < 23; j++){
				if (ScoreMatrix[i][j] >= 0 ){
					System.out.print("  " + ScoreMatrix[i][j]);
				} else {
					System.out.print(" " + ScoreMatrix[i][j]);
				}
			}
			System.out.println(" -4");
		}
		System.out.println("* -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -1");*/
		// DISPLAY BLAST SCOREMATRIX
		System.out.println("   A  R  N  D  C  Q  E  G  H  I  L  K  M  F  P  S  T  W  Y  V  B  Z  X  *");
		for(int i=0; i < 23; i++){
			System.out.print(SYMBOL.get(i));
			for(int j=0; j < 23; j++){
				ScoreMatrix[i][j] = (int)(2*FinalMatrix[i][j]);
				if (ScoreMatrix[i][j] >= 0 ){
					System.out.print("  " + ScoreMatrix[i][j]);
				} else {
					System.out.print(" " + ScoreMatrix[i][j]);
				}
			}
			System.out.println(" -4");
		}
		System.out.println("* -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -1");
		
	
	}
}