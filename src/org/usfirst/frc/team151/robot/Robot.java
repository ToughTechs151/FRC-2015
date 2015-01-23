package org.usfirst.frc.team151.robot;

import org.nashua.tt151.F310;
import org.nashua.tt151.system.TankDrive;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot {
	F310 driver = new F310( 0, 0.1 );
	F310 lifter = new F310( 1, 0.1 );
//	Gyro gyro = new Gyro( 0 );
	
	public Robot() {
		
	}
	
	public void autonomous() {}
	
	public void operatorControl() {
		TankDrive.getInstance().init();
		while ( isOperatorControl() && isEnabled() ) {
			driver.query();
			lifter.query();
			
//			SmartDashboard.putString( "DB/String 0", "" + gyro.getAngle() );
			TankDrive.getInstance().operatorControl( driver, lifter );
		}
	}
	
	public void test() {}
}
