package de;

import java.text.ParseException;

import javax.ejb.EJBException;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Queue;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.naming.InitialContext;
import javax.naming.NamingException;



import de.database.insert.Customer;
import de.dto.NewCustomer;
import de.remotes.CustomerMngmtRemote;
import de.remotes.PoliciesRemote;
import de.validation.ValidationException;

@WebService
public class UsersServices {
	
	//@WebMethod
	//@WebParam(name = "customer") Customer c
	//@WebParam(name = "customer") Param c
	//@WebParam(name = "name") String name
	
	protected CustomerMngmtRemote bean;
	
	public UsersServices() throws NamingException{
		bean = (CustomerMngmtRemote) InitialContext.doLookup("java:global/AssemblyCustomerMngmt/CustomerMngmt/CustomerMngmt");
	}
	
	@WebMethod
	public void approve(@WebParam(name = "id_employee") int idEmployee, @WebParam(name = "id_register") int id){
		bean.approve(idEmployee, id);
	}
	
	@WebMethod
	public void discard(@WebParam(name = "id_register") int id){
		bean.discard(id);
	}
	
	@WebMethod
	public de.dto.Customer get(@WebParam(name = "id_register") int id){
		
		de.dto.Customer res = null;
		try {
			
			PoliciesRemote policies = (PoliciesRemote) InitialContext.doLookup("java:global/AssemblyPolicies/Policies/Defined");
			
			de.database.get.Customer c = bean.get(id);
			if(c!=null){
				res = new de.dto.Customer(
						c.id_customer,
						c.name,
						c.email,
						policies.format(c.birthday),
						c.address,
						c.tax_num,
						c.id_register
						);
			}
		} catch (EJBException e) {
			throw new RuntimeException(e.getCause());
		} catch(NamingException|ParseException e){
			new RuntimeException(e);
		}
		return res;
	}
	@WebMethod
	public String register(@WebParam(name = "customer") NewCustomer c) throws ValidationException{
		
		int id = bean.register(new Customer(c.name, c.email, c.birthday, c.address, c.tax_num));

		try{

			ConnectionFactory cf = new com.sun.messaging.ConnectionFactory();
			Connection connection = cf.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue q = (Queue) session.createQueue("PhysicalQueueOpened");
			sendMessage("registration:"+id, session, q);
			session.close();
			connection.stop();
			connection.close();
			System.out.println("Sent registration notification");
			
		}catch(Exception e){
			System.out.println("Unable to send registration notification");
		}
		
		return id+"";
		
	}

	public static void sendMessage(String msg, Session session, Queue queue) throws JMSException{
		MessageProducer producer = (MessageProducer) session.createProducer(queue);
		TextMessage tm = session.createTextMessage();
		tm.setText(msg);
		producer.send(tm);
		producer.close();
	}
	
}
