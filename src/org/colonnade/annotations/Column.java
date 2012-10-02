package org.colonnade.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.colonnade.serializer.ColonnadeSerializer;
import org.colonnade.serializer.DefaultSerializer;

@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	String family();
	Class<? extends ColonnadeSerializer> serializer() default DefaultSerializer.class;
}
