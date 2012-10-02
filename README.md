Colonnade
=========

Colonnade is simple Java ORM implementation for HBase based on DAO pattern using Java annotations for mapping directives.

Dependencies
============

Colonnade is dependent on:

- flexJSON library (distributed in /lib folder) http://flexjson.sourceforge.net/
- hadoop jars
- hbase jars

Note : If you are working with eclipse I usually just add all jars from /hadoop, /hadoop/lib, /hbase and /hbase/lib folders

Simple mapping
==============

Firstly we need POJO class which will be mapped to table, e.g. User:

```
@Table(name="user")
public class User  {
	
	// This is used for ID
	@Id private String id;
	@Column(family="data") private String email;
	
	public User() {	
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}

```

To map POJO Colonnade uses annotations

- @Id - mark field to be used as row-key id (qualifier)
- @Column - mark field to be persistent
- @Table - link POJO with table named "user"

Saving
======

```
Configuration conf = HBaseConfiguration.create();
		
ColonnadeDAO<User> userDAO = new ColonnadeDAO<User>(conf, User.class);
		
// Create POJO
User user = new User();
user.setId("00000");
user.setEmail("pasalic.zaharije@gmail.com");
		
// Save POJO
dao.save(user);
```

Reading
=======

```
Configuration conf = HBaseConfiguration.create();
		
ColonnadeDAO<User> userDAO = new ColonnadeDAO<User>(conf, User.class);

User user = dao.read("00000".getBytes());
System.out.println(user.getEmail());
		
```

Column Families
===============

Each getter/setter must belong to one column family. Using "Column" annotation you can manage multiple column families:


```
	@Column(family="data") private String email;
	@Column(family="secure") private String passwordHash;
```

This will map "data:email" to email and "secure:passwordHash" to passwordHash.


Serializers
===========

By default all simple types can be serialized without any problems. But sometimes it's good to serialize complex structure. This can be
easily achieved using serializers. Currently Colonnade implements only JSONSerializer but it's easy to extend any format that you want to use.

Let assume that we want to add list of addresses  to be serialized as JSON inside User object:

```
public class Address {
	private String address;
	private String city;
	private String postalCode;

	public Address() {
	}
	public Address(String address, String city, String postalCode) {
		this.address = address;
		this.city = city;
		this.postalCode = postalCode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
}



@Table(name="user")
public class User  {
	
	// This is used for ID
	@Id private String id;
	@Column(family="data") private String email;
	@Column(family="data", serializer=JSONSerializer.class) private ArrayList<Address> addresses;
	
	public User() {	
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public ArrayList<Address> getAddresses() {
		return addresses;
	}
	public void setAddresses(ArrayList<Address> addresses) {
		this.addresses = addresses;
	}
	
}

```


With this 'addresses' will be saved as JSON.

You can easily extend any serializer, jsut need to implement ColonnadeSerializer interface

```
public interface ColonnadeSerializer {
	byte[] serialize(Object object);
	Object deserialize(Class<?> klass, byte[] data);
}
```

