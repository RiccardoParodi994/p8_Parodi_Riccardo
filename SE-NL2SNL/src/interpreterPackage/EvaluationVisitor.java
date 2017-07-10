package interpreterPackage;

import java.util.ArrayList;
import java.util.Stack;
import expressionPackage.StringExpr;
import expressionPackage.Expression;
import expressionPackage.GeneralPhraseExpr;
import expressionPackage.LambdaExpr;


/**
 * Tale classe è sicuramente poco efficace da un punto di vista
 * di valorizzazione del design patter qui utilizzato.
 * 
 * All'interno di essa mi cimento innanzitutto nell'opera 
 * di memorizzazione degli attributi propri di una "lambda expression"
 * senza fare analisi di alcun tipo.
 * Quest'ultime verranno poi, invece, effettuate
 * dalla classe "Predicate Structuring".
 * 
 * @author Riccardo Parodi
 *
 */
public class EvaluationVisitor implements GeneralVisitor {

	private Stack<String> accumulator;
	
	//servono per memorizzare ogni singola componente
	//della "lambda expression" considerata
	private String subject;
	private boolean pastTenseVerb;
	private String adverb;
	private String tempComp;
	private String comp1; private String comp2; 
	
	//mi serve per la gestione di clause di soggetti
	private ArrayList<String> conjunction;
	private ArrayList<String> disjunction;
	
	
	//costruttore
	public EvaluationVisitor() {
	
		accumulator = new Stack<String>();
	
		//servono per memorizzare ogni singola componente
		//della "lambda expression" considerata
		subject = null;
		pastTenseVerb = false;
		adverb = null; 
		tempComp = null;
		comp1 = null;
		comp2 = null;
		
		//mi serve per la gestione di clause di soggetti
		conjunction = null;
		disjunction = null;
		
	}
	
	public void visit(StringExpr toVisit) {	
		
		if(toVisit.getPosTag().equals("subject")) {
			this.subject = toVisit.getValue();  
		}
		else {
			accumulator.push(toVisit.getValue());
		}
	}
	
	
	public void visit(GeneralPhraseExpr toVisit) {

		String tag = toVisit.getPhraseTag();  

		if(tag.equals("superSubject")) {
			
			ArrayList<String> helper = new ArrayList<String>();
			
			boolean conj = true; 
			
			ArrayList<Expression> components = toVisit.getComponents();
			for(int i=0;i<components.size();i++) {
				
				//recupero il componente
				StringExpr component = (StringExpr)components.get(i);
				
				//recupero il valore
				String value = component.getValue();
				
				
				if(value.equals("or")) {
					conj = false;  
				}
				
				if(!value.equals("and") && !value.equals("or") && !value.equals(",") ) {
					
					//ne eseguo l'accept per accumularlo nel mio stack di riferimento
					component.accept(this);
				
					if(this.subject != null) {
						helper.add(this.subject);  this.subject = null;
					}
				}	
			}
			
			//controllo se è una conjuction o disjunction
			if(conj) {
				
				//gli assegno la mia lista di subjects
				this.conjunction = helper;  
			}
			else {
				
				//gli assegno la mia lista di subjects
				this.disjunction = helper;  
			}
			
		}
		else {			
			
			ArrayList<Expression> components = toVisit.getComponents();
			
			//eseguo un primo for per accumulare i miei componenti della generalPhrase nello stack
			for(int i=0;i<components.size();i++) {
				
				//recupero il componente
				Expression component = components.get(i);
				
				//ne eseguo l'accept per accumularlo nel mio stack di riferimento
				component.accept(this);
			}
			
			String total = "";
			
			//ne eseguo quindi un secondo per recuperarli e comporre una stringa 
			for(int i=0;i<components.size();i++) {
				
				String part = accumulator.pop();

				total = part+" "+total;
				
			}
			
			accumulator.push(total);		
		}
	}
	
	
	public void visit(LambdaExpr toVisit) {
		
		String total = ""; 
		
		//memorizzo ogni singola parte 
		
		if(toVisit.getSubject() != null) {
			toVisit.getSubject().accept(this);
		}
		
		
		if(toVisit.getRegentVerb() != null) {
			
			//recupero il verbo
			String verb = toVisit.getRegentVerb().getValue();
			
			//ora eseguo un controllo (probabilmente blando 
			//ed inefficace) per capire se c'è almeno un verbo al passato nelle varie sotto lambda expr
			String verbTag = toVisit.getRegentVerb().getPosTag();
			
			if(verbTag.equals("VBD")) {
				
				this.pastTenseVerb = true;
			}
			
			total = total+" "+verb;
		}
		
		if(toVisit.getAdverb() != null) {
			
			StringExpr adv = toVisit.getAdverb();
			
			adv.accept(this);
			
			String adverb = this.getValue();  this.adverb = adverb;
			
		}
		
		if(toVisit.getComplement1() != null) {
			
			Expression exprComp1 = toVisit.getComplement1();
			
			//lo accumulo
			exprComp1.accept(this);
			
			String comp1 = this.getValue();  this.comp1 = comp1; 
			
			total = total+" "+comp1;
		}
		
		
		if(toVisit.getComplement2() != null) {
			
			Expression exprComp2 = toVisit.getComplement2();
			
			//lo accumulo
			exprComp2.accept(this);
			
			String comp2 = this.getValue();  this.comp2 = comp2; 
			
			total = total+" "+comp2;  
		}
		
		if(toVisit.getTemporalComplement() != null) {
			//Questo if lo utilizzo per memorizzare nella corretta posizione il temporal complement
			
			Expression exprTempComp = toVisit.getTemporalComplement();
			
			//lo accumulo
			exprTempComp.accept(this);
			
			if(this.tempComp == null) {
				this.tempComp = this.getValue();  
			}
		}
		
		accumulator.push(total);
	}
	
	
	
	/**
	 * Metodo per l'interrogazione del valore dell'espressione.
	 * @return Il valore sulla cima dello stack
	 */
	public String getValue() {
		
		return accumulator.pop();
	}

	//getters
	public String getAdverb() {
		return this.adverb;
	}
	
	public StringExpr getComp1() {
		String help = this.comp1; StringExpr c1 = null;
		if(help != null) {
			c1 = new StringExpr(this.comp1, "");
		}
		return c1;
	}

//	public StringExpr getComp2() {
//		String help = this.comp2; StringExpr c2 = null;
//		
//		if(help != null) {			
//			c2 = new StringExpr(this.comp2, "");
//		}
//		return c2;
//	}
	
	public StringExpr getTempComp() {
		String help = this.tempComp; StringExpr tC = null;
		if(help != null) {
			tC = new StringExpr(help, "");
		}
		return tC;
	}
	
	public boolean getPastTenseVerb() {
		return this.pastTenseVerb;
	}
	
	public String getSubject() {
		return this.subject;
	}
	
	public ArrayList<String> getConjunction() {
		return this.conjunction;
	}
	
	public ArrayList<String> getDisjunction() {
		return this.disjunction;
	}
}
