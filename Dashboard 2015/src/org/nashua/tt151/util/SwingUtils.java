package org.nashua.tt151.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * Some convenient methods for working with Swing
 * 
 * @author Kareem El-Faramawi
 */
public class SwingUtils {
	private SwingUtils() {}
	
	/**
	 * Recursively sets the background color of a component and all inner components
	 * 
	 * @param c Root component
	 * @param bg New background color
	 */
	public static void setBG_r( Component c, Color bg ) {
		setColor_r( c, bg, null );
	}
	
	/**
	 * Recursively sets the foreground color of a component and all inner components
	 * 
	 * @param c Root component
	 * @param fg New foreground color
	 */
	public static void setFG_r( Component c, Color fg ) {
		setColor_r( c, null, fg );
	}
	
	/**
	 * Recursively sets the background and foreground colors of a component and all inner components
	 * 
	 * @param c Root component
	 * @param bg New background color
	 * @param fg New foreground color
	 */
	public static void setColor_r( Component c, Color bg, Color fg ) {
		if ( bg != null ) {
			c.setBackground( bg );
		}
		if ( fg != null ) {
			c.setForeground( fg );
		}
		
		// Recurse if this component contains more components
		if ( c instanceof Container ) {
			Component[] comps = ( (Container) c ).getComponents();
			if ( comps.length > 0 ) {
				for ( Component comp : comps ) {
					setColor_r( comp, bg, fg );
				}
			}
		}
	}
	
	/**
	 * Creates a JPanel with an etched border and places a component in it
	 * 
	 * @param c Component to be placed in the panel
	 * @param title Title of the panel
	 * @param titleColor Color of the title text
	 * @return JPanel with the etched border containing the given component
	 */
	public static JPanel placeInTitledEtchedJPanel( Component c, String title, Color titleColor ) {
		JPanel parent = new JPanel( new BorderLayout() );
		parent.add( c, BorderLayout.CENTER );
		TitledBorder tb = new TitledBorder( title );
		tb.setBorder( BorderFactory.createEtchedBorder() );
		tb.setTitleColor( titleColor );
		parent.setBorder( tb );
		return parent;
	}
}
