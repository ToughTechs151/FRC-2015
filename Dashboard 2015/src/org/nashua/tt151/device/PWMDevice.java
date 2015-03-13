package org.nashua.tt151.device;

import java.awt.Color;

import org.nashua.tt151.util.MathUtils;

public class PWMDevice extends Device {
	protected static final Color NEUTRAL = Color.YELLOW;
	protected static final Color FORWARD = Color.GREEN;
	protected static final Color BACKWARD = Color.RED;
	
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
	
	private PWMType type;
	private double value;
	
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
		return type == PWMType.SERVO || value == 0 ? NEUTRAL : ( value > 0 ? FORWARD : BACKWARD );
	}
	
	@Override
	protected String getTypeString() {
		return String.valueOf( type.sh );
	}
	
}
