package org.nashua.tt151.protocol;

/**
 * An interface for an object that can be serialized/deserialized to/from JSON
 * 
 * @author Kareem El-Faramawi
 * 
 * @param <T> The type being (de)serialized
 */
public interface JSONSerializable<T> {
	public String serialize();
	
	public T deserialize( String json );
}
