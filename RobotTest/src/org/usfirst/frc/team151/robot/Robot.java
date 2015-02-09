package org.usfirst.frc.team151.robot;

import org.nashua.tt151.F310;
import org.nashua.tt151.system.MecanumDrive;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SampleRobot;

public class Robot extends SampleRobot {
	F310 driver = new F310( 0, 0.1 );
	F310 lifter = new F310( 1, 0.1 );
<<<<<<< HEAD
//	Gyro gyro = new Gyro( 0 );
	RobotDrive myDrive = new RobotDrive(0, 2, 1, 3);
=======
	Gyro gyro = new Gyro( 0 );
>>>>>>> parent of c134b0a... switch to robotdrive class for mecanum drive
	
	public Robot() {
		
	}
	
	public void autonomous() {}
	
	public void operatorControl() {
		while ( isOperatorControl() && isEnabled() ) {
			myDrive.mecanumDrive_Cartesian(driver.getLeftY(), driver.getLeftX(), driver.getRightX(), 0);
		}
	}
	
	public void test() {
		gyro.reset();
    	gyro.setSensitivity(12.5/1000);
    	gyro.initGyro();
    	try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
    	gyro.reset();
        while (isTest() && isEnabled()) {
        	SmartDashboard.putString("DB/String 0", Double.toString(gyro.getAngle()));
        }
    }
	
}
