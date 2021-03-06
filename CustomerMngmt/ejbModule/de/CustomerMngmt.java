package de;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.sql.DataSource;

import de.database.insert.Customer;
import de.remotes.CustomerMngmtRemote;
import de.remotes.PoliciesRemote;
import de.validation.ValidationException;
 
@Stateless(name="CustomerMngmt")
public class CustomerMngmt implements CustomerMngmtRemote {
    
    public CustomerMngmt(){}
    
    // Not transactional and buggy threfore
    /*public String register(Customer c){

    	String msg = "";

    	Connection conn = null;
    	PreparedStatement s = null;
    	PreparedStatement p = null;
    	ResultSet r = null;
    	try {

    		InitialContext initialContext = new InitialContext();
    		DataSource ds = (javax.sql.DataSource)initialContext.lookup("jdbc/user_list-Pool");

    		conn = ds.getConnection();
    		s = conn.prepareStatement("insert into Register(id_employee_registered_by, id_employee_approved_by) values(NULL,NULL);", Statement.RETURN_GENERATED_KEYS);
    		s.executeUpdate();
    		r = s.getGeneratedKeys();
    		if(r.next()){
    			p = conn.prepareStatement("insert into Customer(name,email,birthday,address,tax_num,id_register) values(?, ?, ?, ?, ?, ?);");
    			p.setString(1, c.name);
    			p.setString(2, c.email);
    			p.setString(3, c.birthday);
    			p.setString(4, c.address);
    			p.setString(5, c.tax_num);
    			p.setInt(6, r.getInt(1));
    			p.execute();
    			msg = "Success";
    		}else{
    			msg = "Fails";
    		}
    		
    	} catch (SQLException | NamingException e) {
    		msg = "Fails: "+e.getMessage();
    	}finally{
    		try {
    			if(p!=null) p.close();
    			if(s!=null) s.close();
    			if(conn!=null) conn.close();
    		} catch (SQLException e) {
    			// Do nothing...
    		}
    	}
    	return msg;
    }*/
    
	@Override
	public Integer register(Customer c) throws ValidationException {
		
		c.validate();
		
		Integer newId = null;

		Connection conn = null;
		PreparedStatement s = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {

			InitialContext initialContext = new InitialContext();
			DataSource ds = (javax.sql.DataSource)initialContext.lookup("jdbc/user_list-Pool");

			conn = ds.getConnection();
			conn.setAutoCommit(false);
			conn.setSavepoint();

			s = conn.prepareStatement("insert into Register(id_employee_registered_by, id_employee_approved_by) values(NULL,NULL);", Statement.RETURN_GENERATED_KEYS);
			s.executeUpdate();
			r = s.getGeneratedKeys();
			if(r.next()){
				newId = r.getInt(1);
				p = conn.prepareStatement("insert into Customer(name,email,birthday,address,tax_num,id_register) values(?, ?, ?, ?, ?, ?);");
				p.setString(1, c.name);
				p.setString(2, c.email);
				p.setString(3, c.birthday);
				p.setString(4, c.address);
				p.setString(5, c.tax_num);
				p.setInt(6, newId);
				p.execute();
				
				conn.commit();
			}else{
				throw new RuntimeException("Failed to insert register record.");
			}
			
		} catch (SQLException | NamingException e) {
			try {
				if(conn!=null && !conn.isClosed()){ conn.rollback(); }
			} catch (SQLException e1) {
				// Do nothing...
			}
			throw new RuntimeException(e);
		} finally {
			try {
				//if(conn!=null && !conn.isClosed()){ conn.setAutoCommit(true); }
				if(p!=null) p.close();
				if(s!=null) s.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				// Do nothing...
			}
		}
		return newId;
	}
	
	@Override
	public de.database.get.Customer get(Integer id) {
		
		/*{
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu1");
	        // create EntityManager
	        EntityManager em = emf.createEntityManager();
	        // Get a transaction instance and start the transaction
	        
	        // In auto commit mode transactions don't work
	        // <property name="hibernate.connection.autocommit" value="true" /> in the persistance.xml may do the trick
	        //EntityTransaction tx = em.getTransaction();
	        //tx.begin();
	        de.database.entities.Customer a = new de.database.entities.Customer(null, "1", "2", new Date(), "3", "4",106);
	        em.persist(a);
	        // Commit the transaction
	        //tx.commit();
	        em.close();
	        emf.close();
		}*/
		
		de.database.get.Customer res = null;
		
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try{
			
			PoliciesRemote policies = (PoliciesRemote) InitialContext.doLookup("java:global/AssemblyPolicies/Policies/Defined");
			
			InitialContext initialContext = new InitialContext();
			DataSource ds = (javax.sql.DataSource) initialContext.lookup("jdbc/user_list-Pool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			p = conn.prepareStatement("select c.id_customer,c.name,c.email,c.birthday,c.address,c.tax_num,c.id_register from Register r, Customer c where r.id_register=c.id_register and r.id_register = ? and r.id_employee_approved_by is null;");
			p.setInt(1, id);
			r = p.executeQuery();
			if(r.next()){
				res = new de.database.get.Customer(
						r.getInt(1), 
						r.getString(2), 
						r.getString(3), 
						policies.parseDate(r.getString(4)),
						r.getString(5),
						r.getString(6),
						r.getInt(7)
				);
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally {
			try {
				if(p!=null) p.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				// Do nothing...
			}
		}
		
		return res;
	}

	@Override
	public void discard(Integer id) {
		Connection conn = null;
		PreparedStatement p = null;
		
		try {
			InitialContext initialContext = new InitialContext();
			
			DataSource ds = (javax.sql.DataSource) initialContext.lookup("jdbc/user_list-Pool");
			conn = ds.getConnection();
			p = conn.prepareStatement("delete from Customer where id_register=?;");
			p.setInt(1, id);
			p.execute();
			p.close();
			
			p = conn.prepareStatement("delete from Register where id_register=?;");
			p.setInt(1, id);
			p.execute();
			
		} catch (NamingException | SQLException e) {
			throw new RuntimeException(e.getMessage());
		}finally {
			try {
				if(p!=null) p.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				// Do nothing...
			}
		}
	}

	@Override
	public void approve(Integer idEmployee, Integer id) {
		Connection conn = null;
		PreparedStatement p = null;
		try{
			
			InitialContext initialContext = new InitialContext();
			DataSource ds = (javax.sql.DataSource) initialContext.lookup("jdbc/user_list-Pool");
			conn = ds.getConnection();
			p = conn.prepareStatement("update Register set id_employee_approved_by=? where id_register=?;");
			p.setInt(1, idEmployee);
			p.setInt(2, id);
			p.execute();
			
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally {
			try {
				if(p!=null) p.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				// Do nothing...
			}
		}
	}
	
}