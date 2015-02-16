package org.nashua.tt151.system;

import org.nashua.tt151.F310;

import edu.wpi.first.wpilibj.Talon;

public class TankDrive extends Subsystem {
	private static TankDrive INSTANCE = null;
	
	Talon l1 = new Talon( 0 );
	Talon l2 = new Talon( 1 );
	Talon r1 = new Talon( 2 );
	Talon r2 = new Talon( 3 );
	
	private TankDrive() {}
	
	public static TankDrive getInstance() {
		if ( INSTANCE == null ) {
			INSTANCE = new TankDrive();
		}
		return INSTANCE;
	}
	
	@Override
	public void init() {
		
	}
	
	@Override
	public void operatorControl( F310 driver, F310 lifter ) {
		double mult = getMultiplier( driver );
		double left = driver.getLeftY() * mult;
		double right = driver.getRightY() * mult;
		
		set( left, right );
		
		if ( driver.getButton( F310.Button.LEFT_BUMPER ) ) {
			setLeft( -.2 );
		}
		if ( driver.getButton( F310.Button.RIGHT_BUMPER ) ) {
			setRight( .2 );
		}
		if(driver.getButton( F310.Button.Y)) {
			set(.25,.25);
		}
//		if ( driver.getButtonPressed( F310.Button.RIGHT_BUMPER ) ) {
//			Timer.delay( .002 );
//		}
	}
	
	private double getMultiplier( F310 driver ) {
		if ( driver.getButton( F310.Button.LEFT_TRIGGER ) ) {
			return 0.25;
		} else if ( driver.getButton( F310.Button.RIGHT_TRIGGER ) ) {
			return 1;
		}
		return 0.5;
	}
	
	public void set(double left, double right) {
		setLeft( -left );
		setRight( right );
	}
	
	private void setLeft(double speed) {
		l1.set( speed );
		l2.set( speed );
	}
	
	private void setRight(double speed) {
		r1.set( speed );
		r2.set( speed );
	}
	
}
