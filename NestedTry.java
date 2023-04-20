package com.concordia.soen;
import java.io.IOException;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TryStatement;

import com.concordia.soen.DestructiveWrapping.Visitor;


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
			ASTNode newNode = node.getParent();
			while(newNode != null)
			{
				if(newNode instanceof TryStatement)
				{
					int startPosition = newNode.getStartPosition();
					int endPosition = node.getStartPosition();
					count+=1;
        			driver.writeToFile("\nNested Try Anti-pattern detected between line " + 
     	                   ((CompilationUnit) node.getRoot()).getLineNumber(startPosition) + 
     	                   " and line " + ((CompilationUnit) node.getRoot()).getLineNumber(endPosition) );
					break;
				}
				newNode = newNode.getParent();
			}
	        return true;
	    }
	}
}
