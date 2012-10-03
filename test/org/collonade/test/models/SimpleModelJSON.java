package org.collonade.test.models;

import org.colonnade.annotations.Column;
import org.colonnade.annotations.Id;
import org.colonnade.annotations.Table;
import org.colonnade.serializer.JSONSerializer;


@Table(name="test_table")
public class SimpleModelJSON {
	// This is used for ID
	@Id private String id;
	@Column(family="data1", serializer=JSONSerializer.class) private JSONModel jsonModel;
	
	public SimpleModelJSON() {
	}
	public SimpleModelJSON(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public JSONModel getJsonModel() {
		return jsonModel;
	}
	public void setJsonModel(JSONModel jsonModel) {
		this.jsonModel = jsonModel;
	}
	
	
}
