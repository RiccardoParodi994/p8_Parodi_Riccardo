package test_funzionale_UC1;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import mainPackage.Main;

public class ScenarioPrincipale_test_case {

	@Before
	public void setUp() throws Exception {
		Main simulatedMain = new Main();
		simulatedMain.main(null);
	}

	@Test
	public void test() throws Exception {
		
		FileReader fr = new FileReader("pattern.txt");
		Scanner in = new Scanner(fr);
		
		//1
		String result = in.nextLine();
		String expectedResult = "Globally, it is never the case that ( { Tapproaching } = 0 ) holds.";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//2
		result = in.nextLine();
		expectedResult = "After ( { Power-up } = 1 ), it is never the case that ( { Tapproaching } = 0 ) holds.";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//3
		result = in.nextLine();
		expectedResult = "After ( { Power-up } = 1 ) and until ( { Pgo } = 0 ), it is never the case that ( { Tapproaching } = 0 ) holds.";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//4
		result = in.nextLine();
		expectedResult = "Before ( { Power-up } = 1 ), it is never the case that ( { Tapproaching } = 0 ) holds.";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//5
		result = in.nextLine();
		expectedResult = "Between ( { Power-up } = 1 ) and ( { Pgo } = 0 ), it is never the case that ( { Tapproaching } = 0 ) holds.";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//6
		result = in.nextLine();
		expectedResult = "Globally, it is always the case that ( { Tapproaching } = 0 ) holds.";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);

		//7
		result = in.nextLine();
		expectedResult = "After ( { Power-up } = 1 ), it is always the case that ( { Tapproaching } = 0 ) holds.";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//8
		result = in.nextLine();
		expectedResult = "After ( { Power-up } = 1 ) and until ( { Pgo } = 0 ), it is always the case that ( { Tapproaching } = 0 ) holds.";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//9
		result = in.nextLine();
		expectedResult = "Before ( { Power-up } = 1 ), it is always the case that ( { Tapproaching } = 0 ) holds.";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//10
		result = in.nextLine();
		expectedResult = "Between ( { Power-up } = 1 ) and ( { Pgo } = 0 ), it is always the case that ( { Tapproaching } = 0 ) holds.";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//11
		result = in.nextLine();
		expectedResult = "Globally, ( { Tapproaching } = 0 ) eventually holds.";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//12
		result = in.nextLine();
		expectedResult = "After ( { Power-up } = 1 ), ( { Tapproaching } = 0 ) eventually holds.";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//13
		result = in.nextLine();
		expectedResult = "After ( { Power-up } = 1 ) and until ( { Pgo } = 0 ), ( { Tapproaching } = 0 ) eventually holds.";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//14
		result = in.nextLine();
		expectedResult = "Before ( { Power-up } = 1 ), ( { Tapproaching } = 0 ) eventually holds.";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//15
		result = in.nextLine();
		expectedResult = "Between ( { Power-up } = 1 ) and ( { Pgo } = 0 ), ( { Tapproaching } = 0 ) eventually holds.";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//16
		result = in.nextLine();
		expectedResult = "Globally,  it is always the case that if ( { Tapproaching } = 1 ) holds, then ( { Gopen } = 0 ) previously held.";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//17
		result = in.nextLine();
		expectedResult = "After ( { Power-up } = 1 ),  it is always the case that if ( { Tapproaching } = 1 ) holds, then ( { Gopen } = 0 ) previously held.";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//18
		result = in.nextLine();
		expectedResult = "After ( { Power-up } = 1 ) and until ( { Pgo } = 0 ),  it is always the case that if ( { Tapproaching } = 1 ) holds, then ( { Gopen } = 0 ) previously held.";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//19
		result = in.nextLine();
		expectedResult = "Before ( { Power-up } = 1 ),  it is always the case that if ( { Tapproaching } = 1 ) holds, then ( { Gopen } = 0 ) previously held.";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//20
		result = in.nextLine();
		expectedResult = "Between ( { Power-up } = 1 ) and ( { Pgo } = 0 ),  it is always the case that if ( { Tapproaching } = 1 ) holds, then ( { Gopen } = 0 ) previously held.";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//21
		result = in.nextLine();
		expectedResult = "Globally,  it is always the case that if ( { Tapproaching } = 1 ) holds, then ( { Tapproaching } = 0 ) eventually holds.";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//22
		result = in.nextLine();
		expectedResult = "After ( { Power-up } = 1 ),  it is always the case that if ( { Tapproaching } = 1 ) holds, then ( { Tapproaching } = 0 ) eventually holds.";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//23
		result = in.nextLine();
		expectedResult = "After ( { Power-up } = 1 ) and until ( { Pgo } = 0 ),  it is always the case that if ( { Tapproaching } = 1 ) holds, then ( { Tapproaching } = 0 ) eventually holds.";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//24
		result = in.nextLine();
		expectedResult = "Before ( { Power-up } = 1 ),  it is always the case that if ( { Tapproaching } = 1 ) holds, then ( { Tapproaching } = 0 ) eventually holds.";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//25
		result = in.nextLine();
		expectedResult = "Between ( { Power-up } = 1 ) and ( { Pgo } = 0 ),  it is always the case that if ( { Tapproaching } = 1 ) holds, then ( { Tapproaching } = 0 ) eventually holds.";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//26
		result = in.nextLine();
		expectedResult = "Globally, it is always the case that if ( { Tapproaching } = 0 ) becomes satisfied, it holds for at least 3 seconds. ";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//27
		result = in.nextLine();
		expectedResult = "After ( { Power-up } = 1 ), it is always the case that if ( { Tapproaching } = 0 ) becomes satisfied, it holds for at least 3 seconds. ";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//28
		result = in.nextLine();
		expectedResult = "After ( { Power-up } = 1 ) and until ( { Pgo } = 0 ), it is always the case that if ( { Tapproaching } = 0 ) becomes satisfied, it holds for at least 3 seconds. ";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//29
		result = in.nextLine();
		expectedResult = "Before ( { Power-up } = 1 ), it is always the case that if ( { Tapproaching } = 0 ) becomes satisfied, it holds for at least 3 seconds. ";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//30
		result = in.nextLine();
		expectedResult = "Between ( { Power-up } = 1 ) and ( { Pgo } = 0 ), it is always the case that if ( { Tapproaching } = 0 ) becomes satisfied, it holds for at least 3 seconds. ";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//31
		result = in.nextLine();
		expectedResult = "Globally, it is always the case that if ( { Tapproaching } = 0 ) becomes satisfied, it holds for less than 3 seconds. ";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//32
		result = in.nextLine();
		expectedResult = "After ( { Power-up } = 1 ), it is always the case that if ( { Tapproaching } = 0 ) becomes satisfied, it holds for less than 3 seconds. ";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//33
		result = in.nextLine();
		expectedResult = "After ( { Power-up } = 1 ) and until ( { Pgo } = 0 ), it is always the case that if ( { Tapproaching } = 0 ) becomes satisfied, it holds for less than 3 seconds. ";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//34
		result = in.nextLine();
		expectedResult = "Before ( { Power-up } = 1 ), it is always the case that if ( { Tapproaching } = 0 ) becomes satisfied, it holds for less than 3 seconds. ";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//35
		result = in.nextLine();
		expectedResult = "Between ( { Power-up } = 1 ) and ( { Pgo } = 0 ), it is always the case that if ( { Tapproaching } = 0 ) becomes satisfied, it holds for less than 3 seconds. ";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//36
		result = in.nextLine();
		expectedResult = "Globally, it is always the case that ( { Tapproaching } = 0 ) holds at least every 3 seconds. ";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//37
		result = in.nextLine();
		expectedResult = "After ( { Power-up } = 1 ), it is always the case that ( { Tapproaching } = 0 ) holds at least every 3 seconds. ";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//38
		result = in.nextLine();
		expectedResult = "After ( { Power-up } = 1 ) and until ( { Pgo } = 0 ), it is always the case that ( { Tapproaching } = 0 ) holds at least every 3 seconds. ";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//39
		result = in.nextLine();
		expectedResult = "Before ( { Power-up } = 1 ), it is always the case that ( { Tapproaching } = 0 ) holds at least every 3 seconds. ";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//40
		result = in.nextLine();
		expectedResult = "Between ( { Power-up } = 1 ) and ( { Pgo } = 0 ), it is always the case that ( { Tapproaching } = 0 ) holds at least every 3 seconds. ";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//41
		result = in.nextLine();
		expectedResult = "Globally,  it is always the case that if ( { Tapproaching } = 1 ) holds , then ( { Gopen } = 0 ) holds after at most 1 minute. ";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//42
		result = in.nextLine();
		expectedResult = "After ( { Power-up } = 1 ),  it is always the case that if ( { Tapproaching } = 1 ) holds , then ( { Gopen } = 0 ) holds after at most 1 minute. ";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//43
		result = in.nextLine();
		expectedResult = "After ( { Power-up } = 1 ) and until ( { Pgo } = 0 ),  it is always the case that if ( { Tapproaching } = 1 ) holds , then ( { Gopen } = 0 ) holds after at most 1 minute. ";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//44
		result = in.nextLine();
		expectedResult = "Before ( { Power-up } = 1 ),  it is always the case that if ( { Tapproaching } = 1 ) holds , then ( { Gopen } = 0 ) holds after at most 1 minute. ";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//45
		result = in.nextLine();
		expectedResult = "Between ( { Power-up } = 1 ) and ( { Pgo } = 0 ),  it is always the case that if ( { Tapproaching } = 1 ) holds , then ( { Gopen } = 0 ) holds after at most 1 minute. ";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//46
		result = in.nextLine();
		expectedResult = "Globally,  it is always the case that if ( { Tapproaching } = 1 ) holds , then ( { Gopen } = 0 ) holds for at least 3 seconds. ";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//47
		result = in.nextLine();
		expectedResult = "After ( { Power-up } = 1 ),  it is always the case that if ( { Tapproaching } = 1 ) holds , then ( { Gopen } = 0 ) holds for at least 3 seconds. ";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//48
		result = in.nextLine();
		expectedResult = "After ( { Power-up } = 1 ) and until ( { Pgo } = 0 ),  it is always the case that if ( { Tapproaching } = 1 ) holds , then ( { Gopen } = 0 ) holds for at least 3 seconds. ";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//49
		result = in.nextLine();
		expectedResult = "Before ( { Power-up } = 1 ),  it is always the case that if ( { Tapproaching } = 1 ) holds , then ( { Gopen } = 0 ) holds for at least 3 seconds. ";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		//50
		result = in.nextLine();
		expectedResult = "Between ( { Power-up } = 1 ) and ( { Pgo } = 0 ),  it is always the case that if ( { Tapproaching } = 1 ) holds , then ( { Gopen } = 0 ) holds for at least 3 seconds. ";
		assertEquals("Non risulta tradotto come previsto", expectedResult, result);
		
		in.close();
		fr.close();
		
	}

}
