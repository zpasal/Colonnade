package org.colonnade.serializer;


import com.google.gson.Gson;

public class JSONSerializer implements ColonnadeSerializer {

	@Override
	public byte[] serialize(Object object) {
		Gson gson = new Gson();
		return gson.toJson(object).getBytes();
	}

	@Override
	public Object deserialize(Class<?> klass, byte[] data) {
		Gson gson = new Gson();
		return gson.fromJson(new String(data), klass);
	}

}
