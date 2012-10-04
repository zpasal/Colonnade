Colonnade
=========

Colonnade is simple Java ORM implementation for HBase based on DAO pattern using Java annotations for mapping directives.


Dependencies
============

Colonnade is dependent on:

- gson library (distributed in /lib folder) http://code.google.com/p/google-gson/
- junit 4.10 (distributed in /lib folder)
- hadoop jars
- hbase jars

Note : If you are working with eclipse I usually just add all jars from /hadoop, /hadoop/lib, /hbase and /hbase/lib folders

Simple mapping
==============

Firstly we need POJO class which will be mapped to table. POJO must have defined default constructor (Constructor without arguments):

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
dao.create(user);
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

Dynamic schema
==============

Sometimes data model is too dynamic and you cannot map data directly to predefined fields. What collonade does is to collect all non-mapped values from hbase and put them in UnmappedColumns collection. 

@Unmapped annotation is used to enable unmapped column read/write:

UnmappedColumns is collection of UnmappedColumn class which is triplet (family, column, value).

```
@Table(name="model")
class Model {
	@id private String id;
	@Column(family="data") private String firstname;
	
	@Unmapped private UnmappedColumns otherColumns;
	
	// generate getters and setters
}

Configuration conf = HBaseConfiguration.create();
		
ColonnadeDAO<Model> dao = new ColonnadeDAO<Model>(conf, Model.class);

// Create dynamic columns
UnmappedColumns otherColumns = new UnmappedColumns();
otherColumns.add("data".getBytes(), "lastname".getBytes(), "pasalic");
		
// Create POJO
Model model = new Model();
user.setId("0");
user.setFirstname("zaharije");
user.setOtherColumns(otherColumns);

dao.create(model);
```
  
  
Unmapped columns will nto be read if there is no annotation @Unmapped present.

Changes
=======

0.4.1
- Access to dynamic columns (unmapped columns)
- Changed flexjson to gson
- Added simple unit tests

0.3.3
- Added custom serializers
- Added JSON serializer using flexjson lib

0.1.2
- Simple mapping with annotations

License
-------

Colonnade is under MIT Licence:

Copyright (C) 2012 Pasalic Zaharije

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
