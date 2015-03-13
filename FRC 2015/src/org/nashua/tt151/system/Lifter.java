package org.nashua.tt151.system;

import java.io.IOException;

import org.nashua.tt151.DashboardConnection;
import org.nashua.tt151.RobotMap;
import org.nashua.tt151.lib.F310;
import org.nashua.tt151.protocol.JSONEncoder;
import org.nashua.tt151.protocol.JSONEncoder.DeviceType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;

public class Lifter extends Subsystem {
	private static Lifter INSTANCE = null;
	
	Talon pulley = new Talon( RobotMap.PWM.LIFTER_PULLEY );
	DigitalInput topLimit = new DigitalInput( RobotMap.DIO.TOP_LIM );
	DigitalInput bottomLimit = new DigitalInput( RobotMap.DIO.BOTTOM_LIM );
	
//	AnalogPotentiometer pot = new AnalogPotentiometer( 2 );
	
	private Lifter() {
		
	}
	
	public static Lifter getInstance() {
		if ( INSTANCE == null ) {
			INSTANCE = new Lifter();
		}
		return INSTANCE;
	}
	
	@Override
	public void init() {
		
	}
	
	@Override
	public void operatorControl( F310 driver, F310 lifter ) {
		// pulley.set( lifter.getRightY() != 0 ? lifter.getRightY() : lifter.getLeftY() * 0.5 );
		
		boolean top = topLimit.get();
		boolean bottom = bottomLimit.get();
		double speed = lifter.getRightY() != 0 ? lifter.getRightY() : lifter.getLeftY() * 0.5;
		
//		SmartDashboard.putString( "DB/String 0", "Top: " + top );
//		SmartDashboard.putString( "DB/String 1", "Bottom: " + bottom );
		
		if ( ( !top && !bottom ) || ( top && !bottom && speed > 0 ) || ( !top && bottom && speed < 0 ) ) {
			pulley.set( speed );
		} else {
			pulley.set( 0.0 );
		}
		
//		SmartDashboard.putString( "DB/String 2", "Pulley: " + speed );
		
//		SmartDashboard.putString( "DB/String 9", "POT: " + MathUtils.round( pot.get(), 5 ) );
	}

	@Override
	public void updateDashboard( DashboardConnection dash ) throws IOException {
		dash.send( JSONEncoder.encodePWM( pulley.get(), RobotMap.PWM.LIFTER_PULLEY, "Pulley", DeviceType.PWM.TALON ) );
		dash.send( JSONEncoder.encodeDIO( topLimit, RobotMap.DIO.TOP_LIM, "Top Limit", DeviceType.DIO.LIMIT ) );
		dash.send( JSONEncoder.encodeDIO( bottomLimit, RobotMap.DIO.BOTTOM_LIM, "Bottom Limit", DeviceType.DIO.LIMIT ) );
	}
}