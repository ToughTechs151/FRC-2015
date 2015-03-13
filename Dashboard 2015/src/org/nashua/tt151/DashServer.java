package org.nashua.tt151;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class DashServer {
	public static interface ConnectionListener {
		public void onConnect( Socket s );
		
		public void onDataReceived( Socket s, String msg );
		
		public void onDisconnect( Socket s );
	}
	
	private ArrayList<Socket> clients = new ArrayList<Socket>();
	private ConnectionListener listener;
	private int port;
	private HashMap<Socket, BufferedReader> readers = new HashMap<Socket, BufferedReader>();
	private ServerSocket server;
	private HashMap<Socket, PrintWriter> writers = new HashMap<Socket, PrintWriter>();
	private HashMap<Socket, Long> lastMsg = new HashMap<Socket, Long>();
	private final long TIMEOUT = 2500;
	public static final String MSG_DISC = "!DC";
	
	public DashServer( final int port, ConnectionListener cl ) throws IOException {
		listener = cl;
		this.port = port;
		server = new ServerSocket( port );
		new Thread() {
			public void run() {
				Logger.logLine( "Server initialized. Listening for connections on port " + port + "..." );
				while ( true ) {
					try {
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
		new Thread() {
			public void run() {
				while ( true ) {
					try {
						for ( int i = 0; i < clients.size(); i++ ) {
							Socket client = clients.get( i );
							if ( client != null ) {
								BufferedReader reader = readers.get( client );
								if ( client.isConnected() && !client.isClosed() ) {
									if ( reader.ready() ) {
										String msg = reader.readLine();
										if ( msg != null && !msg.trim().equals( "" ) && listener != null ) {
											lastMsg.put( client, System.currentTimeMillis() );
											listener.onDataReceived( client, msg );
										}
									}
								} else {
									disconnect( client );
									i--;
								}
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
	
	public void disconnect() {
		try {
			for ( int i = 0; i < clients.size(); i++ ) {
				disconnect( clients.get( i ) );
				i--;
			}
		} catch ( Exception e ) {}
	}
	
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
	
	public int getPort() {
		return port;
	}
	
	public void send( Socket client, String msg ) {
		try {
			PrintWriter writer = writers.get( client );
			System.out.println( msg );
			writer.print( (char) msg.length() );
			writer.print( msg );
			writer.flush();
		} catch ( Exception e ) {}
	}
	
	public void sendAll( String msg ) {
		try {
			for ( int i = 0; i < clients.size(); i++ ) {
				send( clients.get( i ), msg );
			}
		} catch ( Exception e ) {}
	}
}
