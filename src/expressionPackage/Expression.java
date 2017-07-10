package expressionPackage;
//sarà la classe padre della mia struttura per la memorizzazione delle fopcExpr

import interpreterPackage.EvaluationVisitor;
import interpreterPackage.GeneralVisitor;

public abstract class Expression {

	public abstract void print();
	
	
	public abstract void accept(GeneralVisitor visitor);
	
}
