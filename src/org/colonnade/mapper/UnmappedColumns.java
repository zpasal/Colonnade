package org.colonnade.mapper;

import java.util.ArrayList;
import java.util.Iterator;

public class UnmappedColumns {
	private ArrayList<UnmappedColumn> columns = new ArrayList<UnmappedColumn>();
	
	public UnmappedColumns() {
	}
	
	public void add(byte[] family, byte[] column, byte[] value) {
		columns.add(new UnmappedColumn(family, column, value));
	}
	
	public void add(UnmappedColumn column) {
		columns.add(column);
	}
	
	public Iterator<UnmappedColumn> iterator() {
		return columns.iterator();
	}
	
	public ArrayList<UnmappedColumn> getColumns() {
		return columns;
	}
}
