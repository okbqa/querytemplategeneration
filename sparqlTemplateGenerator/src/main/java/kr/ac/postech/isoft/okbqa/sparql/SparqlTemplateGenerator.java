package kr.ac.postech.isoft.okbqa.sparql;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.clearnlp.dependency.DEPNode;
import com.clearnlp.dependency.DEPTree;
import com.clearnlp.reader.AbstractReader;

public class SparqlTemplateGenerator {
	public static final String DEF_LANG = AbstractReader.LANG_EN;
	public static final String TEST_QUESTION = "In which country does the Nile start?";
	
	public static final String[] w5h1 = { "where", "when", "why", "which", "what", "how" };
	
	public static QuestionProcessor qp;
	static {
		try {
			qp = new QuestionProcessor(QuestionProcessor.DEF_LANG, QuestionProcessor.DEF_MODELTYPE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static List<PseudoQueryCandidate> getCandidates() {
		return null;
	}
	
	public static void getPatterns(String question) {
		DEPTree parsed = qp.process(question);
		DEPTree parsedClone = parsed.clone();
		
		Map<String, Double> lambda = new HashMap<String, Double>();
		lambda.put("pos", 0.25);
		lambda.put("dep", 0.25);
		lambda.put("syn", 0.25);
		lambda.put("srl", 0.25);
		
		Map<String, List> rules;
		rules = new HashMap<String, List>();
		
		List<String> posPtns = new ArrayList<String>();
		posPtns.add("NN");
		posPtns.add("NNS");
		posPtns.add("NNP");
		posPtns.add("NNPS");
		posPtns.add("VB");
		posPtns.add("VBD");
		posPtns.add("VBG");
		posPtns.add("VBN");
		posPtns.add("VBP");
		posPtns.add("VBZ");
		
		List<String> depPtns = new ArrayList<String>();
		depPtns.add("");
		
		List<String> synPtns = new ArrayList<String>();
		synPtns.add("");
		
		List<String> srlPtns = new ArrayList<String>();
		srlPtns.add("");
		
		//List<String> collectedNPs = new ArrayList<String>();
		List<DEPNode> collectedVNodes = new ArrayList<DEPNode>();
		List<DEPNode> collectedNNodes = new ArrayList<DEPNode>();
		rules.put("pos", posPtns);
		rules.put("dep", depPtns);
		rules.put("syn", synPtns);
		rules.put("srl", srlPtns);
		
		// rule1 : collect nouns that its head is not a noun.
		// rule2 : collect verbs
		// root 부터 순회를 하는데,
		
		// on pos level.
		Iterator<DEPNode> it = parsed.iterator();
		while (it.hasNext()) {
			DEPNode currNode = it.next();
			if (posPtns.contains(currNode.pos)) {
				 if (currNode.pos.startsWith("N")) {
					DEPNode head = currNode.getHead();
					if (!head.pos.startsWith("N")) {
						collectedNNodes.add(currNode);
					}
				 } else if (currNode.pos.startsWith("V")) {
					List<DEPNode> roots = parsed.getRoots();
					//if (roots.contains(currNode)) {
					collectedVNodes.add(currNode);
					//}
				 }
			}
		}
		
		// on dependency level.
		it = parsed.iterator();
		DEPNode root = parsed.getRoots().get(0);
		while (it.hasNext()) {
			DEPNode currNode = it.next();
			
			//System.out.println(currNode.getSLabel(root));
			for (DEPNode currRoot : collectedVNodes) {
				System.out.println(currNode.getSLabel(currRoot));
			}
		}
	}
}
