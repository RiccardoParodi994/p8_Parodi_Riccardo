package mainPackage;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import expressionPackage.Expression;
import interpreterPackage.Signals;
import interpreterPackage.PosTags;


public class Main {
	
	public static void printParsedSentence(ArrayList<Expression> toPrint) {

		System.out.println("Inizio stampa sentence strutturata e analizzata semanticamente");
		System.out.println();
		
		int size = toPrint.size();
		
		for(int i=0; i<size; i++) {
			
			System.out.println(); System.out.println("inizio stampa componente # "+i); System.out.println();
			
			toPrint.get(i).print();
			
			System.out.println(); System.out.println("fine stampa componente # "+i); System.out.println();
		}
		
		System.out.println();
		System.out.println("fine");
		
	}
	
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		String signalFile = "signals.txt";
		Signals mySignals = new Signals(signalFile);  
		
//		String requirementsFile = "requirementsF.txt";
		String requirementsFile = "requirementsForFunctionalTesting";
		Requirements myReqs = new Requirements(requirementsFile); 
		
		String posTagFile = "pos-tags.txt";
		PosTags myPosTags = new PosTags(posTagFile); 
		

		
		System.out.println("*****************************************************************************************************************************************");
		
		Parser myParser = new Parser();
		Analyzer myA = new Analyzer(myParser, myPosTags, mySignals, true);
		
		Iterator<String> myIterator = myReqs.getAllReqs().iterator();
		
		while(myIterator.hasNext()) {
			
			myA.analyze(myIterator.next());
			
		}
		
		System.out.println("*****************************************************************************************************************************************");
		
		
		FileWriter fw = new FileWriter("pattern.txt");
		PrintWriter pw = new PrintWriter(fw);
		
		Iterator<String> myIt = myA.getPatterns().iterator();
		while(myIt.hasNext()) {
			pw.println(myIt.next());
		}
		
		pw.close();
		fw.close();
	}

}
