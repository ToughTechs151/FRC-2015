package org.nashua.tt151.module;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;

import org.nashua.tt151.device.AnalogDevice;
import org.nashua.tt151.device.DIODevice;
import org.nashua.tt151.device.Device;
import org.nashua.tt151.device.DevicePanel;
import org.nashua.tt151.device.PWMDevice;
import org.nashua.tt151.device.RelayDevice;
import org.nashua.tt151.util.SwingUtils;

/**
 * Module for displaying the state of every device that the robot sends information about
 * 
 * @author Kareem El-Faramawi
 */
public class DeviceModule extends JPanel {
	
	/**
	 * The different types of devices that can be displayed in this module
	 * 
	 * @author Kareem El-Faramawi
	 */
	public enum DeviceType {
		PWM( "PWM" ),
		DIO( "DIO" ),
		RELAY( "Relay" ),
		ANALOG( "Analog" );
		
		private String name;
		
		private DeviceType( String name ) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	}
	
	// All the device panels, each having a set number of channels (according to the RoboRIO)
	private static DevicePanel<PWMDevice> pwm = new DevicePanel<PWMDevice>( DeviceType.PWM.getName(), 10 );
	private static DevicePanel<DIODevice> dio = new DevicePanel<DIODevice>( DeviceType.DIO.getName(), 10 );
	private static DevicePanel<RelayDevice> relay = new DevicePanel<RelayDevice>( DeviceType.RELAY.getName(), 4 );
	private static DevicePanel<AnalogDevice> analog = new DevicePanel<AnalogDevice>( DeviceType.ANALOG.getName(), 4 );
	
	/**
	 * Initializes this module with blank devices
	 */
	public DeviceModule() {
		// Create display panel
		JPanel panel = new JPanel();
		
		// Create device panels
		JPanel pwms = SwingUtils.placeInTitledEtchedJPanel( pwm, pwm.getName(), Color.WHITE );
		JPanel dios = SwingUtils.placeInTitledEtchedJPanel( dio, dio.getName(), Color.WHITE );
		JPanel relays = SwingUtils.placeInTitledEtchedJPanel( relay, relay.getName(), Color.WHITE );
		JPanel analogs = SwingUtils.placeInTitledEtchedJPanel( analog, analog.getName(), Color.WHITE );
		
		// Add device panels
		panel.add( pwms );
		panel.add( dios );
		JPanel relAna = new JPanel( new GridLayout( 2, 1 ) );
		relAna.add( relays );
		relAna.add( analogs );
		panel.add( relAna );
		
		panel = SwingUtils.placeInTitledEtchedJPanel( panel, "Devices", Color.WHITE );
		
		SwingUtils.setBG_r( panel, Color.GRAY.darker().darker().darker() );
		
		setLayout( new BorderLayout() );
		add( panel, BorderLayout.CENTER );
		
		setSize( getPreferredSize() );
	}
	
	/**
	 * Clears all devices from all panels
	 */
	public void clearAllDevices() {
		clearAllAnalogDevices();
		clearAllPWMDevices();
		clearAllDIODevices();
		clearAllRelayDevices();
	}
	
	/**
	 * Clears the analog device in a certain channel
	 * 
	 * @param channel Slot number
	 */
	public void clearAnalogDevice( int channel ) {
		analog.clearDevice( channel );
	}
	
	/**
	 * Clears the PWM device in a certain channel
	 * 
	 * @param channel Slot number
	 */
	public void clearPWMDevice( int channel ) {
		pwm.clearDevice( channel );
	}
	
	/**
	 * Clears the relay device in a certain channel
	 * 
	 * @param channel Slot number
	 */
	public void clearRelayDevice( int channel ) {
		relay.clearDevice( channel );
	}
	
	/**
	 * Clears the DIO device in a certain channel
	 * 
	 * @param channel Slot number
	 */
	public void clearDIODevice( int channel ) {
		dio.clearDevice( channel );
	}
	
	/**
	 * Clears all of the analog devices
	 */
	public void clearAllAnalogDevices() {
		analog.clearAllDevices();
	}
	
	/**
	 * Clears all of the PWM devices
	 */
	public void clearAllPWMDevices() {
		pwm.clearAllDevices();
	}
	
	/**
	 * Clears all of the Relay devices
	 */
	public void clearAllRelayDevices() {
		relay.clearAllDevices();
	}
	
	/**
	 * Clears all of the DIO devices
	 */
	public void clearAllDIODevices() {
		dio.clearAllDevices();
	}
	
	/**
	 * Registers an analog device in its requested channel
	 * 
	 * @param d Device to add to the module
	 */
	public void registerAnalogDevice( AnalogDevice d ) {
		analog.registerDevice( d );
	}
	
	/**
	 * Registers a PWM device in its requested channel
	 * 
	 * @param d Device to add to the module
	 */
	public void registerPWMDevice( PWMDevice d ) {
		pwm.registerDevice( d );
	}
	
	/**
	 * Registers a relay device in its requested channel
	 * 
	 * @param d Device to add to the module
	 */
	public void registerRelayDevice( RelayDevice d ) {
		relay.registerDevice( d );
	}
	
	/**
	 * Registers a DIO device in its requested channel
	 * 
	 * @param d Device to add to the module
	 */
	public void registerDIODevice( DIODevice d ) {
		dio.registerDevice( d );
	}
	
	/**
	 * @param channel Device channel
	 * @return The analog device in this channel
	 */
	public AnalogDevice getAnalogDevice( int channel ) {
		Device d = analog.getDevice( channel );
		return (AnalogDevice) ( d instanceof AnalogDevice ? d : null );
	}
	
	/**
	 * @param channel Device channel
	 * @return The PWM device in this channel
	 */
	public PWMDevice getPWMDevice( int channel ) {
		Device d = pwm.getDevice( channel );
		return (PWMDevice) ( d instanceof PWMDevice ? d : null );
	}
	
	/**
	 * @param channel Device channel
	 * @return The relay device in this channel
	 */
	public RelayDevice getRelayDevice( int channel ) {
		Device d = relay.getDevice( channel );
		return (RelayDevice) ( d instanceof RelayDevice ? d : null );
	}
	
	/**
	 * @param channel Device channel
	 * @return The DIO device in this channel
	 */
	public DIODevice getDIODevice( int channel ) {
		Device d = dio.getDevice( channel );
		return (DIODevice) ( d instanceof DIODevice ? d : null );
	}
}
