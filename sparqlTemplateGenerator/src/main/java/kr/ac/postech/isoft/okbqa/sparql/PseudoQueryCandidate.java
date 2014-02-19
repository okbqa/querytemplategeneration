package kr.ac.postech.isoft.okbqa.sparql;

import java.util.List;


public class PseudoQueryCandidate {
	public String pseudoQueryTemplate;
	public double score;
	public List<Slot> slots;
	public String origQuestion;
	
	class Slot {
		public String subject;
		public String predicate;
		public String object;
	}
}
