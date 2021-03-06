package de.database.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Customer implements Serializable{
	
	private static final long serialVersionUID = -2515008314339222645L;
	@Id @Basic public Integer id_customer;
	@Basic public String name;
	@Basic public String email;
	@Transient public Date birthday;
	@Basic public String address;
	@Basic public String tax_num;
	@Basic public Integer id_register;
	
	public Customer(Integer id_customer, String name, String email, Date birthday, String address, String tax_num, Integer id_register) {
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
