package org.nashua.tt151.protocol;

import org.json.JSONObject;

/**
 * A message containing device information that can be sent to/from the robot
 * 
 * @author Kareem El-Faramawi
 */
public class JSONMessage implements JSONSerializable<JSONMessage> {
	
	/**
	 * Enum representing the type of message being sent
	 */
	public enum Command {
		STATUS_DEVICE( 'M' ), // Info about a device
		STATUS_ROBOT( 'Q' ), // Robot mode info
		LOG( 'L' ), // Logging a message
		UNKNOWN( 'U' ); // Unkown message type
		
		private char sh;
		
		private Command( char sh ) {
			this.sh = sh;
		}
		
		public static Command getFromShorthand( char sh ) {
			for ( Command c : Command.values() ) {
				if ( c.sh == sh ) {
					return c;
				}
			}
			return UNKNOWN;
		}
	}
	
	// Message data
	private Command command;
	private JSONObject contents;
	
	/**
	 * Creates a JSONMessage from a plain JSON string
	 * 
	 * @param raw
	 */
	public JSONMessage( String raw ) {
		JSONMessage msg = deserialize( raw );
		command = msg.getCommand();
		contents = msg.getContents();
	}
	
	/**
	 * Creates a JSONMessage with the given information
	 * 
	 * @param command The type of message this will be
	 * @param contents The contents of this message
	 */
	public JSONMessage( Command command, JSONObject contents ) {
		this.command = command;
		this.contents = contents;
	}
	
	/**
	 * @return The type of message
	 */
	public Command getCommand() {
		return command;
	}
	
	/**
	 * @return The contents of the message
	 */
	public JSONObject getContents() {
		return contents;
	}
	
	/**
	 * Converts this JSONMessage into a string that can be sent over a TCP socket
	 */
	@Override
	public String serialize() {
		JSONObject json = new JSONObject();
		json.put( JSONConstants.KEY_COMMAND, String.valueOf( command.sh ) );
		json.put( JSONConstants.KEY_CONTENT, contents );
		return json.toString();
	}
	
	/**
	 * Converts a raw JSON string into a JSONMessage
	 */
	@Override
	public JSONMessage deserialize( String json ) {
		JSONObject root = new JSONObject( json );
		Command command = Command.getFromShorthand( root.getString( JSONConstants.KEY_COMMAND ).charAt( 0 ) );
		JSONObject contents = root.getJSONObject( JSONConstants.KEY_CONTENT );
		return new JSONMessage( command, contents );
	}
	
}
