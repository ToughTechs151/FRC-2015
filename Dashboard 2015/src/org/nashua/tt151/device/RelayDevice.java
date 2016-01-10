package org.nashua.tt151.device;

import java.awt.Color;

/**
 * A relay to be displayed on the DeviceModule
 * 
 * @author Kareem El-Faramawi
 */
public class RelayDevice extends Device {
	/**
	 * Different directional modes of a relay
	 */
	public enum Direction {
		BACKWARD( '-' ),
		FORWARD( '+' ),
		BOTH( 'B' );
		
		private char sh;
		
		public static Direction getFromShorthand( char sh ) {
			for ( Direction d : Direction.values() ) {
				if ( d.sh == sh ) {
					return d;
				}
			}
			return BOTH;
		}
		
		private Direction( char sh ) {
			this.sh = sh;
		}
	}
	
	/**
	 * Different values of a relay
	 */
	public enum Value {
		REV( '-', Color.RED ),
		FWD( '+', Color.GREEN ),
		OFF( '0', Color.YELLOW ),
		ON( '1', Color.BLUE );
		
		private char sh;
		private Color c; // Color for each value
		
		public static Value getFromShorthand( char sh ) {
			for ( Value v : Value.values() ) {
				if ( v.sh == sh ) {
					return v;
				}
			}
			return OFF;
		}
		
		private Value( char sh, Color c ) {
			this.sh = sh;
			this.c = c;
		}
	}
	
	// Direction and value
	private Direction direction;
	private Value value;
	
	/**
	 * Create a relay
	 * 
	 * @param channel Device channel
	 * @param name Name of the device
	 * @param direction The direction of the relay
	 * @param value The value of the relay
	 */
	public RelayDevice( int channel, String name, Direction direction, Value value ) {
		super( channel, name );
		this.direction = direction;
		this.value = value;
	}
	
	@Override
	protected String getDrawValue() {
		return value.toString();
	}
	
	@Override
	protected Color selectStatusColor() {
		return value.c;
	}
	
	@Override
	protected String getTypeString() {
		return String.valueOf( direction.sh );
	}
	
}
