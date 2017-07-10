package test_strutturale_interpreterPackage;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import expressionPackage.LambdaExpr;
import interpreterPackage.EvaluationVisitor;
import interpreterPackage.TimeConstraints;

public class TimeConstraints_test_case {

	public static TimeConstraints myTC;
	public static LambdaExpr myLE;
	public static EvaluationVisitor myEval;
	
	@Before
	public void setUp() throws Exception {
		myLE = new LambdaExpr(null, null, null, null, null, null);
		myEval = new EvaluationVisitor();
	}

	@Test
	public void testTypeCheck_14_complete() {
		
		myTC = new TimeConstraints(myLE, myEval);
		String s = "be sets to true after at least every 3 minutes";
		myTC.typeCheck(s);
		assertEquals(14, myTC.getType());
	}

	@Test
	public void testTypeCheck_14_uncomplete() {
		
		myTC = new TimeConstraints(myLE, myEval);
		String s = "be sets to true after every 1 minute";
		myTC.typeCheck(s);
		assertEquals(14, myTC.getType());
	}
	
	@Test
	public void testTypeCheck_15_complete() {
		
		myTC = new TimeConstraints(myLE, myEval);
		String s = "be sets to true after at most 3 seconds";
		myTC.typeCheck(s);
		assertEquals(15, myTC.getType());	
	}

	@Test
	public void testTypeCheck_15_uncomplete() {
		
		myTC = new TimeConstraints(myLE, myEval);
		String s = "be sets to true after 1 second";
		myTC.typeCheck(s);
		assertEquals(15, myTC.getType());	
	}
	
	@Test
	public void testTypeCheck_unsuccessfully() {
		
		myTC = new TimeConstraints(myLE, myEval);
		assertEquals(-1, myTC.getType());	
	}

}
