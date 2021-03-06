package de.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Customer")
public class Customer {
	
	@XmlElement(name = "id_customer", required = true)
    public int id_customer;
	@XmlElement(name = "name", required = true)
    public String name;
	@XmlElement(name = "email", required = true)
    public String email;
	@XmlElement(name = "birthday", required = true)
    public String birthday;
	@XmlElement(name = "address", required = true)
    public String address;
	@XmlElement(name = "taxNum", required = true)
    public String tax_num;
	@XmlElement(name = "id_register", required = true)
    public int id_register;
	
    public Customer() {}

	public Customer(int id_customer, String name, String email, String birthday, String address, String tax_num, int id_register) {
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
