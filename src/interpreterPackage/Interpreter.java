package interpreterPackage;

 
import java.util.ArrayList;

import expressionPackage.ComplexLambdaExpr;
import expressionPackage.GeneralPhraseExpr;
import expressionPackage.LambdaExpr;
import expressionPackage.StringExpr;


/**
 * Questa classe ha lo scopo di gestire l'analisi semantica del predicato
 * senza perà effettuarla. 
 * Il suo compito è infatti quello di recuperare eventuali avverbi e sbar 
 * rispettivamente dal parsing della classe "PredicateParser" e dalla classe "VerbPhraseParser",
 * 
 * oppure di gestire la diversa strutturazione del predicato (che comparirà nel pattern associato finale)
 * a seconda che la "verb phrase" sia complessa (VP (VP (and) (VP))) o non,
 * 
 * di istanziare la classe "EvaluationVisitor" per effettuare la visit sulla "lambda Expression"
 * 
 * e infine quello di istanziare la classe "PredicateStructuring" per la strutturazione del predicato.
 *  
 * 
 * @author Riccardo Parodi
 *
 */
public class Interpreter {

	private PosTags posTags;
	
	//Tale arrayList mi permetterà di memorizzare tutti i predicati 
	//che incontro durante il parsing effettuato da "FinalParser"
	private ArrayList<PredicateStructuring> myPredicates;
	
	private StringExpr adverb; 
	private GeneralPhraseExpr sbarVP; 
	
	private boolean noError;
	private boolean secondWay;
	
	
	public Interpreter(PosTags myPosTags){
		
		posTags = myPosTags;
		
		myPredicates = new ArrayList<PredicateStructuring>();
	
		adverb = null;
		sbarVP = null;
		
		noError = true;
		secondWay = false;
	}
	
	
	public int catchThePredicates(String req, int indexToStart, boolean isPronounCase){
		
		
		//istanzio un nuovo parser per il predicato
		PredicateParser myPP = new PredicateParser(posTags, isPronounCase);
		
		if (this.secondWay) {
			myPP.secondWayBe(); 
		}
		
		//recupero l'indice in cui inizia il predicato
		int index = indexToStart;   int finalIndex = -1;
		
		index = myPP.doParsePredicate(index, req);    finalIndex = index;  
		
		if(!myPP.noError()) {
			this.noError = false;
		}
		else {
		
			//controllo subito se fosse stato identificato un avverbio
			this.adverb = myPP.getAdverb();
		
			//controllo l'eventuale sbar + comma e aggiungo il predicatoVP
			this.sbarVP = myPP.getSbarVP();
			
			if(myPP.getPredicateVP() != null) {
				
				//lo recupero
				PredicateStructuring predicateVP = myPP.getPredicateVP();  
				
				//gli setto la posizione
				predicateVP.setPosition(this.myPredicates.size());
				
				//lo aggiungo alla lista
				this.myPredicates.add(predicateVP);
			}
			
			if(myPP.getComplexLambdaExpr() != null || myPP.getLambdaExpr() != null ) {
				
				//recupero la lambdaExpr risultate dal precedente parsing
				
				ComplexLambdaExpr myCL = null;  LambdaExpr myL = null;
				
				if(myPP.isComplex()) {
				
					myCL = myPP.getComplexLambdaExpr();
				
					//se siamo nel caso di un pronome setto il soggetto dell'attuale predicato uguale al predicato dell'ultimo. 
					//Questo perchè in teoria sarà il nome a cui il pronome fa riferimento
					if(isPronounCase) { 
						
						int size = this.getPredicates().size()-1;
						
						if(size >= 0) {
							myCL.setSubject(this.getPredicates().get(size).getLambdaExpr().getSubject()); 
						}
					}
					
					LambdaExpr firstComponent = myCL.getFirst(); firstComponent.setIsPredicate(true);
					LambdaExpr secondComponent = myCL.getSecond(); secondComponent.setIsPredicate(true);
				
					EvaluationVisitor evalForFirst = new EvaluationVisitor();
					firstComponent.accept(evalForFirst); 
					PredicateStructuring first = new PredicateStructuring(firstComponent, evalForFirst);   
					first.setPosition(this.myPredicates.size()); 
					//memorizzo la struttura del predicato dentro la mia LambdaExpr
					first.getLambdaExpr().setPredicateStructure(first);
					this.myPredicates.add(first);
					
					EvaluationVisitor evalForSecond = new EvaluationVisitor();  
					secondComponent.accept(evalForSecond); 
					PredicateStructuring second = new PredicateStructuring(secondComponent, evalForSecond);  
					second.setPosition(this.myPredicates.size()); 
					//memorizzo la struttura del predicato dentro la mia LambdaExpr
					second.getLambdaExpr().setPredicateStructure(second);
					this.myPredicates.add(second);			
				}
				else {
					
					myL = myPP.getLambdaExpr();  myL.setIsPredicate(true);
				
					//se siamo nel caso di un pronome setto il soggetto dell'attuale predicato uguale al predicato dell'ultimo. 
					//Questo perchè in teoria sarà il nome a cui il pronome fa riferimento
					if(isPronounCase) { 
						
						int size = this.getPredicates().size()-1;
						
						if(size >= 0) {	
							myL.setSubject(this.getPredicates().get(size).getLambdaExpr().getSubject()); 
						}
					}
					
					EvaluationVisitor eval = new EvaluationVisitor();
					myL.accept(eval);  
							
					//istanzio un nuovo predicato 
					PredicateStructuring newP = new PredicateStructuring(myL, eval);   newP.setPosition(this.myPredicates.size());  
					
					//memorizzo la struttura del predicato dentro la mia LambdaExpr
					newP.getLambdaExpr().setPredicateStructure(newP);  				
					
					//in teoria dovrei fare un controllo o un'analisi sul predicato e poi aggiungerlo
					this.myPredicates.add(newP);
					
				}
			}	
		}
		
		return finalIndex;
	}
	

	
	public ArrayList<PredicateStructuring> getPredicates() {
		return this.myPredicates;
	}

	public boolean noError() {
		
		return this.noError;
		
	}
	
	public StringExpr getAdverb() {
		return this.adverb;
	}
	
	public GeneralPhraseExpr getSbarVP() {
		return this.sbarVP;
	}
	
	public void secondWayBe() {
		this.secondWay = true;
	}
	
}