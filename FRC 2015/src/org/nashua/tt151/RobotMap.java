package org.nashua.tt151;

public final class RobotMap {
	
	private RobotMap() {}
	
	public static final class PWM {
		public static final int MECANUM_FL = 0;
		public static final int MECANUM_FR = 1;
		public static final int MECANUM_RL = 2;
		public static final int MECANUM_RR = 3;
		public static final int LIFTER_PULLEY = 4;
	}
	
	public static final class ANALOG {
		public static final int GYRO = 0;
	}
	
	public static final class DIO {
		public static final int TOP_LIM = 0;
	}
	
}
