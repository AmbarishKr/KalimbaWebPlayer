package com.witl.kalimba.webplayer;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class TransactionDao {
	@Autowired
	private SessionFactory sessionFactory;
	
	public Transaction getById(String id)
	{
		return (Transaction) sessionFactory.getCurrentSession().get(Transaction.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Transaction> searchEmailid(String emailid)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Transaction.class);
		criteria.add(Restrictions.ilike("emailid", emailid+"%"));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Transaction> getAllUsers()
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Transaction.class);
		return criteria.list();
	}
	
	public  Serializable save(Transaction Transaction)
	{
		return (Serializable)sessionFactory.getCurrentSession().save(Transaction);
	}
	
	public void update(Transaction Transaction)
	{
		sessionFactory.getCurrentSession().merge(Transaction);
	}
	
	public void delete(String id)
	{
		Transaction e = getById(id);
		sessionFactory.getCurrentSession().delete(e);
	}

	
	
}
