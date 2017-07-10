package test_funzionale_UC1;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import mainPackage.Main;

public class ScenarioAlternativo2a_test_case {

	@Before
	public void setUp() throws Exception {
		Main simulatedMain = new Main();
		simulatedMain.main(null);
	}

	@Test
	public void test()  throws Exception {
		
		
		FileReader fr = new FileReader("pattern.txt");
		Scanner in = new Scanner(fr);
		
		//scorro fino all'ultimo requisito intraducibile 
		int i = 0;
		while(in.hasNextLine() && i<50) {
			in.nextLine();
			i++;
		}
		
		String expectedResult = "il parsing semantico è fallito nuovamente. Impossibile effetturare l'associazione requisito pattern. ";
		String result = in.nextLine();
		assertEquals("doveva esserci un messaggio di errore", expectedResult, result);
		
		in.close();
		fr.close();
	}

}
