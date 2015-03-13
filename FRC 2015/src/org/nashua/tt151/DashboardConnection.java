package org.nashua.tt151;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.nashua.tt151.protocol.JSONMessage;

public class DashboardConnection {
	
	private Socket socket;
	private BufferedReader reader;
	private PrintWriter writer;
	private boolean running = true;
	public static final String MSG_DISC = "!DC";
	
	public DashboardConnection( String host ) throws UnknownHostException, IOException {
		this( new Socket( host, 1735 ) );
	}
	
	public DashboardConnection( Socket s ) throws UnknownHostException, IOException {
		this.socket = s;
		reader = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
		writer = new PrintWriter( socket.getOutputStream() );
		
		new Thread() {
			public void run() {
				while ( running ) {
					if ( !socket.isConnected() || socket.isClosed() ) {
						break;
					}
					try {
						String message = reader.readLine();
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
	
	public boolean isRunning() {
		return running;
	}
	
	public void send( String message ) {
		writer.println( message );
		writer.flush();
	}
	
	public void send( JSONMessage message ) {
		send( message.serialize() );
	}
	
}
