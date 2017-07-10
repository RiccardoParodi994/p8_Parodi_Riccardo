package expressionPackage;
import java.util.ArrayList;
import java.util.Iterator;

import interpreterPackage.EvaluationVisitor;
import interpreterPackage.GeneralVisitor;

public class GeneralPhraseExpr extends Expression {

	private String phraseTag;
	private ArrayList<Expression> components;
	
	private int typeOfSentence;
	
	public GeneralPhraseExpr(String pT){
		
		phraseTag = pT;
		components = new ArrayList<Expression>();
		
		typeOfSentence = -1;
		
	}
	
	public ArrayList<Expression> getComponents(){
		return this.components;
	}
	
	public String getPhraseTag(){
		
		return this.phraseTag;
	}
	
	public int getTypeOfSentence() {
		return this.typeOfSentence;
	}

	public void setComponents(ArrayList<Expression> comp){
		this.components = comp;
	}
	
	public void setTypeOfSentence(int type) {
		this.typeOfSentence = type;
	}
	
	public void accept(GeneralVisitor visitor) {
		visitor.visit(this);
	}
	
	public void print(){
	
		if(this.phraseTag.equals("S") || this.phraseTag.equals("complexS") ) {
			System.out.println(); System.out.println("tipo della sentence : "+this.typeOfSentence); 
		}
		
		System.out.print(" { phrase tag : "+phraseTag+" , components : ");
		
		Iterator<Expression> myIt = components.iterator();
		while(myIt.hasNext()){
			myIt.next().print();
		}
		
		System.out.print(" } ");
	
		
	}
	
}
