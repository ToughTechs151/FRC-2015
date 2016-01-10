package org.nashua.tt151;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.nashua.tt151.lib.F310;
import org.nashua.tt151.protocol.JSONEncoder;
import org.nashua.tt151.system.Lifter;
import org.nashua.tt151.system.MecanumDrive;

import edu.wpi.first.wpilibj.SampleRobot;

/**
 * Main robot class
 */
public class Robot extends SampleRobot {
	// Controllers
	F310 driver = new F310( 0, 0.1 );
	F310 lifter = new F310( 1, 0.1 );
	
	// Connection to the dashboard
	DashboardConnection dash = null;
	
	// Used for repeatedly attempting to connect
	boolean tryingToConnect = false;
	
	public Robot() {}
	
	/**
	 * Attempts to establish a connection to the dashboard
	 */
	private void connectToDashboard() {
		// Do nothing if an attempt is already being made
		if ( tryingToConnect ) {
			return;
		}
		tryingToConnect = true;
		
		new Thread() {
			public void run() {
				try {
					// Try to create a new dashboard connection
					if ( tryingToConnect && dash == null ) {
						dash = new DashboardConnection( "10.1.51.5" ); // Connects to a static laptop IP
					}
				} catch ( IOException ex ) { // Thrown if the connection attempt failed
					System.out.println( "[ERR Failed to connect to dashboard: " + ex.getMessage() + "]" );
					dash = null;
				}
				tryingToConnect = false;
			}
		}.start();
	}
	
	@Override
	public void robotInit() {
		// Initialize all subsystems
		MecanumDrive.getInstance().init();
		Lifter.getInstance().init();
		
		// Initial attempt to connect to the dashboard
		connectToDashboard();
		
		/*
		 * Tests the connection to the dashboard every 10ms by attempting to update it with device
		 * information. If the update fails, it tries to reestablish the connection
		 */
		new Timer().scheduleAtFixedRate( new TimerTask() {
			int failedCount = 0;
			
			public void run() {
				if ( dash != null ) { // If a connection is already made
					try {
						// Update dashboard info
						MecanumDrive.getInstance().updateDashboard( dash );
						Lifter.getInstance().updateDashboard( dash );
						updateStatus( dash );
						
						// Currently connected, so reset the failure count
						failedCount = 0;
					} catch ( IOException ex ) { // Thrown if the message failed to send
						System.out.println( "Failed to update dashboard: " + ex.getMessage() );
						// Allow up to 3 connection failures before destroying the current connection
						if ( ++failedCount <= 3 ) {
							failedCount = 0;
							dash = null; // End the connection
							connectToDashboard(); // Try to reestablish the connection
						}
					}
				} else { // If no connection exists, create a connection
					failedCount = 0;
					connectToDashboard();
				}
			}
		}, 1, 10 ); // Schedule set at 10ms
	}
	
	/**
	 * Updates the dashboard status depending on the current operation mode of the robot
	 * 
	 * @param dash Dashboard connection
	 * @throws IOException
	 */
	private void updateStatus( DashboardConnection dash ) throws IOException {
		if ( isEnabled() && isAutonomous() ) {
			dash.send( JSONEncoder.encodeRobotStatus( JSONEncoder.STATUS.AUTO ) );
		} else if ( isEnabled() && isOperatorControl() ) {
			dash.send( JSONEncoder.encodeRobotStatus( JSONEncoder.STATUS.TELEOP ) );
		} else {
			dash.send( JSONEncoder.encodeRobotStatus( JSONEncoder.STATUS.CONNECTED ) );
		}
	}
	
	@Override
	public void autonomous() {
		// oops looks like github was never updated with the new auto
		MecanumDrive.getInstance().init();
		MecanumDrive.getInstance().drive( 0, 0.75, 0, 1250 );
	}
	
	@Override
	public void operatorControl() {
		// Initialize all subsystems
		MecanumDrive.getInstance().init();
		Lifter.getInstance().init();
		
		// Teleop loop
		while ( isOperatorControl() && isEnabled() ) {
			// Update the controller button states
			driver.query();
			lifter.query();
			
			// Run teleop for each subsystem
			MecanumDrive.getInstance().operatorControl( driver, lifter );
			Lifter.getInstance().operatorControl( driver, lifter );
		}
	}
	
	@Override
	public void test() {}
}
