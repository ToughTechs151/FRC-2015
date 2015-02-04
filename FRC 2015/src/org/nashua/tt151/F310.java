package org.nashua.tt151;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Logitech F310 Gamepad (D Mode)
 * All mappings found through Windows "Devices and Printers" menu
 * 
 * @author Kareem El-Faramawi
 */
public class F310 extends Controller {
	/**
	 * Minimum value for a D-Pad button to be considered pressed
	 */
	private static double PAD_THRESHOLD = 0.5;
	
	/**
	 * Constants for all button mappings
	 * 
	 * @author Kareem El-Faramawi
	 */
	public static final class Button {
		public static final int X = 1;
		public static final int A = 2;
		public static final int B = 3;
		public static final int Y = 4;
		public static final int LEFT_BUMPER = 5;
		public static final int RIGHT_BUMPER = 6;
		public static final int LEFT_TRIGGER = 7;
		public static final int RIGHT_TRIGGER = 8;
		public static final int BACK = 9;
		public static final int START = 10;
		public static final int LEFT_JOY_DOWN = 11;
		public static final int RIGHT_JOY_DOWN = 12;
	}
	
	/**
	 * Constants for all axis mappings
	 * 
	 * @author Kareem El-Faramawi
	 */
	public static final class Axis {
		public static final int LEFT_X = 1;
		public static final int LEFT_Y = 2;
		public static final int RIGHT_X = 3;
		public static final int RIGHT_Y = 4;
		public static final int DPAD_X = 5;
		public static final int DPAD_Y = 6;
	}
	
	/**
	 * Creates a Logitech F310 with the given port and dead zone
	 * 
	 * @param port The Driver Station channel of the {@link Joystick}
	 * @param deadZone Minimum distance an axis must be from 0.0 to be returned
	 */
	public F310( int port, double deadZone ) {
		super( 12, port, deadZone );
	}
	
	/**
	 * Creates a Logitech F310 with the given port and a dead zone of 0.0
	 * 
	 * @param port The Driver Station channel of the {@link Joystick}
	 */
	public F310( int port ) {
		this( port, 0.0 );
	}
	
	/**
	 * @return The value of the left X-Axis
	 */
	public double getLeftX() {
		return getAxis( Axis.LEFT_X );
	}
	
	/**
	 * @return The value of the left Y-Axis
	 */
	public double getLeftY() {
		return getAxis( Axis.LEFT_Y );
	}
	
	/**
	 * @return The value of the right X-Axis
	 */
	public double getRightX() {
		return getAxis( Axis.RIGHT_X );
	}
	
	/**
	 * @return The value of the right Y-Axis
	 */
	public double getRightY() {
		return getAxis( Axis.RIGHT_Y );
	}
	
	/**
	 * @return The value of the X-Axis on the D-Pad
	 */
	public double getDPadX() {
		return getAxis( Axis.DPAD_X );
	}
	
	/**
	 * @return The value of the Y-Axis on the D-Pad
	 */
	public double getDPadY() {
		return getAxis( Axis.DPAD_Y );
	}
	
	/**
	 * @return If the D-Pad is pressed up
	 */
	public boolean isDPadUp() {
		return getDPadY() < -PAD_THRESHOLD;
	}
	
	/**
	 * @return If the D-Pad is pressed down
	 */
	public boolean isDPadDown() {
		return getDPadY() > PAD_THRESHOLD;
	}
	
	/**
	 * @return If the D-Pad is pressed right
	 */
	public boolean isDPadRight() {
		return getDPadX() > PAD_THRESHOLD;
	}
	
	/**
	 * @return If the D-Pad is pressed left
	 */
	public boolean isDPadLeft() {
		return getDPadX() < -PAD_THRESHOLD;
	}
}
