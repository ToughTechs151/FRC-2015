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
	// double gyroAngle;
	PIDController gyroController = new PIDController( 0.015, 0.0, 0.0 );
	
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
				// SmartDashboard.putString( "DB/String 3", "" + MathUtils.round( gyro.getRate(), 5 ) );
				// Some random really high values appear, so ignore those
				if ( Math.abs( gyro.getAngle() ) < 1e5 ) {
					
					if ( rawClockwise != 0.0 || ( forward == 0 && right == 0 && Math.abs( gyro.getRate() ) <= 45 ) || Math.abs( gyro.getAngle() - gyroController.getSetpoint() ) > 90 ) {
						// resetGyro();
						gyroController.setSetpoint( gyro.getAngle() );
						
					}
				}
				System.out.println( gyro.getAngle() );
				
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
		gyroController.setSetpoint( 0 );
	}
	
	@Override
	public void operatorControl( F310 driver, F310 lifter ) {
		// Get multiplier to scale speed
		mult = getMultiplier( driver );
		
		if ( driver.getButtonPressed( F310.Button.Y ) ) {
			compensationEnabled = !compensationEnabled;
		}
		
		if ( driver.getButtonPressed( F310.Button.X ) ) {
			resetGyro();
		}
		//
		
		// Debugging
		// if ( driver.getButtonReleased( F310.Button.Y ) ) {
		// drive( 0, 0.25, 0, 2000 );
		// }
		//
		// if ( driver.getButtonReleased( F310.Button.A ) ) {
		// drive( 180, 0.25, 0, 2000 );
		// }
		//
		// if ( driver.getButtonReleased( F310.Button.B ) ) {
		// drive( 90, 0.5, 0, 2000 );
		// }
		//
		// if ( driver.getButtonReleased( F310.Button.X ) ) {
		// drive( -90, 0.5, 0, 2000 );
		// }
		
		// Get axis readings
		forward = -driver.getLeftY() * mult; // CHECK SIGN
		right = driver.getLeftX() * mult;
		rawClockwise = driver.getRightX() * mult;
		
		mecanumDrive();
	}
	
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
	
	public void drive( double direction, double speed, double clockwiseSpeed, long mTime ) {
		speed = MathUtils.clamp( speed, 0, 1 );
		forward = Math.cos( Math.toRadians( direction ) ) * speed;
		right = Math.sin( Math.toRadians( direction ) ) * speed;
		rawClockwise = MathUtils.clamp( clockwiseSpeed, -1.0, 1.0 );
		mTime = (long) MathUtils.clamp( mTime, 0, 10000 );
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
