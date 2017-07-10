package interpreterPackage;

import java.io.FileReader;
import java.util.Scanner;

import java.util.ArrayList;

public class PosTags {
	
	private ArrayList<String> clause;
	private ArrayList<String> phrase;
	private ArrayList<String> word;
	private ArrayList<String> bracket;
	
	
	public PosTags(String fileName) throws Exception{
		
		clause = new ArrayList<String>();
		phrase = new ArrayList<String>();
		word = new ArrayList<String>();
		bracket = new ArrayList<String>();
		
		
		FileReader fileReader = new FileReader(fileName);
		Scanner in = new Scanner(fileReader);
		
		
		String newPosTag = "";
		while(in.hasNextLine()){
			
			
			//salto la prima riga stante per la stringa "clause"
			in.next();
			
			do{
				
				newPosTag = in.next(); 
				if(!newPosTag.equals("Phrase")){
					clause.add(newPosTag);
				}
				
			}while(in.hasNext() && !newPosTag.equals("Phrase"));
			
			
			do{
				
				newPosTag = in.next(); 
				if(!newPosTag.equals("Word")){
					phrase.add(newPosTag); 
				}
				
			}while(in.hasNext() && !newPosTag.equals("Word"));


			
			do{
				
				newPosTag = in.next(); 
				if(!newPosTag.equals("Brackets")){
					word.add(newPosTag); 
				}
			}while(in.hasNext() && !newPosTag.equals("Brackets"));


			
			do{
				
				newPosTag = in.next();
				bracket.add(newPosTag);
				
			}while(in.hasNext());
			
		}
		
		in.close();
		fileReader.close();
		
	}
	
	public boolean isS(String tag) {
		return (tag.equals("S"));
	}
	
	public boolean isSBAR(String tag) {
		
		return (tag.equals("SBAR"));
	}
	
	public boolean isVP(String tag){
		
		return (tag.equals("VP"));
	}

	public boolean isPP(String tag){
		
		return (tag.equals("PP"));
	}	
	
	public boolean isNP(String tag){
		
		return (tag.equals("NP"));
	}
	
	public boolean isADVP(String tag) {
			
		return (tag.equals("ADVP"));
	}
	
	public boolean isWHADVP(String tag) {
		
		return (tag.equals("WHADVP"));
	}
	
	public boolean isWHPP(String tag) {
		
		return (tag.equals("WHPP"));
	}

	public boolean isWHNP(String tag) {
		
		return (tag.equals("WHNP"));
	}
	public boolean isPhraseTag(String tag){
		
		boolean isPhraseTag = false;
		
		for(int i=0; i<phrase.size(); i++){
			
			if(phrase.get(i).equals(tag)){
				isPhraseTag = true;
			}
			
		}
		
		
		return isPhraseTag;
	}
	
	
	public boolean isWordTag(String tag){
		
		boolean isWordTag = false;
		
		for(int i=0; i<word.size(); i++){
			
			if(word.get(i).equals(tag)){
				isWordTag = true;
			}
			
		}
		
		
		return isWordTag;
	}
	
	public boolean isPrepositionWordTag(String tag) {
		
		return (tag.equals("IN"));
	}
	
	public boolean isVerbWordTag(String tag){
		
		String mTag = tag.substring(0,2);
		
		return (mTag.equals("VB") || mTag.equals("MD"));
	}
	
	public boolean isAdverbWordTag(String tag){
		
		return (tag.equals("RB"));
	}	
	
	public ArrayList<String> getClause() {
		return clause;
	}


	public ArrayList<String> getPhrase() {
		return phrase;
	}


	public ArrayList<String> getWord() {
		return word;
	}


	public ArrayList<String> getBracket() {
		return bracket;
	}
	

	public void printPosTags(){
		
		System.out.println();  System.out.println("pos-tags :");  
		
		
		System.out.println();
		for(int i=0;i<clause.size();i++){
			System.out.print(" "+clause.get(i)+" ");
		}
		System.out.println();
		
		
		System.out.println();
		for(int i=0;i<phrase.size();i++){
			System.out.print(" "+phrase.get(i)+" ");
		}
		System.out.println();
		
		
		System.out.println();
		for(int i=0;i<word.size();i++){
			System.out.print(" "+word.get(i)+" ");
		}
		System.out.println();
		
		
		System.out.println();
		for(int i=0;i<bracket.size();i++){
			System.out.print(" "+bracket.get(i)+" ");
		}
		System.out.println();
		
	}

	
}
