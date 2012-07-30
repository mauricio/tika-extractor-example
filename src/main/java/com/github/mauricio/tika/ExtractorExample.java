package com.github.mauricio.tika;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;

public class ExtractorExample {

	public static void main(String[] args) {		
		BodyContentHandler handler = new BodyContentHandler(-1);
		Metadata metadata = new Metadata();
		Parser parser = new AutoDetectParser( new DefaultDetector() );
		
		File input = new File("paper.pdf");
		File output = new File("output.txt");
		
		System.out.printf( "Processing file %s to %s%n", input.getAbsolutePath(), output.getAbsolutePath() );
		
		InputStream inputStream = null;
		Writer writer = null;
		
		try {
			inputStream = new BufferedInputStream( new FileInputStream(input) );
			parser.parse( inputStream, handler, metadata, new ParseContext() );
			
			writer = new FileWriter(output);
			writer.write( handler.toString().replaceAll("[\\t\\n\\f\\r]", " " ));
			
		} catch ( Exception e ) {
			throw new IllegalStateException(e);
		} finally {
			close(inputStream, writer);			
		}
		

	}
	
	public static void close( Closeable ... closeables ) {		
		for( Closeable c : closeables ) {
			if ( c != null ) {
				try {
					
					if ( c instanceof Flushable ) {
						Flushable f = (Flushable) c;
						f.flush();
					}
					
					c.close();
				} catch ( IOException e) {
					throw new IllegalStateException(e);
				}			
			}				
		}
	}

}
