package interpreterPackage;

import java.util.ArrayList;
import java.util.Stack;

import expressionPackage.Expression;
import expressionPackage.GeneralPhraseExpr;
import expressionPackage.LambdaExpr;
import expressionPackage.StringExpr;


/**
 * Tale classe è sicuramente una delle principali del progetto.
 * Essa ha lo scopo di, innanzitutto, memorizzare in una struttura dati, 
 * manipolabile e analizzabile, il parsing sintattico generato dal Parser di Stanford.
 * 
 * In sostanza ha il compito di individuare una di quelle che ho identificato essere
 * le ricorrenti "costruzioni sintattiche" di una sentence S in uscita dal suddetto parser.
 * Le "costruzioni sintattiche" di S ho quindi assunto esser le seguenti : 
 * 
 *  0) (S (predicato))		 
 *  1) (S (predicato) (, ,) (SBAR))
 *  2) (S (SBAR) (, ,) (predicato))
 *  3) (S (SBAR) (, ,) (SBAR) (, ,) (predicato))
 *  4) (S (PP) (, ,) (predicato))
 *  5) (S (PP) (, ,) (SBAR) (, ,) (predicato))
 *  6) (S (S) (CC and) (S)) o (S (S) (, ,) (CC and) (S))
 *  7) (S (VP))
 *  
 *  Dove con predicato intendo la seguente struttura sintattica: "(NP) (VP)" o simili.
 *  
 *  
 * @author Riccardo Parodi
 *
 */
public class FinalParser {

	private boolean correctParsing;
	private boolean noError;
	
	//oggetti indispensabili per il funzionamento del final parser
	private PosTags myPosTags;
	private Interpreter myInterpreter;

	
	
	//memorizzatori utili durante la ricorsione
	private StringExpr dot;
	private Stack<StringExpr> andAccumulator;
	private GeneralPhraseExpr sbarVP;
	private StringExpr specialAdverb;
	private Stack<StringExpr> commaAccumulator;
	private Stack<GeneralPhraseExpr> gpAccumulator;
	private Stack<StringExpr> stringExprAccumulator;
	private Stack<StringExpr> verbAccumulator;
	private Stack<LambdaExpr> predicatesAccumulator;
	private Stack<StringExpr> adverbAccumulator;
	private Stack<GeneralPhraseExpr> npAccumulator;
	private Stack<LambdaExpr> vpAccumulator;
	private Stack<GeneralPhraseExpr> ppAccumulator;
	private Stack<GeneralPhraseExpr> whppAccumulator;
	private Stack<StringExpr> whadvpAccumulator;
	
	//variabili portanti
	private GeneralPhraseExpr sbar;
	private GeneralPhraseExpr sentence;	
	
	
	
	//costruttore
	public FinalParser(PosTags myPT, Interpreter myInt) {
	
		
		this.correctParsing = false;
		this.noError = true;
		
		//oggetti indispensabili per il funzionamento del final parser
		this.myPosTags = myPT;
		this.myInterpreter = myInt;
		
		
		//memorizzatori utili durante la ricorsione
		this.andAccumulator = new Stack<StringExpr>();
		this.dot = null;
		this.commaAccumulator = new Stack<StringExpr>();
		this.sbarVP = null;
		this.specialAdverb = null;
		this.gpAccumulator = new Stack<GeneralPhraseExpr>();
		this.stringExprAccumulator = new Stack<StringExpr>();
		this.verbAccumulator = new Stack<StringExpr>();
		this.predicatesAccumulator = new Stack<LambdaExpr>();
		this.adverbAccumulator = new Stack<StringExpr>();
		this.npAccumulator = new Stack<GeneralPhraseExpr>();
		this.vpAccumulator = new Stack<LambdaExpr>();
		this.ppAccumulator = new Stack<GeneralPhraseExpr>();
		this.whppAccumulator = new Stack<GeneralPhraseExpr>();
		this.whadvpAccumulator = new Stack<StringExpr>();
		
		
		//variabili portanti
		this.sbar = null;
		this.sentence = null;
	}
	
	public void doParse(String req) {
		
		int i = 0;
		
		int index = this.doParse(req, i);
		
		if (index == req.length()  &&  this.noError && this.sentence != null) {	
			this.correctParsing = true;
		}
	}
	
	
	
	public int doParse(String req, int index) {
		
		int i = index;  
	
		if(i < req.length()) {
			
			if(this.controller(i, req)) {
				
				i = i+1;
				
				if(Character.isUpperCase(req.charAt(i))) {
					
					//memorizzo il tag trovato
					String tag = doParseTag(i, req);  
					
					//incremento l'indice della dimensione del tag trovato + 1 per lo spazio bianco seguente
					i = i+tag.length()+1;
					
					//inizio la fase di identificazione del tag			
					if(myPosTags.isS(tag)) {
						
						//andiamo ad analizzare il prossimo componente
						i = this.doParse(req, i);
				
						if(this.sentence != null) {

							//allora siamo nella struttura di S seguente : (S (S) (CC and) (S))
							
							//controllo che non vi sia una sentence già memorizzata
							GeneralPhraseExpr s1 = null;  GeneralPhraseExpr s2 = null;  StringExpr and = null;
							
							s1 = this.sentence;  this.sentence = null;  
							
							//rimando la ricorsione per andare a ricercare un and o un eventuale virgola
							i = this.doParse(req, i+1);
						
							//vado a cercare l'indispensabile terzo elemento
							i = this.doParse(req, i+1);
							
							if(!this.andAccumulator.isEmpty()) {
								and = this.andAccumulator.pop();      
							}
							
							if(i<req.length() && req.charAt(i) == ' ') {
								
								i = this.doParse(req, i+1);
								
								if(!this.commaAccumulator.isEmpty()) {
									this.commaAccumulator.pop();
								}
								
							}
							
							
							if(this.sentence != null) {
								s2 = this.sentence;  this.sentence = null;  
							}
							else {
								this.noError = false; 
								return req.length();
							}
							
							
							//costruisco quindi la mia complexS
							this.sentence = new GeneralPhraseExpr("complexS");
							if(s1 != null && s2 != null) {
								
								this.sentence.getComponents().add(s1);  s1 = null;
								if(and != null) { this.sentence.getComponents().add(and);  and = null; }
								this.sentence.getComponents().add(s2);  s2 = null;
								
								this.sentence.setTypeOfSentence(6);  
							}
							
							i = this.checkCorrectEnd(req, i);
							
							return i;
							
						}
						else if(this.sbar != null) {
							
							
							GeneralPhraseExpr sbar11 = null;  GeneralPhraseExpr sbar12 = null;  StringExpr comma11 = null; StringExpr comma12 = null;  
							StringExpr advp11 = null;  LambdaExpr predicate = null;  GeneralPhraseExpr np = null; StringExpr advp12 = null; LambdaExpr vp = null;  
							
							//memorizzo la sbar
							if(this.sbar != null) {
								sbar11 = this.sbar;  this.sbar = null;  
							}
							
							//adesso troverò necessariamente uno spazio bianco successivamente una virgola 
							i = this.doParse(req, i+1);
							
							//memorizzo la virgola
							if(!this.commaAccumulator.isEmpty()) {
								
								comma11 = this.commaAccumulator.pop();  
								
								//recupero la successiva componente
								i = this.doParse(req, i+1);
								
							}
							
							//considero il caso SBAR , SBAR , NP VP
							if(this.sbar != null) {
								
								sbar12 = this.sbar; this.sbar = null;
								
								//recupero la componente successiva
								i = this.doParse(req, i+1);
								
							}
							else if(this.sbarVP != null) {
								//controllo se eventualmente durante l'analisi della Verb Phrase
								//fossi incappato in una SBAR
								sbar12 = this.sbarVP; this.sbarVP = null;  								
							}
						
						
							//memorizzo la virgola
							if(!this.commaAccumulator.isEmpty()) {
								
								comma12 = this.commaAccumulator.pop();  
								
								//recupero la successiva componente
								i = this.doParse(req, i+1);
								
							}
							
							//controllo se sono incappato in eventuali avverbi  
							if(!this.adverbAccumulator.isEmpty()) {
								
								advp11 = this.adverbAccumulator.pop(); 
								
								
								//recupero la successiva componente
								i = this.doParse(req, i+1);
									
							}
							
							
							
							//e di seguito uno spazio bianco seguito da un np
							if(!this.predicatesAccumulator.isEmpty()) {
								
								//controllo se c'era uno special averb associato al predicato
								if(this.specialAdverb != null) { 
									advp11 = this.specialAdverb; this.specialAdverb = null; 
								}
								
								predicate = this.predicatesAccumulator.pop();   
								
								//fine delle componenti attese (eccetto per il punto)
							}
//							else if(!this.npAccumulator.isEmpty()) {
//								
//								np = this.npAccumulator.pop();  
//	 							
//								
//								//recupero la successiva componente
//								i = this.doParse(req, i+1);
//								
//								
//								//controllo se sono incappato in eventuali avverbi
//								if(!this.adverbAccumulator.isEmpty()) {
//									
//									advp12 = this.adverbAccumulator.pop(); 
//									
//									//recupero la successiva componente
//									i = this.doParse(req, i+1);
//										
//								}
//								
//								
//								//attendo un vp
//								if(!this.vpAccumulator.isEmpty()) {
//									vp = this.vpAccumulator.pop();  
//								}
//								
//								//fine delle componenti attese (eccetto per il punto)
//								
//							}
							else {
								this.noError = false;
								return req.length();
							}
						
							
							//a questo punto faccio una verifica sul punto
							StringExpr dot = null;
							if(i<req.length() && req.charAt(i) == ' ') {
								
								i = this.doParse(req, i+1);
								
								if(this.dot != null) {
									dot = this.dot;  this.dot = null; 
								}
							}
							
							
							//ora non mi resta che memorizzare il tutto nella sentence
						
							this.sentence = new GeneralPhraseExpr(tag);
							
							boolean isType2 = false;
							
							if(sbar11 != null) {
								this.sentence.getComponents().add(sbar11);  sbar11 = null;  this.sentence.setTypeOfSentence(2);  isType2 = true; 
							}
							
							if(comma11 != null) {
								this.sentence.getComponents().add(comma11);  comma11 = null;
							}
						
							if(sbar12 != null) {
								this.sentence.getComponents().add(sbar12);  sbar12 = null;  if(isType2) { this.sentence.setTypeOfSentence(3); }
							}
							
							if(comma12 != null) {
								this.sentence.getComponents().add(comma12);  comma12 = null;
							}
							
							if(advp11 != null) {
								this.sentence.getComponents().add(advp11);  advp11 = null;
							}
							
							if(predicate != null) {
								this.sentence.getComponents().add(predicate);  predicate = null;
							}
//							else if(np != null){
//								this.sentence.getComponents().add(np);  np = null;
//	 							
//								if(advp12 != null) {
//									this.sentence.getComponents().add(advp12);  advp12 = null;
//								}
//								
//								if(vp != null) {
//									this.sentence.getComponents().add(vp);  vp = null;
//								}
//							}
							
							if(dot != null) {
								this.sentence.getComponents().add(dot);  dot = null;
							}
						
						
							
							i = this.checkCorrectEnd(req, i);
						
							return i;
						}
						else if(!this.ppAccumulator.isEmpty()) {
							
							GeneralPhraseExpr pp2 = null;  StringExpr c21 = null;  GeneralPhraseExpr sbar2 = null;
							StringExpr c22 = null;  LambdaExpr predicate2 = null; StringExpr advp21 = null; 
							GeneralPhraseExpr np2 = null; StringExpr advp22 = null; LambdaExpr vp2 = null;  StringExpr d2 = null;  						
							
							pp2 = this.ppAccumulator.pop();
							
							//vado a recuperare la virgola attesa
							i = this.doParse(req, i+1);
							
							//la memorizzo
							if(!this.commaAccumulator.isEmpty()) {
								c21 = this.commaAccumulator.pop();  
							}
							
							//vado a  recuperare o la sbar o il predicato atteso
							i = this.doParse(req, i+1);
							
							//controllo che non sia di tipo 5 : (PP) , (SBAR) , (NP) (VP)
							//controllo che la sentence non sia del tipo PP,SBAR,NP+VP
							if(this.sbar != null) {
								
								sbar2 = this.sbar;  this.sbar = null;
								
								//recupero la parte successiva
								i = this.doParse(req, i+1);
								
								//mi aspetto quindi una comma
								if(!this.commaAccumulator.isEmpty()) {
									c22 = this.commaAccumulator.pop();   
									
									//recupero la parte successiva
									i = this.doParse(req, i+1);
								}
							}
							else if(this.sbarVP != null) {
								//controllo se eventualmente durante l'analisi della Verb Phrase
								//fossi incappato in una SBAR
								sbar2 = this.sbarVP; this.sbarVP = null;  
								
								//non recupero la parte successiva in quanto è già stato fatto
								//durante l'analisi di (PP) (, ,) (predicato)
								
								//mi aspetto quindi una comma
								if(!this.commaAccumulator.isEmpty()) {
									c22 = this.commaAccumulator.pop();   
									
									//recupero la parte successiva
									i = this.doParse(req, i+1);
								}								
							}
							
							//controllo se sono incappato in eventuali avverbi
							if(!this.adverbAccumulator.isEmpty()) {
								
								advp21 = this.adverbAccumulator.pop(); 
								
								//recupero la successiva componente
								i = this.doParse(req, i+1);	
							}
							
							//li memorizzo
							if(!this.predicatesAccumulator.isEmpty()) {
								
								predicate2 = this.predicatesAccumulator.pop();  
								
								//fine delle componenti attese (eccetto per il punto)
							}
//							else if(!this.npAccumulator.isEmpty()) {
//								
//								np2 = this.npAccumulator.pop(); 
//	 							
//								
//								//recupero la successiva componente
//								i = this.doParse(req, i+1);
//								
//								
//								//controllo se sono incappato in eventuali avverbi
//								if(!this.adverbAccumulator.isEmpty()) {
//									
//									advp22 = this.adverbAccumulator.pop();  
//									
//									
//									//recupero la successiva componente
//									i = this.doParse(req, i+1);
//										
//								}
//								
//								
//								//attendo un vp
//								if(!this.vpAccumulator.isEmpty()) {
//									
//									vp2 = this.vpAccumulator.pop();  
//									
//								}
//								
//								//fine delle componenti attese (eccetto per il punto)
//								
//							}
							else {
								this.noError = false;
								return req.length();  
							}
							
							//a questo punto faccio una verifica sul punto
							if(i<req.length() && req.charAt(i) == ' ') {
								
								i = this.doParse(req, i+1);
								
								if(this.dot != null) {
									d2 = this.dot;  this.dot = null;
								}
							}
							
							
							
							//inizio la costruzione della sentence
							this.sentence = new GeneralPhraseExpr(tag);
							
							if(pp2 != null) {
								this.sentence.getComponents().add(pp2);  pp2 = null;  this.sentence.setTypeOfSentence(4);
							}
							if(c21 != null) {
								this.sentence.getComponents().add(c21);  c21 = null;
							}
							if(sbar2 != null) {
								this.sentence.getComponents().add(sbar2);  sbar2 = null;  this.sentence.setTypeOfSentence(5);
							}
							if(c22 != null) {
								this.sentence.getComponents().add(c22);  c22 = null;
							}
							if(advp21 != null) {
								this.sentence.getComponents().add(advp21);  advp21 = null;
							}
							if(predicate2 != null) {
								this.sentence.getComponents().add(predicate2);  predicate2 = null;
							}
//							else if(np2 != null){
//								this.sentence.getComponents().add(np2);  np2 = null;
//	 							
//								if(advp22 != null) {
//									this.sentence.getComponents().add(advp22);  advp22 = null;
//								}
//								
//								if(vp2 != null) {
//									this.sentence.getComponents().add(vp2);  vp2 = null;
//								}
//							}
							
							
							if(d2 != null) {
								this.sentence.getComponents().add(d2);  d2 = null;
							} 
				
							
							
							i = this.checkCorrectEnd(req, i);
							
							return i;
							
						}
						else if(!this.predicatesAccumulator.isEmpty()) {
							
							//qua siamo nei seguenti possibili casi : (NP)(VP) o  (NP)(VP),(SBAR)  
							
							//vado a prendere il predicato atteso
							LambdaExpr p1 = null; 
							if(!this.predicatesAccumulator.isEmpty()) {
							
								p1 =	this.predicatesAccumulator.pop();
							}
			
							
							
							//controllo che non vi sia un ulteriore componente nella sentence
							StringExpr c = null;  GeneralPhraseExpr sb = null; 
							StringExpr d = null;
							if(i<req.length() && req.charAt(i) == ' ') {
								
								i = this.doParse(req, i+1);
							
								
								//devo trovare una virgola
								if(!this.commaAccumulator.isEmpty()) {
									
									c = this.commaAccumulator.pop();
									
									//necessariamente avrò una SBAR
									i = this.doParse(req, i+1);
									
									
									if(this.sbar != null) {
										
										sb = this.sbar;  this.sbar = null;  
										
									}
									else {
										
										this.noError = false;
										return req.length();  
									}
								}
								else if(this.dot != null) {
									d =  this.dot;  this.dot = null;
								}
								
							}
							
							//vado a cercare un ipotetico punto
							if(i<req.length() && req.charAt(i) == ' ') {
								
								i = this.doParse(req, i+1);
								
								if(this.dot != null) {
									d =  this.dot;  this.dot = null;
								}
							}
							
							
							
							//costruisco la sentence
							
							this.sentence = new GeneralPhraseExpr(tag);
							
							
							if(p1 != null ) { 
								
								this.sentence.getComponents().add(p1); p1 = null; 
								this.sentence.setTypeOfSentence(0);  
							
							}
							if(c != null) { this.sentence.getComponents().add(c); c = null; }
							if(sb != null) { 
								
								this.sentence.getComponents().add(sb); sb = null; 
								
								this.sentence.setTypeOfSentence(1);
								
							}
							if(d != null) { this.sentence.getComponents().add(d);  d = null; }
						
		
							i = this.checkCorrectEnd(req, i);
						
							return i;
							
						}
//						else if(!this.vpAccumulator.isEmpty()) {
//							
//						
//							this.sentence = new GeneralPhraseExpr(tag);
//							
//							this.sentence.getComponents().add(this.vpAccumulator.pop());
//							
//							this.sentence.setTypeOfSentence(7);
//							
//							i = this.checkCorrectEnd(req, i);
//							
//							return i;
//							
//						}
						else {
							
							this.noError = false;
							return req.length();  
							
						}
						
					}
					else if(myPosTags.isSBAR(tag)) {
						
						//vado a cercare quella che sarà necessariamente il mio primo componente che sarà o un IN o WHPP o WHADVP o un'altra sbar
						i = this.doParse(req, i);
						
//						if(this.sentence != null) {
//						
//							//sono nel caso particolare di SBAR del tipo  (SBAR (S))
//							
//							GeneralPhraseExpr sbar = new GeneralPhraseExpr(tag);
//						
//							sbar.getComponents().add(this.sentence);  this.sentence = null;
//							
//							this.sbar = sbar;
//							
//							i = this.checkCorrectEnd(req, i);
//							
//							return i;
//							
//						}
//						else 
						if(this.sbar != null) {
							
							//se entro qui significa che ho una SBAR del tipo (SBAR (SBAR) (CC and) (SBAR))
						
							GeneralPhraseExpr sbar1 = null; StringExpr and = null; StringExpr comma = null;  GeneralPhraseExpr sbar2 = null;
							
						
							sbar1 = this.sbar;  this.sbar = null;
							
							//quindi eseguo la ricorsione in quanto mi aspetto una virgola o un and 
							i = this.doParse(req, i+1);
							
							if(!this.commaAccumulator.isEmpty()) {
								comma = this.commaAccumulator.pop();
							}
							
							//a questo punto ci sarà necessariamente una terza parte
							i = this.doParse(req, i+1);
							
							if(!this.andAccumulator.isEmpty()) {
								and = this.andAccumulator.pop();      
							}
							
							//controllo ora che non vi sia un ulteriore quarte parte e concludo
							if(i<req.length() && req.charAt(i)==' ') {
								
								i = this.doParse(req, i+1);
								
							}
							
							if(this.sbar != null) {
								sbar2 = this.sbar;  this.sbar = null;
							}
							
							//costruisco la cLambdaExpr
							GeneralPhraseExpr complexSbar = new GeneralPhraseExpr("ComplexSBAR");
							if(sbar1 != null && sbar2 != null) {                    
							
								complexSbar.getComponents().add(sbar1);  sbar1 = null;
								//se siamo nel caso in cui ci sono sia una virgola che un and,
								//comunque ne memorizzo uno solo
								if(and != null) { 
									complexSbar.getComponents().add(and); 
									and = null; 
								}
								else if(comma != null) { 
									complexSbar.getComponents().add(comma); 
									comma = null; 
								}
								complexSbar.getComponents().add(sbar2);  sbar2 = null;
							
							}
							
							if(complexSbar != null) {
								this.sbar = complexSbar;  complexSbar = null;
							}
							
							i = this.checkCorrectEnd(req, i);
							
							return i;
							
						}
						
						//altrimenti potrà essere di tre tipi principali il primo componente : o una IN o una WHPP o una WHADVP
						
						StringExpr inExpr = null;  StringExpr rb = null;	GeneralPhraseExpr pp = null;
						
						if(!this.stringExprAccumulator.isEmpty()) {


							//la recupero e accumulo
							inExpr = this.stringExprAccumulator.pop(); 
							
						}
						else if(!this.whadvpAccumulator.isEmpty()) {
							
						
							//nota : sarebbe stato meglio farlo come genPhraseExpr e non come stringExpr 
							
							rb = this.whadvpAccumulator.pop();  
							
						}
						else if(!this.whppAccumulator.isEmpty()) {
							
							pp = this.whppAccumulator.pop();  
							
						}
						else {
							
							this.noError = false;
							return req.length();  
						}
						
						//uscito dalla ricerca del primo componente dovrò necessariamente attendere una sentence
						i = this.doParse(req, i+1);
						
						
						//al ritorno di tale ricorsione dovrò andare a recuperarla
						
						GeneralPhraseExpr sentence = null;
						if(this.sentence != null) {
								
							sentence = this.sentence;   this.sentence = null; 
						
						}
						
						
						
						//aggiungo la sbar al relativo accumulatore

						this.sbar = new GeneralPhraseExpr(tag);
						
						if(inExpr != null) { 
							this.sbar.getComponents().add(inExpr); 
							inExpr = null; 
						}
						else if(rb != null) { 
							this.sbar.getComponents().add(rb); 
							rb = null; 
						}
						else if(pp != null) { 
							this.sbar.getComponents().add(pp);  
							pp = null; 
						}

						
						if(sentence != null) {
							this.sbar.getComponents().add(sentence);  sentence = null;
						}
						
						i = this.checkCorrectEnd(req, i);
						
						return i;
	
					}
					else if(myPosTags.isNP(tag)) {
	
						//vado alla ricerca di un eventuale predicato
						
						if(i+17<req.length() && (req.substring(i, i+13).equals("(-LRB- -LCB-)") || req.substring(i, i+17).equals("(NP (-LRB- -LCB-)"))) {
							
							//nel caso (("(-LRB- -LCB-)")) è normale, nel caso (("(NP (-LRB- -LCB-)")) è un superSubject

						
							i = this.myInterpreter.catchThePredicates(req, i-4, false);
							
							if(!this.myInterpreter.noError()) {
								
								i = req.length();  //se c'è un errore nella lettura dei predicati si finisce
								
							}
							else {
								
								int size = this.myInterpreter.getPredicates().size()-1;
								
								if(size+1 > 0) {

									this.predicatesAccumulator.push(this.myInterpreter.getPredicates().get(size).getLambdaExpr());
								
									//controllo inoltre l'eventuale sbar e la aggiungo
									if(this.myInterpreter.getSbarVP() != null) {
										
										this.commaAccumulator.push(new StringExpr(",",","));
										this.sbarVP = this.myInterpreter.getSbarVP();
									}
									
								}
								
							}
								

							
						}
//						else if(!this.predicatesAccumulator.isEmpty()) {
//							
//							GeneralPhraseExpr np = new GeneralPhraseExpr(tag);
//							
//							np.getComponents().add(this.predicatesAccumulator.pop());  
//							
//							this.npAccumulator.push(np);
//							
//							return i;
//							
//						}
						else if(i+5 <req.length() && req.substring(i, i+5).equals("(PRP ")) {
						
							//siamo nel caso del pronome
							i = i+5;

							//per ora ignoro quale tipo di pronome vi sia
							String prp = this.doParseWord(i, req);
							
							i=i+prp.length(); 
							
							//controllo la fine del pronome
							i = this.checkCorrectEnd(req, i);
							
							//controllo la fine dell'NP
							i = this.checkCorrectEnd(req, i);
							
							
							//chiamo l'interpreter
							i = this.myInterpreter.catchThePredicates(req, i, true);
							
							if(!this.myInterpreter.noError()) {
								
								i = req.length();  //se c'è un errore nella lettura dei predicati si finisce
								
							}
							else {
								
								int size = this.myInterpreter.getPredicates().size()-1;
								
								if(size+1 > 0) {

									this.predicatesAccumulator.push(this.myInterpreter.getPredicates().get(size).getLambdaExpr());
									
//									//controllo inoltre l'eventuale sbar e la aggiungo
//									if(this.myInterpreter.getSbarVP() != null) {
//										
//										this.commaAccumulator.push(new StringExpr(",",","));
//										this.sbarVP = this.myInterpreter.getSbarVP();
//									}	
								}
							}
							
							
							return i;
						}
//						else {
//							
//							
//							//quindi mi aspetterò almeno una stringa
//							
//							i = this.doParse(req, i);
//							
//							//la memorizzo
//							StringExpr firstComponentOfNP = null;
//							if(!this.stringExprAccumulator.isEmpty()) {
//								firstComponentOfNP = this.stringExprAccumulator.pop();    
//							}
//							
//							
//							//controllo che non vi siano ulteriori componenti
//							ArrayList<Expression> otherComponentsOfNP = new ArrayList<Expression>();
//							if(i < req.length() && req.charAt(i)== ' ') {
//
//								//significa che la mia phrase avrà più di una componente
//								
//								do{
//									
//									i = this.doParse(req, i+1);
//									
//									//e la memorizzo
//									if(!this.gpAccumulator.isEmpty()) {
//										otherComponentsOfNP.add(this.gpAccumulator.pop());
//									}
//									
//								}while(i<req.length() && req.charAt(i)== ' ');
//							
//							}
//						
//							//costruisco la mia NP
//							GeneralPhraseExpr nounPhrase = new GeneralPhraseExpr(tag);
//							
//							if(firstComponentOfNP != null) {
//								nounPhrase.getComponents().add(firstComponentOfNP);
//							}
//							if(otherComponentsOfNP.size()>0) {
//								
//								nounPhrase.setComponents(otherComponentsOfNP);
//								
//							}
//							
//							
//							//lo inserisco nell'opportuno accumulatore
//							this.npAccumulator.push(nounPhrase);
//							
//							
//							i = this.checkCorrectEnd(req, i);
//						}
						
						return i;
						
					}
					else if(myPosTags.isPP(tag)) {
						
						//vado a prendere la preposizione
						i = this.doParse(req, i);
						
						
						//la recupero dallo stringExprAccumulator
						StringExpr inExpr = this.stringExprAccumulator.pop(); 
						
						
						//a questo punto mi aspetto un ulteriore componente che dovrà essere un np
						i = this.doParse(req, i+1);
						
						
						//recupero il predicato atteso
						LambdaExpr predicatePP = null;  GeneralPhraseExpr sPP = null;  GeneralPhraseExpr npPP = null;
//						if(!this.predicatesAccumulator.isEmpty()) {
//							
//
//							predicatePP = this.predicatesAccumulator.pop();   
//						
//						}
//						else 
						if(this.sentence != null) {
							
							sPP = this.sentence;  this.sentence = null;
							
						}
//						else if(!this.npAccumulator.isEmpty()) {
//
//
//							npPP = this.npAccumulator.pop();
//							
//						}
						else {
							
							this.noError = false;
							return req.length();  
							
						}
						
						
						//inizio la costruzione di pp
						GeneralPhraseExpr pp = new GeneralPhraseExpr(tag);
						
						
						if(inExpr != null) {
							pp.getComponents().add(inExpr);  inExpr = null;
						}
//						if(predicatePP != null) {
//							pp.getComponents().add(predicatePP);  predicatePP = null;
//						}
//						else 
						if(sPP != null) {
							pp.getComponents().add(sPP);  sPP = null;
						}
//						else if(npPP != null) { 
//							pp.getComponents().add(npPP); npPP = null; 
//						}
						
						//assegno al mio attributo noun phrase la np ottenuta
						this.ppAccumulator.push(pp);
						
						
						//faccio il controllo per la correttezza dell'end
						i = this.checkCorrectEnd(req, i);
						
						//quindi ritorno essendo che ho memorizzato tutto quel che dovevo del predicato
						return i;
					
					}
//					else if(myPosTags.isVP(tag)) {
//						
//						
//						//cerco il verbo reggente
//						
//						i = this.doParse(req, i);
//						
//						StringExpr regentVerb = null;  Expression comp = null;
//						if(!this.verbAccumulator.isEmpty()) {
//							regentVerb = this.verbAccumulator.pop();
//						}
//						
//						//controllo che vi sia il complemento oggetto
//						if(i<req.length() && req.charAt(i) == ' ') {
//							
//							i = this.doParse(req, i+1);
//							
//							if(!this.ppAccumulator.isEmpty()) {
//								
//								comp = this.ppAccumulator.pop();
//								
//							}
//							else if(!this.vpAccumulator.isEmpty()) {
//								
//								comp =  this.vpAccumulator.pop();
//								
//							}
//							
//							LambdaExpr vp = new LambdaExpr(null, null, regentVerb, comp, null, null);
//							
//							this.vpAccumulator.push(vp);
//							
//							i = this.checkCorrectEnd(req, i);
//							
//							return i;
//							
//						}
//						
//					}
					else if(myPosTags.isWHADVP(tag)) {
						
						
						//vado a recuperare l'avverbio
						i = this.doParse(req, i);
						
						StringExpr rb = null;
						if(!this.stringExprAccumulator.isEmpty()) {
						
							rb = this.stringExprAccumulator.pop();  
							
						}
						
						
						if(rb != null) {
							
							this.whadvpAccumulator.push(rb);  rb = null;
							
						}
						
						i = this.checkCorrectEnd(req, i);
					
						return i;
						
					}
					else if(myPosTags.isWHPP(tag)) {
						
						//vado a recuperare il primo componente
						
						i = this.doParse(req, i);
						
						StringExpr prep = null;
						if(!this.stringExprAccumulator.isEmpty()) {
							
							prep = this.stringExprAccumulator.pop();  
						}
						
						GeneralPhraseExpr whnp = null;
						if(i<req.length() && req.charAt(i) == ' ') {
							
							i = this.doParse(req, i+1);
							
							//recupero il secondo componente
							if(!this.gpAccumulator.isEmpty()) {
								
								whnp = this.gpAccumulator.pop();   
							}
						}
						
						//costruisco la mia whpp
						
						GeneralPhraseExpr whpPhrase = new GeneralPhraseExpr(tag);
						
						if(prep != null) { 
							whpPhrase.getComponents().add(prep);  
							prep = null; 
						}
						if(whnp != null) { 
							whpPhrase.getComponents().add(whnp);  
							whnp = null; 
						}
						
						//quindi aggiungo la mia pp al relativo accumulatore
						this.whppAccumulator.push(whpPhrase);
						
						i = this.checkCorrectEnd(req, i);
						
						return i;
					}
					else if(myPosTags.isWHNP(tag)) {
						
						i = this.doParse(req, i);
						
						//recupero la prima necessaria componente

						StringExpr first = null;
						if(!this.stringExprAccumulator.isEmpty()) {
							first = this.stringExprAccumulator.pop();  	
						}
						
//						//controllo che non vi siano ulteriori componenti
//						ArrayList<Expression> otherComponentsOfWHNP = new ArrayList<Expression>();
//						if(i<req.length() && req.charAt(i)== ' ') {
//
//							//significa che la mia phrase avrà più di una componente
//							
//							do{
//								
//								i = this.doParse(req, i+1);
//								
//								//e la memorizzo
//								otherComponentsOfWHNP.add(this.gpAccumulator.pop());
//								
//							}while(i<req.length() && req.charAt(i)== ' ');
//						
//						}
					
						//costruisco la mia WHNP
						GeneralPhraseExpr whnPhrase = new GeneralPhraseExpr(tag);
						
						if(first != null) {
							whnPhrase.getComponents().add(first);
						}
//						if(otherComponentsOfWHNP.size()>0) {							
//							whnPhrase.setComponents(otherComponentsOfWHNP);	
//						}
						
						
						//memorizzo la phrase trovata nell'opportuno accumalator
						this.gpAccumulator.push(whnPhrase);
						
						//verifico la correttezza della fine
						i = this.checkCorrectEnd(req, i);
						
						return i;
						
					}
					else if(myPosTags.isADVP(tag)) {
						
						//voglio memorizzare solo l'avverbio
						
						//rilancio la ricorsione
						i = this.doParse(req, i);
						
						StringExpr adv = null;
						if(!this.stringExprAccumulator.isEmpty()) {
							
							adv = this.stringExprAccumulator.pop(); 
						}
						
						if(adv != null) {
							this.adverbAccumulator.push(adv);
						}
						
						i = this.checkCorrectEnd(req, i);
						
						return i;
						
					}
					else if(myPosTags.isWordTag(tag) 
							&& !myPosTags.isPrepositionWordTag(tag) 
							&& !tag.equals("CC")){
						
						
						//mi trovo quindi dentro una Word
					
						
						//memorizzo la mia word ed il relativo tag
						String word = this.doParseWord(i, req);
						
						
						StringExpr taggedWord = new StringExpr(word, tag); 
						
						this.stringExprAccumulator.push(taggedWord);
						
//						if(myPosTags.isVerbWordTag(tag)) {
//							
//							this.verbAccumulator.push(taggedWord);
//							
//						}
//						else {
//							
//							this.stringExprAccumulator.push(taggedWord);
//						
//						}
						//incremento quindi il mio indice della lunghezza della word trovata 
						i = i+word.length();
						
						
						i = this.checkCorrectEnd(req, i);
						
						
						return i;
					}
					else if(myPosTags.isPrepositionWordTag(tag)) {
						
						String word = this.doParseWord(i, req); i = i + word.length();
						
						StringExpr prepExpr = new StringExpr(word, tag);  
						
						this.stringExprAccumulator.push(prepExpr);
						
						i = this.checkCorrectEnd(req, i);
						
						return i;
						
					}
					else if(tag.equals("CC")) {
						
						String cc = "and";
					
						StringExpr and = new StringExpr(cc, tag);  
						
						this.andAccumulator.push(and);
						
						//incremento il contatore
						i = i + cc.length();

						//aggiungo la componente alla mia 
						
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
					this.commaAccumulator.push(commaExpr);
					
					
					//verifico la correttezza della fine 
					i = this.checkCorrectEnd(req, i);

					
					return i;
					
				}
				else if(req.charAt(i) == '.') {
					
					String tag = "."; String dot = ".";
					
					StringExpr dotExpr = new StringExpr(dot, tag);
					
					//incremento il contatore
					i = i +3;

					
					//aggiungo il componente nella mia sentence
					this.dot = dotExpr;
					
					//verifico la correttezza della fine 
					i = this.checkCorrectEnd(req, i);
					
					return i;
					
				}
			
			}
			else if(i+13<req.length() && req.substring(i, i+13).equals("(-LRB- -LCB-)")) {
			
				i = this.myInterpreter.catchThePredicates(req, i, false);
			
				if(!this.myInterpreter.noError()) {
				
					i = req.length();  //se c'è un errore nella lettura dei predicati si finisce
				
				}
				else {
				
					int size = this.myInterpreter.getPredicates().size()-1;
				
					if(size+1 > 0) {

						this.predicatesAccumulator.push(this.myInterpreter.getPredicates().get(size).getLambdaExpr());
				
//						//controllo inoltre l'eventuale sbar e la aggiungo
//						if(this.myInterpreter.getSbarVP() != null) {
//							
//							this.commaAccumulator.push(new StringExpr(",",","));
//							this.sbarVP = this.myInterpreter.getSbarVP();
//						}
						
					}
				
				}
				
				return i;
			
			}
			else if(i+37<req.length() && req.substring(i, i+37).equals("(NP (NP (RB then)) (PRN (-LRB- -LCB-)")) {

				i = this.myInterpreter.catchThePredicates(req, i, false);
			
				if(!this.myInterpreter.noError()) {
				
					i = req.length();  //se c'è un errore nella lettura dei predicati si finisce
				
				}
				else {
					
					int size = this.myInterpreter.getPredicates().size()-1;
				
					if(size+1 > 0) {
						
						this.predicatesAccumulator.push(this.myInterpreter.getPredicates().get(size).getLambdaExpr());
						
						StringExpr adv = this.myInterpreter.getAdverb();
						
						if(adv != null) {
				
							this.specialAdverb =(adv);
						}
					}
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
		while(Character.isAlphabetic(req.charAt(i))){
			
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
		/**
		 * Tale metodo, in base alla distanza del mio indice 'i' rispetto alla fine del "req"
		 * restituisce la condizione per il corretto parsing
		 */
		
		int l = req.length();
		
		boolean condition;
		
		if(i+13>=l) { 
			condition = (req.charAt(i) == '(');
		}
		else if(i+37>= l && i+13<l) {
			condition = ((req.charAt(i) == '(') && (!req.substring(i, i+13).equals("(-LRB- -LCB-)"))); 
		}
		else { 
			//quindi i+37<l
			condition = ((req.charAt(i) == '(') && (!req.substring(i, i+13).equals("(-LRB- -LCB-)") 
					&& (!req.substring(i, i+37).equals("(NP (NP (RB then)) (PRN (-LRB- -LCB-)"))));
		}

		return condition;
	}
	
	
	//getters
	public GeneralPhraseExpr getSentence() {
		return this.sentence;
	}
	
	public ArrayList<PredicateStructuring> getPredicates() {
		return this.myInterpreter.getPredicates();
	}
	
	public GeneralPhraseExpr getSbar() {
		return this.sbar;
	}	
	
	public Interpreter getInterpreter() {
		return this.myInterpreter;
	}
	
	public boolean isCorrectParsing() {
		return this.correctParsing;
	}
	
	public boolean noError() {
		return this.noError;
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

	public void printPredicates() {
		
		System.out.println("************************************************************************************************************"); 
		
		System.out.println(); System.out.println("Inizio stampa predicati ristrutturati : ");
		
		for(int i=0;i<this.myInterpreter.getPredicates().size();i++) {
			
			this.myInterpreter.getPredicates().get(i).print();
	
		}
		
		System.out.println(); System.out.println("Fine stampa predicati ristrutturati  ");	

		System.out.println("************************************************************************************************************"); 
	}
	
	
}
