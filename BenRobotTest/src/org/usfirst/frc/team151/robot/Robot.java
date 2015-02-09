package org.usfirst.frc.team151.robot;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.io.PrintStream;
import org.nashua.tt151.F310;
import org.nashua.tt151.GyroITG3200;
import org.nashua.tt151.system.TankDrive;

public class Robot extends SampleRobot {
	F310 driver = new F310(0, 0.1D);
	F310 lifter = new F310(1, 0.1D);
	Gyro gyro = new Gyro(0);
	private GyroITG3200 m_gyro;
	RobotDrive myDrive = new RobotDrive(0, 2, 1, 3);
	Accelerometer accel;
	
	public Robot() {
		this.m_gyro = new GyroITG3200(I2C.Port.kOnboard);
		this.m_gyro.initialize();
		try {
			Thread.sleep(1L);
		} catch (Exception e) {
		}
		this.m_gyro.initRotation();
	}

	public void autonomous() {
		/**
		 * TODO: convert meters to feet, finish section, comment
		 */
//		
//		double distance = 0;
//		double velocity = 0;
//		
//		
//		while (distance < 10.0){
//			myDrive.mecanumDrive_Cartesian(0.5, 0, 0, 0);
//			double timeOne = System.currentTimeMillis()-1;
//			double timeTwo = System.currentTimeMillis();	
//			velocity = (timeTwo - timeOne) * ((accel.getX()*timeOne + accel.getX()*timeTwo) / 2);
//			
//			timeOne = System.currentTimeMillis()-1;
//			timeTwo = System.currentTimeMillis();	
//			
//			distance = (timeTwo - timeOne) * ((velocity*timeOne + velocity*timeTwo) / 2);
//		}
//		
//		myDrive.mecanumDrive_Cartesian(0, 0, 0, 0);
//		
//		try {
//			Thread.sleep(500);
//		} catch (InterruptedException e) {
//			System.out.println(e.getMessage());
//		}
//		
//		
//		
	}

	public void operatorControl() {
		while ((isOperatorControl()) && (isEnabled())) {
			myDrive.mecanumDrive_Cartesian(driver.getLeftY(), driver.getLeftX(), driver.getRightX(), 0);
		}
	}

	public void test() {
		this.gyro.reset();
		this.gyro.setSensitivity(0.0125D);
		this.gyro.initGyro();
		try {
			Thread.sleep(10L);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
		this.gyro.reset();
		while ((isTest()) && (isEnabled())) {
			SmartDashboard.putString("DB/String 0",
					Double.toString(this.gyro.getAngle()));
			SmartDashboard.putString("DB/String 1",
					Short.toString(this.m_gyro.getRotationX()));
			SmartDashboard.putString("DB/String 2",
					Short.toString(this.m_gyro.getRotationY()));
			SmartDashboard.putString("DB/String 3",
					Short.toString(this.m_gyro.getRotationZ()));
		}
	}
}
