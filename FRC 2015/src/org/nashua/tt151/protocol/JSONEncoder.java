package org.nashua.tt151.protocol;

import org.json.JSONObject;
import org.nashua.tt151.protocol.JSONMessage.Command;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.Relay.Direction;
import edu.wpi.first.wpilibj.Relay.Value;

public class JSONEncoder {
	public static final class DeviceType {
		public static final class PWM {
			public static final String TALON = "T";
			public static final String JAGUAR = "J";
			public static final String VICTOR = "V";
			public static final String SERVO = "S";
		}
		
		public static class DIO {
			public static final String LIMIT = "L";
			public static final String ENCODER = "E";
		}
		
		public static class RELAY {
			public static final String FORWARD = "+";
			public static final String BACKWARD = "-";
			public static final String BOTH = "B";
		}
		
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
	
	private static JSONMessage encode( Object value, int channel, String key, String name, String type ) {
		JSONObject device = new JSONObject();
		device.put( JSONConstants.KEY_DEVICE_KEY, key );
		device.put( JSONConstants.KEY_DEVICE_CHANNEL, channel );
		device.put( JSONConstants.KEY_DEVICE_TYPE, type );
		device.put( JSONConstants.KEY_DEVICE_NAME, name );
		device.put( JSONConstants.KEY_DEVICE_VALUE, value );
		return new JSONMessage( Command.STATUS_DEVICE, device );
	}
	
	public static JSONMessage encodePWM( double value, int channel, String name, String type ) {
		return encode( value, channel, JSONConstants.TYPE_DEVICE_PWM, name, type );
	}
	
	public static JSONMessage encodeDIO( boolean value, int channel, String name, String type ) {
		return encode( value ? 1.0 : 0.0, channel, JSONConstants.TYPE_DEVICE_DIO, name, type );
	}
	
	public static JSONMessage encodeDIO( double value, int channel, String name, String type ) {
		return encode( value, channel, JSONConstants.TYPE_DEVICE_DIO, name, type );
	}
	
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
	
	public static JSONMessage encodeRelay( Value value, int channel, String name, Direction dir ) {
		
		return encode( encodeRelayValue( value ), channel, JSONConstants.TYPE_DEVICE_RELAY, name, encodeRelayDirection( dir ) );
	}
	
	public static JSONMessage encodeAnalog( double value, int channel, String name, String type ) {
		return encode( value, channel, JSONConstants.TYPE_DEVICE_ANALOG, name, type );
	}
	
	public static JSONMessage encodeRobotStatus(String status) {
		JSONObject json = new JSONObject();
		json.put( JSONConstants.KEY_ROBOT_STATE, status );
		return new JSONMessage(Command.STATUS_ROBOT, json);
	}
	// public static JSONMessage encodeDIO(DigitalInput)
}
