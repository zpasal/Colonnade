package org.colonnade.mapper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.colonnade.annotations.*;
import org.colonnade.serializer.ColonnadeSerializer;
import org.colonnade.serializer.DefaultSerializer;


public class ColonnadeDAO<T> {
	private Class<T> reference;
	
	//private Configuration conf;
	private HTable hTable;
	
	private String tableName;

	private ColonnadeColumnEntry idField;
	private ArrayList<ColonnadeColumnEntry> columns = new ArrayList<ColonnadeColumnEntry>();

	
	public ColonnadeDAO(Configuration conf, Class<T> reference) throws IOException, InstantiationException, IllegalAccessException {
		//this.conf = conf;
		this.reference = reference;
		
		// Get table name via annotations
		this.tableName = reference.getAnnotation(Table.class).name();
		
		// Scan all fields and check for CollonadeColumns
		Field[] fields = reference.getDeclaredFields();
		for (int i=0; i<fields.length; i++) {
			Field field = fields[i];
			
			// Field can be Column(family,serializer)
			if (field.isAnnotationPresent(Column.class)) {
				String family = field.getAnnotation(Column.class).family();
				Class<? extends ColonnadeSerializer> serializerClass = field.getAnnotation(Column.class).serializer();
				columns.add(new ColonnadeColumnEntry(family, serializerClass.newInstance(), field));
			}
			// or CollonadeId (this is used for rowkey qualifier)
			else if (field.isAnnotationPresent(Id.class)) {
				idField = new ColonnadeColumnEntry("", new DefaultSerializer(), field);
			}
		}
		
		this.hTable = new HTable(conf, tableName);
	}
	
	
	public void save(T modelObject) throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		Put put = new Put(BeanUtils.getProperty(modelObject, idField.getField().getName()).getBytes());
		
		Iterator<ColonnadeColumnEntry> iter = columns.iterator();
		while(iter.hasNext()) {
			ColonnadeColumnEntry columnEntry = iter.next();
			
			
			ColonnadeSerializer serializer = columnEntry.getSerializer();
			Object objectVal = PropertyUtils.getProperty(modelObject, columnEntry.getField().getName());
			if (objectVal != null) {
				byte[] value = serializer.serialize(objectVal);
				
				put.add(columnEntry.getFamily().getBytes(), columnEntry.getField().getName().getBytes(), value);
			}
		}
		
		hTable.put(put);
	}
	
	public T read(byte[] id) throws IOException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		Get get = new Get(id);
		Result result = hTable.get(get);

		if (result.isEmpty()) {
			return null;
		}
		
		T modelObject = reference.newInstance();
		
		Iterator<ColonnadeColumnEntry> iter = columns.iterator();
		while(iter.hasNext()) {
			ColonnadeColumnEntry columnEntry = iter.next();

			KeyValue kv = result.getColumnLatest(columnEntry.getFamily().getBytes(), columnEntry.getField().getName().getBytes());
			if (kv != null) {
				
				ColonnadeSerializer serializer = columnEntry.getSerializer();
				Object value = serializer.deserialize(columnEntry.getField().getType(), kv.getValue());
				PropertyUtils.setProperty(modelObject, columnEntry.getField().getName(), value);
			}
		}		
		
		return modelObject;
	}
	
	public void close() throws IOException {
		hTable.close();
	}
	
	
}
