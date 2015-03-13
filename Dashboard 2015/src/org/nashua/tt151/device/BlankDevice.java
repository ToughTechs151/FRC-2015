package org.nashua.tt151.device;

import java.awt.Color;
import java.awt.Graphics;

public class BlankDevice extends Device {
	
	public BlankDevice( int channel ) {
		super( channel, "BLANK" );
	}
	
	@Override
	public void paintComponent( Graphics g ) {
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
