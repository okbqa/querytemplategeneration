package kr.ac.postech.isoft.okbqa.sparql;

import java.io.IOException;

public class Test
{
	public static void main(String[] args) throws IOException {
//		QuestionProcessor qp = new QuestionProcessor(QuestionProcessor.DEF_LANG, QuestionProcessor.DEF_MODELTYPE);
//		String question = "Who is the brother of Marc?";
		String q2 = "How many students does the free university in Amsterdam have?";
		String q3 = "In which country does the Nile start?";
//		
//		File questionFile = new File("qald_questions.txt");
//		
//		BufferedReader br = new BufferedReader(new FileReader(questionFile));
//		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("output.txt")));
//		
//		for (String x = br.readLine(); x != null ; x = br.readLine()) {
//			DEPTree parsed = qp.process(x);
//			//System.out.println(parsed.toStringSRL()+"\n");
//			bw.append(parsed.toStringSRL()+"\n\n");
//		}
		
		SparqlTemplateGenerator.getPatterns(q2);
		
		//DEPTree parsed = qp.process(question);
		//DEPTree parsed = qp.process(q2);
		
		//System.out.println(parsed.toStringSRL()+"\n");
	}
}
