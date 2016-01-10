package org.nashua.tt151.system;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.nashua.tt151.DashboardConnection;
import org.nashua.tt151.RobotMap;
import org.nashua.tt151.lib.F310;
import org.nashua.tt151.lib.PIDController;
import org.nashua.tt151.protocol.JSONEncoder;
import org.nashua.tt151.protocol.JSONEncoder.DeviceType;
import org.nashua.tt151.util.MathUtils;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Talon;

public class MecanumDrive extends Subsystem {
	// Singleton instance
	private static MecanumDrive INSTANCE = null;
	
	// Motor controllers
	Talon frontLeft = new Talon( RobotMap.PWM.MECANUM_FL );
	Talon frontRight = new Talon( RobotMap.PWM.MECANUM_FR );
	Talon rearLeft = new Talon( RobotMap.PWM.MECANUM_RL );
	Talon rearRight = new Talon( RobotMap.PWM.MECANUM_RR );
	
	// Gyro for mecanum compensation
	Gyro gyro = new Gyro( RobotMap.ANALOG.GYRO );
	
	// Controller for calculating the rotation adjustment needed for stable driving
	PIDController gyroController = new PIDController( 0.015, 0.0, 0.0 );
	
	// Multiplier to scale speed
	double mult = 0.0;
	
	// Axis readings for inverse kinematic transformation
	double forward = 0.0; // fwd/rev
	double right = 0.0; // left/right
	double rawClockwise = 0.0; // Raw rotation speed read from the controller
	double clockwise = 0.0; // Rotation speed used in IKT calculation
	double rotAdj = 0.0; // Adjustement to the rotation to compensate for wheel slipping / CoG
	
	// Speeds of each motor
	double fl = 0.0;
	double fr = 0.0;
	double rl = 0.0;
	double rr = 0.0;
	
	// Highest value from the IKT used for normalization
	double max = 0.0;
	
	boolean compensationEnabled = true;
	
	private MecanumDrive() {
		/*
		 * The sets up the gyro compensation thread to correct the robot's driving with a PID controller
		 */
		new Timer().scheduleAtFixedRate( new TimerTask() {
			
			@Override
			public void run() {
				
				/*
				 * Every so often the gyro will give a value of what appears to be integer max, causing the
				 * bot to spin full speed to compensate for the huge "change" in rotation, and knocking the
				 * life out of anything and anybody that was unfortunately near it, then it tips over, breaks
				 * its arms, and dies.
				 * This will ignore that :)
				 */
				if ( Math.abs( gyro.getAngle() ) < 1e5 ) {
					
					/*
					 * There are 3 cases here where the PID controller's setpoint is reset:
					 * 1. rawClockwise != 0.0 -> If the robot is currently rotating, there's no point in compensating
					 * 2. ( forward == 0 && right == 0 && Math.abs( gyro.getRate() ) <= 45 ) -> The robot isn't moving
					 * 3. ( Math.abs( gyro.getAngle() - gyroController.getSetpoint() ) > 90 ) ) -> The target angle is more than 90 degrees away
					 */
					if ( rawClockwise != 0.0 || ( forward == 0 && right == 0 && Math.abs( gyro.getRate() ) <= 45 ) || Math.abs( gyro.getAngle() - gyroController.getSetpoint() ) > 90 ) {
						gyroController.setSetpoint( gyro.getAngle() );
					}
				}
				
				// Update the PID controller and get the new adjustment value
				gyroController.setInput( gyro.getAngle() );
				rotAdj = gyroController.performPID();
			}
		}, 1, 1 );
		
		// PID setup
		gyroController.setInputRange( -3600000, 3600000 );
		gyroController.setOutputRange( -1.0, 1.0 );
	}
	
	// Singleton stuff
	
	public static MecanumDrive getInstance() {
		if ( INSTANCE == null ) {
			INSTANCE = new MecanumDrive();
		}
		return INSTANCE;
	}
	
	// Implementing the subsystem methods
	
	@Override
	public void init() {
		resetGyro();
	}
	
	/**
	 * Resets everything relating to the gyro
	 */
	public void resetGyro() {
		gyro.reset();
		gyroController.reset();
		gyroController.setSetpoint( 0 );
	}
	
	@Override
	public void operatorControl( F310 driver, F310 lifter ) {
		// Get multiplier to scale drive speed
		mult = getMultiplier( driver );
		
		// A switch to enable or disable compensation
		if ( driver.getButtonPressed( F310.Button.Y ) ) {
			compensationEnabled = !compensationEnabled;
		}
		
		// A switch to reset the gyro
		if ( driver.getButtonPressed( F310.Button.X ) ) {
			resetGyro();
		}
		
		// Get axis readings
		forward = -driver.getLeftY() * mult;
		right = driver.getLeftX() * mult;
		rawClockwise = driver.getRightX() * mult;
		
		// Everything is ready, so calculate and set wheel speeds
		mecanumDrive();
	}
	
	/**
	 * Applies the Inverse Kinematic Transformation to calculate and then set the wheel speeds
	 */
	private void mecanumDrive() {
		clockwise = rawClockwise;
		
		if ( compensationEnabled ) {
			// Apply rotation adjustment
			clockwise += rotAdj;
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
	
	/**
	 * Gets the multiplier used for scaling the drive speed
	 * 
	 * @param driver Controller to check buttons from
	 * @return Multiplier value
	 */
	private double getMultiplier( F310 driver ) {
		if ( driver.getButton( F310.Button.LEFT_TRIGGER ) ) {
			return 0.25; // Creep mode
		} else if ( driver.getButton( F310.Button.RIGHT_TRIGGER ) ) {
			return 1; // Turbo mode
		}
		return 0.5; // Default 50% speed
	}
	
	/**
	 * Sets the speeds of all wheel motots
	 * 
	 * @param fl Front Left speed
	 * @param fr Front Right speed
	 * @param rl Rear Left speed
	 * @param rr Rear Right speed
	 */
	public void set( double fl, double fr, double rl, double rr ) {
		frontLeft.set( fl );
		frontRight.set( -fr );
		rearLeft.set( rl );
		rearRight.set( -rr );
	}
	
	/**
	 * Autonomously drives the robot for a set period of time
	 * 
	 * @param direction Direction in degrees to drive the robot
	 * @param speed How fast the robot should drive
	 * @param clockwiseSpeed How fast the robot should rotate
	 * @param mTime How long the driving should go on before stopping
	 */
	public void drive( double direction, double speed, double clockwiseSpeed, long mTime ) {
		speed = MathUtils.clamp( speed, 0, 1 );
		forward = Math.cos( Math.toRadians( direction ) ) * speed;
		right = Math.sin( Math.toRadians( direction ) ) * speed;
		rawClockwise = MathUtils.clamp( clockwiseSpeed, -1.0, 1.0 );
		
		// Clamp the running time to no more than 10 seconds, since autonomous is that long
		mTime = (long) MathUtils.clamp( mTime, 0, 10000 );
		
		// Run until the given time is reached
		long start = System.currentTimeMillis();
		while ( ( System.currentTimeMillis() - start ) < mTime ) {
			mecanumDrive();
		}
		
		// Reset everything
		forward = 0;
		right = 0;
		rawClockwise = 0;
		mecanumDrive();
		resetGyro();
	}
	
	@Override
	public void updateDashboard( DashboardConnection dash ) throws IOException {
		dash.send( JSONEncoder.encodePWM( frontLeft.get(), RobotMap.PWM.MECANUM_FL, "FL", DeviceType.PWM.TALON ) );
		dash.send( JSONEncoder.encodePWM( frontRight.get(), RobotMap.PWM.MECANUM_FR, "FR", DeviceType.PWM.TALON ) );
		dash.send( JSONEncoder.encodePWM( rearLeft.get(), RobotMap.PWM.MECANUM_RL, "RL", DeviceType.PWM.TALON ) );
		dash.send( JSONEncoder.encodePWM( rearRight.get(), RobotMap.PWM.MECANUM_RR, "RR", DeviceType.PWM.TALON ) );
		dash.send( JSONEncoder.encodeAnalog( gyro.getAngle(), RobotMap.ANALOG.GYRO, "Gyro", DeviceType.ANALOG.GYRO ) );
		dash.send( JSONEncoder.encodeDIO( compensationEnabled, 9, "Compensation", DeviceType.DIO.LIMIT ) );
	}
}
