package org.nashua.tt151.module;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.nashua.tt151.util.FileIOHelper;

public class InstructionsModule extends JPanel {
	private BufferedImage driver;
	private BufferedImage shooter;
	
	public InstructionsModule() {
		driver = FileIOHelper.loadImage( "/img/driver.png" );
		shooter = FileIOHelper.loadImage( "/img/lifter.png" );
		setPreferredSize( new Dimension( Math.max( driver.getWidth(), shooter.getWidth() ), driver.getHeight() + shooter.getHeight() ) );
		setSize( getPreferredSize() );
		setBackground( Color.GRAY.darker() );
	}
	
	public void paintComponent( Graphics g ) {
		// Draw images
		g.drawImage( driver, 0, 0, this );
		g.drawImage( shooter, 0, driver.getHeight(), this );
		
		// Draw separator
		Graphics2D g2d = (Graphics2D) g;
		Paint oldPaint = g2d.getPaint();
		g2d.setPaint( new LinearGradientPaint( 0, driver.getHeight(), getWidth(), driver.getHeight(), new float[] { 0.0f, 0.5f, 1.0f }, new Color[] { getBackground(), Color.WHITE, getBackground() } ) );
		g2d.drawLine( 0, driver.getHeight(), getWidth(), driver.getHeight() );
		g2d.setPaint( oldPaint );
	}
	
}
