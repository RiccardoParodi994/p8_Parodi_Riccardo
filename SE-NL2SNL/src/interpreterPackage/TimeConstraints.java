package interpreterPackage;

import expressionPackage.LambdaExpr;

public class TimeConstraints {

	private EvaluationVisitor evaluator;
	
	private int num;
	private int type;
	private String timeUnits;
	
	//serve per la memorizzazione della stringa rappresentante il complemento in cui viene specificato il vincolo temporale
	private String tempComplement;

	public TimeConstraints(LambdaExpr l, EvaluationVisitor eval) {
		 
		evaluator = eval;
		
		num = -1; 
		type = -1;  
		
		timeUnits = null;
		tempComplement = null;
		
		//inizio l'analisi della mia lambdaExpr
		
		//controllo innanzitutto il primo complemento per 
		
//		if(eval.getTempComp() != null || eval.getComp1() != null || eval.getComp2() != null) {
		if(eval.getTempComp() != null || eval.getComp1() != null) {
			
			this.doParseComplements();
		}

	}
	
	
	public void doParseComplements() {
		
		EvaluationVisitor eval = this.evaluator; 
		
		String probableTempComplement = null;
		
		if(eval.getTempComp() != null) {
			
			//siamo a cavallo perchè così facendo ho già memorizzato il complemento di tempo
			probableTempComplement = eval.getTempComp().getValue(); 
			this.typeCheck(probableTempComplement);
			
		}
		else if(eval.getComp1() != null){
			
			probableTempComplement = eval.getComp1().getValue();
			this.typeCheck(probableTempComplement);
		}
//		else if(eval.getComp2() != null){
//			
//			probableTempComplement = eval.getComp2().getValue(); 
//			this.typeCheck(probableTempComplement);
//		}

	
	}

	/**
	 * 
	 * @param temporalComplement
	 */
	public void typeCheck(String tempComp) {
		/**
		 * questa funzione setta sia il tipo e il numero, se esistono
		 */
				
		//prima trovo il numero e lo elimino dalla stringa
		int j = -1; int k = -1; 
		for(int i=0;i<tempComp.length() && j<1;i++) {
			
			if(Character.isDigit(tempComp.charAt(i))) {
				
				k = i;
				j = this.doParseNumber(i, tempComp);
			}
		}
		
		
		if(j > 0) {
			
			//significa che un numero è stato trovato quindi è probabile che abbiamo trovato un complemento di tempo
			
			//appunto perchè è solo probabile e non certo dovrei assegnare delle probabilità 
			
			
			//quindi rielaboro la string tempComp
			String newTempComp = tempComp.substring(0, k-1)+" "+tempComp.substring(j+1);
			
			String adapted = this.adapt(newTempComp); 
				
			int i = 0;
			
			for(i=0; i<adapted.length(); i++) {
				
				//quindi procedo con la verifica del tipo di appartenenza
				if(i+12<adapted.length() && adapted.substring(i, i + 12).equals("for at least")) {
					this.type = 16;  this.tempComplement = adapted;  i = i + 12;
				}
				else if(i+13<adapted.length() && adapted.substring(i, i + 13).equals("after at most")) {
					this.type = 15;  this.tempComplement = adapted;  i = i + 13;
				} 
				else if(i+14<adapted.length() && adapted.substring(i, i + 14).equals("at least every")) {
					this.type = 14;  this.tempComplement = adapted;  i = i + 14;
				}
				else if(i+13<adapted.length() && adapted.substring(i, i + 13).equals("for less than")){
					this.type = 13;  this.tempComplement = adapted;  i = i + 13;
				}

			}
			
			if(this.type<0) {
				
				for(i=0; i<adapted.length(); i++) {
					
					//quindi procedo con la verifica del tipo di appartenenza
					if(i+3<adapted.length() && adapted.substring(i, i + 3).equals("for")) {
						this.type = 8;  //assegno 8 perchè potrebbe essere sia 16 che 13 (sò che questo perchè non rispetta granché il concetto di causa-effetto ma non avevo idee migliori)  
						this.tempComplement = adapted;  i = i + 3;
					}
					else if(i+5<adapted.length() && adapted.substring(i, i + 5).equals("after")) {
						this.type = 15;  this.tempComplement = adapted;  i = i + 5;
					}
					else if(i+5<adapted.length() && adapted.substring(i, i + 5).equals("every")) {
						this.type = 14;  this.tempComplement = adapted;  i = i + 5;
					}
				}
			}
			
			
			for(int h = 0; h<adapted.length(); h++) {
				
				if(h+7<adapted.length() && adapted.substring(h, h+7).equals("minutes")) {
					this.timeUnits = "minutes";  
				}
				else if(h+6<adapted.length() && adapted.substring(h, h+6).equals("minute")) {
					this.timeUnits = "minute";  
				}
				else if(h+7<adapted.length() && adapted.substring(h, h+7).equals("seconds")) {
					this.timeUnits = "seconds"; 
				}
				else if(h+6<adapted.length() && adapted.substring(h, h+6).equals("second")) {
					this.timeUnits = "second"; 
				}	
			}
		}
		else {
			//nessun numero è stato trovato
			this.type = -1;
		}
		
	}
	
	
	public int doParseNumber(int i, String text) {
		
		int j = i;
		
		//questo while è necessario per l'analisi completa del numero
		while (j < text.length() && Character.isDigit(text.charAt(j))) {
			j = j + 1;
		}
		
		//restituisce la sottostringa da i a j contenente il numero cercato
		String numAsText = text.substring(i,j);
		
		this.num = Integer.parseInt(numAsText);
		
		return j;
	}
	
	public String adapt(String toA) {

		String result = toA;
		
		for(int i=0; i<toA.length(); i++) {
			
			if(toA.charAt(i) == ' ') {
				
				if(i+1 < toA.length()) {
					
					int j = i+1;  //perchè almeno uno spazio bianco lo voglio lasciare
					
					while(j<toA.length() && toA.charAt(j) == ' ') {	
						j++;	
					}
					
					if(j>i+1) {
						toA = toA.substring(0, i+1)+toA.substring(j);
					}
					i = j;		
				}
			}			
		}

		result = toA+" ";

		return result;
	}
	
	//getters
	public int getNum() {
		return this.num;
	}
	
	public String getTimeUnits() {
		return this.timeUnits;
	}
	
	public int getType() {
		return this.type;
	}
	
	public String getTempComplement() {
		return this.tempComplement;
	}
	
}
