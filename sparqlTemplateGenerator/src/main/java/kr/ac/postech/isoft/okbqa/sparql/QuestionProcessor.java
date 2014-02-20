package kr.ac.postech.isoft.okbqa.sparql;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.clearnlp.component.AbstractComponent;
import com.clearnlp.dependency.DEPTree;
import com.clearnlp.nlp.NLPGetter;
import com.clearnlp.nlp.NLPMode;
import com.clearnlp.reader.AbstractReader;
import com.clearnlp.tokenization.AbstractTokenizer;

public class QuestionProcessor {
	
	public static final String DEF_LANG = AbstractReader.LANG_EN;
	public static final String DEF_MODELTYPE = "general-en";
	
	private AbstractTokenizer tokenizer; 
	private AbstractComponent tagger; 
	private AbstractComponent parser;
	private AbstractComponent identifier;
	private AbstractComponent classifier;
	private AbstractComponent labeler;
	//private AbstractComponent[] components;
	
	public QuestionProcessor(String language, String modelType) throws IOException {
		this.tokenizer = NLPGetter.getTokenizer(language);
		this.tagger = NLPGetter.getComponent(modelType, language, NLPMode.MODE_POS);
		this.parser = NLPGetter.getComponent(modelType, language, NLPMode.MODE_DEP);
		this.identifier = NLPGetter.getComponent(modelType, language, NLPMode.MODE_PRED);
		this.classifier = NLPGetter.getComponent(modelType, language, NLPMode.MODE_ROLE);
		this.labeler = NLPGetter.getComponent(modelType, language, NLPMode.MODE_SRL);
		
		//components = {this.tagger, this.parser, this.identifier, this.classifier, this.labeler};
	}
	
	public DEPTree process(String question) {
		AbstractComponent[] components = {this.tagger, this.parser, this.identifier, this.classifier, this.labeler};
		DEPTree tree = NLPGetter.toDEPTree(tokenizer.getTokens(question));
		
		for (AbstractComponent component : components)
			component.process(tree);
		
		return tree;
	}

}
