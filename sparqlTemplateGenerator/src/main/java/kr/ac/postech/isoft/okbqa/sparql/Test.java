package kr.ac.postech.isoft.okbqa.sparql;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.List;

import com.clearnlp.component.AbstractComponent;
import com.clearnlp.dependency.DEPTree;
import com.clearnlp.nlp.NLPGetter;
import com.clearnlp.nlp.NLPMode;
import com.clearnlp.reader.AbstractReader;
import com.clearnlp.segmentation.AbstractSegmenter;
import com.clearnlp.tokenization.AbstractTokenizer;

/**
 * @since 1.1.0
 * @author Jinho D. Choi ({@code jdchoi77@gmail.com})
 */
public class Test
{
	final String language = AbstractReader.LANG_EN;

	public Test(String modelType, String inputFile, String outputFile) throws Exception
	{
		AbstractTokenizer tokenizer  = NLPGetter.getTokenizer(language);
		AbstractComponent tagger     = NLPGetter.getComponent(modelType, language, NLPMode.MODE_POS);
		AbstractComponent parser     = NLPGetter.getComponent(modelType, language, NLPMode.MODE_DEP);
		AbstractComponent identifier = NLPGetter.getComponent(modelType, language, NLPMode.MODE_PRED);
		AbstractComponent classifier = NLPGetter.getComponent(modelType, language, NLPMode.MODE_ROLE);
		AbstractComponent labeler    = NLPGetter.getComponent(modelType, language, NLPMode.MODE_SRL);

		AbstractComponent[] components = {tagger, parser, identifier, classifier, labeler};

		String sentence = "Who is Horatio Nelson.";
		process(tokenizer, components, sentence);
		//process(tokenizer, components, UTInput.createBufferedFileReader(inputFile), UTOutput.createPrintBufferedFileStream(outputFile));
	}

	public void process(AbstractTokenizer tokenizer, AbstractComponent[] components, String sentence)
	{
		DEPTree tree = NLPGetter.toDEPTree(tokenizer.getTokens(sentence));

		for (AbstractComponent component : components)
			component.process(tree);

		System.out.println(tree.toStringSRL()+"\n");
	}

	public void process(AbstractTokenizer tokenizer, AbstractComponent[] components, BufferedReader reader, PrintStream fout)
	{
		AbstractSegmenter segmenter = NLPGetter.getSegmenter(language, tokenizer);
		DEPTree tree;

		for (List<String> tokens : segmenter.getSentences(reader))
		{
			tree = NLPGetter.toDEPTree(tokens);

			for (AbstractComponent component : components)
				component.process(tree);

			fout.println(tree.toStringSRL()+"\n");
		}

		fout.close();
	}

	public static void main(String[] args)
	{
		//String modelType  = args[0];	// "general-en" or "medical-en"
		String modelType = "general-en";
		//String inputFile  = args[1];
		String inputFile = "iphone5.txt";
		//String outputFile = args[2];
		String outputFile = "iphone5_output.txt";

		try
		{
			new Test(modelType, inputFile, outputFile);
		}
		catch (Exception e) {e.printStackTrace();}
	}
}
