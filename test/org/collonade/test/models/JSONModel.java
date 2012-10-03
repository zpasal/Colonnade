package org.collonade.test.models;

import java.util.ArrayList;
import java.util.HashMap;

public class JSONModel {
	private String string;
	private Integer integer;
	private Double num;
	private ArrayList<String> arrayList;
	private HashMap<Integer, String> hashMap;
	private JSONModel model;
	
	public JSONModel() {}
	
	public JSONModel(String string, Integer integer, Double num,
			ArrayList<String> arrayList, HashMap<Integer, String> hashMap,
			JSONModel model) {
		super();
		this.string = string;
		this.integer = integer;
		this.num = num;
		this.arrayList = arrayList;
		this.hashMap = hashMap;
		this.model = model;
	}
	
	public static JSONModel build(int levels) {
		JSONModel jsonModel = new JSONModel("zaharije", -123, -123.123, null, null, null);
		
		ArrayList<String> list = new ArrayList<String>();
		list.add("str1");
		list.add("str2");
		list.add("");
		jsonModel.setArrayList(list);
		
		HashMap<Integer, String> hMap = new HashMap<Integer, String>();
		hMap.put(-1, "minus one");
		hMap.put(0, "zero");
		hMap.put(1, "plus one");
		jsonModel.setHashMap(hMap);
		
		if (levels > 0) {
			jsonModel.setModel(JSONModel.build(levels-1));
		}
		
		return jsonModel;
	}
	
	public String getString() {
		return string;
	}
	public void setString(String string) {
		this.string = string;
	}
	public Integer getInteger() {
		return integer;
	}
	public void setInteger(Integer integer) {
		this.integer = integer;
	}
	public Double getNum() {
		return num;
	}
	public void setNum(Double num) {
		this.num = num;
	}
	public ArrayList<String> getArrayList() {
		return arrayList;
	}
	public void setArrayList(ArrayList<String> arrayList) {
		this.arrayList = arrayList;
	}
	public HashMap<Integer, String> getHashMap() {
		return hashMap;
	}
	public void setHashMap(HashMap<Integer, String> hashMap) {
		this.hashMap = hashMap;
	}
	public JSONModel getModel() {
		return model;
	}
	public void setModel(JSONModel model) {
		this.model = model;
	}
	
	
}
