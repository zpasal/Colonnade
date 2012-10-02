package org.collonade.mapper;

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
import org.collonade.annotations.*;
import org.collonade.serializer.CollonadeSerializer;
import org.collonade.serializer.DefaultSerializer;


public class CollonadeDAO<T> {
	private Class<T> reference;
	
	//private Configuration conf;
	private HTable hTable;
	
	private String tableName;

	private CollonadeColumnEntry idField;
	private ArrayList<CollonadeColumnEntry> columns = new ArrayList<CollonadeColumnEntry>();

	
	public CollonadeDAO(Configuration conf, Class<T> reference) throws IOException, InstantiationException, IllegalAccessException {
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
				Class<? extends CollonadeSerializer> serializerClass = field.getAnnotation(Column.class).serializer();
				columns.add(new CollonadeColumnEntry(family, serializerClass.newInstance(), field));
			}
			// or CollonadeId (this is used for rowkey qualifier)
			else if (field.isAnnotationPresent(Id.class)) {
				idField = new CollonadeColumnEntry("", new DefaultSerializer(), field);
			}
		}
		
		this.hTable = new HTable(conf, tableName);
	}
	
	
	public void save(T modelObject) throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		Put put = new Put(BeanUtils.getProperty(modelObject, idField.getField().getName()).getBytes());
		
		Iterator<CollonadeColumnEntry> iter = columns.iterator();
		while(iter.hasNext()) {
			CollonadeColumnEntry columnEntry = iter.next();
			
			
			CollonadeSerializer serializer = columnEntry.getSerializer();
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
		
		Iterator<CollonadeColumnEntry> iter = columns.iterator();
		while(iter.hasNext()) {
			CollonadeColumnEntry columnEntry = iter.next();

			KeyValue kv = result.getColumnLatest(columnEntry.getFamily().getBytes(), columnEntry.getField().getName().getBytes());
			if (kv != null) {
				
				CollonadeSerializer serializer = columnEntry.getSerializer();
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
