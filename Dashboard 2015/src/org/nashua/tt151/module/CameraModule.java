package org.nashua.tt151.module;

import ipcapture.IPCapture;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.nashua.tt151.Logger;

import processing.core.PApplet;

/**
 * Module for displaying live camera feed
 * 
 * @author Kareem El-Faramawi
 */
public class CameraModule extends JPanel {
	// Video size
	private static final int WIDTH = 320;
	private static final int HEIGHT = 240;
	
	// Error image
	private final BufferedImage UNAVAILABLE;
	
	// IP camera stuff
	private IPCapture cam;
	private BufferedImage raw; // Raw image from camera
	private BufferedImage display; // Image drawn to dash
	private Timer camThread;
	
	// Camera IP address
	public static final String CAM_IP = "10.1.51.11";
	
	/**
	 * Initializes the camera module
	 */
	public CameraModule() {
		// Create error image for when there is no feed
		UNAVAILABLE = new BufferedImage( WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB );
		Graphics g = UNAVAILABLE.createGraphics();
		g.setColor( Color.BLACK );
		g.fillRect( 0, 0, UNAVAILABLE.getWidth(), UNAVAILABLE.getHeight() );
		g.setColor( Color.RED );
		g.setFont( new Font( "OCR A Std", Font.PLAIN, 26 ) );
		FontMetrics fm = g.getFontMetrics();
		String msg = "CAMERA FEED";
		g.drawString( msg, UNAVAILABLE.getWidth() / 2 - fm.stringWidth( msg ) / 2, UNAVAILABLE.getHeight() / 2 - fm.getAscent() );
		msg = "UNAVAILABLE";
		g.drawString( msg, UNAVAILABLE.getWidth() / 2 - fm.stringWidth( msg ) / 2, UNAVAILABLE.getHeight() / 2 + fm.getAscent() * 2 );
		
		setLayout( new BorderLayout() );
		
		// Add a reset button
		JButton reset = new JButton( "Reset Camera" );
		reset.setForeground( Color.WHITE );
		reset.setBackground( Color.GRAY.darker().darker() );
		reset.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				initCamera();
			}
		} );
		add( reset, BorderLayout.SOUTH );
		
		// Initialize module
		setPreferredSize( new Dimension( WIDTH, HEIGHT + reset.getPreferredSize().height ) );
		setSize( getPreferredSize() );
		initCamera();
	}
	
	/**
	 * Initializes the camera feed
	 */
	private void initCamera() {
		// Closes the connection if one is already made
		if ( camThread != null ) {
			camThread.cancel();
			camThread.purge();
			camThread = null;
		}
		
		// Display error message until the feed is available
		display = UNAVAILABLE;
		
		// Connect to the IP camera with that library Jeremy found once upon a time
		cam = new IPCapture( new PApplet(), "http://" + CAM_IP + "/mjpg/video.mjpg", "FRC", "FRC" );
		cam.init( WIDTH, HEIGHT, IPCapture.RGB );
		cam.start();
		
		// Start the camera update thread at 30FPS
		camThread = new Timer();
		camThread.scheduleAtFixedRate( new TimerTask() {
			public void run() {
				try {
					if ( !cam.isAlive() ) {
						// Attempt to restart the camera feed if it isn't working
						cam.stop();
						cam.start();
						display = UNAVAILABLE;
					} else if ( cam.isAvailable() ) {
						// Read in a new image
						cam.read();
						raw = (BufferedImage) cam.getNative();
						
						// Draw it to the display buffer
						display = new BufferedImage( WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB );
						Graphics g = display.createGraphics();
						g.drawImage( raw, 0, 0, null );
					}
				} catch ( Exception e ) {
					// maybe this will catch why it stops
					Logger.logLine( "Camera feed error: " + e.getMessage() );
				}
			}
		}, 1, 33 ); // 33ms delay = 30FPS
	}
	
	public void paintComponent( Graphics g ) {
		g.drawImage( display, 0, 0, null );
	}
	
}
