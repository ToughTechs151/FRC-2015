package org.nashua.tt151;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A TCP socket server that listens for connections from robots
 * 
 * @author Kareem El-Faramawi
 */
public class DashServer {
	
	/**
	 * A connection listener for handling connection and data events
	 */
	public static interface ConnectionListener {
		public void onConnect( Socket s );
		
		public void onDataReceived( Socket s, String msg );
		
		public void onDisconnect( Socket s );
	}
	
	// Server and port
	private ServerSocket server;
	private int port;
	
	// List of all clients
	private ArrayList<Socket> clients = new ArrayList<Socket>();
	
	// Event listener
	private ConnectionListener listener;
	
	// Maps of streams and message times for all clients
	private HashMap<Socket, BufferedReader> readers = new HashMap<Socket, BufferedReader>();
	private HashMap<Socket, PrintWriter> writers = new HashMap<Socket, PrintWriter>();
	private HashMap<Socket, Long> lastMsg = new HashMap<Socket, Long>();
	
	// Disconnection timeout and special message
	private final long TIMEOUT = 2500;
	public static final String MSG_DISC = "!DC";
	
	/**
	 * Initialize the dashboard server
	 * 
	 * @param port Port to listen on
	 * @param cl ConnectionListener for handling events
	 * @throws IOException
	 */
	public DashServer( final int port, ConnectionListener cl ) throws IOException {
		listener = cl;
		this.port = port;
		server = new ServerSocket( port );
		
		// Thread to listen for new connection
		new Thread() {
			public void run() {
				Logger.logLine( "Server initialized. Listening for connections on port " + port + "..." );
				while ( true ) {
					try {
						// When a client connects, get all IO streams and start listening for messages
						Socket c = server.accept();
						readers.put( c, new BufferedReader( new InputStreamReader( c.getInputStream() ) ) );
						writers.put( c, new PrintWriter( c.getOutputStream() ) );
						clients.add( c );
						lastMsg.put( c, System.currentTimeMillis() );
						if ( listener != null ) {
							listener.onConnect( c );
						}
					} catch ( Exception e ) {}
				}
			}
		}.start();
		
		// Thread to listen for incoming messages, passing them on to the event handler
		new Thread() {
			public void run() {
				while ( true ) {
					try {
						// Attempt to read a message from each client
						for ( int i = 0; i < clients.size(); i++ ) {
							// Get the client socket
							Socket client = clients.get( i );
							if ( client != null ) {
								// Get the input stream
								BufferedReader reader = readers.get( client );
								// Check if the client is active
								if ( client.isConnected() && !client.isClosed() ) {
									if ( reader.ready() ) {
										// Read a message
										String msg = reader.readLine();
										if ( msg != null && !msg.trim().equals( "" ) && listener != null ) {
											// Record the message time
											lastMsg.put( client, System.currentTimeMillis() );
											
											// Call the event handler
											listener.onDataReceived( client, msg );
										}
									}
								} else {
									// Disconnect if the client was not active
									disconnect( client );
									i--;
								}
								
								// If the client timed out, disconnect
								if ( ( System.currentTimeMillis() - lastMsg.get( client ).longValue() ) > TIMEOUT ) {
									Logger.logLine( "Client " + client + " timed out" );
									disconnect( client );
									i--;
								}
							}
						}
					} catch ( Exception e ) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	/**
	 * Disconnects from all clients
	 */
	public void disconnect() {
		try {
			for ( int i = 0; i < clients.size(); i++ ) {
				disconnect( clients.get( i ) );
				i--;
			}
		} catch ( Exception e ) {}
	}
	
	/**
	 * Disconnects from a client
	 * 
	 * @param client Socket of the client
	 */
	public void disconnect( Socket client ) {
		try {
			send( client, MSG_DISC );
			clients.remove( client );
			readers.remove( client );
			writers.remove( client );
			if ( listener != null ) {
				listener.onDisconnect( client );
			}
		} catch ( Exception e ) {}
	}
	
	/**
	 * @return Server port
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Sends a message to a client
	 * 
	 * @param client Socket of the client
	 * @param msg Message to be sent
	 */
	public void send( Socket client, String msg ) {
		try {
			PrintWriter writer = writers.get( client );
			System.out.println( msg );
			writer.print( (char) msg.length() );
			writer.print( msg );
			writer.flush();
		} catch ( Exception e ) {}
	}
	
	/**
	 * Sends a message to all clients
	 * 
	 * @param msg Message to be sent
	 */
	public void sendAll( String msg ) {
		try {
			for ( int i = 0; i < clients.size(); i++ ) {
				send( clients.get( i ), msg );
			}
		} catch ( Exception e ) {}
	}
}
