package org.collonade.examples;

import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.collonade.examples.model.Address;
import org.collonade.examples.model.User;
import org.collonade.mapper.CollonadeDAO;

/*
 * Simple example of mapping few columns from table to User POJO
 * 
 * */
public class Example1 {
	
	public static void main(String[] args) throws Exception {
		Configuration conf = HBaseConfiguration.create();
		
		CollonadeDAO<User> dao = new CollonadeDAO<User>(conf, User.class);
		
		// Create POJO
		User user1 = new User();
		user1.setId("00000");
		user1.setBirthYear(1982);
		user1.setEmail("pasalic.zaharije@gmail.com");
		user1.setFirstname("Zaharije");
		user1.setLastname("Pasalic");
		user1.setPassword("pwd123");
		
		ArrayList<Address> addresses = new ArrayList<Address>();
		user1.setAddresses(addresses);
		user1.getAddresses().add(new Address("Nowhere 19", "Smalltown", "755477-3"));
		user1.getAddresses().add(new Address("Noland 12/3", "Landolia", "889098"));
		
		// Save POJO
		dao.save(user1);
		
		// Try reading same row
		User user2 = dao.read("00000".getBytes());
		System.out.println(user2.getFirstname() + " " + user2.getLastname());
		System.out.println(user2.getAddresses().get(0).getCity());
		
		// Close
		dao.close();
	}

}
