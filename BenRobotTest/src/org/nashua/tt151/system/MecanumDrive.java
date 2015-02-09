package org.nashua.tt151.system;


import java.util.Timer;
import java.util.TimerTask;

import org.nashua.tt151.F310;
import org.nashua.tt151.MathUtils;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Talon;

public class MecanumDrive extends Subsystem {
	//Singleton instance
	private static MecanumDrive INSTANCE = null;
	
	//Motor controllers
	Talon frontLeft = new Talon( 0 );
	Talon frontRight = new Talon( 1 );
	Talon rearLeft = new Talon( 2 );
	Talon rearRight = new Talon( 3 );
	
	//Gyro for mecanum compensation
	Gyro gyro = new Gyro( 0 ); //CHECK PORT
//	double gyroAngle;
	
	//Multiplier to scale speed
	double mult;
	
	//Axis readings for inverse kinematic transformation
	double forward; //fwd/rev
	double right; //left/right
	double clockwise; //rotation
	
	//Speeds of each motor
	double fl;
	double fr;
	double rl;
	double rr;
	
	//Highest value from the IKT used for normalization
	double max;
	
	//Rotation adjustment
	double rotationAdjustment;
	
	private MecanumDrive() {
		new Timer().scheduleAtFixedRate( new TimerTask() {
			
			int timestep = 5;
			double gyroAngle;
			
			@Override
			public void run() {
				try {
					
					//Find change in rotation over a small timestep
					gyroAngle = gyro.getAngle();
					Thread.sleep( timestep );
					gyroAngle = gyro.getAngle() - gyroAngle;
					
					
				} catch ( InterruptedException e ) {
					System.err.println(e.getMessage());
				}
			}
		}, 1, 1 );
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
		//Get multiplier to scale speed
		mult = getMultiplier( driver );
		
		// Get axis readings
		forward = -driver.getLeftY() * mult;
		right = driver.getLeftX() * mult;
		clockwise = driver.getRightX();
		
		// Rotation scaling for smoothness
		clockwise *= 0.5;
		
		// Apply inverse kinematic transformation to convert axis readings to motor values
		fl = forward + clockwise + right;
		fr = forward - clockwise - right;
		rl = forward + clockwise - right;
		rr = forward - clockwise + right;
		
		// Normalize motor output values
		max = MathUtils.absMax( fl, fr, rl, rr );
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
