package test_strutturale_interpreterPackage;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import interpreterPackage.FinalParser;
import interpreterPackage.Interpreter;
import interpreterPackage.PosTags;

public class FinalParser_test_case {

	public static FinalParser myFP;
	public static PosTags myPosTags;
	public static Interpreter myInt;
	
	@Before
	public void setUp() throws Exception {
		myPosTags = new PosTags("pos-tags.txt"); 
	}

	@Test
	public void testDoParseStringInt_successfully() {		
		//analizzo il caso (S (PP) , (SBAR) , (predicato) (. .))
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s0 = "(S (PP (IN After) "
				+ "(S (NP (-LRB- -LCB-) (NNP Tin) (-RRB- -RCB-)) (VP (VBZ becomes) (ADJP (JJ true))))) "
				+ "(, ,) (SBAR (WHADVP (WRB when)) (S (NP (-LRB- -LCB-) (NNP Tdistance) (-RRB- -RCB-)) (VP (VBZ is)))) "
				+ "(, ,) (ADVP (RB then)) (NP (-LRB- -LCB-) (NNP Tleave) (-RRB- -RCB-)) "
				+ "(VP (MD shall) (VP (VBZ sets) (ADJP (JJ true))) (NP (DT every) (CD 3) (NN time) (NNS units))) (. .))";
		
		myFP.doParse(s0, 0);  
		assertTrue(myFP.noError());
	}
	
	@Test
	public void testDoParseStringInt_successfully_0() {	
		//analizzo il caso (S (S) (, ,) (and) (S))
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s1 = "(S (SBAR (IN After) "
				+ "(S (S (NP (NP (-LRB- -LCB-) (NNP Tout0) (-RRB- -RCB-)) "
				+ "(CC or) (NP (-LRB- -LCB-) (NNP Tout1) (-RRB- -RCB-)) "
				+ "(CC or) (NP (-LRB- -LCB-) (NNP Tout2) (-RRB- -RCB-))) "
				+ "(VP (VBZ becomes) (ADJP (JJ false)))) "
				+ "(, ,) (CC and) (S (NP (-LRB- -LCB-) (NNP Tin) (-RRB- -RCB-)) (VP (VBZ becomes) (ADJP (JJ true))) "
				+ "(, ,) (SBAR (WHADVP (WRB when)) (S (NP (-LRB- -LCB-) (NNP Tdistance) (-RRB- -RCB-)) "
				+ "(VP (VBZ is) (NP (QP (JJR equals) (TO to) (CD 5))))))))) "
				+ "(, ,) (NP (-LRB- -LCB-) (NNP Tleave) (-RRB- -RCB-)) (VP (MD shall) (VP (VBZ sets) (TO to))) (. .))";
		
		myFP.doParse(s1, 0); 
		assertTrue(myFP.noError());
	}
	
	@Test
	public void testDoParseStringInt_successfully_1() {
		//analizzo il caso (S (SBAR) , (complexSBAR), (predicato))
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s2 = "(S (SBAR (WHADVP (WRB When)) "
				+ "(S (NP (-LRB- -LCB-) (NNP Tdistance) (-RRB- -RCB-)) (VP (VBZ is) (NP (QP (JJR less) (IN than) (CD 3000)))))) "
				+ "(, ,) (SBAR (SBAR (IN after) (S (NP (-LRB- -LCB-) (NNP Tout) (-RRB- -RCB-)) (VP (VBZ becomes) (ADJP (JJ false))))) "
				+ "(, ,) (CC and) (SBAR (IN after) (S (NP (-LRB- -LCB-) (NNP Tout) (-RRB- -RCB-)) (VP (VBZ becomes) (ADJP (JJ false)))))) "
				+ "(, ,) (ADVP (RB then)) (NP (NP (-LRB- -LCB-) (NNP Tapproaching) (-RRB- -RCB-)) "
				+ "(CC and) (NP (-LRB- -LCB-) (NN Tin) (-RRB- -RCB-))) "
				+ "(VP (MD shall) (VP (VB be) (VP (VBN set) (PP (TO to) (NP (ADJP (JJ true) "
				+ "(PP (IN for) (NP (QP (IN at) (JJS least) (CD 3))))) (NN time) (NNS units)))))) (. .))";
		
		myFP.doParse(s2, 0);
		assertTrue(myFP.noError());	
	}
	
	@Test
	public void testDoParseStringInt_successfully_2() {
		//analizzo il caso (S (predicato) (. .))
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s3_1 = "(S (NP (NP (-LRB- -LCB-) (NNP Tin) (-RRB- -RCB-)) (, ,) (NP (-LRB- -LCB-) (NNP Tout) (-RRB- -RCB-))"
				+ " (CC and) (NP (-LRB- -LCB-) (NNP Toff) (-RRB- -RCB-))) "
				+ "(VP (MD shall) (ADVP (RB previously)) (VP (VBD setted) (PP (TO to) (ADJP (JJ true))))) (. .))";
		
		myFP.doParse(s3_1, 0);  
		assertTrue(myFP.noError());	
	}
	
	@Test
	public void testDoParseStringInt_successfully_3() {
		//analizzo il caso (S (predicato) (complexSBAR) (. .))
		//si noti che la strutturazione del primo predicato mi permette di testare
		//anche lo statement della riga 476 del predicate parser
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s3_2 = "(S "
				+ "(-LRB- -LCB-) (NP (NN request)) (-RRB- -RCB-) (VP (VBZ is) (ADJP (JJ true))) "
				+ "(, ,) "
				+ "(SBAR "
				+ "(SBAR (IN After) (S (NP (-LRB- -LCB-) (NNP Cred) (-RRB- -RCB-)) (VP (VBZ becomes) (ADJP (JJ true))))) "
				+ "(CC and) "
				+ "(SBAR (IN if) (S (NP (PRP it)) "
				+ "(VP "
				+ "(VP (VBZ becomes) (ADJP (JJ true))) "
				+ "(CC and) "
				+ "(VP (VBZ becomes) (DT again) (ADJP (JJ false))))))) "
				+ "(. .))";
		
		myFP.doParse(s3_2, 0);  
		assertTrue(myFP.noError());	
	}

	@Test
	public void testDoParseStringInt_successfully_4() {
		//analizzo il caso (S (PP) , (predicato) (. .)) con la PP := (PP (IN) (NP) (VP , (SBAR)))
		//che quindi forza il tutto al caso (S (PP) , (SBAR) , (predicato) (. .))
		myInt = new Interpreter(myPosTags);
		myInt.secondWayBe();
		myFP = new FinalParser(myPosTags, myInt);
		
		String s4 = "(S "
				+ "(PP (IN After) (S (NP (-LRB- -LCB-) (NNP Tbb) (-RRB- -RCB-)) (VP (VBZ becomes) (ADJP (JJ false)) "
				+ "(, ,) (SBAR (IN if) (S (NP (-LRB- -LCB-) (NNP Tsana) (-RRB- -RCB-)) "
				+ "(VP (VBZ is) (NP (QP (JJR greater) (IN than) (CD 5))))))))) (, ,) "
				+ "(NP (NP (RB then)) (PRN (-LRB- -LCB-) (NP (NN request)) (-RRB- -RCB-))) "
				+ "(VP (MD shall) (VP (VB be) (VP (VBN set) (PP (TO to) (ADJP (JJ true)))))) (. .))";
		
		myFP.doParse(s4, 0);  
		assertTrue(myFP.noError());	
	}
	
	@Test
	public void testDoParseStringInt_successfully_5() {
		//analizzo il caso (S (SBAR) , (predicato) (. .)) con la SBAR := (SBAR (IN) (NP) (VP , (SBAR)))
		//che quindi forza il tutto al caso (S (SBAR) , (SBAR) , (predicato) (. .))
		myInt = new Interpreter(myPosTags);
		myInt.secondWayBe();
		myFP = new FinalParser(myPosTags, myInt);
		
		String s5 = "(S "
				+ "(SBAR (IN After) (S (NP (-LRB- -LCB-) (NNP Tbb) (-RRB- -RCB-)) (VP (VBZ becomes) (ADJP (JJ false)) "
				+ "(, ,) (SBAR (IN if) (S (NP (-LRB- -LCB-) (NNP Tsana) (-RRB- -RCB-)) "
				+ "(VP (VBZ is) (NP (QP (JJR greater) (IN than) (CD 5))))))))) (, ,) "
				+ "(NP (NP (RB then)) (PRN (-LRB- -LCB-) (NP (NN grant)) (-RRB- -RCB-))) "
				+ "(VP (MD shall) (VP (VB be) (VP (VBN set) (PP (TO to) (ADJP (JJ true)))))) (. .))";
		
		myFP.doParse(s5, 0);  
		assertTrue(myFP.noError());	
	}

	@Test
	public void testDoParseStringInt_successfully_6() {
		//analizzo il caso di (NP (PRP))
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s6 = "(S (SBAR (WHADVP (WRB When)) (S (NP (-LRB- -LCB-) (NNP Cred) (-RRB- -RCB-)) (VP (VBZ is) (ADJP (JJ true))))) "
				+ "(, ,) (ADVP (RB then)) (NP (PRP it)) (VP (MD shall) (VP (VBZ holds) (ADJP (JJ true)) (PP (IN for) (NP (CD 2) (NNS minutes))))) (. .))";
		
		myFP.doParse(s6, 0);  
		assertTrue(myFP.noError());	
	}
	
	@Test
	public void testDoParseStringInt_successfully_7() {
		//analizzo il caso (S (predicato) , (SBAR) (. .))
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s7 = "(S (NP (-LRB- -LCB-) (NNP Tin) (-RRB- -RCB-)) "
				+ "(VP (MD shall) (VP (VB be) (VP (VBN set) (PP (TO to) (ADJP (JJ true)))))) "
				+ "(, ,) (SBAR (WHPP (IN in) (WHNP (WDT which))) "
				+ "(S (NP (-LRB- -LCB-) (NNP Cred) (-RRB- -RCB-)) (VP (VBZ is) (ADJP (JJ true))))) (. .))";
		
		myFP.doParse(s7, 0);  
		assertTrue(myFP.noError());	
	}

	@Test
	public void testDoParseStringInt_unsuccessfully_0() {
		//nel caso (S (S) (and) (S)) non si trova la seconda sentence S 
		//e si segnal l'errore
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s0 = "(S (SBAR (IN After) "
				+ "(S (S (NP (-LRB- -LCB-) (NNP Tout) (-RRB- -RCB-)) (VP (VBZ becomes) (ADJP (JJ false)))) "
				+ "(, ,) (CC and) (NP (-LRB- -LCB-) (NNP Tin) (-RRB- -RCB-)) (VP (VBZ becomes) (ADJP (JJ true)) "
				+ "(, ,) (SBAR (WHADVP (WRB when)) (S (NP (-LRB- -LCB-) (NNP Tdistance) (-RRB- -RCB-)) (VP (VBZ is))))))) "
				+ "(, ,) (NP (-LRB- -LCB-) (NNP Tleave) (-RRB- -RCB-)) (VP (MD shall)) (. .))";
		
		myFP.doParse(s0, 0);  
		assertTrue(!myFP.noError());	
		
	}
	
	@Test
	public void testDoParseStringInt_unsuccessfully_1() {
		//nel caso (S (PP) , (SBAR) , (predicato)) non viene individuato
		//il predicato atteso
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s1 = "(S (PP (IN After) "
				+ "(S (NP (-LRB- -LCB-) (NNP Tin) (-RRB- -RCB-)) (VP (VBZ becomes) (ADJP (JJ true))))) "
				+ "(, ,) (SBAR (WHADVP (WRB when)) (S (NP (-LRB- -LCB-) (NNP Tdistance) (-RRB- -RCB-)) (VP (VBZ is)))) "
				+ "(, ,) (. .))";
		
		myFP.doParse(s1, 0);
		assertTrue(!myFP.noError());
	}
	
	@Test
	public void testDoParseStringInt_unsuccessfully_2() {
		//nel caso (S (predicato) , (SBAR)) se si identifica predicato e virgola
		//ci si aspetterà necessariamente una SBAR
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s2 = "(S (NP (-LRB- -LCB-) (NNP Tin) (-RRB- -RCB-)) "
				+ "(VP (MD shall) (VP (VB be) (VP (VBN set) (PP (TO to) (ADJP (JJ true)))))) (, ,) (. .))";
		
		myFP.doParse(s2, 0);  
		assertTrue(!myFP.noError());	
	}
	
	@Test
	public void testDoParseStringInt_unsuccessfully_3() {
		//siamo nel caso in cui la sentence inizia con un pos-tag inatteso
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s3 = "(S (ADJP (JJ true)) "
				+ "(VP (MD shall) (VP (VB be) (VP (VBN set) (PP (TO to) (ADJP (JJ true)))))) (, ,) (. .))";
		
		myFP.doParse(s3, 0);  
		assertTrue(!myFP.noError());
	}
	
	@Test
	public void testDoParseStringInt_unsuccessfully_4() {
		//siamo nel caso in cui la SBAR inizia con un pos-tag inatteso.
		//Quindi nè IN, nè WHPP e nè WHADVP 
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s4 = "(S (NP (-LRB- -LCB-) (NNP Tin) (-RRB- -RCB-)) "
				+ "(VP (MD shall) (VP (VB be) (VP (VBN set) (PP (TO to) (ADJP (JJ true)))))) "
				+ "(, ,) (SBAR (ADJP (JJ true)) (S (NP (-LRB- -LCB-) (NNP Tsana) (-RRB- -RCB-)) "
				+ "(VP (VBZ is) (NP (QP (JJR greater) (IN than) (CD 5)))))) (. .))";
		
		myFP.doParse(s4, 0);  
		assertTrue(!myFP.noError());
	}
	
	@Test
	public void testDoParseStringInt_unsuccessfully_5() {
		//siamo nel caso in cui la PP non ha il secondo membro atteso
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s5 = "(S (PP (IN After) (UCP (NP (-LRB- -LCB-) (NNP Tsana) (-RRB- -RCB-)) "
				+ "(VP (VBZ is) (NP (QP (JJR greater) (IN than) (CD 5)))))) (, ,) (NP (-LRB- -LCB-) (NNP Tin) (-RRB- -RCB-)) "
				+ "(VP (MD shall) (VP (VB be) (VP (VBN set) (PP (TO to) (ADJP (JJ true)))))) (. .))";
		
		myFP.doParse(s5, 0);  
		assertTrue(!myFP.noError());
	}
	
	@Test
	public void testDoParseStringInt_unsuccessfully_6() {
		//siamo nel caso di un predicato con il seguente inizio di 
		//struttura sintattica "(NP (NP (RB then)) ...)"
		//ma la NP identificante la NN non termina correttamente
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s6 = "(S (PP (IN After) (S (NP (-LRB- -LCB-) (NNP Tsana) (-RRB- -RCB-)) "
				+ "(VP (VBZ is) (NP (QP (JJR greater) (IN than) (CD 5)))))) (, ,) "
				+ "(NP (NP (RB then)) (PRN (-LRB- -LCB-) (NP (NN request) (-RRB- -RCB-))) "
				+ "(VP (MD shall) (VP (VB be) (VP (VBN set) (PP (TO to) (ADJP (JJ true)))))) (. .))";
		
		myFP.doParse(s6, 0);  
		assertTrue(!myFP.noError());
	}
	
	@Test
	public void testDoParseStringInt_unsuccessfully_7() {
		//siamo nel caso precedente ma col problema di non avere il 
		//predicato terminante correttamente con (-RRB- -RCB-)
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s7 = "(S (PP (IN After) (S (NP (-LRB- -LCB-) (NNP Tsana) (-RRB- -RCB-)) "
				+ "(VP (VBZ is) (NP (QP (JJR greater) (IN than) (CD 5)))))) (, ,) "
				+ "(NP (NP (RB then)) (PRN (-LRB- -LCB-) (NP (NN request)))) "
				+ "(VP (MD shall) (VP (VB be) (VP (VBN set) (PP (TO to) (ADJP (JJ true)))))) (. .))";
		
		myFP.doParse(s7, 0);  
		assertTrue(!myFP.noError());
	}
	
	@Test
	public void testDoParseStringInt_unsuccessfully_8() {
		//analizzo il caso di (NP (PRP))
		//ma con il mancato ritrovamento della VP attesa al suo seguito
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s8 = "(S (SBAR (WHADVP (WRB When)) (S (NP (-LRB- -LCB-) (NNP Cred) (-RRB- -RCB-)) (VP (VBZ is) (ADJP (JJ true))))) "
				+ "(, ,) (ADVP (RB then)) (NP (PRP it)) (. .))";
		
		myFP.doParse(s8, 0);  
		assertTrue(!myFP.noError());	
	}
	
	@Test
	public void testDoParseStringInt_unsuccessfully_9() {
		//analizzo il caso (S (predicato) (complexSBAR) (. .))
		//con la strutturazione del primo predicato mi permette di testare
		//anche lo statement della riga 476 del predicate parser.
		//In questo caso però verifico che sia segnalato il fallimento dell'analisi del predicato 
		//per via della mancata corretta conclusione dell'NP
		myInt = new Interpreter(myPosTags);
		myFP = new FinalParser(myPosTags, myInt);
		
		String s9 = "(S (-LRB- -LCB-) (NP (NN request) (-RRB- -RCB-) (VP (VBZ is) (ADJP (JJ true)))) "
				+ "(, ,) (SBAR (SBAR (IN After) (S (NP (-LRB- -LCB-) (NNP Cred) (-RRB- -RCB-)) (VP (VBZ becomes) (ADJP (JJ true))))) "
				+ "(CC and) (SBAR (IN if) (S (NP (PRP it)) (VP (VBZ becomes) (ADJP (JJ true)))))) "
				+ "(. .))";
		
		myFP.doParse(s9, 0);  
		assertTrue(!myFP.noError());	
	}
	
	@Test
	public void testController() {
		
		//mi permette di testare il caso di "i+37>= l && i+13<l"
		myFP = new FinalParser(myPosTags, myInt);
		
		String s = "(S (PP (IN After) (S (NP (-LRB- -LCB-) (NNP Tsana) (-RRB- -RCB-)) "
				+ "(VP (VBZ is) (NP (QP (JJR greater) (IN than) (CD 5)))))) (, ,) (NP (NN Tom)) "
				+ " (. .))";
		
		myFP.doParse(s, 0);  
		assertTrue(!myFP.noError());
		
	}
	
	
}
