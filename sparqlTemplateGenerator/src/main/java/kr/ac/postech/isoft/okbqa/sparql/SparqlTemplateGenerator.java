package kr.ac.postech.isoft.okbqa.sparql;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	
	public static void collectDecendants(DEPNode subTreeRoot, List<DEPNode> container, int depth) {
		if (depth != 0 && subTreeRoot.pos.startsWith("N") || subTreeRoot.pos.startsWith("V") || subTreeRoot.pos.startsWith("W") || subTreeRoot.pos.startsWith("J")) {
			container.add(subTreeRoot);
		}
		List<DEPNode> branches = subTreeRoot.getDependentNodeList();
		for (DEPNode subTree : branches) {
			collectDecendants(subTree, container, depth + 1);
		}
	}
	
	public static void collectAncestors(DEPNode leaf, List<DEPNode> container, int depth) {
		try {
		if ((depth != 0 && leaf != null && leaf.pos != null) && (leaf.pos.startsWith("N") || leaf.pos.startsWith("V") || leaf.pos.startsWith("W") || leaf.pos.startsWith("J"))) {
			container.add(leaf);
		} else if (leaf != null) {
			collectAncestors(leaf.getHead(), container, depth + 1);
		}
		} catch(NullPointerException e) {
			System.out.println("123123");
		}
	}
	
	 public static <T> List<T> union(List<T> list1, List<T> list2) {
	        Set<T> set = new HashSet<T>();

	        set.addAll(list1);
	        set.addAll(list2);

	        return new ArrayList<T>(set);
	    }
	
	public static void getPatterns(String question, Writer r) throws IOException {
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
		// noun, noun phrases
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
		
		// adjectives
		posPtns.add("JJ");
		posPtns.add("JJR");
		posPtns.add("JJS");
		
		// wh-s
		posPtns.add("WDT");
		posPtns.add("WP");
		posPtns.add("WRB");
		
		List<String> depPtns = new ArrayList<String>();
		depPtns.add("");
		
		List<String> synPtns = new ArrayList<String>();
		synPtns.add("");
		
		List<String> srlPtns = new ArrayList<String>();
		srlPtns.add("");
		
		//List<String> collectedNPs = new ArrayList<String>();
		List<DEPNode> collectedVNodes = new ArrayList<DEPNode>();
		List<DEPNode> collectedNNodes = new ArrayList<DEPNode>();
		List<DEPNode> collectedJVNodes = new ArrayList<DEPNode>();
		List<DEPNode> collectedJNNodes = new ArrayList<DEPNode>();
		List<DEPNode> collectedArgNodes = new ArrayList<DEPNode>();
		List<DEPNode> collectedLATNodes = new ArrayList<DEPNode>();
		
		rules.put("pos", posPtns);
		rules.put("dep", depPtns);
		rules.put("syn", synPtns);
		rules.put("srl", srlPtns);
		
		// rule1 : collect nouns that its head is not a noun.
		// rule2 : collect verbs
		// traverse nodes
		
		
//		it = collectedNNodes.iterator();
//		while(it.hasNext()){
//			DEPNode currNode=it.next();
//			for(DEPNode DN: currNode.getDependentNodeList())
//			{
//				if(DN.pos.contains(s)
//			}
//		}
		DEPNode root = parsed.getRoots().get(0);
		// on pos level.
		Iterator<DEPNode> it = parsed.iterator();
		while (it.hasNext()) {
			DEPNode currNode = it.next();
			if (posPtns.contains(currNode.pos)) {
				
				 if (currNode.pos.startsWith("N")) {
					DEPNode head = currNode.getHead();
					if (!head.pos.startsWith("N")) {
						collectedLATNodes.add(currNode);
					}
				
				} else if (currNode.pos.startsWith("V")) {
					//List<DEPNode> roots = parsed.getRoots();
					if (!"aux".equals(currNode.getHeadArc().getLabel()))
						collectedVNodes.add(currNode);
				
				} else if (currNode.pos.startsWith("J")) {
					DEPNode head = currNode.getHead();
					if (head.pos.startsWith("N")) {
						collectedJNNodes.add(currNode);
					} else if (head.pos.startsWith("V")) {
						collectedJVNodes.add(currNode);
					}
				} else if (currNode.pos.startsWith("W")) {
					DEPNode head = currNode.getHead();
					if (head.pos.startsWith("N")) {
						collectedNNodes.add(currNode);
					}
				}
			}
			if (currNode.getSLabel(root) != null && currNode.getSLabel(root).matches("^.*A[0-9].*")) {
				collectedArgNodes.add(currNode);
			}
		}
		System.out.println(parsed.toStringSRL() + "\n"); 
		List<DEPNode> decendantForLatNode = new ArrayList<DEPNode>();
		List<DEPNode> headForLatNode = new ArrayList<DEPNode>();
		for (DEPNode latNode : collectedLATNodes) {
			collectDecendants(latNode, decendantForLatNode, 0);
			collectAncestors(latNode, headForLatNode, 0);
		}
		List<DEPNode> union = union(decendantForLatNode, union(headForLatNode, collectedArgNodes));
		r.append("-------LAT-------\n");
		for(DEPNode node : collectedLATNodes) {
			r.append(node.toStringSRL() + "\n");
		}
		r.append("-------/LAT------\n");
		
		for(DEPNode node: union)
		{
			r.append(node.toStringSRL()+"\n");
			//System.out.println(node.toStringSRL()+"\n");
		}
	}
}
