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
	private static boolean fileCreated; // Indicates if the files exists/was created
	private static final String LOG_PATH = "Dashboard2015Logs" + File.separator + System.currentTimeMillis() + ".log";
	private static final File LOG_FILE = new File( LOG_PATH );
	private static BufferedWriter out; // Output stream to log file
	
	/**
	 * Creates the log file if it doesn't exist
	 */
	private static void createLogFile() {
		if ( !LOG_FILE.exists() ) {
			try {
				// Create director and file
				LOG_FILE.getParentFile().mkdirs();
				LOG_FILE.createNewFile();
				fileCreated = true;
				
				// Get output stream
				out = new BufferedWriter( new FileWriter( LOG_FILE ) );
			} catch ( IOException e ) {
				System.err.println( "FAILED TO CREATE LOG FILE" );
				e.printStackTrace();
			}
		} else {
			fileCreated = true;
		}
	}
	
	/**
	 * Prints out a message ending in a newline to the log file
	 * 
	 * @param message Message to print
	 */
	public static void logLine( String message ) {
		System.out.println( message );
		if ( !fileCreated ) {
			createLogFile();
		}
		try {
			// Separately print out each line in the message
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
