package org.nashua.tt151.system;

import org.nashua.tt151.F310;
import org.nashua.tt151.RobotMap;

import edu.wpi.first.wpilibj.Talon;

public class Lifter extends Subsystem {
	private static Lifter INSTANCE = null;
	
	private Talon pulley = new Talon( RobotMap.PWM.LIFTER_PULLEY );
	
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
		pulley.set( driver.getRightY() );
	}
}