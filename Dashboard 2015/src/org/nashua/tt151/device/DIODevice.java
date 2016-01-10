package org.nashua.tt151.device;

import java.awt.Color;

/**
 * A general Digital IO device to be displayed on the DeviceModule
 * 
 * @author Kareem El-Faramawi
 */
public class DIODevice extends Device {
	
	// Colors for the different states of the device
	private final static Color ON = new Color( 0, 255, 0 );
	private final static Color OFF = new Color( 255, 0, 0 );
	private final static Color OTHER = new Color( 0, 255, 255 );
	
	// Values for interpreting a switch as true/false
	public static double LIM_ON = 1.0;
	public static double LIM_OFF = 0.0;
	
	/**
	 * Different types of DIO devices
	 */
	public enum DIOType {
		LIMIT_SWITCH( 'L' ),
		ENCODER( 'E' ),
		UNKNOWN( 'U' );
		
		private char sh;
		
		public static DIOType getFromShorthand( char sh ) {
			for ( DIOType d : DIOType.values() ) {
				if ( d.sh == sh ) {
					return d;
				}
			}
			return UNKNOWN;
		}
		
		private DIOType( char sh ) {
			this.sh = sh;
		}
	}
	
	// Value and type
	private double value;
	private DIOType type;
	
	/**
	 * Creates a DIODevice
	 * 
	 * @param channel Device channel
	 * @param name Name of the device
	 * @param value Value of the device
	 * @param type The type of device
	 */
	public DIODevice( int channel, String name, double value, DIOType type ) {
		super( channel, name );
		this.value = value;
		this.type = type;
	}
	
	@Override
	protected String getDrawValue() {
		// ON/OFF for a switch
		// Number for other devices
		return type == DIOType.LIMIT_SWITCH ? ( value == LIM_ON ? "ON" : "OFF" ) : String.format( "%.2f", value );
	}
	
	@Override
	protected Color selectStatusColor() {
		// Red/Green for a switch
		// Cyan for other devices
		return type == DIOType.LIMIT_SWITCH ? ( value == LIM_ON ? ON : OFF ) : OTHER;
	}
	
	@Override
	protected String getTypeString() {
		return String.valueOf( type.sh );
	}
	
}
