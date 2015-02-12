package org.nashua.tt151;

import org.nashua.tt151.system.MecanumDrive;
import org.nashua.tt151.system.TankDrive;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SampleRobot;

public class Robot extends SampleRobot {
	F310 driver = new F310( 0, 0.1 );
	F310 lifter = new F310( 1, 0.1 );
	
//	RobotDrive drive = new RobotDrive( 0, 2, 1, 3 );
//	Gyro gyro = new Gyro( 0 );
	
	public Robot() {
//		drive.setInvertedMotor( RobotDrive.MotorType.kFrontRight, true );
//		drive.setInvertedMotor( RobotDrive.MotorType.kRearRight, true );
	}
	
	public void autonomous() {}
	
	public void operatorControl() {
		// TankDrive.getInstance().init();
		MecanumDrive.getInstance().init();
		while ( isOperatorControl() && isEnabled() ) {
			driver.query();
			lifter.query();
			
//			drive.mecanumDrive_Cartesian( driver.getLeftX(), -driver.getLeftY(), driver.getRightX(), 0 );
			// SmartDashboard.putString( "DB/String 0", "" + gyro.getAngle() );
			// TankDrive.getInstance().operatorControl( driver, lifter );
			MecanumDrive.getInstance().operatorControl( driver, lifter );
		}
	}
	
	public void test() {}
}
