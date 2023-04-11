package com.concordia.soen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class driver {
	
	public static void main(String args[]) throws IOException
	{
		
		for(String filename : args)
		{
			File directory = new File(filename);
			readDirectory(directory);
		}
		
	}
	public static void readDirectory(File directory) throws IOException {
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                readDirectory(file); // Recursively read subdirectories
            } else {
                read(file); // Read each file
            }
        }
    }
	public static void read(File file) throws IOException 
    {
		if(file.getName().contains(".java") || file.getName().contains(".JAVA"))
		{
			Path path = Paths.get(file.getPath());
	    	String source =null;
			try {
				source = Files.lines(path).collect(Collectors.joining("\n"));
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			writeToFile("\nLooking for Anti-Patterns in file: "+file.getName());
			int destWrapCount = DestructiveWrapping.checkDestructiveWrapping(source);
			int throwSinkCount = ThrowsKitchenSink.checkThrowKitchenSink(source);
			int catchReturnNullCount = CatchAndReturnNullFinder.checkCatchAndReturnNullFinder(source);
			int nestedTryCount = NestedTry.checkNestedTry(source);		
			if((destWrapCount!=0) || (throwSinkCount!=0) || (catchReturnNullCount!=0) || (nestedTryCount!=0) )
			{
				String send = "\nNumber of Anti-patterns found in the file are below:\n"+"Destructive Wrapping: "+destWrapCount+"\nThrow Kitchen Sink: "+throwSinkCount+"\nCatch and return null: "+catchReturnNullCount+"\nNested try: "+nestedTryCount+"\n--------------------------------------------------------------------";
				writeToFile(send);
			}

		}
    }
	public static void writeToFile(String text)
	{
		try {
			FileWriter fw = new FileWriter("antiPattern.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
		    PrintWriter out = new PrintWriter(bw);
		    out.print(text);
		    out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
}
