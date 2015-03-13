package org.nashua.tt151.device;

import java.awt.Color;
import java.awt.Dimension;

public class DIODevice extends Device {
	
	private final static Color ON = new Color( 0, 255, 0 );
	private final static Color OFF = new Color( 255, 0, 0 );
	private final static Color OTHER = new Color( 0, 255, 255 );
	
	public static double LIM_ON = 1.0;
	public static double LIM_OFF = 0.0;
	
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
	
	private DIOType type;
	private double value;
	
	public DIODevice( int channel, String name, double value, DIOType type ) {
		super( channel, name );
		this.value = value;
		this.type = type;
	}
	
	@Override
	protected String getDrawValue() {
		return type == DIOType.LIMIT_SWITCH ? ( value == LIM_ON ? "ON" : "OFF" ) : String.format( "%.2f", value );
	}
	
	@Override
	protected Color selectStatusColor() {
		return type == DIOType.LIMIT_SWITCH ? ( value == LIM_ON ? ON : OFF ) : OTHER;
	}
	
	@Override
	protected String getTypeString() {
		return String.valueOf( type.sh );
	}
	
}
