package interpreterPackage;

import java.util.ArrayList;

import expressionPackage.Expression;
import expressionPackage.GeneralPhraseExpr;
import expressionPackage.LambdaExpr;
import expressionPackage.StringExpr;

/**
 * Questa classe, implementante il secondo tipo di visitor del progetto,
 * ha lo scopo di costruire la principale (che sarà necessariamente attesa), 
 * un eventuale scope e un eventuale subordinata.
 * Il tutto prendendo in ingresso la "strutturazione semantica" avvenuta con la classe
 * FinalParser e sfruttando la consapevolezza che le suddette "strutturazioni semantiche"
 * rispettano dei modelli frequenti facilmente riconoscibili.
 * 
 * In realtà, però, l'altra vera potenzialità di questa classe è la sua capacità di 
 * raggruppamento dei predicati. Essa infatti tramite un meccanismo di avanzamento 
 * dell'indice dell'arrayList "grouper" riesce a raggruppare i predicati individuati
 * in un primo gruppo, se appartenenti allo scope, in un secondo se appartenenti
 * alla subordinata e ad un terzo se alla principale.
 * Tutto ciò è necessario per permettere alla classe "Associator" di fare un primo 
 * smistamento dei 16 possibili casi di "specification" possibili proprio grazie 
 * al numero di gruppi.
 * 
 * @author Riccardo Parodi
 *
 */
public class FinalVisitor implements GeneralVisitor {

	private PosTags myPT;
	
	//per lo scope
	private String scopePreposition;
	private String scope;
	
	//per la subordinata
	private String subordinatePreposition;
	private String subordinate;
	
	//per la principale
	private String main;

	
	//in generale
	private String and;  private String comma;
	
	private String predicate;
	private String predicateOf0;
	private String predicateOf6;
	
	private ArrayList<ArrayList<Integer>> grouper;  private int indexOfGroup;
	
	//da modificare
	private ArrayList<PredicateStructuring> myPredicates;
	
	//costruttore
	public FinalVisitor(PosTags myPT, ArrayList<PredicateStructuring> myPredicates) {
		
		this.myPT = myPT;
		
		//per lo scope
		this.scopePreposition = null;
		this.scope = null;
		
		//per la subordinata
		this.subordinatePreposition = null;
		this.subordinate = null;
		
		//per la principale
		this.main = null;
		
		//in generale
		this.and = null;  this.comma = null;
		
		this.predicate = null;
		this.predicateOf0 = null;
		this.predicateOf6 = null;
		
		this.grouper = new ArrayList<ArrayList<Integer>>();  
		this.grouper.add(new ArrayList<Integer>());  
		this.indexOfGroup = 0;
		
		//da modificare
		this.myPredicates = myPredicates;
		
	}
	
	
	public boolean doAssociation(GeneralPhraseExpr myS) {
		/**
		 * Prendo quindi in input la "GeneralPhraseExpr" generata dal
		 * parsing della classe "FinalParser" e, facendo un primo smistamento
		 * individuando il tipo di sentence (tra i primi 5 significativi)
		 * procedo con l'esecuzione dell'accept per ognuno dei componenti 
		 * semi-noti (semi perchè ogni tipo ha una sua struttura predeterminata 
		 * con leggere modifiche di sorta).
		 * Così facendo arriverò a comporre correttamente la struttura periodale del 
		 * mio requisito, avrò raggruppato i predicati e sarò quindi a un passo 
		 * dall'associazione requisito-pattern.
		 * 
		 */
	
		String tag = myS.getPhraseTag();
		
		if(this.myPT.isS(tag)) {
			
			int type = myS.getTypeOfSentence();  
			
			
			
			if(type == 0) {
				
				//siamo nel caso (S (P))
				
				//recupero quello che dovrà necessariamente essere il mio predicato
				LambdaExpr p = (LambdaExpr)myS.getComponents().get(0);
				
				//ne eseguo l'accept per memorizzare il predicato
				p.accept(this);
					
				this.scope = "Globally";
				
				
				return true;
				
			}
			else if(type == 1) {
				//siamo nel caso (S (P), (SBAR))
				
				//non è mai capitato un caso di requisito 
				//che generasse questa struttura
				
			}
			else if(type == 2) {
				
				//siamo nel caso (S (SBAR), (P))
				
				//inizio quindi l'analisi del primo elemento che sappiamo essere una SBAR
				GeneralPhraseExpr sbar = (GeneralPhraseExpr)myS.getComponents().get(0);
				
				//quindi ne eseguo l'accept
				sbar.accept(this);
				
				//controllo allora se ho memorizzato lo scope
				if(this.scope != null)  {
					
			
					//incremento l'indice del grouper e aggiungo una nuova componente al grouper
					this.indexOfGroup++;  this.grouper.add(new ArrayList<Integer>());
					
					for(int i=1; i<myS.getComponents().size(); i++) {
						
						Expression expr = myS.getComponents().get(i);   
						
						expr.accept(this);
						
					}
					
				}
				else {
				
					this.scope = "Globally";
					
					
					//incremento l'indice del grouper e aggiungo una nuova componente al grouper
					this.indexOfGroup++;  this.grouper.add(new ArrayList<Integer>());
					
					for(int i=1; i<myS.getComponents().size(); i++) {
						
						Expression expr = myS.getComponents().get(i);   
						
						expr.accept(this);					
					}
				}

				//ritorno positivamente dal parsing
				return true;
				
			}
			else if(type == 3) {
				
				//siamo nel caso (S (SBAR), (SBAR), (P))
				
				//inizio quindi l'analisi del primo elemento che sappiamo essere una SBAR
				GeneralPhraseExpr sbar1 = (GeneralPhraseExpr)myS.getComponents().get(0);
				
				//quindi ne eseguo l'accept
				sbar1.accept(this);
				
				//incremento l'indice del grouper e aggiungo una nuova componente al grouper
				this.indexOfGroup++;  this.grouper.add(new ArrayList<Integer>());
				
				//recupero la comma facendone l'accept
				StringExpr comma = (StringExpr) myS.getComponents().get(1);  comma.accept(this);
				
				//recupero la seconda sbar facendone l'accept
				GeneralPhraseExpr sbar2 = (GeneralPhraseExpr)myS.getComponents().get(2);
				
				sbar2.accept(this);
		
				//incremento l'indice del grouper e aggiungo una nuova componente al grouper
				this.indexOfGroup++;  this.grouper.add(new ArrayList<Integer>());
				
				for(int i=3; i<myS.getComponents().size(); i++) {
					
					Expression expr = myS.getComponents().get(i);   
					
					expr.accept(this);	
				}
				
				//ritorno positivamente dal parsing
				return true;
				
			}			
			else if(type == 4) {
				
				//siamo nel caso (S (PP), (P))
				
				//inizio quindi l'analisi del primo elemento che sappiamo essere una PP
				GeneralPhraseExpr pp = (GeneralPhraseExpr) myS.getComponents().get(0);
				
				//quindi ne eseguo l'accept
				pp.accept(this);
				
				//controllo allora se ho memorizzato lo scope
				if(this.scope != null)  {
								
					//incremento l'indice del grouper e aggiungo una nuova componente al grouper
					this.indexOfGroup++;  this.grouper.add(new ArrayList<Integer>());
					
					for(int i=1; i<myS.getComponents().size(); i++) {
						
						Expression expr = myS.getComponents().get(i);   
						
						expr.accept(this);	
					}
				}
				else {
				
					this.scope = "Globally";
					
					//incremento l'indice del grouper e aggiungo una nuova componente al grouper
					this.indexOfGroup++;  this.grouper.add(new ArrayList<Integer>());
					
					for(int i=1; i<myS.getComponents().size(); i++) {
						
						Expression expr = myS.getComponents().get(i);   
						
						expr.accept(this);	
					}
				}
				
				return true;
			}
			else if(type == 5) {
				
				//siamo nel caso (S (PP), (SBAR), (P))
				
				//inizio quindi l'analisi del primo elemento che sappiamo essere una PP
				GeneralPhraseExpr pp = (GeneralPhraseExpr) myS.getComponents().get(0);
				
				//quindi ne eseguo l'accept
				pp.accept(this);
				
				//incremento l'indice del grouper e aggiungo una nuova componente al grouper
				this.indexOfGroup++;  this.grouper.add(new ArrayList<Integer>());
				
				//recupero la comma facendone l'accept
				StringExpr comma = (StringExpr) myS.getComponents().get(1);  comma.accept(this);
				
				//recupero la seconda sbar facendone l'accept
				GeneralPhraseExpr sbar2 = (GeneralPhraseExpr)myS.getComponents().get(2);
				
				sbar2.accept(this);
		
				//incremento l'indice del grouper e aggiungo una nuova componente al grouper
				this.indexOfGroup++;  this.grouper.add(new ArrayList<Integer>());
				
				for(int i=3; i<myS.getComponents().size(); i++) {
					
					Expression expr = myS.getComponents().get(i);   
					
					expr.accept(this);	
				}
				
				//ritorno positivamente dal parsing
				return true;
				
			}
		}
		
		return false;
	}
	
	
	public void visit(GeneralPhraseExpr toVisit) {
		
		String tag = toVisit.getPhraseTag();
		
		if(this.myPT.isS(tag) || tag.equals("complexS")) {
			
			int type = toVisit.getTypeOfSentence();  
						
			if(type == 0) {

				//siamo nel caso (S (P))
				
				//recupero quello che dovrà necessariamente essere il mio predicato
				LambdaExpr p = (LambdaExpr)toVisit.getComponents().get(0);
				
				//ne eseguo l'accept per memorizzare il predicato
				p.accept(this);
				
				String total0 = null;
				if(this.predicate != null) {

					total0 = this.predicate;  this.predicate = null;
					
					this.predicateOf0 = total0;  total0 = null;		
				}
			}
			else if(type == 1) {
				
				//siamo nel caso (S (P), (SBAR))
			
				//non è mai capitato un caso di requisito 
				//che generasse questa struttura
				
			}
			else if(type == 2) {
				
				//siamo nel caso (S (SBAR), (P))
				
				//non è mai capitato un caso di requisito 
				//che generasse questa struttura
				
			}
			else if(type == 3) {
				
				//siamo nel caso (S (SBAR), (SBAR), (P))
				
				//non è mai capitato un caso di requisito 
				//che generasse questa struttura
				
			}			
			else if(type == 4) {
				
				//siamo nel caso (S (PP), (P))
				
				//non è mai capitato un caso di requisito 
				//che generasse questa struttura
				
			}
			else if(type == 5) {

				//siamo nel caso (S (PP), (SBAR), (P))
				
				//non è mai capitato un caso di requisito 
				//che generasse questa struttura
				
			}
			else if(type == 6) {

				//siamo nel caso (S (S) (and) (S))
				
				//controllo innazzitutto di non essere nel caso di due sotto S di tipo 0
				GeneralPhraseExpr s1 = (GeneralPhraseExpr)toVisit.getComponents().get(0);
				StringExpr operator = (StringExpr)toVisit.getComponents().get(1);
				GeneralPhraseExpr s2 = (GeneralPhraseExpr)toVisit.getComponents().get(2);
				
				//faccio innanzitutto l'accept di s1 e controllo esser di tipo 0
				s1.accept(this);
				
				String p1 = null;  String op = null;  String p2 = null;  String total6 = null;
				if(this.predicateOf0 != null) {

					p1 = this.predicateOf0;  this.predicateOf0 = null;
					
					//ora mando l'accept di operator e mi aspetto di trovare un and o simili
					operator.accept(this);
					
					if(this.and != null) {

						op = this.and; this.and = null;
						
						//a questo punto mando l'accept di s2
						s2.accept(this);
						
						if(this.predicateOf0 != null) {

							p2 = this.predicateOf0;  this.predicateOf0 = null;
						}
						
						//a questo punto costruisco il predicato composto derivato dalla s del sesto tipo
						total6 = p1+" "+op+" "+p2;
						
					}
					
					if(total6 != null) {
						this.predicateOf6 = total6;  total6 = null;
					}
				}
				else if(this.predicateOf6 != null) {

					p1 = this.predicateOf6;  this.predicateOf6 = null;
					
					//ora mando l'accept di operator e mi aspetto di trovare un and o simili
					operator.accept(this);
					
					if(this.and != null) {

						op = this.and; this.and = null;
						
						//a questo punto mando l'accept di s2
						s2.accept(this);
						
						if(this.predicateOf0 != null) {

							p2 = this.predicateOf0;  this.predicateOf0 = null;
						}
						else if(this.predicateOf6 != null) {

							p2 = this.predicateOf6;  this.predicateOf6 = null;
						}
						
						//a questo punto costruisco il predicato composto derivato dalla s del sesto tipo
						total6 = p1+" "+op+" "+p2;
						
					}
					
					if(total6 != null) {
						this.predicateOf6 = total6;  total6 = null;
					}
				}
				
			}

		}
		else if(this.myPT.isSBAR(tag)) {
			
			//recupero la prima componente che non so se sarà una preposition o una wh; La seconda sarà invece necessariamente una sentence
			Expression preposition = toVisit.getComponents().get(0);
			
			GeneralPhraseExpr s = (GeneralPhraseExpr)toVisit.getComponents().get(1);
			
			preposition.accept(this);
			
			String prep = null;  String pred = null;
			
			if(this.scopePreposition != null) {
				
				prep = this.scopePreposition;  this.scopePreposition = null;
				
				//quindi siamo dentro un probabile scope
				
				//eseguiamo l'accept delle conseguente sentence
				s.accept(this);
				
				if(this.predicateOf0 != null) {
					
					pred = this.predicateOf0;  this.predicateOf0 = null;				
				}
				else if(this.predicateOf6 != null) {
					
					pred = this.predicateOf6;  this.predicateOf6 = null;
				}
				
				//quindi costruisco l'ipotetico scope
				this.scope = prep+" "+pred;  prep = pred = null; 
				
			}
			else if(this.subordinatePreposition != null) {
				
				//quindi siamo dentro un probabile scope
				
				//eseguiamo l'accept delle conseguente sentence
				s.accept(this);
				
				if(this.predicateOf0 != null) {

					pred = this.predicateOf0;  this.predicateOf0 = null;
				}
				else if(this.predicateOf6 != null) {

					pred = this.predicateOf6;  this.predicateOf6 = null;
				}
				
				//quindi costruisco 
				this.subordinate = pred;  pred = null;  
	
			}
			
			
		}
		else if(tag.equals("ComplexSBAR")) {
		
			//recupero il primo elemento, che in una complex sbar dovrebbe essere o una SBAR o un'altra complexSBAR
			
			GeneralPhraseExpr first = (GeneralPhraseExpr)toVisit.getComponents().get(0);  
			StringExpr operator = (StringExpr)toVisit.getComponents().get(1);  
			GeneralPhraseExpr second = (GeneralPhraseExpr)toVisit.getComponents().get(2);  
			
			first.accept(this);
			
			String scope1 = null;  String op = null;  String scope2 = null;  boolean isBigScope = false;
			
			if(this.scope != null) {
			
				//ho quindi trovato un'altro scope che presumibilmente sarà o after o before o between
				
				scope1 = this.scope;  this.scope = null;
				
				//mando quindi l'accept per l'operator per recuperare l'and
				operator.accept(this);
				
				if(this.and != null || this.comma != null) {
					
					if(this.and != null) { 	
						op = this.and;  this.and = null; 
					}
					else if(this.comma != null) { 	
						op = this.comma;  this.comma = null; 
					}
					
					//vado quindi a cercare quello che potrebbe essere un secondo scope
					
					//se c'è costruisco il nuovo bigScope altrimenti mi fermo
					second.accept(this);
					
					if(this.scope != null) {
						
						//ho quindi trovato un'altro scope che presumibilmente sarà until
						
						scope2 = this.scope;  this.scope = null;
						
						isBigScope = true;
						
						//allora precedo alla costruzione del nuovo scope
						this.scope = scope1+" "+op+" "+scope2;  scope1 = op = scope2 = null;  
					}
					else {
						
						//...........ci sarà una parte della specification
						isBigScope = false;
						
					}
					
				}
				
				if(!isBigScope) {
					
					//quindi memorizzo lo scope
					this.scope = scope1;  scope1 = null;  
					
				}
				
			}

			
		}
		else if(this.myPT.isPP(tag)) {
			

			//siamo nel caso PP
			
			//prendiamo innanzitutto il primo elemento che sappiamo essere una stringExpr
			StringExpr preposition = (StringExpr)toVisit.getComponents().get(0);

			//poi prendo la seconda parte che so essere o una lambda expr contenente un predicato o una sentence di sesto tipo
			Expression mainPart = toVisit.getComponents().get(1);
			
			
			preposition.accept(this);
			
			//controllo che sia stata correttamente memorizzata e se si entro nella costruzione dello scope
			String prep = null;  String pred = null;
			if(this.scopePreposition != null) {

				prep = this.scopePreposition;  this.scopePreposition = null;
				
				mainPart.accept(this);
				
				//controllo quindi che sia stato recuperato o un predicato semplice di s di tipo 0
				if(this.predicateOf0 != null) {

					pred = this.predicateOf0;  this.predicateOf0 = null;
				}
				else if(this.predicateOf6 != null) {

					pred = this.predicateOf6;  this.predicateOf6 = null;  
				}
				
				//quindi costruisco lo scope identificato
				this.scope = prep+" "+pred;
				
			}
			else {
				
				//..........è qualche parte della specification
				
			}
		
		}
		
	}

	
	public void visit(LambdaExpr toVisit) {
		
		boolean isPredicate = toVisit.isPredicate();  
	
		if(isPredicate) {
			
			//memorizzo quindi il predicato
			
			if(toVisit.getPredicateStructure().isShall()) {
				
				this.main = toVisit.getPredicateStructure().getPredicateP();
				
			}
			else {

				this.predicate = toVisit.getPredicateStructure().getPredicateP();  
			}
			
			//raggruppo
			this.groupPredicates(toVisit.getPredicateStructure().getPosition());
			
			//lo elimino quindi dal mio elenco di predicati
			this.deleteFromPredicates(toVisit.getPredicateStructure().getPosition());
			
		}
	}
	
	public void visit(StringExpr toVisit) {
		
		String tag = toVisit.getPosTag();  String value = toVisit.getValue();
		
		if(this.isScopePreposition(value)) {

			this.scopePreposition = value;
		}
		else if (this.isSubordinatePreposition(value)) {

			this.subordinatePreposition = value;
		}
		else if(value.equals("and")) {
			
			this.and = toVisit.getValue();  
		}
		else if(value.equals(",")) {

			this.comma = toVisit.getValue();
		}
	}
	
	
	
	//methods
	public void deleteFromPredicates(int pos) {
		/**
		 * Segna come visitato il predicato
		 */
		
		for(int i=0;i<this.myPredicates.size();i++) {
			
			if(this.myPredicates.get(i).getPosition() == pos) {
				
				this.myPredicates.get(i).setVisited(true);		
			}	
		}
	}
	
	public void groupPredicates(int pos) {
		/**
		 * Sfruttando l'attributo "position" proprio di ogni
		 * predicato, e precedentemente identificato durante l'esecuzione 
		 * della classe "FinalParser", raggruppo i predicati nella propria zona 
		 * di influenza in base all'avanzamento di "indexOfGroup".
		 *
		 */
		
		this.grouper.get(this.indexOfGroup).add(pos);
	}
	
	/**
	 * 
	 * @param value 
	 * @return se è o no è la preposizione introducente lo Scope
	 */
	public boolean isScopePreposition(String value) {

		return (value.equals("After") 
				|| value.equals("Before") || value.equals("before")
				|| value.equals("Between") || value.equals("until"));	
	}
	
	/**
	 * 
	 * @param value
	 * @return se è o non è la preposizione introducente una subordinata
	 */
	public boolean isSubordinatePreposition(String value) {
		
		return (value.equals("When") || value.equals("when") 
				|| value.equals("if") || value.equals("If"));
	}
	
	//getters
	public String getScope() {
		return this.scope;
	}
	
	public String getSubordinate() {
		return this.subordinate;
	}
	
	public String getSubordinatePreposition() {
		return this.subordinatePreposition;
	}
	
	public String getMain() {
		return this.main;
	}
	
	public ArrayList<PredicateStructuring> getPredicates() {
		return this.myPredicates;
	}
	
	public ArrayList<ArrayList<Integer>> getGrouper() {
		return this.grouper;
	}
	
	/**
	 * 
	 * @return il numero di predicati visitati
	 */
	public int sizeOfNotVisitedPredicates() {
		
		int size = 0;
		
		for(int i=0;i<this.myPredicates.size();i++) {
			
			if(!this.myPredicates.get(i).isVisited()) {
				size++;
			}
		}
		return size;
	}
	
	
	//printer
	public void printPredicates() {
		
		System.out.println("************************************************************************************************************"); 
		
		System.out.println(); System.out.println("Inizio stampa predicati ristrutturati : ");
		
		for(int i=0;i<this.myPredicates.size();i++) {
			
			if(!this.myPredicates.get(i).isVisited()) {
				this.myPredicates.get(i).print();
			}
	
		}
		
		System.out.println(); System.out.println("Fine stampa predicati ristrutturati  ");	

		System.out.println("************************************************************************************************************"); 
	}
}
