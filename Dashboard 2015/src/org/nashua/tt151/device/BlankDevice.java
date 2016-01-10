package org.nashua.tt151.device;

import java.awt.Color;
import java.awt.Graphics;

/**
 * A blank, placeholder device
 * 
 * @author Kareem El-Faramawi
 */
public class BlankDevice extends Device {
	
	/**
	 * Creates a BlankDevice
	 * 
	 * @param channel Device channel
	 */
	public BlankDevice( int channel ) {
		super( channel, "BLANK" );
	}
	
	@Override
	public void paintComponent( Graphics g ) {
		// Special draw method for blank devices, there isn't much to draw
		drawBG( g );
		drawChannel( g );
		drawStatusLight( g );
	}
	
	@Override
	protected String getDrawValue() {
		return "";
	}
	
	@Override
	protected Color selectStatusColor() {
		return Color.GRAY;
	}
	
	@Override
	protected String getTypeString() {
		return "";
	}
}
