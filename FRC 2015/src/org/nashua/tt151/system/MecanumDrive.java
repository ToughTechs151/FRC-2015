package org.nashua.tt151.system;

import org.nashua.tt151.F310;
import org.nashua.tt151.MathUtils;
import org.nashua.tt151.PIDController;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Talon;

public class MecanumDrive extends Subsystem {
	// Singleton instance
	private static MecanumDrive INSTANCE = null;
	
	// Motor controllers
	Talon frontLeft = new Talon( 0 );
	Talon frontRight = new Talon( 1 );
	Talon rearLeft = new Talon( 2 );
	Talon rearRight = new Talon( 3 );
	
	// Gyro for mecanum compensation
	Gyro gyro = new Gyro( 0 ); // CHECK PORT
	// double gyroAngle;
	PIDController gyroController = new PIDController( 0.15, 0, 0 );
	
	// Multiplier to scale speed
	double mult = 0.0;
	
	// Axis readings for inverse kinematic transformation
	double forward = 0.0; // fwd/rev
	double right = 0.0; // left/right
	double clockwise = 0.0; // rotation
	
	// Speeds of each motor
	double fl = 0.0;
	double fr = 0.0;
	double rl = 0.0;
	double rr = 0.0;
	
	// Highest value from the IKT used for normalization
	double max = 0.0;
	
	boolean compensationEnabled = true;
	
	private MecanumDrive() {
		gyroController.setInputRange( -3600000, 3600000 );
		gyroController.setOutputRange( -1.0, 1.0 );
	}
	
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
		// Get multiplier to scale speed
		mult = getMultiplier( driver );
		
		// Get axis readings
		forward = -driver.getLeftY() * mult; // CHECK SIGN
		right = driver.getLeftX() * mult;
		clockwise = driver.getRightX();
		
		// Rotation scaling for smoothness
		clockwise *= 0.5;
		
		// angleSetpoint += clockwise * 0.01;
		gyroController.setSetpoint( gyroController.getSetpoint() + clockwise * 0.01 );
		gyroController.setInput( gyro.getAngle() );
		
		if ( compensationEnabled ) {
			// Apply rotation adjustment
			// clockwise += rotationAdjustment;
			clockwise += gyroController.performPID();
		}
		
		// toggle compensation
		if ( driver.getButtonReleased( F310.Button.Y ) ) {
			compensationEnabled = !compensationEnabled;
		}
		
		// Apply inverse kinematic transformation to convert axis readings to motor values
		fl = forward + right + clockwise;
		fr = forward - right - clockwise;
		rl = forward - right + clockwise;
		rr = forward + right - clockwise;
		
		// Normalize motor output values
		max = MathUtils.absMax( fl, fr, rl, rr );
		if ( max > 1 ) {
			fl /= max;
			fr /= max;
			rl /= max;
			rr /= max;
		}
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
		frontRight.set( -fr );
		rearLeft.set( rl );
		rearRight.set( -rr );
	}
}
