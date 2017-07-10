package interpreterPackage;
import expressionPackage.ComplexLambdaExpr;

import expressionPackage.Expression;
import expressionPackage.GeneralPhraseExpr;
import expressionPackage.LambdaExpr;
import expressionPackage.StringExpr;

import java.util.Stack;

/**
 * Tale classe ha lo scopo di analizzare una "Verb Phrase" in tutto il suo contenuto.
 * Durante l'analisi della "Verb Phrase" ho l'obiettivo di identificare le componenti
 * di quella che sarà una "lambda expression".
 * 
 * Per "lambda expression" intendo una struttura atta a memorizzare
 * un soggetto (precedentemente individuato e memorizzato), un verbo, 
 * un eventuale complemento oggetto/ complemento generico,  
 * un eventuale secondo complemento e (essendo che nell'associazione requisiti-pattern
 * ha un importanza notevole) un eventuale complemento di tempo.
 * Sia chiaro però che tale analisi spesso incappa in ricorsioni in quanto, 
 * un complemento, se dotato di verbo, viene a sua volta considerato come "lamda expression". 
 * 
 * 
 * @author Riccardo Parodi
 *
 */
public class VerbPhraseParser {
	
	//nei due seguenti attributi vi memorizzo i miei unici due parametri passati al costruttore
	private PosTags myPosTags;
	private Expression subject;
	
	//lo utilizzo per accumulare i complementi di tempo determinato
	private Stack<Expression> temporalComplementAccumulator; 
	//lo utilizzo per affrontare l'accumulazione dell'oggetto semplice (quindi non "lambda expression")
	private Stack<Expression> complementAccumulator; 
	//lo utilizzo per affrontare l'accumulazione di ciò che è sicuramente una stringExpr
	private Stack<StringExpr> stringAccumulator; 
	//lo utilizzo per memorizzare ciò che saranno sicuramente avverbi
	private Stack<StringExpr> adverbAccumulator; 
	
	//è necessario per affrontare il caso (VP (VP ..) (CC and) (VP ..))
	private Stack<LambdaExpr> lambdaExprAccumulator;	
	private ComplexLambdaExpr cLambdaExpr;
	
	//i seguenti tre attributi permettono di "mandare" al "FinalParser" la "SBAR" inattesa ma trovata 
	private StringExpr comma;
	private GeneralPhraseExpr sbar; 
	private PredicateStructuring predicate;	
	//il seguente boolean abilita l'operazione della memorizzazione della SBAR di cui sopra
	private boolean secondWay;

	private boolean noError;
	
	
	public VerbPhraseParser(PosTags posTags, Expression sub) {
		
		myPosTags = posTags;
		subject = sub;
		
		temporalComplementAccumulator = new Stack<Expression>();
		complementAccumulator = new Stack<Expression>();
		stringAccumulator = new Stack<StringExpr>();
		adverbAccumulator = new Stack<StringExpr>();

		lambdaExprAccumulator = new Stack<LambdaExpr>();
		cLambdaExpr = null;

		comma = null;
		sbar = null;
		predicate = null;
		secondWay = false;
		
		noError = true;
	
	}
	

	public int doParse(int index, String req){
		
		//arrivo qui che ho una "(VP" da cui partire

		int i = index;
		
		if(i<req.length()) {
		
			if(req.charAt(i) == '(') {
				
				i = i+1;
				
				if(Character.isUpperCase(req.charAt(i))) {
					
					//memorizzo il tag trovato
					String tag = doParseTag(i, req);
					
					//incremento l'indice della dimensione del tag trovato + 1 per lo spazio bianco seguente
					i = i+tag.length()+1;
					
					//inizio la fase di identificazione del tag	
					if(myPosTags.isVP(tag)) {
						
						//inizio quindi la ricorsione in quanto ho trovato una VP
							
						//vado ad analizzare quello che in teoria sarà il mio verbo reggente o eventualmente un avverbio portante
						i = doParse(i, req);
					
						LambdaExpr vp1 = null;  LambdaExpr vp2 = null;
						if(!this.lambdaExprAccumulator.isEmpty()) {
										
							//se entro qui dentro significa che ho una VP del tipo (VP (VP) (, ,) (CC and) (VP))
							//oppure (VP (VP) (CC and) (VP))
							
							vp1 = this.lambdaExprAccumulator.pop();  
							
							//quindi eseguo una successiva ricorsione in quanto mi aspetto o una virgola o un and
							i = doParse(i+1, req);
							
							//a questo punto ci sarà necessariamente una terza ulteriore componente. 
							//Che sarà o un and se prima c'era la , o il VP atteso
							i = doParse(i+1, req);
		
							//controllo ora che non vi sia un ulteriore quarte parte e concludo
							if(i<req.length() && req.charAt(i)==' ') {
								i = doParse(i+1, req);
							}
							
							if(!this.lambdaExprAccumulator.isEmpty()) {
								
								vp2 = this.lambdaExprAccumulator.pop(); 
								
							}
							
							//costruisco la cLambdaExpr
							ComplexLambdaExpr cLambdaExpr = null;
							if(vp1 != null && vp2 != null) {
								cLambdaExpr = new ComplexLambdaExpr(vp1, vp2);  
							}
							
							if(cLambdaExpr != null) {
								this.cLambdaExpr = cLambdaExpr;  cLambdaExpr = null;
							}
							
							i = this.checkCorrectEnd(req, i);
							
							return i;
							
						}
						
						
						StringExpr adverb = null;
						//controllo se il verbo è preceduto da un avverbio
						if(!adverbAccumulator.isEmpty()) {
							
							//se entro qui significa che c'è un avverbio prima del mio verbo reggente
							
							//quindi lo memorizzo
							adverb = adverbAccumulator.pop(); 
							
							//e rilancio la ricorsione per andare alla ricerca del verbo reggente necessariamente atteso
							if(i<req.length() && req.charAt(i)== ' ') {
								
								//significa che la mia VP ha ancora altre parts of speech
								i = doParse(i+1, req);
								
							}
							else {
								this.noError = false;
								return req.length();
							}
							
						}
			
						
						//memorizzo il verbo
						StringExpr regentVerb = null;
						if(! stringAccumulator.isEmpty()) {
							regentVerb = stringAccumulator.pop();  
						}
						
						
						if(i<req.length() && req.charAt(i)== ' '){
							
							//significa che la mia VP ha ancora altre parts of speech
							i = doParse(i+1, req);
						
							//controllo che il verbo non sia seguito da un avverbio
							if(!adverbAccumulator.isEmpty()) {
								
								//è seguito da un avverbio
								
								//lo memorizzo
								adverb = adverbAccumulator.pop();
								
								//e rimando la ricorsione per andare alla ricerca di un eventuale complemento
								if(i<req.length() && req.charAt(i)== ' '){
									
									//significa che la mia VP ha ancora altre parti (o un oggetto o un complemento)	
									i = doParse(i+1, req);
									
								}
								
							}
							else if(!stringAccumulator.isEmpty()) {
							
								
								//questa parte serve ad eliminare possibili parole di cui non mi interessa
								//recuperare la semantica. Le scarto quindi
								stringAccumulator.pop(); 
								
								//e rimando la ricorsione per andare alla ricerca di un eventuale complemento
								if(i<req.length() && req.charAt(i)== ' '){
									
									//significa che la mia VP ha ancora altre parti (o un oggetto o un complemento)
									i = doParse(i+1, req);
								}	
							}
						}
						
						
						//al ritorno di quest'ultima ricorsione dovrei aver memorizzato il mio oggetto
						
						//istanzio un primo eventuale complemento e un eventuale complemento di termine
						Expression comp1 = null;  Expression temp = null;
						
						//memorizzo l'oggetto (sempre che ve ne sia uno)
						if(!(complementAccumulator.isEmpty())) {
							
							comp1 = complementAccumulator.pop(); 
						
						}
						else if(!lambdaExprAccumulator.isEmpty()) {
							
							comp1 = lambdaExprAccumulator.pop(); 

						}
						else if(!temporalComplementAccumulator.isEmpty()) {
							
							temp = temporalComplementAccumulator.pop(); 
						}
	
						
						
						//istanzio un eventuale complement e un eventuale virgola
						Expression comp2 = null;   
						StringExpr commaExpr = null;
						
						//controllo eventuali altre parti
						if(i<req.length() && req.charAt(i)== ' '){
							
							//significa che la mia VP ha ancora altre parts of speech
							
							i = doParse(i+1, req);
						
							if(!complementAccumulator.isEmpty()) {
								
								comp2 = complementAccumulator.pop();  
							}
//							else if(!lambdaExprAccumulator.isEmpty()) {
//								
//								comp2 = lambdaExprAccumulator.pop(); 
//
//							}
							else if(!temporalComplementAccumulator.isEmpty()) {
								
								temp = temporalComplementAccumulator.pop();
								
							}
							else if(comma != null) {
							
								commaExpr = comma;  comma = null;
							
							}
							else {
								this.noError = false;
								return req.length();
							}
						}
						
						
						
						//costruisco la fopc expression
						LambdaExpr verbPhrase = new LambdaExpr(null ,adverb , regentVerb, comp1, comp2, temp);
						
						//la metto dentro il mio stack
						lambdaExprAccumulator.push(verbPhrase);  
						
					//----------------------------------------------------------------------------------------------------------------
					//inizio la ricerca di sbar
					
					if(commaExpr != null) {
					
						if(i<req.length() && req.charAt(i) == ' ') {
														
							i = this.doParse(i+1, req);
							
							if(sbar != null) {
								comma = commaExpr;  		
							}
						}
					}
					else if(i<req.length() && req.charAt(i) == ' ') {
						
						i = this.doParse(i+1, req);
						
						if(comma != null) {
							
							commaExpr = comma;  comma = null;
							if(commaExpr != null) {
								
								if(req.charAt(i) == ' ') {
									
									i = this.doParse(i+1, req);
									
									if(sbar != null) {
										
										comma = commaExpr;  
										
									}
								}	
							}
							
						}
						
					}

					
					//fine ricerca di sbar
					//----------------------------------------------------------------------------------------------------------------
					
						
						i = this.checkCorrectEnd(req, i);  
						
						return i;
						
					}
					else if(myPosTags.isSBAR(tag) && this.secondWay) {
					
						Interpreter myInterpreter = new Interpreter(myPosTags);  
					
						FinalParser myFP = new FinalParser(myPosTags, myInterpreter);	
					
						i = myFP.doParse(req, i-6);
					
						int size = myFP.getInterpreter().getPredicates().size();
					
						this.sbar = myFP.getSbar();
					
						if(size>=1) {
							this.predicate = myFP.getInterpreter().getPredicates().get(0);
						}
					
						return i;
					
					}
					else if(myPosTags.isPhraseTag(tag) && !(myPosTags.isVP(tag)) 
							&& !(myPosTags.isADVP(tag)) && !(myPosTags.isPP(tag))) {
						
						//mi trovo quindi dentro una phrase
						
						//istanzio la mia struttura dati per la memorizzazione della phrase
						GeneralPhraseExpr myGenPhrase = new GeneralPhraseExpr(tag); 
						
							//recupero quella componente che deve necessariamente esserci 
							i = doParse(i, req);
								
							//e la memorizzo
							if(!complementAccumulator.isEmpty()) {
									
								myGenPhrase.getComponents().add(complementAccumulator.pop());
								
							}
//							else if(!temporalComplementAccumulator.isEmpty()) {
//
//								myGenPhrase.getComponents().add(temporalComplementAccumulator.pop());
//								
//							}
							else if(!stringAccumulator.isEmpty()) {
								myGenPhrase.getComponents().add(stringAccumulator.pop());
							}
							else {
								this.noError = false;
								return req.length();
							}
							
							if(i<req.length() && req.charAt(i)== ' ') {

								//significa che la mia phrase avrà più di una componente
								
								do{
									
									i = doParse(i+1, req);
									
									//e la memorizzo
									if(!complementAccumulator.isEmpty()) {	
										
										myGenPhrase.getComponents().add(complementAccumulator.pop());
									}
									else if(!temporalComplementAccumulator.isEmpty()) {
										
										myGenPhrase.getComponents().add(temporalComplementAccumulator.pop());
										
									}
									else {
										this.noError = false;
										return req.length();										
									}
									
									
								}while(i<req.length() && req.charAt(i)== ' ');
							
							}
							
						
						//inserisco dentro l'object accumulator la mia phrase
						complementAccumulator.push(myGenPhrase);
						
						
						i = this.checkCorrectEnd(req, i); 
						
						
						return i;
						
					}
					else if(myPosTags.isPP(tag)) {
						
						//mi trovo quindi dentro una Phrase
						
						
						//istanzio la mia struttura dati per la memorizzazione della phrase
						GeneralPhraseExpr myGenPhrase = new GeneralPhraseExpr(tag); 

						//recupero quella componente che deve necessariamente esserci 
						i = doParse(i, req);
						
						//e la memorizzo
						StringExpr firstComponent = null;
						if(!complementAccumulator.isEmpty()) {
							
							firstComponent = (StringExpr)complementAccumulator.pop();
							myGenPhrase.getComponents().add(firstComponent);
						}
						else {
							this.noError = false;
							return req.length();
						}
						
						
						if(i<req.length() && req.charAt(i)== ' ') {

							//significa che la mia phrase avrà più di una componente
							
							do{
								
								i = doParse(i+1, req);
								
								//e la memorizzo
								
								if(!complementAccumulator.isEmpty()) {
									myGenPhrase.getComponents().add(complementAccumulator.pop());
								}
								
							}while(i<req.length() && req.charAt(i)== ' ');
						
						}
						
						
						if(myPosTags.isPrepositionWordTag(firstComponent.getPosTag()) 
								&& (firstComponent.getValue().equals("for") 
								|| firstComponent.getValue().equals("after"))) {
							
							//allora è probabile che sarà un complemento di tempo determinato
							temporalComplementAccumulator.push(myGenPhrase);
							
						}
						else {
							
							//inserisco dentro l'object accumulator la mia phrase
							complementAccumulator.push(myGenPhrase);
						}
						
						
						i = this.checkCorrectEnd(req, i); 

						
						return i;
						
					}
					else if(myPosTags.isADVP(tag)) {
						
						//significa che ho identificato un avverbio portante
						
						
						//memorizzo nell'attributo opportuno l'avverbio
						i = doParse(i, req);
											 
						i = this.checkCorrectEnd(req, i);
	
						return i;
					
					}
					else if(myPosTags.isWordTag(tag) && !(tag.equals("CC"))){
						
						//mi trovo quindi dentro una Word
						
						//memorizzo la mia word ed il relativo tag
						String word = null;
						
						//faccio un controllo per il corretto parsing
						if(Character.isDigit(req.charAt(i))) {
							
							word = doParseNumber(i, req);
						}
						else {
							
							word = doParseWord(i, req);
						}
						
						
						StringExpr taggedWord = new StringExpr(word, tag); 
						
						if(myPosTags.isVerbWordTag(tag) || tag.equals("DT")){
							
							//la devo inserire nello stack sA perchè è un verbo
							stringAccumulator.push(taggedWord);
							
						}
						else if(myPosTags.isAdverbWordTag(tag)){
							
							//la devo inserire nell'attributo adverb
							adverbAccumulator.push(taggedWord);
						}
						else{
							
							//la devo inserire nello stack oA perchè sarà un parte di un complemento
							complementAccumulator.push(taggedWord);
							
						}
						
						//incremento quindi il mio indice della lunghezza della word trovata 
						i = i+word.length();
												
						i = this.checkCorrectEnd(req, i); 
						
						return i;

					}
					else if(tag.equals("CC")) {
						
						String cc = "and";
					
						//incremento il contatore
						i = i + cc.length();

						//aggiungo la componente alla mia 
						
						//verifico la correttezza della fine 
						i = this.checkCorrectEnd(req, i);
						
						return i;
						
					}
					else {							
						this.noError = false;
						return req.length();
					}
					
				}
				else if(req.charAt(i) == ',') {
					
					String tag = ",";  String comma = ",";
					
					StringExpr commaExpr = new StringExpr(comma, tag);
					
					if(commaExpr != null) {
						this.comma = commaExpr;  commaExpr = null;
					}
					
					//incremento il contatore
					i = i +3;

					//aggiungo la componente alla mia 
					
					//verifico la correttezza della fine 
					i = this.checkCorrectEnd(req, i);
					
					return i;
					
				}
				
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
		
		String parsedWord = null;
		
		int j=i;
		while(Character.isLowerCase(req.charAt(i))){
			
			i++;
		}
		
		parsedWord = req.substring(j, i) ;
		
		return parsedWord;
	}
	
	/**
	 * 
	 * @param i
	 * @param req
	 * @return parsedNumber
	 */
	public String doParseNumber(int i, String req) {

		int j = i;
		
		//questo while è necessario per l'analisi completa del numero
		while (j < req.length() && Character.isDigit(req.charAt(j))) {
		
			j = j + 1;
		
		}
		
		//restituisce la sottostringa da i a j contenente il numero cercato
		String numAsText = req.substring(i,j);
		
		return numAsText;
	}
	
	//getters
	public Expression getLambdaExpr() {

		if(!this.lambdaExprAccumulator.isEmpty()) {
		
			LambdaExpr result = this.lambdaExprAccumulator.pop();  result.setSubject(this.subject); 
			return result;
		
		}
		else if(this.cLambdaExpr != null){
		
			ComplexLambdaExpr result = this.cLambdaExpr;  result.setSubject(this.subject);
			return result;
		}

		return null;
	}
		
	public PredicateStructuring getPredicate() {
		return this.predicate;
	}
	
	public GeneralPhraseExpr getSbar() {
		return this.sbar;
	}
	
	public StringExpr getComma() {
		return this.comma;
	}
	
	public boolean isComplex() {
		return (this.cLambdaExpr != null);
	}
	
	public boolean noError() {		
		return this.noError;
	}
		
	//setter
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
//			this.noError = false;
//			return req.length();
//		}
		
		return i;
	}
	
}
