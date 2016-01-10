package org.nashua.tt151;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.nashua.tt151.protocol.JSONMessage;

/**
 * A simple TCP socket connection. This is used as a client connecting to the laptop (server)
 */
public class DashboardConnection {
	// Socket connection
	private Socket socket;
	
	// I/O streams
	private BufferedReader reader;
	private PrintWriter writer;
	
	private boolean running = true;
	
	// Special message that, if received, will cause the robot to close the connection
	public static final String MSG_DISC = "!DC";
	
	/**
	 * Connects to the given host over port 1735, as stated in the FRC manual as the port used for things like
	 * this. Double check this port in the rules if this class is being reused.
	 * 
	 * @param host IP address of the laptop
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public DashboardConnection( String host ) throws UnknownHostException, IOException {
		this( new Socket( host, 1735 ) );
	}
	
	/**
	 * Sets up the connection to the given socket
	 * 
	 * @param s Socket connection
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public DashboardConnection( Socket s ) throws UnknownHostException, IOException {
		this.socket = s;
		
		// Set up I/O streams
		reader = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
		writer = new PrintWriter( socket.getOutputStream() );
		
		// Listen for messages
		new Thread() {
			public void run() {
				while ( running ) {
					// Quit if the socket is no longer connected
					if ( !socket.isConnected() || socket.isClosed() ) {
						break;
					}
					try {
						
						// Attempt to read a message from the input stream
						String message = reader.readLine();
						
						// If no message is received or the DC message is received, disconnect from the server
						if ( message == null || message.equals( MSG_DISC ) ) {
							disconnect();
							break;
						}
						
					} catch ( IOException e ) {
						System.err.println( "Error reading from input: " + e.getMessage() );
					}
					Thread.yield();
				}
			}
		}.start();
	}
	
	/**
	 * Closes all streams and disconnects from the server
	 */
	public void disconnect() {
		try {
			send( MSG_DISC );
			socket.close();
			reader.close();
			writer.close();
			running = false;
		} catch ( IOException e ) {
			System.err.println( "Error disconnecting: " + e.getMessage() );
		}
	}
	
	/**
	 * @return If the connection is currently active
	 */
	public boolean isRunning() {
		return running;
	}
	
	/**
	 * Sends a string message to the server
	 * 
	 * @param message Message to send
	 */
	public void send( String message ) {
		writer.println( message );
		writer.flush();
	}
	
	/**
	 * Serializes and sends a JSON message to the server
	 * 
	 * @param message JSON message to be sent
	 */
	public void send( JSONMessage message ) {
		send( message.serialize() );
	}
	
}
