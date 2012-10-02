package org.collonade.serializer;

public interface CollonadeSerializer {
	byte[] serialize(Object object);
	Object deserialize(Class<?> klass, byte[] data);
}
