/**
 * 
 */
package com.madhu.demo.dao;

import java.util.List;

import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;

import com.madhu.demo.entity.Customer;
import com.madhu.demo.util.SortUtils;

/**
 * @author 15197
 * @Repository – It is used for DAO implementations.  
 * It will automatically register DAO implementation (Thanks to component scanning). 
 * In addition, Spring also provides translation of any JDBC related exceptions. 
 * @Repository and @Controller annotations are inherited from @Component.
 */
@Repository
public class CustomerDAOImpl implements CustomerDAO {

	// need to inject the session factory
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * Spring provides a special annotation @Transactional which automagically begins and end a transaction for our Hibernate code.
	 * * no need for us to explicitly do this in your code
	 * 
	 * The Spring magic happens behind the scenes.
	 * 
	 * This is in place of session.beginTransaction(); -> DO HIBERATE STUFF -> session.getTransaction(). commit()
	 * 
	 * If you are using Service class, then remove @transactional, 
	 * because we want this DAO implementation to run in the context of the transaction 
	 * that was defined by the service layer. 
	 */
	/*@Override
	//@Transactional
	public List<Customer> getCustomers() {
		// get the current Hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
				
		// create a query
		Query<Customer> theQuery = currentSession.createQuery("from Customer order by firstName", Customer.class);
		
		// execute query and get result list
		List<Customer> customers = theQuery.getResultList();
		
		// return results
		return customers;
	}*/

	@Override
	public void saveCustomer(Customer theCustomer) {
		// get the current Hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		/**
		 * in hibernate API, there are 2 ways to add data to database.
		 * save(): INSERT new record
		 * update: UPDATE existing record
		 * So, instead of save or update we will use another hibernate api called
		 * saveOrUpdate().
		 * if primary key/ id is null, then INSERT new customer
		 * else UPDATE existing customer
		 */
		//save the customer to database - for INSERT ONLY. We'll use saveOrUpdate.
		//currentSession.save(theCustomer);
		currentSession.saveOrUpdate(theCustomer);
	}

	@Override
	public Customer getCustomer(int theId) {
		// get the current Hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		// now retrieve/read from database using the primary key
		Customer theCustomer = currentSession.get(Customer.class, theId);
		
		// return the found customer
		return theCustomer;
	}

	@Override
	public void deleteCustomer(int theId) {
		// get the current Hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// delete the customer from database wrt primary key
		//currentSession.delete(theId);
		// delete object with primary key
		Query theQuery = 
				currentSession.createQuery("delete from Customer where id=:customerId");
		theQuery.setParameter("customerId", theId);
		
		theQuery.executeUpdate();
	}

	@Override
	public List<Customer> searchCustomers(String theSearchName) {
		// get the current Hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		Query theQuery = null;
		
		// only search by name if theSearchName is not empty
		if(theSearchName != null && theSearchName.trim().length() > 0)	{
			theQuery =currentSession.createQuery("from Customer where lower(firstName) like :theName or lower(lastName) like :theName", Customer.class);
			theQuery.setParameter("theName", "%" + theSearchName.toLowerCase() + "%");
		}	else	{
			// theSearchName is empty ... so just get all customers
			theQuery = currentSession.createQuery("from Customer", Customer.class);
		}
		
		// execute query and get result list
		List<Customer> customers = theQuery.getResultList();
		
		// return the results 
		return customers;
	}

	@Override
	public List<Customer> getCustomers(int theSortField) {
		// get the current Hibernate session
		Session currentSession = sessionFactory.getCurrentSession();

		// determine sort field
		String theFieldName = null;
		
		switch(theSortField)	{
		case SortUtils.FIRST_NAME:
			theFieldName = "firstName";
			break;
		case SortUtils.LAST_NAME:
			theFieldName = "lastName";
			break;
		case SortUtils.EMAIL:
			theFieldName = "email";
			break;
			default:
				// if nothing matches, the deault to sort by firstName
				theFieldName="firstName";
		}
		
		// create a query
		String queryString = "from Customer order by "+theFieldName;
		Query<Customer> theQuery = currentSession.createQuery(queryString,Customer.class);
		
		// execute query and get result list
		List<Customer> customers = theQuery.getResultList();
		return customers;
	}

}
