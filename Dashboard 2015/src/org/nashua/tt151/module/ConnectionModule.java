package org.nashua.tt151.module;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * Banner that displays the connection status of the robot
 * 
 * @author Kareem El-Faramawi
 */
public class ConnectionModule extends JPanel {
	
	/**
	 * The various states of connection the robot can have
	 */
	public enum State {
		DISCONNECTED( 'D', new Color( 255, 0, 0 ), 2 ),
		CONNECTED( 'C', new Color( 0, 255, 0 ), 3 ),
		TELEOP( 'T', new Color( 0, 0, 255 ), 4 ),
		AUTONOMOUS( 'A', new Color( 255, 0, 255 ), 3 );
		
		private char sh;
		private Color c; // Banner color
		private int repeat; // # of times to repeat the word
		
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
	
	public static int HEIGHT = 30; // Banner size
	private State state; // Curret state
	private int scrollX = 0; // Keeps track of scrolling
	
	/**
	 * Create a ConnectionModule in the default DISCONNECTED state
	 */
	public ConnectionModule() {
		this( State.DISCONNECTED );
	}
	
	/**
	 * Create a ConnectionModule
	 * 
	 * @param state Initial state
	 */
	public ConnectionModule( State state ) {
		this.state = state;
	}
	
	/**
	 * Set the state of this module
	 * 
	 * @param state Connection state
	 */
	public void setState( State state ) {
		this.state = state;
	}
	
	/**
	 * @return Current connection state
	 */
	public State getState() {
		return state;
	}
	
	public void paintComponent( Graphics g ) {
		// Fill bg
		g.setColor( state.c );
		g.fillRect( 0, 0, getWidth(), getHeight() );
		
		Font oldFont = g.getFont();
		g.setFont( g.getFont().deriveFont( 41.0f ).deriveFont( Font.BOLD ) );
		g.setColor( Color.WHITE );
		
		// Magic to get the cool scrolling text
		int dispWidth = getWidth() / state.repeat;
		for ( int i = -1; i < state.repeat; i++ ) {
			g.drawString( state == State.TELEOP ? "TELE-OP" : state.toString(), ( i * dispWidth ) + scrollX, getHeight() );
		}
		scrollX = ( scrollX + 1 ) % dispWidth;
		g.setFont( oldFont );
	}
}
