package org.nashua.tt151.system;

import java.io.IOException;

import org.nashua.tt151.DashboardConnection;
import org.nashua.tt151.RobotMap;
import org.nashua.tt151.lib.F310;
import org.nashua.tt151.protocol.JSONEncoder;
import org.nashua.tt151.protocol.JSONEncoder.DeviceType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;

/**
 * All code related to the lifter subsystem
 */
public class Lifter extends Subsystem {
	private static Lifter INSTANCE = null;
	
	// Winch that lifts the arms
	Talon pulley = new Talon( RobotMap.PWM.LIFTER_PULLEY );
	
	// Limit switches
	DigitalInput topLimit = new DigitalInput( RobotMap.DIO.TOP_LIM );
	DigitalInput bottomLimit = new DigitalInput( RobotMap.DIO.BOTTOM_LIM );
	
	// Singleton stuff
	
	private Lifter() {}
	
	public static Lifter getInstance() {
		if ( INSTANCE == null ) {
			INSTANCE = new Lifter();
		}
		return INSTANCE;
	}
	
	// Implementing the subsystem methods
	
	@Override
	public void init() {}
	
	@Override
	public void operatorControl( F310 driver, F310 lifter ) {
		// Get the states of the two limit switches, true means pressed
		boolean top = topLimit.get();
		boolean bottom = bottomLimit.get();
		
		/*
		 * Calculate the speed that the pulley will be set at.
		 * If the right stick is moved, its full speed value is used, otherwise a half speed value of the left
		 * stick is used
		 */
		double speed = lifter.getRightY() != 0 ? lifter.getRightY() : lifter.getLeftY() * 0.5;
		
		/*
		 * There are 3 cases where the pulley speed is allowed to be set:
		 * 	1. ( !top && !bottom ) 				-> Neither limit switch is pressed
		 * 	2. ( top && !bottom && speed > 0 ) 	-> Only the top switch is pressed, and we want to move down
		 * 	3. ( !top && bottom && speed < 0 ) 	-> Only the bottom switch is pressed, and we want to move up
		 */
		if ( ( !top && !bottom ) || ( top && !bottom && speed > 0 ) || ( !top && bottom && speed < 0 ) ) {
			pulley.set( speed );
		} else {
			// The pulley is otherwised stopped
			pulley.set( 0.0 );
		}
	}
	
	@Override
	public void updateDashboard( DashboardConnection dash ) throws IOException {
		dash.send( JSONEncoder.encodePWM( pulley.get(), RobotMap.PWM.LIFTER_PULLEY, "Pulley", DeviceType.PWM.TALON ) );
		dash.send( JSONEncoder.encodeDIO( topLimit.get(), RobotMap.DIO.TOP_LIM, "Top Limit", DeviceType.DIO.LIMIT ) );
		dash.send( JSONEncoder.encodeDIO( bottomLimit.get(), RobotMap.DIO.BOTTOM_LIM, "Bottom Limit", DeviceType.DIO.LIMIT ) );
	}
}