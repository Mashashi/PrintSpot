package de.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NewCustomer")
public class NewCustomer {
	
	@XmlElement(name = "name", required = true)
    public String name;
	@XmlElement(name = "email", required = true)
    public String email;
	@XmlElement(name = "birthday")
    public String birthday;
	@XmlElement(name = "address", required = true)
    public String address;
	@XmlElement(name = "taxNum", required = true)
    public String tax_num;
	
    public NewCustomer() {}

	public NewCustomer(String name, String email, String birthday, String address, String tax_num) {
		super();
		this.name = name;
		this.email = email;
		this.birthday = birthday;
		this.address = address;
		this.tax_num = tax_num;
	}
	
}
