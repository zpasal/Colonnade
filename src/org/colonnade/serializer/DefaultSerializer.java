package org.colonnade.serializer;

import org.apache.commons.beanutils.ConvertUtils;

public class DefaultSerializer implements ColonnadeSerializer {

	public byte[] serialize(Object object) {
		return ConvertUtils.convert(object).getBytes();
	}

	@Override
	public Object deserialize(Class<?> klass, byte[] data) {
		return ConvertUtils.convert(new String(data), klass);
	}

}
