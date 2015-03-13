package org.nashua.tt151.module;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;

import javax.swing.JPanel;

import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/**
 * Monitors bandwidth usage between dashboard and robot
 * @author Brian Ashworth
 */
public class BandwidthModule extends JPanel {
	private static final Sigar s = new Sigar();
	private static double bytes = 0;
	private static double current = 0;
	private static double average = 0;
	private static double[] averageBuffer = new double[20];
	private static double averageTemp = 0.0;
	private static int count = 0;
	private static double peak = 0;
	private static int indices = 0;
	
	private static final Color LOW = Color.GREEN.darker().darker().darker();
	private static final Color MED = Color.YELLOW.darker().darker().darker().darker().darker();
	private static final Color HIGH = Color.RED.darker().darker().darker();
	private static final Color OVER = Color.MAGENTA;
	
	public BandwidthModule() {
		new java.util.Timer().scheduleAtFixedRate( new java.util.TimerTask() {
			public void run() {
				try {
					for ( String ifc : s.getNetInterfaceList() ) {
						NetInterfaceConfig nic = s.getNetInterfaceConfig( ifc );
						if ( !nic.getAddress().equals( "0.0.0.0" ) && !nic.getType().contains( "Loopback" ) ) {
							NetInterfaceStat nis = s.getNetInterfaceStat( ifc );
							double nBytes = nis.getTxBytes() + nis.getRxBytes();
							if ( bytes > 0 ) {
								current = ( nBytes - bytes ) * 8 / 1024.0 / 1024.0;
								if ( count == averageBuffer.length - 1 ) {
									averageBuffer[count] = current;
									count = 0;
								} else {
									averageBuffer[count] = current;
									count++;
								}
								indices = 0;
								for ( int i = 0; i < averageBuffer.length; i++ ) {
									averageTemp += averageBuffer[i];
									if ( averageBuffer[i] > 0.0 ) {
										indices++;
									}
								}
								average = averageTemp / indices;
								averageTemp = 0;
								peak = Math.max( current, peak );
							}
							bytes = nBytes;
						}
					}
				} catch ( SigarException e ) {
					e.printStackTrace();
				}
			}
		}, 1, 1000 );
		setBackground( Color.GRAY.darker().darker().darker() );
	}
	
	public void paintComponent( Graphics g ) {
		Font oldFont = g.getFont();
		g.setFont( g.getFont().deriveFont( 24f ) );
		FontMetrics fm = g.getFontMetrics();
		String str;
		
		g.setColor( current < 4 ? LOW : current < 6 ? MED : current < 7 ? HIGH : OVER );
		g.fillRect( 0, 0, getWidth(), getHeight() / 3 );
		g.setColor( Color.WHITE );
		str = String.format( "Current: %.02f Mbps", current );
		g.drawString( str, getWidth() / 2 - fm.stringWidth( str ) / 2, ( getHeight() / 3 ) / 2 + fm.getAscent() / 2 );
		
		g.setColor( average < 4 ? LOW : average < 6 ? MED : average < 7 ? HIGH : OVER );
		g.fillRect( 0, getHeight() / 3, getWidth(), getHeight() / 3 );
		g.setColor( Color.WHITE );
		str = String.format( "Average: %.02f Mbps", average );
		g.drawString( str, getWidth() / 2 - fm.stringWidth( str ) / 2, getHeight() / 3 + ( getHeight() / 3 ) / 2 + fm.getAscent() / 2 );
		
		g.setColor( peak < 4 ? LOW : peak < 6 ? MED : peak < 7 ? HIGH : OVER );
		g.fillRect( 0, getHeight() / 3 * 2, getWidth(), getHeight() / 3 );
		g.setColor( Color.WHITE );
		str = String.format( "Peak: %.02f Mbps", peak );
		g.drawString( str, getWidth() / 2 - fm.stringWidth( str ) / 2, getHeight() / 3 * 2 + ( getHeight() / 3 ) / 2 + fm.getAscent() / 2 );
		
		Graphics2D g2d = (Graphics2D) g;
		Paint oldPaint = g2d.getPaint();
		g2d.setPaint( new LinearGradientPaint( 0, 0, getWidth(), 0, new float[] { 0.0f, 0.5f, 1.0f }, new Color[] { getBackground(), Color.WHITE, getBackground() } ) );
		for ( int i = 0; i < 3; i++ ) {
			g2d.drawLine( 0, getHeight() / 3 * i, getWidth(), getHeight() / 3 * i );
		}
		g2d.setPaint( oldPaint );
		g.setFont( oldFont );
	}
}
