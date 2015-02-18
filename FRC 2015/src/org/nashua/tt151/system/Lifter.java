package org.nashua.tt151.system;

import org.nashua.tt151.F310;
import org.nashua.tt151.RobotMap;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Lifter extends Subsystem {
	private static Lifter INSTANCE = null;
	
	private Talon pulley = new Talon( RobotMap.PWM.LIFTER_PULLEY );
	
	AnalogPotentiometer pot = new AnalogPotentiometer(2);
	
	private Lifter() {
		
	}
	
	public static Lifter getInstance() {
		if ( INSTANCE == null ) {
			INSTANCE = new Lifter();
		}
		return INSTANCE;
	}
	
	@Override
	public void init() {
		
	}
	
	@Override
	public void operatorControl( F310 driver, F310 lifter ) {
		pulley.set( driver.getRightY() );
		
		double rollOver = 0;
		if (pot.get() > 0.99 && pulley.get() > 0){
			rollOver++;
		}
		if (pot.get() < 0.01 && pulley.get() < 0){
			rollOver--;
		}
		
		SmartDashboard.putString("DB/String 9", "POT: " + (pot.get() + rollOver));
	}
}