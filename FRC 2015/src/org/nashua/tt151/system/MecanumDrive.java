package org.nashua.tt151.system;

import java.util.Timer;
import java.util.TimerTask;

import org.nashua.tt151.F310;
import org.nashua.tt151.MathUtils;
import org.nashua.tt151.PIDController;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
	PIDController gyroController = new PIDController( 0.02, 0.0, 0.0 );
	
	// Multiplier to scale speed
	double mult = 0.0;
	
	// Axis readings for inverse kinematic transformation
	double forward = 0.0; // fwd/rev
	double right = 0.0; // left/right
	double rawClockwise = 0.0; // rotation
	double clockwise = 0.0;
	double rotAdj = 0.0;
	
	// Speeds of each motor
	double fl = 0.0;
	double fr = 0.0;
	double rl = 0.0;
	double rr = 0.0;
	
	// Highest value from the IKT used for normalization
	double max = 0.0;
	
	boolean compensationEnabled = true;
	
	private MecanumDrive() {
		new Timer().scheduleAtFixedRate( new TimerTask() {
			
			@Override
			public void run() {
				if ( Math.abs( gyro.getAngle() ) < 1e5 ) {
					if ( rawClockwise != 0.0 || Math.abs( gyro.getAngle() - gyroController.getSetpoint() ) > 90 ) {
						// resetGyro();
						
						gyroController.setSetpoint( gyro.getAngle() );
						
					}
				}
				System.out.println(gyro.getAngle());
				
				gyroController.setInput( gyro.getAngle() );
				rotAdj = gyroController.performPID();
			}
		}, 1, 1 );
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
		resetGyro();
	}
	
	public void resetGyro() {
		gyro.reset();
		gyroController.reset();
	}
	
	@Override
	public void operatorControl( F310 driver, F310 lifter ) {
		// Get multiplier to scale speed
		mult = getMultiplier( driver );
		
		// Get axis readings
		forward = -driver.getLeftY() * mult; // CHECK SIGN
		right = driver.getLeftX() * mult;
		rawClockwise = driver.getRightX();
		
		SmartDashboard.putNumber( "DB/Slider 0", forward );
		SmartDashboard.putNumber( "DB/Slider 1", right );
		SmartDashboard.putNumber( "DB/Slider 2", rawClockwise );
		
		// Rotation scaling for smoothness
		rawClockwise *= 0.5;
		
		clockwise = rawClockwise;
		if ( compensationEnabled ) {
			// // Apply rotation adjustment
			clockwise += rotAdj;
		}
		
		SmartDashboard.putString( "DB/String 5", "Gyro: " + (double) ( (int) ( gyro.getAngle() * 1e4 ) ) / 1e4 );
		SmartDashboard.putString( "DB/String 6", "Set: " + (double) ( (int) ( gyroController.getSetpoint() * 1e4 ) ) / 1e4 );
		SmartDashboard.putString( "DB/String 7", "Clk: " + clockwise );
		
		// toggle compensation
		if ( driver.getButtonReleased( F310.Button.Y ) ) {
			compensationEnabled = !compensationEnabled;
		}
		SmartDashboard.putString( "DB/String 4", "Comp: " + compensationEnabled );
		
		if ( driver.getButtonPressed( F310.Button.X ) ) {
			resetGyro();
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
		// set(driver.getRightX(), driver.getLeftX(), driver.getRightY(),0);
		// set( driver.getLeftX(), driver.getLeftX(), driver.getLeftX(), driver.getLeftX() );
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
		
		SmartDashboard.putString( "DB/String 0", "FL: " + fl );
		SmartDashboard.putString( "DB/String 1", "FR: " + fr );
		SmartDashboard.putString( "DB/String 2", "RL: " + rl );
		SmartDashboard.putString( "DB/String 3", "RR: " + rr );
	}
}
