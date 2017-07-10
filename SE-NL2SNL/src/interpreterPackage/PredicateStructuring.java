package interpreterPackage;
import java.util.ArrayList;
import java.util.Stack;

import expressionPackage.LambdaExpr;

/**
 * Questa classe è una delle principale per la riuscita della 
 * corretta associazione requisito-pattern.
 * Ha il compito di settare quelle che ho identificato essere
 * proprietà "smistanti". Nel senso che avendo la possibilità di sapere
 * se un requisito, o comunque una parte di esso, ha per esempio un complemento
 * temporale ad esso associato riesco a ridurre il campo degli 80 possibili 
 * pattern associabili al requisito. 
 * E idem per gli altri attributi qui sotto istanziati.
 * 
 * Un secondo, ma comunque necessario, compito che ha tale classe è quello di 
 * passare dalla forma "NL" a "SNL" del predicato.
 * Quindi passare da, per esempio "{Tin} is greater than 3" a "( {Tin} > 3 )" 
 * 
 * @author Riccardo Parodi
 *
 */
public class PredicateStructuring {

	private expressionPackage.LambdaExpr myLambdaExpr;
	
	private boolean isShall;
	private TimeConstraints myTimeCon;
	private String adverbType;
	private boolean isPastTense;
	private String predicateP;
	
	private int position;
	private boolean visited;
	
	private Stack<PredicateStructuring> pAccumulator;

	
	public PredicateStructuring(expressionPackage.LambdaExpr l, EvaluationVisitor eval) {
		
		myLambdaExpr = l;
		
		isShall = this.setIfIsShall(l);
		myTimeCon = new TimeConstraints(l, eval);  
		adverbType = this.setAdverbType(l, eval); 
		isPastTense = this.setVerbalTense(l, eval);
		predicateP = this.setPredicateP(l, eval);
		
		position = -1;
		visited = false;
		
		pAccumulator = new Stack<PredicateStructuring>();
	}
	
	//setters atipici (alcuni ritornano un valore anziché "void")
	
	/**
	 * 
	 * @param l
	 * @return se il predicato contiene "shall"
	 */
	public boolean setIfIsShall(expressionPackage.LambdaExpr l) {
		
		boolean isShall = false;
		
		if(l.getRegentVerb() != null) {
			
			if(l.getRegentVerb().getValue().equals("shall")) {
				isShall = true;
			}
		}
		
		return isShall;
	}
	
	public String setAdverbType(expressionPackage.LambdaExpr l, EvaluationVisitor eval) {
		/**
		 * il metodo ha il solo scopo di recuperare un eventuale avverbio
		 */
		return eval.getAdverb();
	}
	
	
	public boolean setVerbalTense(expressionPackage.LambdaExpr l, EvaluationVisitor eval) {		
		/**
		 * il metodo ha il solo scopo di controllare se è past tense
		 */
		return eval.getPastTenseVerb();
	}
	
	
	public String setPredicateP(expressionPackage.LambdaExpr l, EvaluationVisitor evaluator) {
		
		String predicateP = null;
		
		//prendo l'"evaluetor" già istanziato nella classe "interpreter" chiamante 
		EvaluationVisitor eval = evaluator;
		
		//recupero la "lambda expression" sotto forma di stringa dopo la "evaluation"
		String toBeStructured = eval.getValue(); 
		
		//chiamo il metodo per la sua corretta strutturazione
		predicateP = this.predicatePStructuring(toBeStructured, eval);
		
		return predicateP;
	}
	
	public String predicatePStructuring(String toBeS, EvaluationVisitor eval) {
		/**
		 * In questo metodo prima di tutto controllo se sono nel caso di un soggetto semplice
		 * o in una clause di soggetti (che quindi verrà memorizzata dalla classe "EvaluationVisitor" 
		 * in "conjuction" o "disjunction"). In base a ciò, una volta recuperato l'operatore grazie al
		 * metodo "getOperator()" costruirò il predicato che verrà poi inserito nel pattern finale.
		 */
		
		String structuredP = "";
		
		if(eval.getConjunction() != null || eval.getDisjunction() != null) {
			
			ArrayList<String> helper = new ArrayList<String>();
			
			//recupero innanzitutto l'operatore
			String operator = this.getOperator(toBeS);
			
			if(eval.getConjunction() != null) {
				
				helper = eval.getConjunction();  int size = helper.size();
				//iniziamo la composizione della congiunzione		
			
					
				for(int i=0;i<(size-1);i++) {
					structuredP = structuredP + "( { " + helper.get(i) +" } "+operator+" "+") and ";						
				}

				structuredP = structuredP + "( { " + helper.get(size-1) +" } "+operator+" "+")";

			}
			else {
				
				helper = eval.getDisjunction();  int size = helper.size();
				//iniziamo la composizione della disgiunzione		
									
				for(int i=0;i<(size-1);i++) {
					structuredP = structuredP + "( { " + helper.get(i) +" } "+this.getOperator(toBeS)+" "+") or ";
				}

				structuredP = structuredP + "( { " + helper.get(size-1) +" } "+this.getOperator(toBeS)+" "+")";			
			}
		}
		else {
			
			if(eval.getSubject() != null) {
				
				String subject = eval.getSubject(); 
				
				structuredP = "( { " + subject +" } "+this.getOperator(toBeS)+" "+")";	
			}
		}

		return structuredP;
	}
	
	/**
	 * 
	 * @param toBeStructured
	 * @return l'operatore tradotto
	 */
	public String getOperator(String toBeS) {
		/**
		 * Tale metodo ha il compito di individuare all'interno della "lambda expression"
		 * la presenza di uno dei seguenti quattro casi possibili di "operatori" :
		 * 
		 *  1) "Subject" is greater than "number" 
		 *  2) "Subject" is less than "number"
		 *  3) "Subject" is equals to "number"
		 *  4) "Subject" "verb" "TRUE/FALSE"
		 *  
		 * E, identificato uno dei suddetti quattro casi, tradurli come segue :
		 * 
		 *  1) "Subject" > "number"
		 *  2) "Subject" < "number"
		 *  3) "Subject" = "number"
		 *  4) "Subject" = "TRUE/FALSE"
		 *  
		 */
		

		String operator = null;
		
		for(int i=0;i<toBeS.length();i++) {	
			
			//inizio la ricerca dei 4 casi noti 
			if((i+7)<toBeS.length() && toBeS.substring(i, i+7).equals("greater")) {
				
				operator = "> ";
			
				for(int j=i+7;j<toBeS.length();j++) {
					
					if(Character.isDigit(toBeS.charAt(j))) {
						
						int stop = this.doParseNumber(j, toBeS);
						operator = operator + toBeS.substring(j, stop);
						return operator;
					}
				}				
			}
			
			if((i+4)<toBeS.length() && toBeS.substring(i, i+4).equals("less")) {
				
				operator = "< ";
			
				for(int j=i+4;j<toBeS.length();j++) {
					
					if(Character.isDigit(toBeS.charAt(j))) {	
						
						int stop = this.doParseNumber(j, toBeS);
						operator = operator + toBeS.substring(j, stop);
						return operator;
					}	
				}		
			}
			
			if((i+5)<toBeS.length() && toBeS.substring(i, i+5).equals("equal")) {
				
				operator = "= ";
			
				for(int j=i+5;j<toBeS.length();j++) {
					
					if(Character.isDigit(toBeS.charAt(j))) {
						
						int stop = this.doParseNumber(j, toBeS);
						operator = operator + toBeS.substring(j, stop);
						return operator;
					}
				}
			}
			
			//cerco di vedere se c'è qualche verbo che possa anticipare la presenza di "true" o "false"
			if((i+7)<toBeS.length() && (toBeS.substring(i, i+2).equals("be")  || toBeS.substring(i, i+4).equals("hold")
					|| toBeS.substring(i, i+3).equals("set") || toBeS.substring(i, i+7).equals("becomes") 
					|| toBeS.substring(i, i+2).equals("is") || toBeS.substring(i, i+3).equals("are"))) {
				
				operator = "= ";
			
				for(int j=i;j<toBeS.length();j++) {
					
					if((j+2)<toBeS.length() && toBeS.substring(j, j+2).equals("to")) {
										
						for(int k=j+2;k<toBeS.length();k++) {
							
							if((k+4)<toBeS.length() && toBeS.substring(k, k+4).equals("true")) {							
								operator = operator +"1";
								return operator;
							}
							else if((k+5)<toBeS.length() && toBeS.substring(k, k+5).equals("false")) {
								operator = operator +"0";
								return operator;
							}
						}
						
					}
					
					if((j+4)<toBeS.length() && toBeS.substring(j, j+4).equals("true")) {
					
						operator = operator +"1";
						return operator;
						
					}
					else if((j+5)<toBeS.length() && toBeS.substring(j, j+5).equals("false")) {
	
						operator = operator +"0";						
						return operator;
						
					}	
				}	
			}
		
		}

		operator = toBeS;
		
		return operator;
	}
	
	
	public int doParseNumber(int i, String text) {
		
		int j = i;

		//questo while è necessario per l'analisi completa del numero
		while (j < text.length() && Character.isDigit(text.charAt(j))) {
			j = j + 1;
		}
		return j;
	}
	
	//getters
	public LambdaExpr getLambdaExpr() {
		return this.myLambdaExpr;
	}
	
	public String getPredicateP() {
		return this.predicateP;
	}
	
	public boolean isShall() {
		return this.isShall;
	}
	
	public String getAdverb() {
		return this.adverbType;
	}
	
	public TimeConstraints getTimeConstraints() {
		return this.myTimeCon;
	}
	
	public boolean isVisited() {
		return this.visited;
	}
	
	public int getPosition() {
		return this.position;
	}
	
	public boolean isPastTense() {
		return this.isPastTense;
	}
	
	public boolean hasTempComplement() {
		return (this.myTimeCon.getNum() > 0 && this.myTimeCon.getType()>0);
	}
	
	//setters tipici
	public void setPosition(int pos) {
		this.position = pos;
	}
	
	
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	
	//printer
	public void print() {
		
		System.out.println(); System.out.println("Inizio stampa del predicato a strutturazione avvenuta : "); System.out.println();
		
		System.out.println("Is shall ? "+this.isShall);
		
		System.out.print("My temporal complement : "+myTimeCon.getNum()+" , : "+myTimeCon.getType()+" ");
		if(myTimeCon.getTempComplement() != null) { System.out.print(" and than : "+myTimeCon.getTempComplement()); } System.out.println();
		
		if(this.adverbType != null) {System.out.println("My adverb type : "+this.adverbType); } else { System.out.println("no adverb!");}
		
		System.out.println("Is past tense : "+this.isPastTense);
		
		System.out.println("My predicate P restructured : "+this.predicateP);

		System.out.println(); System.out.println("Fine stampa del predicato a strutturazione avvenuta "); System.out.println();
	}
	
}
