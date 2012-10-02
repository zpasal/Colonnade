package org.colonnade.serializer;

public interface ColonnadeSerializer {
	byte[] serialize(Object object);
	Object deserialize(Class<?> klass, byte[] data);
}
