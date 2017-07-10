package interpreterPackage;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.FileReader;
import java.util.Scanner;

public class Signals {
	
	private ArrayList<Signal> signalsFromFile;
	
	public Signals(String fileName) throws Exception{
		
		
		FileReader fileReader = new FileReader(fileName);
		Scanner in = new Scanner(fileReader);
	
		signalsFromFile = new ArrayList<Signal>();

		while(in.hasNext()){
			signalsFromFile.add(new Signal(in.next(), null));
		}
		in.close();
		fileReader.close();
		
	}
	
	//getter
	public ArrayList<Signal> getSignalsFromFile(){
		return signalsFromFile;
	}
	
//	public String getTypeOfSignal(String signalName){
//		
//		String typeOfSignal = "";
//		
//		Iterator<Signal> myIt = this.signalsFromFile.iterator();
//		while(myIt.hasNext()){
//			
//			Signal signal = myIt.next();
//			if(signal.getName().equals(signalName)){
//				typeOfSignal = signal.getType();
//			}
//		}
//		
//		return typeOfSignal;
//	}
	
	//printer
	public void printSignals(){
		
		Iterator<Signal> myIt = this.signalsFromFile.iterator();
		while(myIt.hasNext()){
			Signal myHelperSignal = myIt.next();
			System.out.println(myHelperSignal.getName()+" - "+myHelperSignal.getType());
		}
		
	}
	
	

}
