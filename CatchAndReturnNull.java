package com.concordia.soen;

import java.io.IOException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ReturnStatement;

public class CatchAndReturnNull {
	static int count=0;
	public static int checkCatchAndReturnNullFinder(String source) throws IOException
	{
		ASTParser parser = ASTParser.newParser(AST.getJLSLatest());
		
			parser.setSource(source.toCharArray());
			
			ASTNode root = parser.createAST(null);
					
			root.accept(new Visitor());
			int ans = count;
			count =0;
			return ans;
	}
	static class Visitor extends ASTVisitor
	{
		 private boolean hasAntiPattern = false;
	  
		 
		@Override
	    public boolean visit(ReturnStatement node) {
			int startPosition = node.getStartPosition();
	        Expression expression = node.getExpression();
	        if (expression != null && expression.getNodeType() == ASTNode.NULL_LITERAL) {
	            ASTNode parent = node.getParent();
	            while (parent != null && !(parent instanceof CatchClause)) {
	                parent = parent.getParent();
	            }
	            if (parent instanceof CatchClause) {
	                hasAntiPattern = true;
	                int parentPosition = parent.getStartPosition();
	                count +=1;
                	driver.writeToFile("Catch n return null found at line " +   
	                ((CompilationUnit) node.getRoot()).getLineNumber(startPosition) + " in catch clause at line " +
	                ((CompilationUnit) node.getRoot()).getLineNumber(parentPosition));
	                
	                
	            }
	        }
	        return true;
	    }
		
	}
}
