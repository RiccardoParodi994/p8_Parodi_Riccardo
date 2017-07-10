package mainPackage;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.FileReader;
import java.util.Scanner;


public class Requirements {
	
	private ArrayList<String> reqs;
	

	public Requirements(String fileName)  throws Exception {
		
		FileReader fileReader = new FileReader(fileName);
		Scanner in = new Scanner(fileReader);
		
		
		reqs = new ArrayList<String>();
		
		while(in.hasNextLine()){
			
			String nextReq = in.nextLine();
			reqs.add(nextReq); 
			
		}
		
		in.close();
		fileReader.close();
		
	}
	
	public ArrayList<String> getAllReqs() {
		return this.reqs;
	}
	
	public String getTheReq(int index){
		
		return reqs.get(index);
	}
	
	public void printReqs(){
		
		System.out.println(); System.out.println(" requirements :"); System.out.println();
		
		Iterator<String> myIt = reqs.iterator();
		while(myIt.hasNext()){
			
			System.out.println(myIt.next());
		}
		
	}
	
}

