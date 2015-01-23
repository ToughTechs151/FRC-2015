package org.nashua.tt151.system;

import org.nashua.tt151.F310;

import edu.wpi.first.wpilibj.Talon;

public class MecanumDrive extends Subsystem {
	private static MecanumDrive INSTANCE = null;
	
	Talon frontRight = new Talon( 0 );
	Talon rearRight = new Talon( 1 );
	Talon frontLeft = new Talon( 2 );
	Talon rearLeft = new Talon( 3 );
	
	
	
	private MecanumDrive() {}
	
	public static MecanumDrive getInstance() {
		if ( INSTANCE == null ) {
			INSTANCE = new MecanumDrive();
		}
		return INSTANCE;
	}
	
	@Override
	public void init() {
		
	}
	
	@Override
	public void operatorControl( F310 driver, F310 lifter ) {
		double forward = -driver.getLeftY();
		double right = driver.getLeftX();
		double rotation = driver.getRightX();
		
		
		
	}
	
	private double getMultiplier( F310 driver ) {
		if ( driver.getButton( F310.Button.LEFT_TRIGGER ) ) {
			return 0.25;
		} else if ( driver.getButton( F310.Button.RIGHT_TRIGGER ) ) {
			return 1;
		}
		return 0.5;
	}
	
}
