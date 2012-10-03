package org.collonade.test;

import junit.framework.TestCase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.collonade.test.models.JSONModel;
import org.collonade.test.models.SimpleModel;
import org.collonade.test.models.SimpleModelJSON;
import org.collonade.test.models.SimpleModelWithCF;
import org.colonnade.mapper.ColonnadeDAO;

public class SimpleTests  extends TestCase {
	private Configuration conf;
	private HBaseAdmin admin;
	
	public SimpleTests(String name) {
		super(name);
		
		conf = HBaseConfiguration.create();
		try {
			admin = new HBaseAdmin(conf);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	
	public void testSimplAccess() throws Exception {
		ColonnadeDAO<SimpleModel> dao = new ColonnadeDAO<SimpleModel>(conf, SimpleModel.class);
		
		SimpleModel model = new SimpleModel("0", "pasalic.zaharije@gmail.com", 3.141592653589793, 1982);
		dao.create(model);
	
		SimpleModel readModel = dao.read("0".getBytes());
		
		assertEquals(model.getId(), readModel.getId());
		assertEquals(model.getEmail(), readModel.getEmail());
		assertEquals(model.getIncome(), readModel.getIncome());
		assertEquals(model.getBirthYear(), readModel.getBirthYear());
	}
	
	public void testColumnFamilyAnnotations() throws Exception {
		ColonnadeDAO<SimpleModelWithCF> dao = new ColonnadeDAO<SimpleModelWithCF>(conf, SimpleModelWithCF.class);
		
		SimpleModelWithCF model = new SimpleModelWithCF("1", "zaharije", "pasalic");
		dao.create(model);
	
		SimpleModelWithCF readModel = dao.read("1".getBytes());
		
		assertEquals(model.getId(), readModel.getId());
		assertEquals(model.getFirstname(), readModel.getFirstname());
		assertEquals(model.getLastname(), readModel.getLastname());
	}
	
	public void testJSONSerialization() throws Exception {
		ColonnadeDAO<SimpleModelJSON> dao = new ColonnadeDAO<SimpleModelJSON>(conf, SimpleModelJSON.class);
		
		SimpleModelJSON model = new SimpleModelJSON("2");
		model.setJsonModel(JSONModel.build(3));
		dao.create(model);

		SimpleModelJSON readModel = dao.read("2".getBytes());
		
		// lvl0 asserts
		
		assertEquals(model.getId(), readModel.getId());
		assertEquals(model.getJsonModel().getString(), 		readModel.getJsonModel().getString());
		assertEquals(model.getJsonModel().getNum(), 		readModel.getJsonModel().getNum());
		assertEquals(model.getJsonModel().getInteger(), 	readModel.getJsonModel().getInteger());
		assertEquals(model.getJsonModel().getArrayList(), 	readModel.getJsonModel().getArrayList());
		assertEquals(model.getJsonModel().getHashMap(), 	readModel.getJsonModel().getHashMap());
		// lvl1
		assertEquals(model.getJsonModel().getModel().getString(), 	readModel.getJsonModel().getModel().getString());
		assertEquals(model.getJsonModel().getModel().getNum(), 		readModel.getJsonModel().getModel().getNum());
		assertEquals(model.getJsonModel().getModel().getInteger(), 	readModel.getJsonModel().getModel().getInteger());
		assertEquals(model.getJsonModel().getModel().getArrayList(),readModel.getJsonModel().getModel().getArrayList());
		assertEquals(model.getJsonModel().getModel().getHashMap(), 	readModel.getJsonModel().getModel().getHashMap());
		// lvl2
		assertEquals(model.getJsonModel().getModel().getModel().getString(), 	readModel.getJsonModel().getModel().getModel().getString());
		assertEquals(model.getJsonModel().getModel().getModel().getNum(), 		readModel.getJsonModel().getModel().getModel().getNum());
		assertEquals(model.getJsonModel().getModel().getModel().getInteger(), 	readModel.getJsonModel().getModel().getModel().getInteger());
		assertEquals(model.getJsonModel().getModel().getModel().getArrayList(), readModel.getJsonModel().getModel().getModel().getArrayList());
		assertEquals(model.getJsonModel().getModel().getModel().getHashMap(), 	readModel.getJsonModel().getModel().getModel().getHashMap());
	}
		
	
	protected void setUp() {
		System.out.println("Creating tables ...");
		try {
			if (!admin.tableExists("test_table")) {
				HTableDescriptor tableDescriptor = new HTableDescriptor("test_table");
				HColumnDescriptor data1CF = new HColumnDescriptor("data1");
				HColumnDescriptor data2CF = new HColumnDescriptor("data2");
				tableDescriptor.addFamily(data1CF);		
				tableDescriptor.addFamily(data2CF);
				admin.createTable(tableDescriptor);
			}
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
		
		System.out.println("Done.");
	}	

	protected void tearDown() {
		System.out.println("Deleting tables ...");
		try {
			admin.disableTable("test_table");
			admin.deleteTable("test_table");
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
		System.out.println("Done.");
	}
	
}
