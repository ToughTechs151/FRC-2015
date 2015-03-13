package org.nashua.tt151;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.nashua.tt151.lib.F310;
import org.nashua.tt151.protocol.JSONEncoder;
import org.nashua.tt151.system.Lifter;
import org.nashua.tt151.system.MecanumDrive;

import edu.wpi.first.wpilibj.SampleRobot;

public class Robot extends SampleRobot {
	F310 driver = new F310( 0, 0.1 );
	F310 lifter = new F310( 1, 0.1 );
	DashboardConnection dash = null;
	boolean tryingToConnect = false;
	
	public Robot() {}
	
	/**
	 * Attempts to establish a connection the the dashboard
	 */
	private void connectToDashboard() {
		if ( tryingToConnect ) {
			return;
		}
		tryingToConnect = true;
		new Thread() {
			public void run() {
				try {
					// Try to create a new dashboard connection
					if ( tryingToConnect && dash == null ) {
						dash = new DashboardConnection( "10.1.51.5" );
					}
				} catch ( IOException ex ) { // Thrown if the connection attempt failed
					System.out.println( "[ERR Failed to connect to dashboard: " + ex.getMessage() + "]" );
					dash = null;
				}
				tryingToConnect = false;
			}
		}.start();
	}
	
	public void robotInit() {
		MecanumDrive.getInstance().init();
		Lifter.getInstance().init();
		
		// Initial attempt to connect to the dashboard
		connectToDashboard();
		
		/*
		 * Tests the connection to the dashboard every 100ms and tries to
		 * reestablish the connection if the test fails
		 */
		new Timer().scheduleAtFixedRate( new TimerTask() {
			int failedCount = 0;
			
			public void run() {
				if ( dash != null ) { // If a connection is already made
					try {
						MecanumDrive.getInstance().updateDashboard( dash );
						Lifter.getInstance().updateDashboard( dash );
						updateStatus( dash );
						
						failedCount = 0;
					} catch ( IOException ex ) { // Thrown if the message failed to send
						System.out.println( "Failed to update dashboard: " + ex.getMessage() );
						// Allow up to 3 connection failures before destroying the connection
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
		}, 1, 10 );
	}
	
	private void updateStatus( DashboardConnection dash ) throws IOException {
		if ( isEnabled() && isAutonomous() ) {
			dash.send( JSONEncoder.encodeRobotStatus( JSONEncoder.STATUS.AUTO ) );
		} else if ( isEnabled() && isOperatorControl() ) {
			dash.send( JSONEncoder.encodeRobotStatus( JSONEncoder.STATUS.TELEOP ) );
		} else {
			dash.send( JSONEncoder.encodeRobotStatus( JSONEncoder.STATUS.CONNECTED ) );
		}
	}
	
	public void autonomous() {
		MecanumDrive.getInstance().init();
		MecanumDrive.getInstance().drive( 0, 0.75, 1250 );
	}
	
	public void operatorControl() {
		MecanumDrive.getInstance().init();
		Lifter.getInstance().init();
		while ( isOperatorControl() && isEnabled() ) {
			driver.query();
			lifter.query();
			
			MecanumDrive.getInstance().operatorControl( driver, lifter );
			Lifter.getInstance().operatorControl( driver, lifter );
		}
	}
	
	public void test() {}
}
