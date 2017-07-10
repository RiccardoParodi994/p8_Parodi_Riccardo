package expressionPackage;

import java.util.ArrayList;

import interpreterPackage.EvaluationVisitor;
import interpreterPackage.GeneralVisitor;

/**
 * 
 * Mi serve per memorizzare dei requisiti che si presentano non più nella 
 * semplice forma di (np)(vp). Bensì in quella di (np)+(vp(vp  +  vp  +  ...))
 * 
 */


public class ComplexLambdaExpr extends Expression {

	private LambdaExpr first;
	private LambdaExpr second;
	private Expression subject;
	
	
	public ComplexLambdaExpr(LambdaExpr f, LambdaExpr s) {
		
		this.subject = null;
		
		this.first = f;  this.second = s;
	}
	
	public LambdaExpr getFirst() {
		return this.first;
	}

	public LambdaExpr getSecond() {
		return this.second;
	}
	
	public Expression getSubject() {
		return subject;
	}
	

	
	public void setSubject(Expression subject) {
		
		this.subject = subject;
		
		this.first.setSubject(subject);
		this.second.setSubject(subject);
	}
	
	public void accept(GeneralVisitor visitor) {
		
		//visitor.visit(this);
		
	}
	
	
	public void print() {
		
		System.out.println(); System.out.println("Start printing complexLambdaExpr : ");
		
		System.out.println("Subject : "); this.subject.print(); System.out.println();
		
		System.out.println("First : "); this.first.print(); System.out.println();
		
		System.out.println("Second : ");  this.second.print();  System.out.println();
		
		System.out.println(); System.out.println("End printing complexLambdaExpr");
	}
}
