package org.colonnade.mapper;

public class UnmappedColumn {
	private byte[] family;
	private byte[] column;
	private byte[] value;
	
	public UnmappedColumn(byte[] family, byte[] column, byte[] value) {
		super();
		this.family = family;
		this.column = column;
		this.value = value;
	}
	public byte[] getFamily() {
		return family;
	}
	public void setFamily(byte[] family) {
		this.family = family;
	}
	public byte[] getColumn() {
		return column;
	}
	public void setColumn(byte[] column) {
		this.column = column;
	}
	public byte[] getValue() {
		return value;
	}
	public void setValue(byte[] value) {
		this.value = value;
	}
	
	
}
