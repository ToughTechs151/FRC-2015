package org.nashua.tt151.module;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;

import org.nashua.tt151.device.AnalogDevice;
import org.nashua.tt151.device.BlankDevice;
import org.nashua.tt151.device.DIODevice;
import org.nashua.tt151.device.Device;
import org.nashua.tt151.device.DevicePanel;
import org.nashua.tt151.device.PWMDevice;
import org.nashua.tt151.device.RelayDevice;
import org.nashua.tt151.util.SwingUtils;

public class DeviceModule extends JPanel {
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
	
	// constant arrays of set size representing device ports
	private static DevicePanel<PWMDevice> pwm = new DevicePanel<PWMDevice>( DeviceType.PWM.getName(), 10 );
	private static DevicePanel<DIODevice> dio = new DevicePanel<DIODevice>( DeviceType.DIO.getName(), 10 );
	private static DevicePanel<RelayDevice> relay = new DevicePanel<RelayDevice>( DeviceType.RELAY.getName(), 4 );
	private static DevicePanel<AnalogDevice> analog = new DevicePanel<AnalogDevice>( DeviceType.ANALOG.getName(), 4 );
	
	public DeviceModule() {
		// Create display panel
		JPanel panel = new JPanel();
		
		// Device Panels
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
	
	public void clearAllDevices() {
		clearAllAnalogDevices();
		clearAllPWMDevices();
		clearAllDIODevices();
		clearAllRelayDevices();
	}
	
	public void clearAnalogDevice( int slot ) {
		analog.clearDevice( slot );
	}
	
	public void clearPWMDevice( int slot ) {
		pwm.clearDevice( slot );
	}
	
	public void clearRelayDevice( int slot ) {
		relay.clearDevice( slot );
	}
	
	public void clearDIODevice( int slot ) {
		dio.clearDevice( slot );
	}
	
	public void clearAllAnalogDevices() {
		analog.clearAllDevices();
	}
	
	public void clearAllPWMDevices() {
		pwm.clearAllDevices();
	}
	
	public void clearAllRelayDevices() {
		relay.clearAllDevices();
	}
	
	public void clearAllDIODevices() {
		dio.clearAllDevices();
	}
	
	public void registerAnalogDevice( AnalogDevice d ) {
		analog.registerDevice( d );
	}
	
	public void registerPWMDevice( PWMDevice d ) {
		pwm.registerDevice( d );
	}
	
	public void registerRelayDevice( RelayDevice d ) {
		relay.registerDevice( d );
	}
	
	public void registerDIODevice( DIODevice d ) {
		dio.registerDevice( d );
	}
	
	public AnalogDevice getAnalogDevice( int slot ) {
		Device d = analog.getDevice( slot );
		return (AnalogDevice) ( d instanceof AnalogDevice ? d : null );
	}
	
	public PWMDevice getPWMDevice( int slot ) {
		Device d = pwm.getDevice( slot );
		return (PWMDevice) ( d instanceof PWMDevice ? d : null );
	}
	
	public RelayDevice getRelayDevice( int slot ) {
		Device d = relay.getDevice( slot );
		return (RelayDevice) ( d instanceof RelayDevice ? d : null );
	}
	
	public DIODevice getDIODevice( int slot ) {
		Device d = dio.getDevice( slot );
		return (DIODevice) ( d instanceof DIODevice ? d : null );
	}
}
