package org.nashua.tt151;

/**
 * This class contains the port mappings for everything on the robot
 * This is how you get electrical and software to stop fighting and agree on ports
 */
public final class RobotMap {
	
	private RobotMap() {}
	
	public static final class PWM {
		public static final int MECANUM_FL = 0; // Front Left
		public static final int MECANUM_FR = 1; // Front Right
		public static final int MECANUM_RL = 2; // Rear Left
		public static final int MECANUM_RR = 3; // Rear Right
		public static final int LIFTER_PULLEY = 4;
	}
	
	public static final class ANALOG {
		public static final int GYRO = 0;
	}
	
	public static final class DIO {
		public static final int TOP_LIM = 0;
		public static final int BOTTOM_LIM = 1;
	}
	
}
