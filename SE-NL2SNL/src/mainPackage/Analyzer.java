package mainPackage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.ScoredObject;
import interpreterPackage.Associator;
import interpreterPackage.FinalParser;
import interpreterPackage.Interpreter;
import interpreterPackage.PosTags;
import interpreterPackage.Signal;
import interpreterPackage.Signals;


/**
 * Tale classe è l'unica che viene istanziata nel main e agisce come segue :
 * prende innanzitutto il requisito dal Main chiamante e ne esegue un controllo.
 * Questo per via del fatto che si cerca il più possibile di rendere il requisito
 * pronto per la traduzione (quindi, per fare un esempio, si eviterà di far 
 * fallire il parsing semantico per via del mancato rispettamento del formato 
 * per i segnali seguente '{' + "Signal" + '}').
 * Dopo di chè si manda in esecuzione il Parser di Stanford che genera, per un 
 * massimo di venti volte, alberi sintattici che vengono di volta
 * in volta mandati in input all' "Associator" che, dopo averne effettuato
 * l'analisi semantica, cerca di eseguirne l'associazione. 
 * 
 * Se i primi venti tentativi di associazione falliscono si fa una modifica al
 * "FinalParser" e si "forza" l'analisi dell'albero sintattico generato 
 * dal Parser di Stanford. La modifica consiste nel "dare il via libera" alla
 * classe "VerbPhraseParser" per l'analisi di strutture sintattiche come segue : 
 * "(VP () ... (, ,) (SBAR))". Tale "SBAR" viene quindi "inviata" alla classe
 * "Interpreter" che a sua volta la rimanda alla chiamante "FinalParser" che la
 *  tratta come se fosse esterna alla "VP". Così facendo rientrerei nei casi 
 *  in cui mi aspetto essere gli alberi sintettici per la loro corretta analisi 
 *  semantica.
 *  Questo secondo tentativo viene eseguito, di nuovo, per un massimo di venti volte
 *  fino a che non si riesce nella corretta associazione.
 * 
 * @author Riccardo Parodi
 *
 */
public class Analyzer {

	private String numOfReq;
	private ArrayList<String> patterns;
	
	private Parser myParser;
	private PosTags myPosTags;
	private Signals mySignals;
	private boolean analyze;
	
	public Analyzer (Parser myParser, PosTags myPosTags, Signals mySignals,
			boolean analyze) {
		
		this.myParser = myParser;
		this.myPosTags = myPosTags;
		this.mySignals = mySignals;
		this.analyze = analyze;
		
		this.patterns = new ArrayList<String>();
	}
	
	public void analyze(String req) throws Exception {
		
		
		String controlledReq = this.controll(req, mySignals);
		
		Parser stanfordParser = myParser;
		
		if(analyze) {
			System.out.println("---------------------------------------------------------------------------------------------------------------------");
			System.out.println(controlledReq);
			if(this.numOfReq != null) {
				System.out.print("Req "+this.numOfReq+" : ");
			}
		}
		
		boolean hasFailed = true;
		
		List<ScoredObject<Tree>> myList = stanfordParser.doMultipleSyntacticParse(controlledReq);
		Iterator<ScoredObject<Tree>> myIt = myList.iterator();
		
		
		while (myIt.hasNext() && hasFailed) {
			
			String toAssociate = myIt.next().toString();  
					
			Interpreter myInterpreter = new Interpreter(myPosTags);  
			
			FinalParser myFP = new FinalParser(myPosTags, myInterpreter);	
			
			String adapted = adapt(toAssociate);  myFP.doParse(adapted);
			
			
			if(myFP.isCorrectParsing()) {
				
				//qui andrà chiamata la classe per l'associazione
				Associator myAssociator = new Associator(myPosTags, myFP, analyze);	
				this.patterns.add(myAssociator.getPattern());
			
				hasFailed = false;
			}	
			
		}
		
		if(hasFailed) {

			if(analyze) {
				System.out.println("Il parsing sintattico non ha generato il risultato atteso."); 
				System.out.println("La seguente traduzione deriva dalla forzatura del parsing sintattico "
						+ "effettuato dal Parser di Stanford : ");
			}
			
			myList = stanfordParser.doMultipleSyntacticParse(controlledReq);
			myIt = myList.iterator();
			
			hasFailed = true;
			
			while (myIt.hasNext() && hasFailed) {
				
				String toAssociate = myIt.next().toString();  
						
				Interpreter mySecondInterpreter = new Interpreter(myPosTags);  
				mySecondInterpreter.secondWayBe();
			
				FinalParser mySecondFP = new FinalParser(myPosTags, mySecondInterpreter);	
				
				String adapted = adapt(toAssociate);  mySecondFP.doParse(adapted);	
				
				if(mySecondFP.isCorrectParsing()) {
				
					//qui andrà chiamata la classe per l'associazione
					Associator myAssociator = new Associator(myPosTags, mySecondFP, analyze);				
					this.patterns.add(myAssociator.getPattern());
					
					hasFailed = false;
				}
			}
			
		}

		if(analyze) {
			if (hasFailed) {
				System.out.println("il parsing semantico è fallito nuovamente. "
						+ "Impossibile effetturare l'associazione requisito pattern. ");
				this.patterns.add("il parsing semantico è fallito nuovamente. Impossibile effetturare l'associazione requisito pattern. ");
			}
			System.out.println("---------------------------------------------------------------------------------------------------------------------");

		}


		
	}
	
	public static String adapt(String toA) {
		
		String help = null;

		if(toA.substring(0, 6).equals("(ROOT ")) {

			int i = 0;
			for(i=toA.length()-1; i>=0 && (toA.charAt(i) != '@'); i--) {}
	
			int index = i;		
			if(index != 0) {
				int stop = index - 2;
				help = toA.substring(6, stop);
			}
		}

		return help;
	}
	
	/**
	 * 
	 * @param requirement
	 * @param mySignals
	 * @return una stringa controllata e pronta per il Parser di Stanford
	 */
	public String controll(String req, Signals mySignals) {
		
		String controlled = req;
		
		controlled = this.storeTheNumberOfReq(req);
		
		controlled = this.checkTheDot(controlled);
		
		controlled = this.checkTheBraces(controlled, mySignals);
		
		return controlled;
	}
	
	public String storeTheNumberOfReq(String req) {
		/**
		 * Mi permette di recuperare il numero associato al requisito condiderato
		 */
		
		String modifiedReq = req;
		
		for(int i = 0; i<req.length(); i++) {
			
			if(i+4<req.length() && req.substring(i, i+4).equals("{Req")) {
				
				int start = i;
				
				int stop = this.doParseNumber(i+4, req);
				
				if(req.charAt(stop) == '}') {
					
					modifiedReq = req.substring(0, start)+req.substring(stop+1);
					
					return modifiedReq;
				}
			}
		}
		
		return modifiedReq;
	}
	
	public int doParseNumber(int i, String req) {
	
		int j = i;
		
		//questo while è necessario per l'analisi completa del numero
		while (j < req.length() && Character.isDigit(req.charAt(j))) {	
			j = j + 1;
		}
		
		//restituisce la sottostringa da i a j contenente il numero cercato
		String numAsText = req.substring(i,j);
		
		this.numOfReq = numAsText;  
		 
		return j;
	}
	
	public String checkTheDot(String req) {
		/**
		 * Se non c'è il punto alla fine del requisito lo aggiungo
		 */
		
		String modified = req;
		
		if(req.charAt(req.length()-1) != '.' && req.charAt(req.length()-2) != '.'
				&& req.charAt(req.length()-3) != '.') {
			modified = req+".";
		}
		
		return modified;
	}
	
	public String checkTheBraces(String req, Signals mySignals) {
		/**
		 * Individuo i segnali all'interno del requisito e controllo che i caratteri
		 * che li (i segnali) seguono e precedono siano rispettivamente i seguenti } {.
		 * Altrimenti li inserisco "manualmente".
		 */

		String modified = req;
		
		int length = modified.length();
		
		Iterator<Signal> myIt = mySignals.getSignalsFromFile().iterator();

		while(myIt.hasNext()) {
			
			String signal = myIt.next().getName();
			
			int sL = signal.length();
			
			for(int i = 0; i<length; i++) {
				
				if((i+ (sL)) < modified.length() && modified.substring(i, i+ (sL)).equals(signal)) {

					//ho trovato il segnale correttamente e procedo alla verifica dei caratteri (i-1) e (i+sL)
					
					if (modified.charAt(i-1) != '{') {
						
						//quindi procedo alla risoluzione del problema
						
						String firstHalf = modified.substring(0, i)+"{";  
						
						String secondHalf = modified.substring(i);  
						
						modified = firstHalf+secondHalf;  
						
						//aggiorno la lunghezza
						length = modified.length();
						
						//e ovviamente anche l'indice i
						i = i+1;
					}
					
					if (modified.charAt(i+sL) != '}') {
						
						//quindi procedo alla risoluzione del problema
						
						String firstHalf = modified.substring(0, i+(sL)); 
						
						String secondHalf = "}"+modified.substring(i+(sL));  
						
						modified = firstHalf+secondHalf;  
					
						//aggiorno la lunghezza
						length = modified.length();
						
						//e ovviamente anche l'indice i
						i = i+1;
					}
					
					i = i + sL; 
				}
			}
		}		
		
		return modified;
	}
	
	//getter
	public ArrayList<String> getPatterns() {
		return this.patterns;
	}
		
}
