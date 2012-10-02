package org.collonade.serializer;

import org.apache.commons.beanutils.ConvertUtils;

public class DefaultSerializer implements CollonadeSerializer {

	public byte[] serialize(Object object) {
		return ConvertUtils.convert(object).getBytes();
	}

	@Override
	public Object deserialize(Class<?> klass, byte[] data) {
		return ConvertUtils.convert(new String(data), klass);
	}

}
