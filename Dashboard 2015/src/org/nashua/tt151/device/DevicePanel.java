package org.nashua.tt151.device;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;

import javax.swing.JPanel;

public class DevicePanel<T extends Device> extends JPanel {
	private T[] devices;
	static BlankDevice[] blanks = null;
	private String name;
	
	public DevicePanel( String name, int channels ) {
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
		setBackground( Color.GRAY.darker().darker().darker().darker() );
		clearAllDevices();
	}
	
	public void registerDevice( T device ) {
		int channel = device.getChannel();
		if ( channel < 0 || channel >= devices.length ) {
			System.err.println( "Channel " + channel + " out of range!" );
			return;
		}
		devices[channel] = device;
	}
	
	public void clearDevice( int channel ) {
		devices[channel] = null;
	}
	
	public void clearAllDevices() {
		for ( int i = 0; i < devices.length; i++ ) {
			clearDevice( i );
		}
	}
	
	public T getDevice( int channel ) {
		if ( channel < 0 || channel >= devices.length ) {
			System.err.println( "Channel " + channel + " out of range!" );
			return null;
		}
		return devices[channel] != null ? devices[channel] : (T) blanks[channel];
	}
	
	public T[] getDevices() {
		return devices;
	}
	
	public String getName() {
		return name;
	}
	
	public int getNumChannels() {
		return devices.length;
	}
	
	public boolean isSlotAvailable( int channel ) {
		return getDevice( channel ) instanceof BlankDevice;
	}
	
	public void paintComponent( Graphics g ) {
		g.setColor( getBackground() );
		g.fillRect( 0, 0, getWidth(), getHeight() );
		int y = 0;
		for ( int i = 0; i < devices.length; i++ ) {
			getDevice( i ).paintComponent( g.create( 0, y, Device.WIDTH, Device.HEIGHT ) );
			y += Device.HEIGHT;
		}
		
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
