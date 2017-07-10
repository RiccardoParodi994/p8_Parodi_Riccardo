package test_strutturale_interpreterPackage;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import expressionPackage.StringExpr;
import interpreterPackage.PosTags;
import interpreterPackage.VerbPhraseParser;

public class VerbPhraseParser_test_case {

	public static VerbPhraseParser myVPP;
	public static PosTags myPosTags;
	
	@Before
	public void setUp() throws Exception {
		myPosTags = new PosTags("pos-tags.txt");
	}

	@Test
	public void testDoParse_successfully() {
		
		myVPP = new VerbPhraseParser(myPosTags, new StringExpr("Tin", "NN"));
		myVPP.secondWayBe();
		
		String s1 = "(VP (VP (MD shall) (ADVP (RB never)) (VP (VB be) (VP (VBN set) (PP (TO to) (ADJP (JJ false))) "
				  + "(PP (IN for) (NP (QP (IN less) (JJS than) (CD 3)) (NN time) (NNS units)))))"
			  	  + ") (, ,) (CC and) (VP (ADVP (RB previously)) (VBZ is) (ADJP (ADJP (JJR greater)) (PP (IN than) (NP (CD 3000))))"
			  	  + " (, ,) (SBAR (IN after) (S (NP (-LRB- -LCB-) (NNP Tin) (-RRB- -RCB-)) (VP (VBZ holds))))))";
		
		myVPP.doParse(0, s1);
		assertTrue(myVPP.noError());
		
		myVPP = new VerbPhraseParser(myPosTags, new StringExpr("Tin", "NN"));
		myVPP.secondWayBe();
		
		String s2 = "(VP (VBZ is) (DT also) (ADJP (JJ false)) (NP (DT every) (NP (CD 3) (NNS units))) "
				  + "(, ,) (SBAR (IN after) (S (NP (-LRB- -LCB-) (NNP Tin) (-RRB- -RCB-))"
				  + " (VP (VP (VBZ holds) (PP (IN for) (NP (QP (IN less) (JJS than) (CD 3)) (NN time) (NNS units)))) "
				  + "(CC and) (VP (VBZ is))))))";
		 
		myVPP.doParse(0, s2);   
		assertTrue(myVPP.noError()); 
	}

	@Test
	public void testDoParse_unsuccessfully() {

		//testo se una volta identificato un avverbio,
		//quest'ultimo non venga seguito dal verbo atteso, sia segnalato l'errore
		myVPP = new VerbPhraseParser(myPosTags, new StringExpr("Tin", "NN"));
		myVPP.secondWayBe();
		
		String s3 = "(VP (ADVP (RB never)))";
		
		myVPP.doParse(0, s3);
		assertTrue(!myVPP.noError());

		//testo venga considerato errore la situazione definita s4;
		//ovvero un'attesa di un secondo complemento che non verrà soddisfatta
		myVPP = new VerbPhraseParser(myPosTags, new StringExpr("Tin", "NN"));
		myVPP.secondWayBe();
		
		String s4 = "(VP (VBZ becomes) (ADJP (JJ true)) )";
		
		myVPP.doParse(0, s4);
		assertTrue(!myVPP.noError());
		
		//testo che sia segnalato errore se,
		//identificata una phrase di tipo PP,
		//essa non viene seguita necessariamente da almeno
		//una part of speech
		myVPP = new VerbPhraseParser(myPosTags, new StringExpr("Tin", "NN"));
		myVPP.secondWayBe();
		
		String s5 = "(VP (VBZ becomes) (PP ))";
		
		myVPP.doParse(0, s5);
		assertTrue(!myVPP.noError());
		
		//testo che sia segnalato errore se,
		//identificata una phrase di tipo generico 
		//(che escluda i casi seguenti : VP, ADVP e PP),
		//essa non viene seguita necessariamente da almeno
		//una part of speech
		myVPP = new VerbPhraseParser(myPosTags, new StringExpr("Tin", "NN"));
		myVPP.secondWayBe();
		
		String s6 = "(VP (VBZ becomes) (ADJP ))";
		
		myVPP.doParse(0, s6);
		assertTrue(!myVPP.noError());
		
		//testo che sia segnalato errore se identifico la suddetta phrase,
		//identifico la necessaria part of speech ma, trovato lo spazio bianco (' ')
		//che mi "costringe" ad attendere un'altra part of speech, essa non viene identificata
		myVPP = new VerbPhraseParser(myPosTags, new StringExpr("Tin", "NN"));
		myVPP.secondWayBe();
		
		String s7 = "(VP (VBZ becomes) (ADJP (TO to) ))";
		
		myVPP.doParse(0, s7);
		assertTrue(!myVPP.noError());
		
		//testo la segnalazione dell'errore a tag non definito nella classe PosTags
		myVPP = new VerbPhraseParser(myPosTags, new StringExpr("Tin", "NN"));
		myVPP.secondWayBe();
		
		String s8 = "(VP (LU errore))";
		
		myVPP.doParse(0, s8);
		assertTrue(!myVPP.noError());
		
	}
	
	@Test 
	public void testGetLambdaExpr_unsuccessfully() {
		
		//non è significativo
		myVPP = new VerbPhraseParser(myPosTags, null);
		String s = "(NP )";
		myVPP.doParse(0, s); 
		assertNull(myVPP.getLambdaExpr());
	}
	
	@Test
	public void testCheckCorrectEnd() {
		
		myVPP = new VerbPhraseParser(myPosTags, new StringExpr("Tin", "NN"));
		myVPP.secondWayBe();
		
		String s = "(VP (VBZ becomes) (ADJP (JJ true)) (, , (SBAR ()))";
		
		myVPP.doParse(0, s);
		assertTrue(!myVPP.noError());
		
	}
	
}
