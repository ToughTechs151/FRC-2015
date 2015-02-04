package org.nashua.tt151;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Controller that uses a WPILibJ {@link Joystick} internally. This works by storing the state of the
 * controller buttons in memory and updating when query() is called. One old state is stored to allow
 * convenient methods for checking if a button was just pressed or released. A dead zone can be used to set a
 * minimum value for an axis so tiny values aren't used.
 * 
 * NOTE: query() MUST be called at the beginning of every loop for the buttons to be updated
 * 
 * @author Kareem El-Faramawi
 */
public abstract class Controller {
	/**
	 * {@link Joystick} to get all values from
	 */
	private Joystick joystick;
	
	/**
	 * Stores a one update old state of the buttons
	 */
	private boolean[] oldState;
	
	/**
	 * Stores the current state of the buttons
	 */
	private boolean[] state;
	
	/**
	 * A minimum distance from 0.0 an Axis has to read to be returned
	 */
	private double deadZone;
	
	/**
	 * Creates a Controller with a given number of buttons, port, and dead zone
	 * 
	 * @param buttons Number of buttons on this Controller
	 * @param port The Driver Station channel of the {@link Joystick}
	 * @param deadZone Minimum distance an axis must be from 0.0 to be returned
	 */
	public Controller( int buttons, int port, double deadZone ) {
		joystick = new Joystick( port );
		oldState = new boolean[buttons];
		state = new boolean[buttons];
		this.deadZone = deadZone;
	}
	
	/**
	 * Creates a Controller with a given number of buttons, port, and a dead zone of 0.0
	 * 
	 * @param buttons Number of buttons on this Controller
	 * @param portThe Driver Station channel of the {@link Joystick}
	 */
	public Controller( int buttons, int port ) {
		this( buttons, port, 0.0 );
	}
	
	/**
	 * @return The distance from 0.0 an axis must be to be returned
	 */
	public double getDeadZone() {
		return deadZone;
	}
	
	/**
	 * Sets the dead zone for this controller
	 * 
	 * @param deadZone Minimum distance an axis must be from 0.0 to be returned
	 */
	public void setDeadZone( double deadZone ) {
		this.deadZone = deadZone;
	}
	
	/**
	 * Stores away the old state of the buttons and saves the current state of the buttons. This must be
	 * called the beginning of the control loop to update the buttons. The results of all button methods will
	 * not change until this is called.
	 */
	public void query() {
		// Copy the current state into the old state array
		System.arraycopy( state, 0, oldState, 0, state.length );
		
		// Save the current state of the controller
		for ( int i = 0; i < state.length; i++ ) {
			state[i] = joystick.getRawButton( i + 1 );
		}
	}
	
	/**
	 * Checks if a button is current pressed
	 * 
	 * @param button Button number
	 * @return If the button is down
	 */
	public boolean getButton( int button ) {
		return state[button - 1];
	}
	
	/**
	 * Checks if a button was just pressed
	 * 
	 * @param button Button number
	 * @return If the button was just pressed down
	 */
	public boolean getButtonPressed( int button ) {
		return !oldState[--button] && state[button];
	}
	
	/**
	 * Checks if a button was just released
	 * 
	 * @param button Button number
	 * @return If the button was just released
	 */
	public boolean getButtonReleased( int button ) {
		return oldState[--button] && !state[button];
	}
	
	/**
	 * Reads an axis value and returns it if it is past the dead zone
	 * 
	 * @param axis Axis number
	 * @return Axis value
	 */
	public double getAxis( int axis ) {
		double val = joystick.getRawAxis( axis );
		return Math.abs( val ) > deadZone ? val : 0.0;
	}
	
	/**
	 * @return The {@link Joystick} that is being internally used
	 */
	public Joystick getJoystick() {
		return joystick;
	}
	
}
