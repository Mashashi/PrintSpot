/**
 * 
 */
package de.database.get;

import java.util.Date;

/**
 * @author Rafael
 *
 */
@SuppressWarnings("serial")
public class Employee extends Customer{
	public String rank;
	
	public Employee(int id_customer, String name, String email, Date birthday, String address, String tax_num, int id_register, String rank) {
		super(id_customer, name, email, birthday, address, tax_num, id_register);
		this.rank = rank;
	}
}
