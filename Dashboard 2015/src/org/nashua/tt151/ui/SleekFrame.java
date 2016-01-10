package org.nashua.tt151.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import org.nashua.tt151.util.FileIOHelper;

/**
 * A JFrame with a custom window, because the default OS style is way too mainstream
 * 
 * @author Kareem El-Faramawi
 */
public class SleekFrame extends JFrame implements MouseListener, MouseMotionListener {
	public final static int TITLE_HEIGHT = 28;
	public final static int BORDER = 10;
	private Font titleFont = null;
	
	// Images for corner buttons
	private BufferedImage minimizeSheet;
	private BufferedImage closeSheet;
	private BufferedImage minimize;
	private BufferedImage close;
	
	// Mouse location on title bar
	private Point mousePoint = null;
	
	private BufferedImage drawImage;
	
	public SleekFrame( String title ) {
		this( title, Color.DARK_GRAY );
	}
	
	public SleekFrame( String title, Color bg ) {
		super( title );
		setBackground( bg );
		setUndecorated( true );
		setDefaultCloseOperation( EXIT_ON_CLOSE );
		addMouseListener( this );
		addMouseMotionListener( this );
		minimizeSheet = FileIOHelper.loadImage( "/img/minimize.png" );
		closeSheet = FileIOHelper.loadImage( "/img/close.png" );
		minimize = minimizeSheet.getSubimage( 0, 0, TITLE_HEIGHT, TITLE_HEIGHT );
		close = closeSheet.getSubimage( 0, 0, TITLE_HEIGHT, TITLE_HEIGHT );
	}
	
	public void setSize( int width, int height ) {
		super.setSize( width + ( BORDER * 2 ), height + TITLE_HEIGHT + BORDER );
	}
	
	public void setTitleFont( Font f ) {
		titleFont = f;
	}
	
	public void paint( Graphics bg ) {
		// Draw components to a separate image
		BufferedImage cImg = new BufferedImage( getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB );
		super.paint( cImg.getGraphics() );
		
		// Draw frame to an image to eliminate flickering
		drawImage = new BufferedImage( getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB );
		Graphics g = drawImage.createGraphics();
		
		// Draw main window
		g.setColor( getBackground() );
		g.fillRect( 0, 0, getWidth(), getHeight() );
		
		// Draw icon and minimize / close buttons
		g.drawImage( getIconImage(), 0, 0, this );
		g.drawImage( minimize, getWidth() - TITLE_HEIGHT * 2, 0, this );
		g.drawImage( close, getWidth() - TITLE_HEIGHT, 0, this );
		
		// Draw title
		Font oldFont = g.getFont();
		if ( titleFont != null ) {
			g.setFont( titleFont );
		}
		FontMetrics fm = g.getFontMetrics();
		g.setColor( Color.WHITE );
		g.drawString( getTitle(), getWidth() / 2 - fm.stringWidth( getTitle() ) / 2, TITLE_HEIGHT / 2 + fm.getAscent() / 2 );
		g.setFont( oldFont );
		
		// Draw components to main image
		g.drawImage( cImg, BORDER, TITLE_HEIGHT, getWidth() - BORDER, getHeight() - BORDER, BORDER, TITLE_HEIGHT, getWidth() - BORDER, getHeight() - BORDER, null );
		
		// Draw separator between title bar and window
		Graphics2D g2d = (Graphics2D) g;
		Paint oldPaint = g2d.getPaint();
		g2d.setPaint( new LinearGradientPaint( 0, TITLE_HEIGHT, getWidth(), TITLE_HEIGHT, new float[] { 0.0f, 0.5f, 1.0f }, new Color[] { getBackground(), Color.WHITE, getBackground() } ) );
		g2d.drawLine( 0, TITLE_HEIGHT, getWidth(), TITLE_HEIGHT );
		g2d.setPaint( oldPaint );
		
		// Draw final image to screen
		bg.drawImage( drawImage, 0, 0, this );
	}
	
	public void mouseDragged( MouseEvent e ) {
		if ( mousePoint != null && mousePoint.x < getWidth() - TITLE_HEIGHT * 2 ) {
			// Drag the window around
			setLocation( e.getXOnScreen() - mousePoint.x, e.getYOnScreen() - mousePoint.y );
		}
	}
	
	public void mouseMoved( MouseEvent e ) {
		// Minimize
		if ( new Rectangle( getWidth() - ( TITLE_HEIGHT * 2 ), 0, TITLE_HEIGHT, TITLE_HEIGHT ).contains( e.getPoint() ) ) {
			minimize = minimizeSheet.getSubimage( TITLE_HEIGHT, 0, TITLE_HEIGHT, TITLE_HEIGHT ); // Hovering
		} else {
			minimize = minimizeSheet.getSubimage( 0, 0, TITLE_HEIGHT, TITLE_HEIGHT ); // Default
		}
		// Close
		if ( new Rectangle( getWidth() - TITLE_HEIGHT, 0, TITLE_HEIGHT, TITLE_HEIGHT ).contains( e.getPoint() ) ) {
			close = closeSheet.getSubimage( TITLE_HEIGHT, 0, TITLE_HEIGHT, TITLE_HEIGHT ); // Hovering
		} else {
			close = closeSheet.getSubimage( 0, 0, TITLE_HEIGHT, TITLE_HEIGHT ); // Default
		}
	}
	
	public void mouseExited( MouseEvent e ) {
		// Reset all button images
		minimize = minimizeSheet.getSubimage( 0, 0, TITLE_HEIGHT, TITLE_HEIGHT );
		close = closeSheet.getSubimage( 0, 0, TITLE_HEIGHT, TITLE_HEIGHT );
	}
	
	public void mousePressed( MouseEvent e ) {
		if ( e.getY() <= TITLE_HEIGHT ) {
			mousePoint = e.getPoint();
			// Minimize button pressed
			if ( new Rectangle( getWidth() - ( TITLE_HEIGHT * 2 ), 0, TITLE_HEIGHT, TITLE_HEIGHT ).contains( e.getPoint() ) ) {
				minimize = minimizeSheet.getSubimage( TITLE_HEIGHT * 2, 0, TITLE_HEIGHT, TITLE_HEIGHT );
			}
			// Close button pressed
			if ( new Rectangle( getWidth() - TITLE_HEIGHT, 0, TITLE_HEIGHT, TITLE_HEIGHT ).contains( e.getPoint() ) ) {
				close = closeSheet.getSubimage( TITLE_HEIGHT * 2, 0, TITLE_HEIGHT, TITLE_HEIGHT );
			}
		}
	}
	
	public void mouseReleased( MouseEvent e ) {
		mousePoint = null;
		// Minimize
		if ( new Rectangle( getWidth() - ( TITLE_HEIGHT * 2 ), 0, TITLE_HEIGHT, TITLE_HEIGHT ).contains( e.getPoint() ) ) {
			setExtendedState( JFrame.ICONIFIED );
		}
		// Close
		if ( new Rectangle( getWidth() - TITLE_HEIGHT, 0, TITLE_HEIGHT, TITLE_HEIGHT ).contains( e.getPoint() ) ) {
			dispatchEvent( new WindowEvent( this, WindowEvent.WINDOW_CLOSING ) );
		}
		// Reset all button images
		minimize = minimizeSheet.getSubimage( 0, 0, TITLE_HEIGHT, TITLE_HEIGHT );
		close = closeSheet.getSubimage( 0, 0, TITLE_HEIGHT, TITLE_HEIGHT );
	}
	
	public void mouseClicked( MouseEvent e ) {}
	
	public void mouseEntered( MouseEvent e ) {}
	
}
