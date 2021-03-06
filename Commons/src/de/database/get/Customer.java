package de.database.get;

import java.io.Serializable;
import java.util.Date;

public class Customer implements Serializable{
	
	private static final long serialVersionUID = -2515008314339222645L;
	
	public int id_customer;
	public String name;
	public String email;
	public Date birthday;
	public String address;
	public String tax_num;
	public int id_register;
	
	public Customer(int id_customer, String name, String email, Date birthday, String address, String tax_num, int id_register) {
		super();
		this.id_customer = id_customer;
		this.name = name;
		this.email = email;
		this.birthday = birthday;
		this.address = address;
		this.tax_num = tax_num;
		this.id_register = id_register;
	}
	
}
