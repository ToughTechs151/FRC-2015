package org.nashua.tt151.system;

import java.io.IOException;

import org.nashua.tt151.DashboardConnection;
import org.nashua.tt151.lib.F310;

/**
 * Base for all subsystems on the robot
 * Subsystems will follow the singleton pattern as follows:
 **********************************************************
 * -Have a private constructor to prevent external initialization
 * -Have a static INSTANCE variable of that subsystem and a getInstance method:
 *
 * <pre>
 *	private static [CLASSNAME] INSTANCE;
 *	public static [CLASSNAME] getInstance() {
 *		if ( INSTANCE == null ) {
 *			INSTANCE = new [CLASSNAME]();
 *		}
 *		return INSTANCE;
 *	}
 * </pre>
 *
 * @author Kareem El-Faramawi
 */
public abstract class Subsystem {
	protected Subsystem() {}

	/**
	 * Initialize all components of the Subsystem here
	 */
	public abstract void init();
	
	/**
	 * Send data about the subsystem to the dashboard
	 * @param dash 
	 */
	public abstract void updateDashboard(DashboardConnection dash) throws IOException;

	/**
	 * Method to be called from the Tele-Op loop
	 *
	 * @param driver  Driver joystick
	 * @param shooter Shooter joystick
	 */
	public abstract void operatorControl( F310 driver, F310 lifter );
}