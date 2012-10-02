package org.colonnade.mapper;

import java.lang.reflect.Field;

import org.colonnade.serializer.ColonnadeSerializer;

public class ColonnadeColumnEntry {
	private String family;
	private ColonnadeSerializer serializer;
	private Field field;
	
	public ColonnadeColumnEntry(String family, ColonnadeSerializer serializer, Field field) {
		super();
		this.family = family;
		this.serializer = serializer;
		this.field = field;
	}
	public String getFamily() {
		return family;
	}
	public void setFamily(String family) {
		this.family = family;
	}
	public ColonnadeSerializer getSerializer() {
		return serializer;
	}
	public void setSerializer(ColonnadeSerializer serializer) {
		this.serializer = serializer;
	}
	public Field getField() {
		return field;
	}
	public void setField(Field field) {
		this.field = field;
	}
	
}
