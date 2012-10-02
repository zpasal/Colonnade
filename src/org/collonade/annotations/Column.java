package org.collonade.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.collonade.serializer.CollonadeSerializer;
import org.collonade.serializer.DefaultSerializer;

@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	String family();
	Class<? extends CollonadeSerializer> serializer() default DefaultSerializer.class;
}
