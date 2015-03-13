package org.nashua.tt151;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Simple message logger that writes to a text file
 * 
 * @author Kareem El-Faramawi
 */
public class Logger {
	private static boolean fileCreated;
	private static final String LOG_PATH = "Dashboard2015Logs" + File.separator + System.currentTimeMillis() + ".log";
	private static final File LOG_FILE = new File( LOG_PATH );
	private static BufferedWriter out;
	
	private static void createLogFile() {
		if ( !LOG_FILE.exists() ) {
			try {
				LOG_FILE.getParentFile().mkdirs();
				LOG_FILE.createNewFile();
				fileCreated = true;
				out = new BufferedWriter( new FileWriter( LOG_FILE ) );
			} catch ( IOException e ) {
				System.err.println( "FAILED TO CREATE LOG FILE" );
				e.printStackTrace();
			}
		} else {
			fileCreated = true;
		}
	}
	
	public static void logLine( String message ) {
		System.out.println(message);
		if ( !fileCreated ) {
			createLogFile();
		}
		try {
			for ( String line : message.split( "\n" ) ) {
				out.write( line );
				out.newLine();
			}
			out.flush();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
}
