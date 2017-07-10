package expressionPackage;

import interpreterPackage.GeneralVisitor;
import interpreterPackage.PredicateStructuring;

 public class LambdaExpr extends Expression {

	private Expression subject;
	private StringExpr regentVerb;
	private StringExpr adverb;
	private Expression complement1;  
	private Expression complement2; 
	private Expression temporalComplement;
	
	
	private PredicateStructuring myPS;
	private boolean isPredicate; 
	
	
	public LambdaExpr(Expression sub, StringExpr adv, StringExpr rV, 
					  Expression comp1, Expression comp2, Expression temp){
		
		subject = sub;
		adverb = adv;
		regentVerb = rV;
		complement1 = comp1;
		complement2 = comp2;
		temporalComplement = temp;
		
		myPS = null;
		isPredicate = false;
		
	}

	public StringExpr getRegentVerb() {
		return regentVerb;
	}

	public Expression getSubject() {
		return subject;
	}

	public StringExpr getAdverb() {
		return adverb;
	}
	
	public Expression getComplement1() {
		return complement1;
	}

	public Expression getComplement2() {
		return complement2;
	}

	public Expression getTemporalComplement() {
		return temporalComplement;
	}
	
	public PredicateStructuring getPredicateStructure() {
		return myPS;
	}
	
	public boolean isPredicate() {
		return isPredicate;
	}

	
	
	public void setRegentVerb(StringExpr regentVerb) {
		this.regentVerb = regentVerb;
	}

	public void setSubject(Expression subject) {
		
		this.subject = subject;
	}

	public void setComplement1(Expression object) {
		this.complement1 = object;
	}

	public void setTempComplement(Expression object) {
		this.temporalComplement = object;
	}
	
	public void setPredicateStructure(PredicateStructuring myPS) {
		this.myPS = myPS;
	}
	
	public void setIsPredicate(boolean isPredicate) {
		this.isPredicate = isPredicate;
	}

	
	public void accept(GeneralVisitor visitor) {
		visitor.visit(this);
	}

	public void print(){
		
		System.out.println();
		System.out.println("start printing of lambda expression parts");
		
		if(subject != null) {
			subject.print();
		}
		if(adverb != null) {
			adverb.print();
		}
		
		if(regentVerb != null) {
			regentVerb.print();
		}
		
		if(complement1 != null) {
			System.out.println(); System.out.println(); System.out.print("Primo complemento : "); complement1.print(); System.out.println();
		}
		if(complement2 != null) {
			System.out.println(); System.out.println(); System.out.print("Secondo complemento : "); complement2.print(); System.out.println();
		}
		if(temporalComplement != null) {
			System.out.println(); System.out.println(); System.out.print("Complemento di tempo : "); temporalComplement.print(); System.out.println();
		}
		System.out.println("is predicate : "+this.isPredicate);
		
	
		System.out.
		println("end printing of lambda expression parts");
		System.out.println();
	}
	
}



