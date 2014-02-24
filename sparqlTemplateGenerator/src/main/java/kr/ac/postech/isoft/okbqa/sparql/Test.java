package kr.ac.postech.isoft.okbqa.sparql;

import java.io.*;
public class Test
{
	public static void main(String[] args) throws IOException {
//		QuestionProcessor qp = new QuestionProcessor(QuestionProcessor.DEF_LANG, QuestionProcessor.DEF_MODELTYPE);
//		String question = "Who is the brother of Marc?";
		String q2 = "How many students does the free university in Amsterdam have?";
		String q3 = "In which country does the Nile start?";
		String q4 = "What is the second highest mountain on earth?";
		String q5 = "Who is the mayor of Berlin?";
		
		String filepath = "qald_questions.txt";
		String outfilepath = "qald_out.txt";
		BufferedReader in = new BufferedReader(new FileReader(filepath));
		BufferedWriter out = new BufferedWriter(new FileWriter(outfilepath));
		String s;
		int sentCnt = 0;

	      while ((s = in.readLine()) != null) {
	        //System.out.println(s);
	        out.append(s + "\n");
	        SparqlTemplateGenerator.getPatterns(s, out);
	        out.append("========================================"+ ++sentCnt +"\n");
	        System.out.println("========================================"+ sentCnt +"\n");
	        
	      }
	      in.close();
	      out.flush();
	      out.close();
		
		
	}
}
