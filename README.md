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

Using simple mapping is straight forward using CollonadeDAO class. 

Firstly we need POJO class which will be mapped to table, r.g. User:

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

