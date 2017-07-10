package interpreterPackage;

import expressionPackage.ComplexLambdaExpr;
import expressionPackage.Expression;
import expressionPackage.GeneralPhraseExpr;
import expressionPackage.LambdaExpr;
import expressionPackage.StringExpr;

public class PredicateParser {

	
	private boolean secondWay;
	private boolean isPronounCase;
	private boolean foundVP;
	
	
	private StringExpr subject;
	private GeneralPhraseExpr superSubject;
	
	private StringExpr adverb;
	
	private GeneralPhraseExpr sbarVP; 
	private PredicateStructuring predicateVP;
	
	private StringExpr comma;
	private StringExpr and;  private StringExpr or; 
	
	private LambdaExpr lambdaExpr;
	private ComplexLambdaExpr cLambdaExpr;
	private boolean complex;
	
	
	private PosTags myPosTags;
	private boolean noError;
	
	
	public PredicateParser(PosTags myP, boolean isPronounCase) {
	
		this.secondWay = false;
		
		this.isPronounCase = isPronounCase;
		
		this.foundVP = false;
		
		this.subject = null;
		this.superSubject = null;
		
		this.adverb = null;
		
		this.sbarVP = null;
		this.predicateVP = null;
		
		this.comma = null; this.and = null; this.or = null;
		
		this.lambdaExpr = null;
		this.cLambdaExpr = null;
		this.complex = false;
		
		
		this.myPosTags = myP;
		this.noError = true;
	}
	
	public int doParsePredicate(int index, String req) {
		
		int i = index;
		
		 
		if(!this.isPronounCase) {
			i = this.doParse(i, req);  
		}
		else {
			//salto la parte di analisi del soggetto e passo direttamente alla VP
		}
		
		//cerco il VP
		if(i<req.length() && req.charAt(i) == ' ') {
			
			i = this.doParse(i+1, req);  
			
			if(!this.foundVP) {
				
				//quindi non ho trovato la vp attesa
				this.noError = false;
				
			}
			
		}
		
		return i;
	}
	
	public int doParse(int index, String req) {
		
		
		int i = index;
		
		if(i < req.length()) {
		
			if(this.controller(i, req)) {
				
				i = i+1;
				
				if(Character.isUpperCase(req.charAt(i))) {
					
					String tag = doParseTag(i, req);
					
					i = i+tag.length()+1;
					
					
					if(myPosTags.isNP(tag)) {
						
						//sono dentro la NP
						
						if(req.substring(i, i+7).equals("(NP (RB")) {
							
							//sono dentro alla (NP (NP (RB then)) (PRN (-LRB- -LCB-) (NP (NN)) (-RRB- -RCB-)))

							//mi aspetto di trovare un avverbio
							
							if(req.substring(i, i+4).equals("(NP ")) {
								i = this.doParse(i+4, req);
								
								//controllo la fine di NP
								i=this.checkCorrectEnd(req, i);
							}
							
							//uscito dal precedente if dovrei aver trovato l'avverbio
							if(this.adverb != null) {
								
								//perfetto, possiamo procedere. Non memorizzo l'avverbio perchè lo rimando al chiamante
								
								if(req.substring(i,i+6).equals(" (PRN ")) {
									
									i = i+5;
									
									//a questo punto rimando la ricorsione perchè legga (PRN (-LRB- -LCB-) (NP (NN)) (-RRB- -RCB-))
									i = this.doParse(i+1, req);
									
									//controllo la fine del (PRN
									i = this.checkCorrectEnd(req, i);
								}
								
								//controllo la fine del'NP generale e ritorno
								i = this.checkCorrectEnd(req, i);
								
								return i;
							}
//							else {
//								
//								this.noError = false;
//								return i;
//							}
							
						}
						else if(i+13<req.length() && req.substring(i, i+13).equals("(-LRB- -LCB-)")) {
							
							//controllo se ho subito un NP del tipo {subject}						
							
							i = i+13;
							
							//salto lo spazio bianco
							i=i+1;
							
							i = this.doParse(i, req); 
							
							boolean endOfSub = false;
							if(req.charAt(i) == ' ') {
								i = i+1;
							}
//							else if(req.charAt(i) == ')') {
//								
//								//non so bene come mai,
//								//ma tante volte capita che il parser di stanford concluda così la seguente : ({ (NNP)) }
//								
//								i = this.checkCorrectEnd(req, i); 
//								
//								endOfSub = true;
//								
//								if(req.charAt(i) == ' ') {
//									i = i+1;
//								}
//							}
							else {
								
								this.noError = false;
								return i;
							}
							
							
							if(i+13<req.length() && req.substring(i, i+13).equals("(-RRB- -RCB-)")) {
								i = i+13; 
							}
							else {
								
								this.noError = false;
								return i;
							}
							
							//controllo di aver concluso correttamente il mio NP di tipo {subject}
							if(!endOfSub) {
								i = this.checkCorrectEnd(req, i);
							}
							
							return i;
							
						}
						else {
							
							
							//significa che ho una clause di NP
							StringExpr s1 = null;  StringExpr s2 = null;  StringExpr s3 = null;
							StringExpr comma1 = null;  
							StringExpr or1 = null;  StringExpr or2 = null;
							StringExpr and1 = null;  StringExpr and2 = null;  //mancano gli ipotetici or
			
							
							//vado a cercare il prossimo elemento
							i = this.doParse(i, req);
							
							
							//dovrà essere necessariamente un soggetto
							if(this.subject != null) {
								s1 = this.subject;  this.subject = null;  
							}
							else {
								
								this.noError = false;
								return i;
								
							}
							
							//a questo punto mi aspetto uno spazio
							if(req.charAt(i) == ' ') {
								i = this.doParse(i+1, req);
							}
							
							//cerco ciò che ho trovato
							if(this.comma != null) {
								comma1 = this.comma; this.comma = null;
							}
							else if(this.and != null) {
								and1 = this.and;  this.and = null;
							}
							else if(this.or != null) {
								//non è mai capitato
								or1 = this.or;  this.or = null;
							}
							
							//a questo punto mi aspetto uno spazio
							if(req.charAt(i) == ' ') {
								
								i = this.doParse(i+1, req);
								
								//dovrà essere necessariamente un soggetto
								if(this.subject != null) {
									s2 = this.subject;  this.subject = null;  
								}
								else {

									this.noError = false;
									return i;
									
								}
							}
							
							
							//a questo punto mi aspetto uno spazio
							if(req.charAt(i) == ' ') {
								
								i = this.doParse(i+1, req);
								
								//cerco ciò che ho trovato
								if(this.and != null) {
									and2 = this.and;  this.and = null;
								}
								else if(this.or != null) {
									//non è mai capitato
									or2 = this.or;  this.or = null;
								}

							}
							
							//a questo punto mi aspetto uno spazio
							if(req.charAt(i) == ' ') {
								
								i = this.doParse(i+1, req);
							
								//dovrà essere necessariamente un soggetto
								if(this.subject != null) {
									s3 = this.subject;  this.subject = null;
								}
								else {
									this.noError = false;
									return i;
								}
								
							}
							
							//a questo punto costruisco il mio super subject
							this.superSubject = new GeneralPhraseExpr("superSubject");
							
							
							//il primo soggetto lo memorizzo necessariamente
							this.superSubject.getComponents().add(s1);  s1 = null;
							
							if(comma1 != null) { 
								this.superSubject.getComponents().add(comma1); comma1 = null; 
							} 
							else if(and1 != null) { 
								this.superSubject.getComponents().add(and1); and1 = null; 
							}
							else if(or1 != null) {
								this.superSubject.getComponents().add(or1); or1 = null; 
							}
							
							//memorizzo il secondo necessariamente
							this.superSubject.getComponents().add(s2);  s2 = null;
							
							if(and2 != null) { 
								this.superSubject.getComponents().add(and2); and2 = null; 
							}
							else if(or2 != null) {
								this.superSubject.getComponents().add(or2); or2 = null; 
							}
							
							//vedo se c'è n'è un terzo
							if(s3 != null) { 
								this.superSubject.getComponents().add(s3); s3 = null; 
							} 
							
							
							//controllo la corretta fine
							i = this.checkCorrectEnd(req, i);
							
							return i;
							
						}
						
					}
					else if(myPosTags.isWordTag(tag) && !tag.equals("CC")) {
						
						
						//mi trovo quindi dentro una Word
						
						//memorizzo la mia word ed il relativo tag
						String word = doParseWord(i, req); 
						
						if(myPosTags.isAdverbWordTag(tag)) {
							this.adverb = new StringExpr(word,tag);
						}
						else {
							//setto il Segnale del predicato considerato
							this.subject = new StringExpr(word, "subject"); 
						}
						
						
						//incremento quindi il mio indice della lunghezza della word trovata 
						i = i+word.length();
						
						i = this.checkCorrectEnd(req, i);
						
						
						return i;
						
					}
					else if(myPosTags.isVP(tag)) {
						
						//vado al parsing della VP attesa e recupero il soggetto precedentemente identificato
						
						Expression subject = null;
						
						if(!this.isPronounCase) {
							
							if(this.subject!= null) {
								subject = this.subject; 
							}
							else if(this.superSubject != null) {
								subject = this.superSubject; 
							}
							else {
								this.noError = false;
								return i;
							}
							
						}
						
						VerbPhraseParser myVPP = new VerbPhraseParser(myPosTags, subject);

						if(this.secondWay) {
							myVPP.secondWayBe();  
						}
						
						
						//torno indietro per farne il corretto parsing
						i = i-4;
						
						i = myVPP.doParse(i, req);
					
						//assegno quindi la lambdaExpr al mio attributo per la sua memorizzazione
					
						if(myVPP.isComplex()) {
							this.cLambdaExpr = (ComplexLambdaExpr)myVPP.getLambdaExpr();  this.complex = myVPP.isComplex();
						}
						else {
							this.lambdaExpr = (LambdaExpr)myVPP.getLambdaExpr();  this.complex = myVPP.isComplex();
						}
						
						//recupero eventuali ", + sbar"
						if(myVPP.getComma() != null && myVPP.getSbar() != null && myVPP.getPredicate() != null) {
							
							this.sbarVP = myVPP.getSbar();
							
							this.predicateVP = myVPP.getPredicate();
							
						}
						
						
						//controllo che sia andata a buon fine
						if(!myVPP.noError()) {
							
							this.noError = false;
							
						}
					
						this.foundVP = true;
						
						//ritorno al chiamante
						return i;
						
					}
					else if(tag.equals("CC")) {

						String value = this.doParseWord(i, req);
						
						if(value.equals("and")) {
							this.and = new StringExpr(value, tag);
						}
						else if(value.equals("or")) {
							this.or = new StringExpr(value, tag);
						}
						
						i = i+value.length();
						
						//verifico la correttezza della fine 
						i = this.checkCorrectEnd(req, i);
						
						return i;
						
					}
					
				}
				else if(req.charAt(i) == ',') {
					
					
					String tag = ",";  String comma = ",";
					
					StringExpr commaExpr = new StringExpr(comma, tag);
					
					//incremento il contatore
					i = i +3;
					
					//aggiungo il componente nella mia sentence
					this.comma = commaExpr;
					
					
					//verifico la correttezza della fine 
					i = this.checkCorrectEnd(req, i);

					
					return i;
					
				}
			}
			else if(i+13<req.length() && req.substring(i, i+13).equals("(-LRB- -LCB-)")) {
				
				i = i+13;
				
				//salto lo spazio bianco
				i=i+1;
				
				if(req.substring(i, i+3).equals("(NP")) {
					i = i+3;
					
					if(req.charAt(i) == ' ') {
						i=i+1;
					}
				}
				
				i = this.doParse(i, req); 

				
				//controllo la fine della NP
				if(req.charAt(i) == ')') {
					
					i = this.checkCorrectEnd(req, i);
					
					if(req.charAt(i) == ' ') {
						i = i+1;
					}
				}
				else {
					this.noError = false;
					return i;
				}
				
				if(req.substring(i, i+13).equals("(-RRB- -RCB-)")) {
					i = i+13; 
				}
				else {
					this.noError = false;
					return i;
				}
						
				return i;
				
			}
		}
		return i;
	}
	
	
	
	/**
	 * 
	 * @param i
	 * @param req
	 * @return tag
	 */
	public String doParseTag(int i, String req){
		
		String tag = null;
		
		int j=i;
		while(Character.isUpperCase(req.charAt(i))){
			
			i++;
		}
		
		tag = req.substring(j, i) ;
		
		return tag;
	}
	
	/**
	 * 
	 * @param i
	 * @param req
	 * @return parsedWord
	 */
	public String doParseWord(int i, String req){
		
		String word = null;
		
		int j=i;
		while(req.charAt(i) != ')'){
			
			i++;
		}
		
		word = req.substring(j, i) ;
		
		return word;
	}
	
	/**
	 * 
	 * @param i
	 * @param req
	 * @return condition
	 */
	public boolean controller(int i, String req) {
		
		int l = req.length();
		
		boolean condition;
		
		if(i+13<l) { condition = (req.charAt(i)== '(' && !req.substring(i, i+13).equals("(-LRB- -LCB-)")); }
		else { condition = (req.charAt(i)== '('); }
		
		return condition;
	}
	
	//getters
	public LambdaExpr getLambdaExpr() {
		return this.lambdaExpr;
	}
	
	public ComplexLambdaExpr getComplexLambdaExpr() {
		return this.cLambdaExpr;
	}
	
	public boolean isComplex() {
		return this.complex;
	}
	
	public boolean noError() {
		return this.noError;
	}
	
	public GeneralPhraseExpr getSbarVP() {
		return this.sbarVP;	
	}
	
	public PredicateStructuring getPredicateVP() {
		return this.predicateVP;
	}

	public StringExpr getAdverb() {
		return this.adverb;
	}

	public void secondWayBe() {
		this.secondWay = true;
	}
	
	
	//metodo per la corretta terminazione
	public int checkCorrectEnd(String req, int j) {
		
		int i = j;
		
		if (i >=  req.length()) {
			
			this.noError = false;
			return req.length();
		}
		else if (req.charAt(i) != ')') {
		
			this.noError = false;
			return req.length();
		}
		else if(req.charAt(i) == ')') {
			
			
			//quindi incremento di 1
			i = i+1;
		}
//		else {
//			
//			this.noError = false;
//			return req.length();
//		}
		
		return i;
	}
}
