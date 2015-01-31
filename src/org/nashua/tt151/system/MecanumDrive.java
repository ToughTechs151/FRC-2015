package org.nashua.tt151.system;

import org.nashua.tt151.F310;
import org.nashua.tt151.MathUtils;

import edu.wpi.first.wpilibj.Talon;

public class MecanumDrive extends Subsystem {
	private static MecanumDrive INSTANCE = null;
	
	Talon frontLeft = new Talon( 0 );
	Talon frontRight = new Talon( 1 );
	Talon rearLeft = new Talon( 2 );
	Talon rearRight = new Talon( 3 );
	
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
		//Get multiplier to scale speed
		double mult = getMultiplier( driver );
		
		// Get axis readings
		double forward = -driver.getLeftY() * mult;
		double right = driver.getLeftX() * mult;
		double clockwise = driver.getRightX();
		
		// Rotation scaling for smoothness
		clockwise *= 0.5;
		
		// Apply inverse kinematic transformation to convert axis readings to motor values
		double fl = forward + clockwise + right;
		double fr = forward - clockwise - right;
		double rl = forward + clockwise - right;
		double rr = forward - clockwise + right;
		
		// Normalize motor output values
		double max = MathUtils.max( fl, fr, rl, rr );
		fl /= max;
		fr /= max;
		rl /= max;
		rr /= max;
		
		// Set motor speeds
		set( fl, fr, rl, rr );
	}
	
	private double getMultiplier( F310 driver ) {
		if ( driver.getButton( F310.Button.LEFT_TRIGGER ) ) {
			return 0.25;
		} else if ( driver.getButton( F310.Button.RIGHT_TRIGGER ) ) {
			return 1;
		}
		return 0.5;
	}
	
	public void set( double fl, double fr, double rl, double rr ) {
		frontLeft.set( fl );
		frontRight.set( fr );
		rearLeft.set( rl );
		rearRight.set( rr );
	}
}
