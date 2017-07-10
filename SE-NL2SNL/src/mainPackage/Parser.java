package mainPackage;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.lexparser.LexicalizedParserQuery;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.ScoredObject;


public class Parser {
	
	private final static String PCG_MODEL = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";

	private final TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "invertible=true");
	
	private final LexicalizedParser parser = LexicalizedParser.loadModel(PCG_MODEL);
	
	//il seguente dovrebbe aver lo scopo di fornirmi più di un albero sintattico
	private final LexicalizedParserQuery parserQuery  = parser.lexicalizedParserQuery();
	
	
//	public Tree parse(String str) {
//		
//		List<CoreLabel> tokens = tokenize(str);
//		
//		Tree tree = parser.apply(tokens);
//		
//		return tree;
//	}

	private List<CoreLabel> tokenize(String str ){
		
		Tokenizer<CoreLabel> tokenizer = tokenizerFactory.getTokenizer(new StringReader(str));
		
		return tokenizer.tokenize();
	}
	
	
	public List<ScoredObject<Tree>> doMultipleSyntacticParse(String stringToParse) {
		
		
		parserQuery.parse(this.tokenize(stringToParse));
		
		parserQuery.getBestPCFGParses();
		
		//List<ScoredObject<Tree>> kBest = parserQuery.getKBestPCFGParses(20);
		
		
		
		return parserQuery.getKBestPCFGParses(20);
	}
	
//	public String doSyntacticParse(String stringToParse) {
//
//		
//		Tree tree = parser.parse(stringToParse);
//	
//		
//		
//		//stampa la struttura ad albero
//		//tree.indentedListPrint();
//		
//		Tree[] result = tree.children();
//		String stringParsed = result[0].toString();  
//		
//
//		return stringParsed;	
//		
//	}
	
}
