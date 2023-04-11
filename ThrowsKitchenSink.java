package com.concordia.soen;

import java.io.IOException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class ThrowsKitchenSink {
	static int count=0;
	public static int checkThrowKitchenSink(String source) throws IOException
	{
		ASTParser parser = ASTParser.newParser(AST.getJLSLatest());
		parser.setSource(source.toCharArray());
		ASTNode root = parser.createAST(null);
		Visitor visitor = new Visitor();
		root.accept(visitor);
		int ans = count;
		count =0;
		return ans;
	}
	static class Visitor extends ASTVisitor
	{
		@Override
		public boolean visit(MethodDeclaration declaration)
		{
			int startPosition = declaration.getStartPosition();
			int length = declaration.getLength();
			int endPosition = startPosition + length -1;
			
			if(declaration.thrownExceptionTypes().size() > 1)
			{
				count+=1;
				driver.writeToFile("\nThrows Kitchen Sink Anti-pattern detected between line " + 
		                   ((CompilationUnit) declaration.getRoot()).getLineNumber(startPosition) + 
		                   " and line " + ((CompilationUnit) declaration.getRoot()).getLineNumber(endPosition));
			}
			return true; 
		}	
	}
}