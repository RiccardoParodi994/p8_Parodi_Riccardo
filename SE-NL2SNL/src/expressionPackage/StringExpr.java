package expressionPackage;

import interpreterPackage.GeneralVisitor;

/**
 * Classe atta alla memorizzazione/gestione delle singole parole di un requisito
 *  e dei relativi POS-TAG.
 *   
 * @author Riccardo Parodi
 *
 */
public class StringExpr extends Expression {

	private String value;
	private String posTag;
	
	public StringExpr(String v, String pT){
		value = v;
		posTag = pT;
	}

	public String getValue() {
		return value;
	}

	public String getPosTag() {
		return posTag;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setPosTag(String posTag) {
		this.posTag = posTag;
	}
	
	public void accept(GeneralVisitor visitor) {
		visitor.visit(this);
	}
	
	public void print(){
		System.out.print(" [ value : "+value+" , pos-tag : "+posTag+" ] ");
	}
	
}
