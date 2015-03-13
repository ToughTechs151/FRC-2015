package org.nashua.tt151.protocol;

public interface JSONSerializable<T> {
	public String serialize();
	
	public T deserialize(String json);
}
