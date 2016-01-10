package org.nashua.tt151.protocol;

import org.json.JSONObject;
import org.nashua.tt151.Logger;
import org.nashua.tt151.device.AnalogDevice.AnalogType;
import org.nashua.tt151.device.AnalogDevice;
import org.nashua.tt151.device.DIODevice;
import org.nashua.tt151.device.DIODevice.DIOType;
import org.nashua.tt151.device.PWMDevice;
import org.nashua.tt151.device.PWMDevice.PWMType;
import org.nashua.tt151.device.RelayDevice;
import org.nashua.tt151.device.RelayDevice.Direction;
import org.nashua.tt151.device.RelayDevice.Value;
import org.nashua.tt151.module.ConnectionModule.State;
import org.nashua.tt151.module.DeviceModule.DeviceType;

/**
 * Convenient methods for parsing the information received from the robot
 * 
 * @author Kareem El-Faramawi
 */
public class JSONParser {
	
	/**
	 * Logs a string received from the robot
	 * 
	 * @param json JSON message
	 */
	public static void processLog( JSONObject json ) {
		Logger.logLine( json.getString( JSONConstants.KEY_MSG ) );
	}
	
	/**
	 * Parses the state of the robot (teleop, auto, etc.)
	 * 
	 * @param json JSON message
	 * @return The robot's curret state
	 */
	public static State parseRobotState( JSONObject json ) {
		return State.getFromShorthand( json.getString( JSONConstants.KEY_ROBOT_STATE ).charAt( 0 ) );
	}
	
	/**
	 * Gets the DeviceType of the device described a JSON message
	 * 
	 * @param json JSON message
	 * @return The type of device contained in the message
	 */
	public static DeviceType getDeviceType( JSONObject json ) {
		String key = json.getString( JSONConstants.KEY_DEVICE_KEY );
		if ( key.equals( JSONConstants.TYPE_DEVICE_PWM ) ) {
			return DeviceType.PWM;
		} else if ( key.equals( JSONConstants.TYPE_DEVICE_DIO ) ) {
			return DeviceType.DIO;
		} else if ( key.equals( JSONConstants.TYPE_DEVICE_RELAY ) ) {
			return DeviceType.RELAY;
		} else if ( key.equals( JSONConstants.TYPE_DEVICE_ANALOG ) ) {
			return DeviceType.ANALOG;
		}
		return null;
	}
	
	/**
	 * Parses and constructs a PWMDevice from a JSON message
	 * 
	 * @param json JSON message
	 * @return Deserialized PWMDevice
	 */
	public static PWMDevice parsePWMDevice( JSONObject json ) {
		PWMType type = PWMType.getFromShorthand( json.getString( JSONConstants.KEY_DEVICE_TYPE ).charAt( 0 ) );
		String name = json.getString( JSONConstants.KEY_DEVICE_NAME );
		int channel = json.getInt( JSONConstants.KEY_DEVICE_CHANNEL );
		double value = json.getDouble( JSONConstants.KEY_DEVICE_VALUE );
		return new PWMDevice( channel, name, value, type );
	}
	
	/**
	 * Parses and constructs a DIODevice from a JSON message
	 * 
	 * @param json JSON message
	 * @return Deserialized DIODevice
	 */
	public static DIODevice parseDIODevice( JSONObject json ) {
		DIOType type = DIOType.getFromShorthand( json.getString( JSONConstants.KEY_DEVICE_TYPE ).charAt( 0 ) );
		String name = json.getString( JSONConstants.KEY_DEVICE_NAME );
		int channel = json.getInt( JSONConstants.KEY_DEVICE_CHANNEL );
		double value = json.getDouble( JSONConstants.KEY_DEVICE_VALUE );
		return new DIODevice( channel, name, value, type );
	}
	
	/**
	 * Parses and constructs a AnalogDevice from a JSON message
	 * 
	 * @param json JSON message
	 * @return Deserialized AnalogDevice
	 */
	public static AnalogDevice parseAnalogDevice( JSONObject json ) {
		AnalogType type = AnalogType.getFromShorthand( json.getString( JSONConstants.KEY_DEVICE_TYPE ).charAt( 0 ) );
		String name = json.getString( JSONConstants.KEY_DEVICE_NAME );
		int channel = json.getInt( JSONConstants.KEY_DEVICE_CHANNEL );
		double value = json.getDouble( JSONConstants.KEY_DEVICE_VALUE );
		return new AnalogDevice( channel, name, value, type );
	}
	
	/**
	 * Parses and constructs a RelayDevice from a JSON message
	 * 
	 * @param json JSON message
	 * @return Deserialized RelayDevice
	 */
	public static RelayDevice parseRelayDevice( JSONObject json ) {
		Direction direction = Direction.getFromShorthand( json.getString( JSONConstants.KEY_DEVICE_TYPE ).charAt( 0 ) );
		String name = json.getString( JSONConstants.KEY_DEVICE_NAME );
		int channel = json.getInt( JSONConstants.KEY_DEVICE_CHANNEL );
		Value value = Value.getFromShorthand( json.getString( JSONConstants.KEY_DEVICE_VALUE ).charAt( 0 ) );
		return new RelayDevice( channel, name, direction, value );
	}
}
