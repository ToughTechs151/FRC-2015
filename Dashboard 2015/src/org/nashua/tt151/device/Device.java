package org.nashua.tt151.device;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;

import javax.swing.JPanel;

/**
 * General Device class that is the basis for everything in the DeviceModule
 * 
 * @author Kareem El-Faramawi
 */
public abstract class Device extends JPanel {
	// Device panel size
	public static final int WIDTH = 100;
	public static final int HEIGHT = 40;
	
	// Graphics sizes
	protected static final int STATUS_WIDTH = WIDTH / 6; // Width of the status light for the device
	protected float drawFontSize = 22.0f;
	
	// Device data
	protected final int channel;
	protected String name;
	
	/**
	 * Creates a Device
	 * 
	 * @param channel Device channel
	 * @param name Device name to be displayed
	 */
	public Device( int channel, String name ) {
		this.channel = channel;
		this.name = name;
		setPreferredSize( new Dimension( WIDTH, HEIGHT ) );
		setSize( getPreferredSize() );
		setBackground( Color.GRAY.darker().darker() );
	}
	
	/**
	 * @return This Device's channel
	 */
	public int getChannel() {
		return channel;
	}
	
	/**
	 * @return This Device's name
	 */
	public String getName() {
		return name;
	}
	
	public void paintComponent( Graphics g ) {
		// Doesn't this just look lovely?
		drawBG( g );
		drawValue( g );
		drawChannel( g );
		drawName( g );
		drawType( g );
		drawStatusLight( g );
	}
	
	/**
	 * Fills in the Device background
	 * 
	 * @param g Graphics object
	 */
	protected void drawBG( Graphics g ) {
		g.setColor( getBackground() );
		g.fillRect( 0, 0, getWidth(), getHeight() );
	}
	
	/**
	 * @return The value of this Device represented as a String
	 */
	protected abstract String getDrawValue();
	
	/**
	 * Draws the value of this Device at the center of the panel
	 * 
	 * @param g Graphics object
	 */
	protected void drawValue( Graphics g ) {
		g.setColor( Color.WHITE );
		Font oldFont = g.getFont();
		g.setFont( g.getFont().deriveFont( drawFontSize ) );
		FontMetrics fm = g.getFontMetrics();
		String val = getDrawValue();
		g.drawString( val, ( getWidth() + STATUS_WIDTH ) / 2 - fm.stringWidth( val ) / 2, getHeight() / 2 + fm.getAscent() / 2 );
		g.setFont( oldFont );
	}
	
	/**
	 * Draws the channel number of this Device at the top right corner
	 * 
	 * @param g Graphics object
	 */
	protected void drawChannel( Graphics g ) {
		g.setColor( Color.WHITE );
		FontMetrics fm = g.getFontMetrics();
		String s = "" + channel;
		g.drawString( s, WIDTH - fm.stringWidth( s ), fm.getAscent() );
	}
	
	/**
	 * Draws the name of this Device at the top left corner
	 * 
	 * @param g Graphics object
	 */
	protected void drawName( Graphics g ) {
		g.setColor( Color.WHITE );
		FontMetrics fm = g.getFontMetrics();
		g.drawString( name, STATUS_WIDTH, fm.getAscent() );
	}
	
	/**
	 * @return The shorthand name of this Device's type
	 */
	protected abstract String getTypeString();
	
	/**
	 * Draws the type of this Device at the bottom right corner
	 * 
	 * @param g Graphics object
	 */
	protected void drawType( Graphics g ) {
		g.setColor( Color.WHITE );
		FontMetrics fm = g.getFontMetrics();
		String s = getTypeString();
		g.drawString( s, WIDTH - fm.stringWidth( s ), HEIGHT );
	}
	
	/**
	 * @return The color of the status light
	 */
	protected abstract Color selectStatusColor();
	
	/**
	 * Draws the status light on the left side of the panel
	 * 
	 * @param g Graphics object
	 */
	protected void drawStatusLight( Graphics g ) {
		g.setColor( Color.GRAY.darker() );
		g.fillRect( 0, 0, STATUS_WIDTH, getHeight() );
		Graphics2D g2d = (Graphics2D) g;
		Paint oldPaint = g2d.getPaint();
		g2d.setPaint( new LinearGradientPaint( 0, 0, 0, getHeight(), new float[] { 0.0f, 0.5f, 1.0f }, new Color[] { g.getColor(), selectStatusColor(), g.getColor() } ) );
		g2d.fillRect( 0, 0, STATUS_WIDTH, getHeight() );
		g2d.setPaint( oldPaint );
	}
}
