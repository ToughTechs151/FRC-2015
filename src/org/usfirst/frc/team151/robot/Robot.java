package org.usfirst.frc.team151.robot;

import org.nashua.tt151.F310;
import org.nashua.tt151.system.TankDrive;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot {
	F310 driver = new F310( 0, 0.1 );
	F310 lifter = new F310( 1, 0.1 );
	Gyro gyro = new Gyro( 0 );
	private GyroITG3200 m_gyro;
	
	public Robot() {
		//creates and initializes the gyro
		m_gyro = new GyroITG3200(I2C.Port.kOnboard);
    	m_gyro.initialize();
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
        	SmartDashboard.putString("DB/String 1", Double.toString(m_gyro.getRotationX()));
        	SmartDashboard.putString("DB/String 2", Double.toString(m_gyro.getRotationY()));
        	SmartDashboard.putString("DB/String 3", Double.toString(m_gyro.getRotationZ()));
        }
    }
	
}
