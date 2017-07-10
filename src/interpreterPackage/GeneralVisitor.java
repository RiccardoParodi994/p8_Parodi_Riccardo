package interpreterPackage;

import expressionPackage.GeneralPhraseExpr;
import expressionPackage.LambdaExpr;
import expressionPackage.StringExpr;

public interface GeneralVisitor {

	public void visit(StringExpr toVisit);
	public void visit(GeneralPhraseExpr toVisit);
	public void visit(LambdaExpr toVisit);
	
}
