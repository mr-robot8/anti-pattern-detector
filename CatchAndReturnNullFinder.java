package com.concordia.soen;

import java.io.IOException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.TryStatement;

public class CatchAndReturnNullFinder {
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
		@Override
	    public boolean visit(TryStatement node) 
		{
			int startPosition = node.getStartPosition();
			int length = node.getLength();
			int endPosition = startPosition + length -1;
			
			for (Object obj : node.catchClauses()) 
			{
				CatchClause catchClause = (CatchClause) obj;
	            for (Object catchObj : catchClause.getBody().statements())
	            {
	            	if (catchObj instanceof ReturnStatement) 
	            	{
	            		ReturnStatement rt = (ReturnStatement) catchObj;
	                    if (rt.getExpression() == null || rt.getExpression() instanceof NullLiteral) 
	                    {
	                    	count +=1;
	                    	driver.writeToFile("\nCatch and Return Null Anti-pattern detected between line " + 
	         	                   ((CompilationUnit) node.getRoot()).getLineNumber(startPosition) + 
	         	                   " and line " + ((CompilationUnit) node.getRoot()).getLineNumber(endPosition) );
	                    }
	                }
	            }
	        }
	        return true;
	    }
	}
}