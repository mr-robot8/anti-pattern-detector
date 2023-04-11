package com.concordia.soen;
import java.io.IOException;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TryStatement;


public class NestedTry {
	static int count=0;
	public static int checkNestedTry(String source) throws IOException
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
			
	        if (node.getBody() != null) 
	        {
	        	for (Object statement : node.getBody().statements()) 
	        	{
	        		if (statement instanceof TryStatement) 
	        		{
	        			count+=1;
	        			driver.writeToFile("\nNested Try Anti-pattern detected between line " + 
	     	                   ((CompilationUnit) node.getRoot()).getLineNumber(startPosition) + 
	     	                   " and line " + ((CompilationUnit) node.getRoot()).getLineNumber(endPosition) );
	        			break;
	                }
	            }
	        }
	        return true;
	    }
	}
}
