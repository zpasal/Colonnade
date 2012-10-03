package org.colonnade.mapper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NavigableMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.colonnade.annotations.Column;
import org.colonnade.annotations.Id;
import org.colonnade.annotations.Table;
import org.colonnade.annotations.Unmapped;
import org.colonnade.serializer.ColonnadeSerializer;
import org.colonnade.serializer.DefaultSerializer;


public class ColonnadeDAO<T> {
	private Class<T> reference;
	
	//private Configuration conf;
	private HTable hTable;
	
	private String tableName;

	private ColonnadeColumnEntry idField;
	private Field unmappedField;
	private Hashtable<String, ColonnadeColumnEntry> columns = new Hashtable<String, ColonnadeColumnEntry>();
	
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
				columns.put(field.getName(), new ColonnadeColumnEntry(family, serializerClass.newInstance(), field));
			}
			// or CollonadeId (this is used for rowkey qualifier)
			else if (field.isAnnotationPresent(Id.class)) {
				idField = new ColonnadeColumnEntry("", new DefaultSerializer(), field);
			}
			else if (field.isAnnotationPresent(Unmapped.class)) {
				unmappedField = field;
			}
		}
		
		this.hTable = new HTable(conf, tableName);
	}
	
	
	public void create(T modelObject) throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		Put put = new Put(BeanUtils.getProperty(modelObject, idField.getField().getName()).getBytes());
		
		Iterator<ColonnadeColumnEntry> iter = columns.values().iterator();
		while(iter.hasNext()) {
			ColonnadeColumnEntry columnEntry = iter.next();
			
			ColonnadeSerializer serializer = columnEntry.getSerializer();
			Object objectVal = PropertyUtils.getProperty(modelObject, columnEntry.getField().getName());
			if (objectVal != null) {
				byte[] value = serializer.serialize(objectVal);
				put.add(columnEntry.getFamily().getBytes(), columnEntry.getField().getName().getBytes(), value);
			}
		}
		
		// if unmappedField is present, add them to Put also
		if (unmappedField != null) {
			UnmappedColumns unmapped = (UnmappedColumns)PropertyUtils.getProperty(modelObject, unmappedField.getName());
			
			if (unmapped != null) {
				Iterator<UnmappedColumn> unmappedIterator = unmapped.iterator();
				while(unmappedIterator.hasNext()) {
					UnmappedColumn unmappedColumn = unmappedIterator.next();
					
					put.add(unmappedColumn.getFamily(), unmappedColumn.getColumn(), unmappedColumn.getValue());
				}
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
		
		// write ID
		BeanUtils.setProperty(modelObject, idField.getField().getName(), new String(id));		
		
		Iterator<Entry<byte[], NavigableMap<byte[], byte[]>>> familyIter = result.getNoVersionMap().entrySet().iterator();
		
		while(familyIter.hasNext()) {
			Entry<byte[], NavigableMap<byte[], byte[]>> familyEntry = familyIter.next();
			
			byte[] family = familyEntry.getKey();
			
			Iterator<Entry<byte[], byte[]>> columnIter = familyEntry.getValue().entrySet().iterator();
			
			UnmappedColumns unmappedColumns = null;
			
			if (unmappedField != null) {
				unmappedColumns = new UnmappedColumns();
			}
			
			while(columnIter.hasNext()) {
				Entry<byte[], byte[]> columnEntry = columnIter.next();
				
				byte[] column = columnEntry.getKey();
				byte[] value = columnEntry.getValue();
				
				ColonnadeColumnEntry colonadeEntry = columns.get(new String(column));
				
				if (colonadeEntry != null) {
					ColonnadeSerializer serializer = colonadeEntry.getSerializer();
					Object serializedValue = serializer.deserialize(colonadeEntry.getField().getType(), value);
					PropertyUtils.setProperty(modelObject, colonadeEntry.getField().getName(), serializedValue);
					
				}
				else if (unmappedField != null) {
					unmappedColumns.add(family, column, value);
				}
			}
			
			if (unmappedField != null) {
				PropertyUtils.setProperty(modelObject, unmappedField.getName(), unmappedColumns);
			}
			
		}
		
		return modelObject;
	}
	
	public void close() throws IOException {
		hTable.close();
	}
	
	
}
