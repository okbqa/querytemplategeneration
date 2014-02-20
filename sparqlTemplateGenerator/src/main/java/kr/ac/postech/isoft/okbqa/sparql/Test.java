package kr.ac.postech.isoft.okbqa.sparql;

import java.io.IOException;

public class Test
{
	public static void main(String[] args) throws IOException {
//		QuestionProcessor qp = new QuestionProcessor(QuestionProcessor.DEF_LANG, QuestionProcessor.DEF_MODELTYPE);
//		String question = "Who is the brother of Marc?";
		String q2 = "How many students does the free university in Amsterdam have?";
		String q3 = "In which country does the Nile start?";
		String q4 = "What is the second highest mountain on earth?";
		String q5 = "Who is the mayor of Berlin?";
		
		SparqlTemplateGenerator.getPatterns(q4);
	}
}
