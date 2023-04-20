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
	static int total_files = 0;
	static int total_destructive_wrappings = 0;
	static int total_throw_kitchen_sink = 0;
	static int total_catch_and_return_null = 0;
	static int total_nested_try = 0;
	public static void main(String args[]) throws IOException
	{
		
		for(String filename : args)
		{
			File directory = new File(filename);
			readDirectory(directory);
		}
		System.out.println("Total number of files scanned: \t"+total_files);
		System.out.println("Summary of anti-patterns in the projects");
		System.out.println("Destructive Wrapping: \t"+total_destructive_wrappings);
		System.out.println("Throws Kitchen Sink: \t"+total_throw_kitchen_sink);
		System.out.println("Catch and return Null: \t"+total_catch_and_return_null);
		System.out.println("Nested Try: \t"+total_nested_try);
		
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
			total_files+=1;
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
			int catchReturnNullCount = CatchAndReturnNull.checkCatchAndReturnNullFinder(source);
			int nestedTryCount = NestedTry.checkNestedTry(source);	
			total_destructive_wrappings += destWrapCount;
			total_throw_kitchen_sink += throwSinkCount;
			total_catch_and_return_null += catchReturnNullCount;
			total_nested_try += nestedTryCount;
			if((destWrapCount!=0) || (throwSinkCount!=0) || (catchReturnNullCount!=0) || (nestedTryCount!=0) )
			{
				String send = "\nPath for file "+file.getName()+" is: "+path.toString().substring(25, path.toString().length())+"\nNumber of Anti-patterns found in the file are below:\n"+"Destructive Wrapping: "+destWrapCount+"\nThrow Kitchen Sink: "+throwSinkCount+"\nCatch and return null: "+catchReturnNullCount+"\nNested try: "+nestedTryCount+"\n--------------------------------------------------------------------";
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
