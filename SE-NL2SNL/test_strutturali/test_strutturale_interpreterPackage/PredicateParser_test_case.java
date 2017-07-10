package test_strutturale_interpreterPackage;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import interpreterPackage.PosTags;
import interpreterPackage.PredicateParser;

public class PredicateParser_test_case {

	public static PosTags myPosTags;
	public static PredicateParser myPP;
	
	@Before
	public void setUp() throws Exception {
		myPosTags = new PosTags("pos-tags.txt"); 
	}
	
	@Test
	public void testDoParsePredicate_unsuccessfully_VPmissing() {
		
		myPP = new PredicateParser(myPosTags, false);
		
		String s0 = "(NP (-LRB- -LCB-) (NNP Tin) (-RRB- -RCB-))";
	
		myPP.doParsePredicate(0, s0);
		assertTrue(myPP.noError());
	}
	
	@Test
	public void testDoParse_unsuccessfully_0() {
		//verifico che venga segnalato errore per il mancato ' ' atteso
		//al termine della NNP
		myPP = new PredicateParser(myPosTags, false);
		String s0 = "(NP (-LRB- -LCB-) (NNP Tin)(-RRB- -RCB-))";
	
		myPP.doParse(0, s0);
		assertTrue(!myPP.noError());
	}
	
	@Test
	public void testDoParse_unsuccessfully_1() {
		//verifico che venga segnalato errore per il mancato "(-RRB- -RCB-)" atteso
		//al termine della NNP
		myPP = new PredicateParser(myPosTags, false);
		String s1 = "(NP (-LRB- -LCB-) (NNP Tin) )";
	
		myPP.doParse(0, s1);
		assertTrue(!myPP.noError());
	}

	@Test
	public void testDoParse_unsuccessfully_2() {
		//verifico che venga segnalato errore per il mancato ritrovamento 
		//del primo soggetto atteso in una clause di NP
		myPP = new PredicateParser(myPosTags, false);
		String s2 = "(NP (NP (-LRB- -LCB-) (-RRB- -RCB-)) (, ,) (NP (-LRB- -LCB-) (NNP Tin) (-RRB- -RCB-)))";
	
		myPP.doParse(0, s2);
		assertTrue(!myPP.noError());
	}
	
	@Test
	public void testDoParse_unsuccessfully_3() {
		//verifico che venga segnalato errore per il mancato ritrovamento 
		//del terzo soggetto atteso in una clause di NP
		myPP = new PredicateParser(myPosTags, false);
		String s3 = "(NP (NP (-LRB- -LCB-) (NNP Tout) (-RRB- -RCB-)) (, ,) (NP (-LRB- -LCB-) (NNP Tin) (-RRB- -RCB-))"
				+ " (CC and) (NP (-LRB- -LCB-) (-RRB- -RCB-)))";
	
		myPP.doParse(0, s3);
		assertTrue(!myPP.noError());
	}
	
	@Test
	public void testDoParse_unsuccessfully_4() {
		//verifico che venga segnalato errore per il mancato ritrovamento 
		//del secondo soggetto atteso in una clause di NP
		myPP = new PredicateParser(myPosTags, false);
		String s4 = "(NP (NP (-LRB- -LCB-) (NNP Tout) (-RRB- -RCB-)) (, ,) (NP (-LRB- -LCB-) (-RRB- -RCB-))"
				+ " (CC and) (NP (-LRB- -LCB-) (NNP Tin) (-RRB- -RCB-)))";
	
		myPP.doParse(0, s4);
		assertTrue(!myPP.noError());
	}
	
	@Test
	public void testDoParse_unsuccessfully_5() {
		//verifico che venga segnalato errore per il mancato ritrovamento 
		//del secondo soggetto atteso in una clause di NP
		myPP = new PredicateParser(myPosTags, false);
		String s4 = "(VP (VBZ holds))";
	
		myPP.doParse(0, s4);
		assertTrue(!myPP.noError());
	}
}
