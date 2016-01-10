package org.nashua.tt151.protocol;

import org.json.JSONObject;
import org.nashua.tt151.protocol.JSONMessage.Command;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.Relay.Direction;
import edu.wpi.first.wpilibj.Relay.Value;

/**
 * A bunch of methods to construct JSONMessages out of device data
 * 
 * @author Kareem El-Faramawi
 */
public class JSONEncoder {
	
	/**
	 * A set of shorthand names for each device type to be sent to the server
	 */
	public static final class DeviceType {
		
		/**
		 * PWM device names
		 */
		public static final class PWM {
			public static final String TALON = "T";
			public static final String JAGUAR = "J";
			public static final String VICTOR = "V";
			public static final String SERVO = "S";
		}
		
		/**
		 * Digital IO device names
		 */
		public static class DIO {
			public static final String LIMIT = "L";
			public static final String ENCODER = "E";
		}
		
		/**
		 * Relay device names
		 */
		public static class RELAY {
			public static final String FORWARD = "+";
			public static final String BACKWARD = "-";
			public static final String BOTH = "B";
		}
		
		/**
		 * Analog device names
		 */
		public static class ANALOG {
			public static final String GYRO = "G";
			public static final String ACCEL = "A";
			public static final String POT = "P";
		}
	}
	
	public static final class STATUS {
		public static final String CONNECTED = "C";
		public static final String TELEOP = "T";
		public static final String AUTO = "A";
	}
	
	/**
	 * Constructs a JSONMessage using the given information
	 * 
	 * @param value Value of the device
	 * @param channel The channel the device is plugged into
	 * @param key A key used for identifying the type of device
	 * @param name The name of the device to be shown on the dash
	 * @param type The shorthand name for the exact device type
	 * @return JSONMessage containing all of the device's information
	 */
	private static JSONMessage encode( Object value, int channel, String key, String name, String type ) {
		JSONObject device = new JSONObject();
		device.put( JSONConstants.KEY_DEVICE_KEY, key );
		device.put( JSONConstants.KEY_DEVICE_CHANNEL, channel );
		device.put( JSONConstants.KEY_DEVICE_TYPE, type );
		device.put( JSONConstants.KEY_DEVICE_NAME, name );
		device.put( JSONConstants.KEY_DEVICE_VALUE, value );
		return new JSONMessage( Command.STATUS_DEVICE, device );
	}
	
	/**
	 * Constructs a JSONMessage for a PWM device
	 * 
	 * @param value Value of the device
	 * @param channel The channel the device is plugged into
	 * @param name The name of the device to be shown on the dash
	 * @param type The shorthand name for the exact device type
	 * @return JSONMessage containing all of the device's information
	 */
	public static JSONMessage encodePWM( double value, int channel, String name, String type ) {
		return encode( value, channel, JSONConstants.TYPE_DEVICE_PWM, name, type );
	}
	
	/**
	 * Constructs a JSONMessage for a DIO device
	 * 
	 * @param value Value of the device
	 * @param channel The channel the device is plugged into
	 * @param name The name of the device to be shown on the dash
	 * @param type The shorthand name for the exact device type
	 * @return JSONMessage containing all of the device's information
	 */
	public static JSONMessage encodeDIO( boolean value, int channel, String name, String type ) {
		return encode( value ? 1.0 : 0.0, channel, JSONConstants.TYPE_DEVICE_DIO, name, type );
	}
	
	/**
	 * Constructs a JSONMessage for a DIO device
	 * 
	 * @param value Value of the device
	 * @param channel The channel the device is plugged into
	 * @param name The name of the device to be shown on the dash
	 * @param type The shorthand name for the exact device type
	 * @return JSONMessage containing all of the device's information
	 */
	public static JSONMessage encodeDIO( double value, int channel, String name, String type ) {
		return encode( value, channel, JSONConstants.TYPE_DEVICE_DIO, name, type );
	}
	
	/**
	 * Encodes a relay's value into a string that can be put into the JSON data
	 * 
	 * @param value Value of the relay
	 * @return Encoded value
	 */
	private static String encodeRelayValue( Value value ) {
		switch ( value ) {
			case kForward:
				return "+";
			case kOff:
				return "0";
			case kOn:
				return "1";
			case kReverse:
				return "-";
		}
		return "";
	}
	
	/**
	 * Encodes a relay's direction into a string that can be put into the JSON data
	 * 
	 * @param dir Direction of the relay
	 * @return Encoded direction
	 */
	private static String encodeRelayDirection( Direction dir ) {
		switch ( dir ) {
			case kForward:
				return "+";
			case kBoth:
				return "B";
			case kReverse:
				return "-";
		}
		return "";
	}
	
	/**
	 * Constructs a JSONMessage for a Relay
	 * 
	 * @param value Value of the relay
	 * @param channel The channel the relay is plugged into
	 * @param name The name of the relay to be shown on the dash
	 * @param dir Direction of the relay
	 * @return JSONMessage containing all of the relay's information
	 */
	public static JSONMessage encodeRelay( Value value, int channel, String name, Direction dir ) {
		return encode( encodeRelayValue( value ), channel, JSONConstants.TYPE_DEVICE_RELAY, name, encodeRelayDirection( dir ) );
	}
	
	/**
	 * Constructs a JSONMessage for an analog device
	 * 
	 * @param value Value of the device
	 * @param channel The channel the device is plugged into
	 * @param name The name of the device to be shown on the dash
	 * @param type The shorthand name for the exact device type
	 * @return JSONMessage containing all of the device's information
	 */
	public static JSONMessage encodeAnalog( double value, int channel, String name, String type ) {
		return encode( value, channel, JSONConstants.TYPE_DEVICE_ANALOG, name, type );
	}
	
	/**
	 * Constructs a JSONMessage containing the robot's current mode
	 * 
	 * @param status Current mode
	 * @return JSONMessage containing the robot status
	 */
	public static JSONMessage encodeRobotStatus( String status ) {
		JSONObject json = new JSONObject();
		json.put( JSONConstants.KEY_ROBOT_STATE, status );
		return new JSONMessage( Command.STATUS_ROBOT, json );
	}
}
