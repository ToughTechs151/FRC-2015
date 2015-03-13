package org.nashua.tt151.device;

import java.awt.Color;

public class AnalogDevice extends Device {
	public enum AnalogType {
		ACCELEROMETER( 'A', new Color( 0, 0, 255 ) ),
		GYRO( 'G', new Color( 255, 255, 0 ) ),
		POTENTIOMETER( 'P', new Color( 0, 255, 0 ) ),
		UNKNOWN( 'U', new Color( 255, 0, 0 ) );
		
		private char sh;
		private Color c;
		
		public static AnalogType getFromShorthand( char sh ) {
			for ( AnalogType a : AnalogType.values() ) {
				if ( a.sh == sh ) {
					return a;
				}
			}
			return UNKNOWN;
		}
		
		private AnalogType( char sh, Color c ) {
			this.sh = sh;
			this.c = c;
		}
	}
	
	private double value;
	private AnalogType type;
	
	public AnalogDevice( int channel, String name, double value, AnalogType type ) {
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
		return type.c;
	}
	
	@Override
	protected String getTypeString() {
		return String.valueOf( type.sh );
	}
	
}
