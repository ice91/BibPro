import java.io.*;
import java.util.*;

public class BlockGen{
	public static void main(String[] args){
		Vector<ALIGNMENT> align_list = new Vector<ALIGNMENT>();
		//Vector<String> A_TERM = new Vector<String>();
		Vector<String> A_RULE = new Vector<String>();
		TreeMap<String, Integer> A_SUM = new TreeMap<String, Integer>();
		Vector<String> L_RULE = new Vector<String>();
		TreeMap<String, Integer> L_SUM = new TreeMap<String, Integer>();
		Vector<String> W_RULE = new Vector<String>();
		TreeMap<String, Integer> W_SUM = new TreeMap<String, Integer>();
		Vector<String> V_RULE = new Vector<String>();
		TreeMap<String, Integer> V_SUM = new TreeMap<String, Integer>();
		Vector<String> P_RULE = new Vector<String>();
		TreeMap<String, Integer> P_SUM = new TreeMap<String, Integer>();
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
		String tmp_term;
		for(int i=0; i < align_list.size(); i++){
			//System.out.println(align_list.get(i).ExtractSymbol('A'));
			//A_TERM.add(align_list.get(i).ExtractSymbol('A'));
			// Author Blocking Rules
			for(int j=0; j < align_list.get(i).A_rule1().size(); j++){
				//System.out.println(align_list.get(i).A_rule().get(j));
				tmp_term = align_list.get(i).A_rule1().get(j);
				if (A_RULE.contains(tmp_term)){
					int sum = A_SUM.get(tmp_term).intValue() + 1;
					A_SUM.put(tmp_term, new Integer(sum));
				} else {
					A_RULE.add(tmp_term);
					A_SUM.put(tmp_term, new Integer(1));
				}
			}
			for(int j=0; j < align_list.get(i).A_rule2().size(); j++){
				tmp_term = align_list.get(i).A_rule2().get(j);
				if (A_RULE.contains(tmp_term)){
					int sum = A_SUM.get(tmp_term).intValue() + 1;
					A_SUM.put(tmp_term, new Integer(sum));
				} else {
					A_RULE.add(tmp_term);
					A_SUM.put(tmp_term, new Integer(1));
				}
			}
			for(int j=0; j < align_list.get(i).A_rule3().size(); j++){
				tmp_term = align_list.get(i).A_rule3().get(j);
				if (A_RULE.contains(tmp_term)){
					int sum = A_SUM.get(tmp_term).intValue() + 1;
					A_SUM.put(tmp_term, new Integer(sum));
				} else {
					A_RULE.add(tmp_term);
					A_SUM.put(tmp_term, new Integer(1));
				}
			}
			// Venue Blocking Rules
			for(int j=0; j < align_list.get(i).L_rule1().size(); j++){
				//System.out.println(align_list.get(i).A_rule().get(j));
				tmp_term = align_list.get(i).L_rule1().get(j);
				if (L_RULE.contains(tmp_term)){
					int sum = L_SUM.get(tmp_term).intValue() + 1;
					L_SUM.put(tmp_term, new Integer(sum));
				} else {
					L_RULE.add(tmp_term);
					L_SUM.put(tmp_term, new Integer(1));
				}
			}
			for(int j=0; j < align_list.get(i).L_rule2().size(); j++){
				tmp_term = align_list.get(i).L_rule2().get(j);
				if (L_RULE.contains(tmp_term)){
					int sum = L_SUM.get(tmp_term).intValue() + 1;
					L_SUM.put(tmp_term, new Integer(sum));
				} else {
					L_RULE.add(tmp_term);
					L_SUM.put(tmp_term, new Integer(1));
				}
			}
			for(int j=0; j < align_list.get(i).L_rule3().size(); j++){
				tmp_term = align_list.get(i).L_rule3().get(j);
				if (L_RULE.contains(tmp_term)){
					int sum = L_SUM.get(tmp_term).intValue() + 1;
					L_SUM.put(tmp_term, new Integer(sum));
				} else {
					L_RULE.add(tmp_term);
					L_SUM.put(tmp_term, new Integer(1));
				}
			}
		}
		
		// 產生A block rule
		for(int i=0; i < A_RULE.size(); i++){
			System.out.println(A_RULE.get(i) + " " + A_SUM.get(A_RULE.get(i)).intValue());
		}
		
	  // 產生L block rule
		for(int i=0; i < L_RULE.size(); i++){
			System.out.println(L_RULE.get(i) + " " + L_SUM.get(L_RULE.get(i)).intValue());
		}
		
		/*for(int i=0; i < A_TERM.size(); i++){
			System.out.println(A_TERM.get(i));
			Regex_Processing(A_TERM.get(i));
		}*/

	}
	
	
}