package interpreterPackage;


import java.util.ArrayList;

import expressionPackage.GeneralPhraseExpr;

public class Associator {
	
	private FinalVisitor fv;
	
	private String pattern;
	private boolean correctAssociation;
	
	public Associator(PosTags myPosTags,
			FinalParser myFP, boolean analyze) throws Exception {
	
	
		this.pattern = "impossibile associarne il pattern.";
		this.correctAssociation = false;
		
		FinalVisitor fv = new FinalVisitor(myPosTags, myFP.getPredicates());  
		
		this.fv = fv;
		
		GeneralPhraseExpr sentence = myFP.getSentence();
		
		boolean correctParsing = false;
		
		if(sentence != null) {
			correctParsing = fv.doAssociation(sentence);
		}
		
		
		if (correctParsing) {
			
			//procedo all'associazione
			String pattern = this.buildPattern(fv.getGrouper(), fv.getScope(), fv.getSubordinate(), fv.getMain());
			
			if(pattern != null) {
				
				this.correctAssociation = true;
				if(analyze) {
					this.pattern = pattern;
					System.out.println(pattern); 
				}
			}
			else {
				
				this.correctAssociation = false;
				if(analyze) {
					System.out.println("impossibile associarne il pattern.");
				}
				
			}
			
		}
		else {
			
			this.correctAssociation = false;
			if(analyze) {
				System.out.println("impossibile associarne il pattern.");
			}
		}
		

	}

	
	//MOLTO IMPORTANTE
	public String buildPattern(ArrayList<ArrayList<Integer>> grouper, String scope, String subordinate, String main) {
		
		//printGrouper(grouper);
		
		String pattern = null;  int size = grouper.size();  int indexOfLastPredicate = grouper.size()-1;
		
		//faccio l'opportuna modifica alla size del grouper se c'è lo scope in modo da non contare il suo gruppo di predicati
		if(!scope.equals("Globally")) {
			size = size-1;  
		}

		
		if(size == 1) {
			
			//prendo quindi la mia unica componente
			
			
			//quindi costruisco il pattern 
			
			//prendo innanzitutto la predicate structure del mio predicato
			PredicateStructuring p1Structure = this.getPredicateStructure(grouper.get(indexOfLastPredicate).get(0));  
		
			if(p1Structure != null) {
				
				//inizio la sua analisi
				if(p1Structure.isShall()) {
					
					
					//controllo quindi se è o non complemento di tempo
					if(p1Structure.hasTempComplement()) {
						
						
						String timeUnits = "time unit(s)";
						if(p1Structure.getTimeConstraints().getTimeUnits() != null) { 
							timeUnits = p1Structure.getTimeConstraints().getTimeUnits();
						}
						
						
						//quindi recuperiamo il suo tipo e creiamo il pattern
						if(p1Structure.getTimeConstraints().getType() == 12 
								|| p1Structure.getTimeConstraints().getType() == 16
								|| p1Structure.getTimeConstraints().getType() == 8) {
							
							pattern = scope+", it is always the case that if "+main+" becomes satisfied, "
									+ "it holds for at least "+p1Structure.getTimeConstraints().getNum()+" "+timeUnits+". ";
						}
						else if(p1Structure.getTimeConstraints().getType() == 13) {
							pattern = scope+", it is always the case that if "+main+" becomes satisfied, "
									+ "it holds for less than "+p1Structure.getTimeConstraints().getNum()+" "+timeUnits+". ";
						}
						else if(p1Structure.getTimeConstraints().getType() == 14) {
							pattern = scope+", it is always the case that "+main+" holds "
									+ "at least every "+p1Structure.getTimeConstraints().getNum()+" "+timeUnits+". ";
						}
					}
					else {
						
						if(p1Structure.getAdverb() == null) {
							
							pattern = scope+", "+main+" eventually holds.";
							
						}
						else {
							
							//ora la discriminante è il tipo di avverbio
							if(p1Structure.getAdverb().equals("never")) {
								
								pattern = scope+", it is never the case that "+main+" holds.";
								
							}
							else if(p1Structure.getAdverb().equals("always")) {
								
								pattern = scope+", it is always the case that "+main+" holds.";
							}
						}
						
					}
					
					
				}
				
			}
			
			
		}
		else if(size == 2) {
			
			//quindi costruisco il pattern
			
			//prendo innanzitutto la prima componente che detta le ramificazioni tra i possibili pattern
			PredicateStructuring p1Structure = this.getPredicateStructure(grouper.get(indexOfLastPredicate).get(0));  

			if(p1Structure != null) {
				
				//inizio la sua analisi
				if(p1Structure.isShall()) {
					
					//controllo quindi se è o non complemento di tempo
					if(p1Structure.hasTempComplement()) {
						
						
						String timeUnits = "time unit(s)";
						if(p1Structure.getTimeConstraints().getTimeUnits() != null) { 
							timeUnits = p1Structure.getTimeConstraints().getTimeUnits();
						}
						
						
						//quindi recuperiamo il suo tipo e creiamo il pattern
						if(p1Structure.getTimeConstraints().getType() == 15) {
							
							pattern = scope+", "+" it is always the case that if "+subordinate+" holds , "
									+ "then "+main+" holds after at most "+p1Structure.getTimeConstraints().getNum()+" "+timeUnits+". ";
							
						}
						else if (p1Structure.getTimeConstraints().getType() == 12 
								|| p1Structure.getTimeConstraints().getType() == 16
								|| p1Structure.getTimeConstraints().getType() == 8) {
							
							pattern = scope+", "+" it is always the case that if "+subordinate+" holds , "
									+ "then "+main+" holds for at least "+p1Structure.getTimeConstraints().getNum()+" "+timeUnits+". ";
						}
						
					}
					else {
						
						//a questo punto le discriminanti diventano : 1 il tempo del verbo e 2 un ipotetico avverbio previously 
						if(p1Structure.isPastTense() || (p1Structure.getAdverb() != null && p1Structure.getAdverb().equals("previously"))) {
							
							//allora sono nel seguente caso
							pattern = scope+", "+" it is always the case that if "+subordinate+" holds, then "+main+" previously held.";
						}
						else {
							
							pattern = scope+", "+" it is always the case that if "+subordinate+" holds, then "+main+" eventually holds.";
						}
						
					}
				}
			}
			
		}
		else if(size == 3) {
			
			//avrei bisogno di esempi dei pattern 6-7-9-10 per fare completare questa parte applicando la mia idea 
			
		}
		else if(size == 4) {
			
			
			//idem come sopra per il pattern 11
			
		}
		
		
		return pattern;
	}
	
	
	public PredicateStructuring getPredicateStructure(int pos) {
		
		PredicateStructuring myP = null;
		
		for(int i=0;i<this.fv.getPredicates().size();i++) {
			
			if(this.fv.getPredicates().get(i).getPosition() == pos) {
				
				myP = this.fv.getPredicates().get(i);
				
			}
			
		}
		
		return myP;
	}
	
	/**
	 * 
	 * @return TRUE se è avvenuta correttamente l'associazione requisito-pattern
	 */
	public boolean isCorrectAssociation() {
		return this.correctAssociation;
	}
	
	public String getPattern() {
		return pattern;
	}
	
	//printer
	public static void printGrouper(ArrayList<ArrayList<Integer>> grouper) {
		
		System.out.println("size del grouper : "+grouper.size()+"; inizio stampa del grouper");
		for(int i=0;i<grouper.size();i++) {
			System.out.print("componenti nel gruppo #: ("+i+") : ");
			for(int j=0;j<grouper.get(i).size();j++) {
				System.out.print(" "+grouper.get(i).get(j)+" , ");
			}
			System.out.println("fine stampa del grouper");
			System.out.println();
		}
		
	}
}
