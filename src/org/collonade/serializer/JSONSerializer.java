package org.collonade.serializer;


import flexjson.JSONDeserializer;

public class JSONSerializer implements CollonadeSerializer {

	@Override
	public byte[] serialize(Object object) {
		return new flexjson.JSONSerializer().serialize(object).getBytes();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object deserialize(Class<?> klass, byte[] data) {
		return new JSONDeserializer().deserialize(new String(data), klass);	
	}

}
