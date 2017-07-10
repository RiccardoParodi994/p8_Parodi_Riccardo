package test_strutturale_interpreterPackage;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import interpreterPackage.Associator;
import interpreterPackage.FinalParser;
import interpreterPackage.Interpreter;
import interpreterPackage.PosTags;

public class Associator_test_case {

	public static PosTags myPosTags;
	public static Interpreter myInt;
	public static FinalParser myFP;
	public static Associator myAssociator;
	
	@Before
	public void setUp() throws Exception {
		myPosTags = new PosTags("pos-tags.txt");
	}


	@Test
	public void testConstructor_successfully_spec1() throws Exception {
		
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s = "(S (NP (-LRB- -LCB-) (NNP Pgreen) (-RRB- -RCB-)) "
				+ "(VP (MD shall) (ADVP (RB never)) (VP (VBZ becomes) (ADJP (JJ true)))) (. .))";
		
		myFP.doParse(s);  
		myAssociator = new Associator(myPosTags, myFP, false);
		assertTrue(myAssociator.isCorrectAssociation());
	}

	@Test
	public void testConstructor_successfully_spec2() throws Exception {
		
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s = "(S (NP (-LRB- -LCB-) (NNP Pgreen) (-RRB- -RCB-)) "
				+ "(VP (MD shall) (ADVP (RB always)) (VP (VBZ becomes) (ADJP (JJ true)))) (. .))";
		
		myFP.doParse(s);  
		myAssociator = new Associator(myPosTags, myFP, false);
		assertTrue(myAssociator.isCorrectAssociation());
	}

	@Test
	public void testConstructor_successfully_spec5() throws Exception {
		
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s = "(S (SBAR (WHADVP (WRB When)) "
				+ "(S (NP (-LRB- -LCB-) (NNP Cgrn) (-RRB- -RCB-)) "
				+ "(VP (VBZ is) (ADJP (JJ true))))) (, ,) "
				+ "(ADVP (RB then)) (NP (PRP it)) (VP (MD shall) "
				+ "(VP (VBD held) (ADJP (JJ true)))) (. .))";
		
		myFP.doParse(s);  
		myAssociator = new Associator(myPosTags, myFP, false);
		assertTrue(myAssociator.isCorrectAssociation());
	}

	@Test
	public void testConstructor_successfully_spec8() throws Exception {
		
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s = "(S (SBAR (WHADVP (WRB When)) "
				+ "(S (NP (-LRB- -LCB-) (NNP Cgrn) (-RRB- -RCB-)) "
				+ "(VP (VBZ is) (ADJP (JJ true))))) (, ,) "
				+ "(ADVP (RB then)) (NP (PRP it)) (VP (MD shall) "
				+ "(VP (VBZ holds) (ADJP (JJ true) "
				+ "(PP (IN for) (NP (CD 1)))) (NP (NN minute)))) (. .))";
		
		myFP.doParse(s);  
		myAssociator = new Associator(myPosTags, myFP, false);
		assertTrue(myAssociator.isCorrectAssociation());
	}

	@Test
	public void testConstructor_successfully_spec12() throws Exception {
		
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s = "(S (NP (-LRB- -LCB-) (NNP Pgreen) (-RRB- -RCB-)) "
				+ "(VP (MD shall) (ADVP (RB always)) (VP (VBZ becomes) (ADJP (JJ true))"
				+ " (PP (IN for) (NP (QP (IN at) (JJS least) (CD 3)) (NN time) (NNS units))))) (. .))";
		
		myFP.doParse(s);  
		myAssociator = new Associator(myPosTags, myFP, false);
		assertTrue(myAssociator.isCorrectAssociation());
	}

	@Test
	public void testConstructor_successfully_spec13() throws Exception {
		
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s = "(S (NP (-LRB- -LCB-) (NNP Pgreen) (-RRB- -RCB-)) "
				+ "(VP (MD shall) (ADVP (RB always)) (VP (VBZ becomes) (ADJP (JJ true))"
				+ " (PP (IN for) (NP (QP (JJR less) (IN than) (CD 3)) (NN time) (NNS units))))) (. .))";
		
		myFP.doParse(s);  
		myAssociator = new Associator(myPosTags, myFP, false);
		assertTrue(myAssociator.isCorrectAssociation());
	}
	
	@Test
	public void testConstructor_successfully_spec14() throws Exception {
		
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s = "(S (NP (-LRB- -LCB-) (NNP Pgreen) (-RRB- -RCB-)) "
				+ "(VP (MD shall) (VP (VBZ becomes) (ADJP (JJ true))"
				+ " (NP (DT every) (CD 3) (NNS seconds)))) (. .))";
		
		myFP.doParse(s);  
		myAssociator = new Associator(myPosTags, myFP, false);
		assertTrue(myAssociator.isCorrectAssociation());
	}
	
	@Test
	public void testConstructor_successfully_spec15() throws Exception {
		
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s = "(S "
				+ "(SBAR (IN If) "
				+ "(S (NP (-LRB- -LCB-) (NNP Cred) (-RRB- -RCB-)) "
				+ "(VP (VBZ is) (ADJP (JJ true))))) (, ,) "
				+ "(ADVP (RB then)) (NP (-LRB- -LCB-) (NNP Pgo) (-RRB- -RCB-)) "
				+ "(VP (MD shall) (VP (VB be) (NP (NNS sets)) (PP (TO to) (NP (ADJP (JJ true) "
				+ "(PP (IN after) (NP (QP (IN at) (JJS most) (CD 3))))) (NNS seconds))))) (. .)))";
		
		myFP.doParse(s);  
		myAssociator = new Associator(myPosTags, myFP, false);
		assertTrue(myAssociator.isCorrectAssociation());
	}
	
	@Test
	public void testConstructor_successfully_spec16() throws Exception {
		
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s = "(S (SBAR (WHADVP (WRB When)) "
				+ "(S (NP (-LRB- -LCB-) (NNP Tdistance) (-RRB- -RCB-)) "
				+ "(VP (VBZ is) (NP (QP (JJR greater) (IN than) (CD 10000)))))) (, ,) "
				+ "(NP (-LRB- -LCB-) (NNP Tleave) (-RRB- -RCB-)) "
				+ "(VP (MD shall) (VP (VB be) (VP (VBN set) (PP (TO to) (NP (NP (JJ false)) "
				+ "(PP (IN for) (NP (QP (IN at) (JJS least) (CD 3)) (NNS minutes)))))))) (. .))";
		
		myFP.doParse(s);  
		myAssociator = new Associator(myPosTags, myFP, false);
		assertTrue(myAssociator.isCorrectAssociation());
	}

	@Test
	public void testConstructor_unsuccessfully_0() throws Exception {
		
		//causa "correctParsing = false"
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s = "(S (NP (-LRB- -LCB-) (NNP Pgreen) (-RRB- -RCB-)) "
				+ "(VP (MD) (. .))";
		
		myFP.doParse(s);  
		myAssociator = new Associator(myPosTags, myFP, false);
		assertTrue(!myAssociator.isCorrectAssociation());
	}

	@Test
	public void testConstructor_unsuccessfully_1() throws Exception {
		
		//causa "this.buildPattern = false"
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s = "(S (NP (-LRB- -LCB-) (NNP Pgreen) (-RRB- -RCB-)) "
				+ "(VP (VBZ becomes)) (. .))";
		
		myFP.doParse(s);  
		myAssociator = new Associator(myPosTags, myFP, false);
		assertTrue(!myAssociator.isCorrectAssociation());
	}
	
	@Test
	public void testDoAssociation_type0_sentence() throws Exception {
		
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s = "(S (NP (-LRB- -LCB-) (NNP Pgreen) (-RRB- -RCB-)) "
				+ "(VP (MD shall) (VP (VB be) (NP (NNS sets)) (PP (TO to) (ADJP (JJ false))))) (. .))";
		
		myFP.doParse(s);  
		myAssociator = new Associator(myPosTags, myFP, false);
		assertTrue(myAssociator.isCorrectAssociation());
	}
	
	@Ignore
	public void testDoAssociation_type1_sentence() {
		
		//non è mai capitato un caso di requisito 
		//che generasse questa struttura

	}

	@Test
	public void testDoAssociation_type2_sentence_withScope() throws Exception {
		
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s = "(S (SBAR (IN Before) "
				+ "(S "
				+ "(S "
				+ "(NP (NP (-LRB- -LCB-) (NNP Cyel) (-RRB- -RCB-)) "
				+ "(CC and)"
				+ " (NP (-LRB- -LCB-) (NNP Pstop) (-RRB- -RCB-))) "
				+ "(VP (VBP are) (ADJP (JJ true)))) "
				+ "(CC and) "
				+ "(S (NP (-LRB- -LCB-) (NNP Pgreen) (-RRB- -RCB-)) "
				+ "(VP (VBZ is) (ADJP (JJ true)))))) "
				+ "(, ,) "
				+ "(ADVP (RB then)) (NP (-LRB- -LCB-) (NNP Pgreen) (-RRB- -RCB-))" 
				+ " (VP (MD shall) (VP (VB be) (NP (NNS sets)) (PP (TO to) (ADJP (JJ true))))) (. .))";
		
		myFP.doParse(s);  
		myAssociator = new Associator(myPosTags, myFP, false);
		assertTrue(myAssociator.isCorrectAssociation());

	}

	@Test
	public void testDoAssociation_type2_sentence_withoutScope() throws Exception {
		
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s = "(S (SBAR (WHADVP (WRB when)) "
				+ "(S "
				+ "(S "
				+ "(NP (NP (-LRB- -LCB-) (NNP Cyel) (-RRB- -RCB-)) "
				+ "(CC and)"
				+ " (NP (-LRB- -LCB-) (NNP Pstop) (-RRB- -RCB-))) "
				+ "(VP (VBP are) (ADJP (JJ true)))) "
				+ "(CC and) "
				+ "(S (NP (-LRB- -LCB-) (NNP Pgreen) (-RRB- -RCB-)) "
				+ "(VP (VBZ is) (ADJP (JJ true)))))) "
				+ "(, ,) "
				+ "(ADVP (RB then)) (NP (-LRB- -LCB-) (NNP Pgreen) (-RRB- -RCB-)) "
				+ "(VP (MD shall) (VP (VB be) (NP (NNS sets)) (PP (TO to) (ADJP (JJ true))))) (. .))";
		
		myFP.doParse(s);  
		myAssociator = new Associator(myPosTags, myFP, false);
		assertTrue(myAssociator.isCorrectAssociation());

	}
	
	@Test
	public void testDoAssociation_type3_sentence() throws Exception {
		
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s = "(S (SBAR "
				+ "(SBAR (IN After) (S (NP (NP (-LRB- -LCB-) (NNP Cyel) (-RRB- -RCB-)) "
				+ "(CC and) "
				+ "(NP (-LRB- -LCB-) (NNP Pstop) (-RRB- -RCB-))) (VP (VBP are) (ADJP (JJ true))))) "
				+ "(CC and) "
				+ "(SBAR (IN if) (S (NP (-LRB- -LCB-) (NNP Pgo) (-RRB- -RCB-)) (VP (VBZ becomes) (ADJP (JJ false)))))) "
				+ "(, ,) "
				+ "(SBAR (IN if) (S (NP (-LRB- -LCB-) (NNP Pred) (-RRB- -RCB-)) (VP (VBZ is) (ADJP (JJ true))))) "
				+ "(, ,) "
				+ "(ADVP (RB then)) (NP (-LRB- -LCB-) (NNP Pgreen) (-RRB- -RCB-)) "
				+ "(VP (MD shall) (VP (VB be) (NP (NNS sets)) (PP (TO to) (ADJP (JJ false))))) (. .))";
		
		myFP.doParse(s);  
		myAssociator = new Associator(myPosTags, myFP, false);
		assertTrue(myAssociator.isCorrectAssociation());

	}
	
	@Test
	public void testDoAssociation_type4_sentence_withScope() throws Exception {
		
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s = "(S (PP (IN Between) "
				+ "(S "
				+ "(S "
				+ "(S (NP (-LRB- -LCB-) (NNP Taa) (-RRB- -RCB-)) (VP (VBZ becomes) (ADJP (JJ true)))) (CC and) "
				+ "(S (NP (-LRB- -LCB-) (NNP Tab) (-RRB- -RCB-)) (VP (VBZ becomes) (ADJP (JJ false))))) "
				+ "(CC and)"
				+ " (S (NP (-LRB- -LCB-) (NNP Pgo) (-RRB- -RCB-)) (VP (VBZ becomes) (ADJP (JJ false)))))) "
				+ "(, ,)"
				+ " (ADVP (RB then)) (NP (-LRB- -LCB-) (NNP Pred) (-RRB- -RCB-)) "
				+ "(VP (MD shall) (VP (VB be) (NP (NNS sets)) (PP (TO to) (ADJP (JJ true))))) (. .))";
		
		myFP.doParse(s);  
		myAssociator = new Associator(myPosTags, myFP, false);
		assertTrue(myAssociator.isCorrectAssociation());

	}

	@Test
	public void testDoAssociation_type4_sentence_withoutScope() throws Exception {
		
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s = "(S (PP (IN if) "
				+ "(S "
				+ "(S (NP (-LRB- -LCB-) (NNP Cgrn) (-RRB- -RCB-)) (VP (VBZ is) (ADJP (JJ true)))) (CC and) "
				+ "(S (NP (-LRB- -LCB-) (NNP Pstop) (-RRB- -RCB-)) (VP (VBZ is) (ADJP (JJ true)))))) "
				+ "(, ,)"
				+ " (ADVP (RB then)) (NP (-LRB- -LCB-) (NNP Pred) (-RRB- -RCB-)) "
				+ "(VP (MD shall) (VP (VB be) (NP (NNS sets)) (PP (TO to) (ADJP (JJ true))))) (. .))";
		
		myFP.doParse(s);  
		myAssociator = new Associator(myPosTags, myFP, false);
		assertTrue(myAssociator.isCorrectAssociation());

	}

	@Test
	public void testDoAssociation_type5_sentence_withScope() throws Exception {
		
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s = "(S (PP (IN Between) "
				+ "(S (NP (-LRB- -LCB-) (NNP Cgrn) (-RRB- -RCB-)) (VP (VBZ is) (ADJP (JJ true))))) "
				+ "(, ,) "
				+ "(SBAR "
				+ "(SBAR (IN After) (S (NP (NP (-LRB- -LCB-) (NNP Cyel) (-RRB- -RCB-)) "
				+ "(CC and) "
				+ "(NP (-LRB- -LCB-) (NNP Pstop) (-RRB- -RCB-))) (VP (VBP are) (ADJP (JJ true))))) "
				+ "(, ,) "
				+ "(SBAR (IN until) (S (NP (-LRB- -LCB-) (NNP Pgo) (-RRB- -RCB-)) (VP (VBZ becomes) (ADJP (JJ false)))))) "
				+ "(, ,)"
				+ " (ADVP (RB then)) (NP (-LRB- -LCB-) (NNP Pred) (-RRB- -RCB-)) "
				+ "(VP (MD shall) (VP (VB be) (NP (NNS sets)) (PP (TO to) (ADJP (JJ true))))) (. .))";
		
		myFP.doParse(s);  
		myAssociator = new Associator(myPosTags, myFP, false);
		assertTrue(myAssociator.isCorrectAssociation());

	}
	
	@Test
	public void testDoAssociation_unrecognizableTypeOfSentence() throws Exception {
		
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s = "(S "
				+ "(S (NP (-LRB- -LCB-) (NNP Cgrn) (-RRB- -RCB-)) (VP (VBZ is) (ADJP (JJ true)))) (CC and) "
				+ "(S (NP (-LRB- -LCB-) (NNP Pstop) (-RRB- -RCB-)) (VP (VBZ is) (ADJP (JJ true)))))";
		
		myFP.doParse(s);  
		myAssociator = new Associator(myPosTags, myFP, false);
		assertTrue(!myAssociator.isCorrectAssociation());
		
	}
	
	@Test
	public void testVisit_GeneralPhraseExpr_type6_sentence_0() throws Exception {
		//del tipo : (S (S (S) (and) (S)) (and) (S (S) (and) (S)))
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s = "(S (PP (IN After) "
				+ "(S "
				+ "(S "
				+ "(S (NP (-LRB- -LCB-) (NNP Taa) (-RRB- -RCB-)) (VP (VBZ becomes) (ADJP (JJ true)))) (CC and) "
				+ "(S (NP (-LRB- -LCB-) (NNP Tab) (-RRB- -RCB-)) (VP (VBZ becomes) (ADJP (JJ false))))) (CC and) "
				+ "(S "
				+ "(S (NP (-LRB- -LCB-) (NNP Tba) (-RRB- -RCB-)) (VP (VBZ becomes) (ADJP (JJ true)))) (CC and) "
				+ "(S (NP (-LRB- -LCB-) (NNP Tbb) (-RRB- -RCB-)) (VP (VBZ becomes) (ADJP (JJ false))))))) (, ,)"
				+ " (SBAR (IN if) (S (NP (-LRB- -LCB-) (NNP Tsana) (-RRB- -RCB-)) (VP (VBZ is) (NP (QP (JJR greater) (IN than) (CD 5)))))) (, ,) "
				+ "(NP (-LRB- -LCB-) (NNP Tin) (-RRB- -RCB-)) "
				+ "(VP (MD shall) (VP (VB be) (VP (VBN set) (PP (TO to) (ADJP (JJ true)))))) (. .))";
		
		myFP.doParse(s);   
		myAssociator = new Associator(myPosTags, myFP, false);
		assertTrue(myAssociator.isCorrectAssociation());

	}
	
	@Test
	public void testVisit_GeneralPhraseExpr_type6_sentence_1() throws Exception {
		//del tipo : (S (S) (and) (S))
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s = "(S (PP (IN After) "
				+ "(S "
				+ "(S (NP (-LRB- -LCB-) (NNP Taa) (-RRB- -RCB-)) (VP (VBZ becomes) (ADJP (JJ true)))) "
				+ "(CC and) "
				+ "(S (NP (-LRB- -LCB-) (NNP Tba) (-RRB- -RCB-)) (VP (VBZ becomes) (ADJP (JJ true)))))) (, ,)"
				+ " (SBAR (IN if) (S (NP (-LRB- -LCB-) (NNP Tsana) (-RRB- -RCB-)) (VP (VBZ is) (NP (QP (JJR greater) (IN than) (CD 5)))))) (, ,) "
				+ "(NP (-LRB- -LCB-) (NNP Tin) (-RRB- -RCB-)) "
				+ "(VP (MD shall) (VP (VB be) (VP (VBN set) (PP (TO to) (ADJP (JJ true)))))) (. .))";
		
		myFP.doParse(s);  
		myAssociator = new Associator(myPosTags, myFP, false);
		assertTrue(myAssociator.isCorrectAssociation());

	}
}
