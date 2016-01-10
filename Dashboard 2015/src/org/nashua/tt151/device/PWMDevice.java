package org.nashua.tt151.device;

import java.awt.Color;

/**
 * A general analog device to be displayed on the DeviceModule
 * 
 * @author Kareem El-Faramawi
 */
public class PWMDevice extends Device {
	// PWM state colors
	protected static final Color NEUTRAL = Color.YELLOW;
	protected static final Color FORWARD = Color.GREEN;
	protected static final Color BACKWARD = Color.RED;
	
	/**
	 * Different PWM device types
	 */
	public enum PWMType {
		JAGUAR( 'J' ),
		VICTOR( 'V' ),
		TALON( 'T' ),
		SERVO( 'S' ),
		UNKNOWN( 'U' );
		
		private char sh;
		
		public static PWMType getFromShorthand( char sh ) {
			for ( PWMType p : PWMType.values() ) {
				if ( p.sh == sh ) {
					return p;
				}
			}
			return UNKNOWN;
		}
		
		private PWMType( char sh ) {
			this.sh = sh;
		}
	}
	
	// Value and type
	private double value;
	private PWMType type;
	
	/**
	 * Creates a PWMDevice
	 * 
	 * @param channel Device channel
	 * @param name Name of the device
	 * @param value Value of the device
	 * @param type The type of device
	 */
	public PWMDevice( int channel, String name, double value, PWMType type ) {
		super( channel, name );
		this.value = value;
		this.type = type;
	}
	
	@Override
	protected String getDrawValue() {
		return String.format( "%.2f", value );
	}
	
	@Override
	protected Color selectStatusColor() {
		// Yellow for neutral
		// Red/Green for forward/backward
		return type == PWMType.SERVO || value == 0 ? NEUTRAL : ( value > 0 ? FORWARD : BACKWARD );
	}
	
	@Override
	protected String getTypeString() {
		return String.valueOf( type.sh );
	}
	
}
