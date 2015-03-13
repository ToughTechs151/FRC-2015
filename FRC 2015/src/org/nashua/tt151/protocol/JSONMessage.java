package org.nashua.tt151.protocol;

import org.json.JSONObject;

public class JSONMessage implements JSONSerializable<JSONMessage> {
	public enum Command {
		STATUS_DEVICE( 'M' ),
		STATUS_ROBOT( 'Q' ),
		LOG( 'L' ),
		UNKNOWN( 'U' );
		
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
	
	private Command command;
	private JSONObject contents;
	
	public JSONMessage( String raw ) {
		JSONMessage msg = deserialize( raw );
		command = msg.getCommand();
		contents = msg.getContents();
	}
	
	public JSONMessage( Command command, JSONObject contents ) {
		this.command = command;
		this.contents = contents;
	}
	
	public Command getCommand() {
		return command;
	}
	
	public JSONObject getContents() {
		return contents;
	}
	
	@Override
	public String serialize() {
		JSONObject json = new JSONObject();
		json.put( JSONConstants.KEY_COMMAND, String.valueOf( command.sh ) );
		json.put( JSONConstants.KEY_CONTENT, contents );
		return json.toString();
	}
	
	@Override
	public JSONMessage deserialize( String json ) {
		JSONObject root = new JSONObject( json );
		Command command = Command.getFromShorthand( root.getString( JSONConstants.KEY_COMMAND ).charAt( 0 ) );
		JSONObject contents = root.getJSONObject( JSONConstants.KEY_CONTENT );
		return new JSONMessage( command, contents );
	}
	
}
