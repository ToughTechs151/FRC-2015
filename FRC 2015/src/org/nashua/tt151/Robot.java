package org.nashua.tt151;

import org.nashua.tt151.system.Lifter;
import org.nashua.tt151.system.MecanumDrive;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot {
	F310 driver = new F310( 0, 0.1 );
	F310 lifter = new F310( 1, 0.1 );
	AnalogPotentiometer pot = new AnalogPotentiometer(2);
	public Robot() {}
	
	public void autonomous() {}
	
	public void operatorControl() {
		MecanumDrive.getInstance().init();
		Lifter.getInstance().init();
		while ( isOperatorControl() && isEnabled() ) {
			driver.query();
			lifter.query();
			SmartDashboard.putString( "DB/String 5", "POT: " + MathUtils.round( pot.get(), 5 ) );
			MecanumDrive.getInstance().operatorControl( driver, lifter );
			Lifter.getInstance().operatorControl( driver, lifter );
		}
	}
	
	public void test() {}
}
