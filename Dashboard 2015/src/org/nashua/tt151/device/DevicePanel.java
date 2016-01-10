package org.nashua.tt151.device;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;

import javax.swing.JPanel;

import org.nashua.tt151.Logger;

/**
 * A DevicePanel is like a glorified array of devices that draws these devices the dash
 * 
 * @author Kareem El-Faramawi
 *
 * @param <T> The type of device stored in this panel
 */
public class DevicePanel<T extends Device> extends JPanel {
	private T[] devices; // Array of all devices
	static BlankDevice[] blanks = null; // Blank placeholders, used across all DevicePanels
	private String name; // Name of this panel
	
	/**
	 * Initialize a DevicePanel
	 * 
	 * @param name The name to be displayed for this panel
	 * @param channels The number of channels this panel will have
	 */
	public DevicePanel( String name, int channels ) {
		// Initialize the blank devices if needed
		if ( blanks == null ) {
			blanks = new BlankDevice[10];
			for ( int i = 0; i < blanks.length; i++ ) {
				blanks[i] = new BlankDevice( i );
			}
		}
		
		devices = (T[]) new Device[channels];
		this.name = name;
		setPreferredSize( new Dimension( Device.WIDTH, Device.HEIGHT * channels ) );
		setSize( getPreferredSize() );
		setBackground( Color.GRAY.darker().darker().darker().darker() ); // another one
		clearAllDevices();
	}
	
	/**
	 * Places a device into the panel at the given channel, if the channel is valid
	 * 
	 * @param device Device to place in the panel
	 */
	public void registerDevice( T device ) {
		int channel = device.getChannel();
		if ( channel < 0 || channel >= devices.length ) {
			Logger.logLine( "ERROR: Channel " + channel + " out of range!" );
			return;
		}
		devices[channel] = device;
	}
	
	/**
	 * Clears the device at the given channel
	 * 
	 * @param channel Device channel
	 */
	public void clearDevice( int channel ) {
		devices[channel] = null;
	}
	
	/**
	 * Clears all devices in the panel
	 */
	public void clearAllDevices() {
		for ( int i = 0; i < devices.length; i++ ) {
			clearDevice( i );
		}
	}
	
	/**
	 * Gets the device at a certain channel
	 * 
	 * @param channel Device channel
	 * @return Device contained in the panel
	 */
	public T getDevice( int channel ) {
		if ( channel < 0 || channel >= devices.length ) {
			Logger.logLine( "ERROR: Channel " + channel + " out of range!" );
			return null;
		}
		return devices[channel] != null ? devices[channel] : (T) blanks[channel];
	}
	
	/**
	 * @return Array of all devices
	 */
	public T[] getDevices() {
		return devices;
	}
	
	/**
	 * @return The name of this panel
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return The number of channels in this panel
	 */
	public int getNumChannels() {
		return devices.length;
	}
	
	/**
	 * @param channel Device channel
	 * @return If the given channel is open for a new device
	 */
	public boolean isChannelAvailable( int channel ) {
		return getDevice( channel ) instanceof BlankDevice;
	}
	
	public void paintComponent( Graphics g ) {
		// Fill bg
		g.setColor( getBackground() );
		g.fillRect( 0, 0, getWidth(), getHeight() );
		
		// Paint each device
		int y = 0;
		for ( int i = 0; i < devices.length; i++ ) {
			getDevice( i ).paintComponent( g.create( 0, y, Device.WIDTH, Device.HEIGHT ) );
			y += Device.HEIGHT;
		}
		
		// Paint separators
		Graphics2D g2d = (Graphics2D) g;
		Paint oldPaint = g2d.getPaint();
		g2d.setPaint( new LinearGradientPaint( 0, 0, getWidth(), 0, new float[] { 0.0f, 0.5f, 1.0f }, new Color[] { getBackground(), Color.WHITE, getBackground() } ) );
		y = 0;
		for ( int i = 0; i < devices.length - 1; i++ ) {
			y += Device.HEIGHT;
			g2d.drawLine( 0, y, getWidth(), y );
		}
		g2d.setPaint( oldPaint );
	}
	
}
