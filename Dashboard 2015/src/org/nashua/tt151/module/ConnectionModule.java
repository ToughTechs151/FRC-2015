package org.nashua.tt151.module;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;

public class ConnectionModule extends JPanel {
	public enum State {
		DISCONNECTED( 'D', new Color( 255, 0, 0 ), 2 ),
		CONNECTED( 'C', new Color( 0, 255, 0 ), 3 ),
		TELEOP( 'T', new Color( 0, 0, 255 ), 4 ),
		AUTONOMOUS( 'A', new Color( 255, 0, 255 ), 3 );
		
		private char sh;
		private Color c;
		private int repeat;
		
		private State( char sh, Color c, int repeat ) {
			this.sh = sh;
			this.c = c;
			this.repeat = repeat;
		}
		
		public static State getFromShorthand( char sh ) {
			for ( State s : State.values() ) {
				if ( s.sh == sh ) {
					return s;
				}
			}
			return DISCONNECTED;
		}
	}
	
	public static int HEIGHT = 30;
	private State state;
	private int scrollX = 0;
	
	public ConnectionModule() {
		this( State.DISCONNECTED );
	}
	
	public ConnectionModule( State state ) {
		this.state = state;
	}
	
	public void setState( State state ) {
		this.state = state;
	}
	
	public State getState() {
		return state;
	}
	
	public void paintComponent( Graphics g ) {
		g.setColor( state.c );
		g.fillRect( 0, 0, getWidth(), getHeight() );
		Font oldFont = g.getFont();
		g.setFont( g.getFont().deriveFont( 41.0f ).deriveFont( Font.BOLD ) );
		FontMetrics fm = g.getFontMetrics();
		g.setColor( Color.WHITE );
		int dispWidth = getWidth() / state.repeat;
		for ( int i = -1; i < state.repeat; i++ ) {
			g.drawString( state == State.TELEOP ? "TELE-OP" : state.toString(), ( i * dispWidth ) + scrollX, getHeight() );
		}
		scrollX = ( scrollX + 1 ) % dispWidth;
		g.setFont( oldFont );
	}
}
