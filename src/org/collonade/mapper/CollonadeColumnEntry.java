package org.collonade.mapper;

import java.lang.reflect.Field;

import org.collonade.serializer.CollonadeSerializer;

public class CollonadeColumnEntry {
	private String family;
	private CollonadeSerializer serializer;
	private Field field;
	
	public CollonadeColumnEntry(String family, CollonadeSerializer serializer, Field field) {
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
	public CollonadeSerializer getSerializer() {
		return serializer;
	}
	public void setSerializer(CollonadeSerializer serializer) {
		this.serializer = serializer;
	}
	public Field getField() {
		return field;
	}
	public void setField(Field field) {
		this.field = field;
	}
	
}
