package org.nashua.tt151.device;

import java.awt.Color;

public class RelayDevice extends Device {
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
	
	public enum Value {
		REV( '-', Color.RED ),
		FWD( '+', Color.GREEN ),
		OFF( '0', Color.YELLOW ),
		ON('1', Color.BLUE);
		
		private char sh;
		private Color c;
		
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
	
	private Direction direction;
	private Value value;
	
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
