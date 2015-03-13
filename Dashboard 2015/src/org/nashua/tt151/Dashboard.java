package org.nashua.tt151;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import org.json.JSONObject;
import org.nashua.tt151.DashServer.ConnectionListener;
import org.nashua.tt151.module.BandwidthModule;
import org.nashua.tt151.module.CameraModule;
import org.nashua.tt151.module.ConnectionModule;
import org.nashua.tt151.module.ConnectionModule.State;
import org.nashua.tt151.module.DeviceModule;
import org.nashua.tt151.module.InstructionsModule;
import org.nashua.tt151.protocol.JSONMessage;
import org.nashua.tt151.protocol.JSONParser;
import org.nashua.tt151.ui.SleekFrame;
import org.nashua.tt151.util.FileIOHelper;

public class Dashboard {
	static SleekFrame frame;
	static DeviceModule devices = new DeviceModule();
	static ConnectionModule state = new ConnectionModule();
	static CameraModule camera = new CameraModule();
	static InstructionsModule instructions = new InstructionsModule();
	static BandwidthModule bandwidth = new BandwidthModule();
	int port = 1735;
	private DashServer server;
	
	public static void main( String[] args ) {
		new Dashboard();
	}
	
	public Dashboard() {
		Logger.logLine( "[FRC 2015 Dashboard]" );
		Logger.logLine( Calendar.getInstance().getTime().toString() );
		Logger.logLine( "Camera IP Address: " + CameraModule.CAM_IP );
		Logger.logLine( "Port: " + port );
		
		// Create window
		frame = new SleekFrame( "Dashboard 2015" );
		frame.setIconImage( FileIOHelper.loadImage( "/img/tticon.png" ) );
		frame.setTitleFont( new Font( "OCR A Std", Font.PLAIN, 20 ) );
		
		final int HEIGHT = devices.getHeight() + ConnectionModule.HEIGHT;
		final int WIDTH = devices.getWidth() + camera.getWidth() + instructions.getWidth();
		
		JPanel panel = new JPanel( null );
		panel.setBackground( Color.GRAY.darker().darker() );
		
		// Position all modules
		devices.setBounds( 0, 0, devices.getWidth(), devices.getHeight() );
		camera.setBounds( devices.getWidth(), 0, camera.getWidth(), camera.getHeight() );
		instructions.setBounds( devices.getWidth() + camera.getWidth(), 0, instructions.getWidth(), instructions.getHeight() );
		bandwidth.setBounds( devices.getWidth(), camera.getHeight(), camera.getWidth(), devices.getHeight() - camera.getHeight() );
		state.setBounds( 0, devices.getHeight(), WIDTH, ConnectionModule.HEIGHT );
		
		// Add all modules
		panel.add( devices );
		panel.add( camera );
		panel.add( instructions );
		panel.add( bandwidth );
		panel.add( state );
		
		// Display frame
		panel.setBounds( SleekFrame.BORDER, SleekFrame.TITLE_HEIGHT, WIDTH, HEIGHT );
		frame.setLayout( null );
		frame.add( panel );
		frame.setSize( WIDTH, HEIGHT );
		frame.setVisible( true );
		
		new Timer().schedule( new TimerTask() {
			public void run() {
				frame.repaint();
			}
		}, 1, 1 );
		
		// Create server connection
		try {
			server = new DashServer( port, new ConnectionListener() {
				public void onConnect( Socket s ) {
					Logger.logLine( "Connected to: " + s + " at " + Calendar.getInstance().getTime().toString() );
					state.setState( State.CONNECTED );
					devices.clearAllDevices();
				}
				
				public void onDataReceived( Socket s, String message ) {
					JSONMessage msg = new JSONMessage( message );
					JSONObject json = msg.getContents();
					switch ( msg.getCommand() ) {
						case LOG:
							JSONParser.processLog( json );
							break;
						case STATUS_DEVICE:
							switch ( JSONParser.getDeviceType( json ) ) {
								case ANALOG:
									devices.registerAnalogDevice( JSONParser.parseAnalogDevice( json ) );
									break;
								case DIO:
									devices.registerDIODevice( JSONParser.parseDIODevice( json ) );
									break;
								case PWM:
									devices.registerPWMDevice( JSONParser.parsePWMDevice( json ) );
									break;
								case RELAY:
									devices.registerRelayDevice( JSONParser.parseRelayDevice( json ) );
									break;
								default:
									Logger.logLine( "Invalid device type" );
									break;
							}
							break;
						case STATUS_ROBOT:
							JSONParser.parseRobotState( json );
							break;
						case UNKNOWN:
						default:
							Logger.logLine( "Invalid message: " + message );
							break;
					
					}
				}
				
				public void onDisconnect( Socket s ) {
					Logger.logLine( "Disconnected from: " + s + " at " + Calendar.getInstance().getTime().toString() );
					state.setState( State.DISCONNECTED );
					devices.clearAllDevices();
				}
			} );
		} catch ( IOException e ) {
			Logger.logLine( "Port " + port + " is already in use. Dashboard shutting down." );
			frame.dispatchEvent( new WindowEvent( frame, WindowEvent.WINDOW_CLOSING ) );
		}
	}
	
}
