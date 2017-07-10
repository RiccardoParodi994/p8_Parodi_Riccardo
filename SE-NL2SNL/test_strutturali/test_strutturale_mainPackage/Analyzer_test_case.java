package test_strutturale_mainPackage;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import interpreterPackage.PosTags;
import interpreterPackage.Signals;
import mainPackage.Analyzer;
import mainPackage.Parser;

public class Analyzer_test_case {

	public static Analyzer myAnalyzer;
	public static Parser myParser;
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testControll_withNumberOfReq() throws Exception {
		myParser = new Parser();
		String s = "When Tdistance} is less than 3000, {Tapproaching shall {Req5} be set to true";
		myAnalyzer = new Analyzer(myParser, new PosTags("pos-tags.txt"), new Signals("signals.txt"), false);
//		myAnalyzer.analyze(s);
		String controlledReq = myAnalyzer.controll(s, new Signals("signals.txt"));
		String expectedControlledReq = "When {Tdistance} is less than 3000, {Tapproaching} shall  be set to true.";
		assertEquals(expectedControlledReq, controlledReq);
	}

	@Test
	public void testControll_withoutNumberOfReq() throws Exception {
		myParser = new Parser();
		String s = "When Tdistance} is less than 3000, {Tapproaching shall be set to true";
		myAnalyzer = new Analyzer(myParser, new PosTags("pos-tags.txt"), new Signals("signals.txt"), false);
//		myAnalyzer.analyze(s);
		String controlledReq = myAnalyzer.controll(s, new Signals("signals.txt"));
		String expectedControlledReq = "When {Tdistance} is less than 3000, {Tapproaching} shall be set to true.";
		assertEquals(expectedControlledReq, controlledReq);
	}
	
	@Test
	public void testConstructor_successfully_firstWay() throws Exception {
		myParser = new Parser();
		String s = "When {Cgrn} is true, then {Pstop} shall {Req10} be sets true.";
		myAnalyzer = new Analyzer(myParser, new PosTags("pos-tags.txt"), new Signals("signals.txt"), false);
		myAnalyzer.analyze(s);
	}
	
	@Test
	public void testConstructor_successfully_secondWay() throws Exception {
		myParser = new Parser();
		String s = "After {Power-up}, {Tin} are true, if Tdistance} is less than 3000, {Tapproaching shall be set to true";
		myAnalyzer = new Analyzer(myParser, new PosTags("pos-tags.txt"), new Signals("signals.txt"), false);
		myAnalyzer.analyze(s);
	}
	
	@Test
	public void testConstructor_unsuccessfully_secondWay() throws Exception {
		myParser = new Parser();
		String s = "After {Power-up}, {Tin}, {Gopen} are true, if Tdistance} is less than 3000, {Tapproaching shall be set to true";
		myAnalyzer = new Analyzer(myParser, new PosTags("pos-tags.txt"), new Signals("signals.txt"),false);
		myAnalyzer.analyze(s);
	}
}
