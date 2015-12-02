package com.witl.kalimba.webplayer.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.witl.kalimba.webplayer.common.Payment;

@Repository
@Transactional
public class PaymentDao {
@Autowired
private SessionFactory sessionFactory;
	
	public PaymentDao(){}
	
	public Payment getById(String id)
	{
		return (Payment) sessionFactory.getCurrentSession().get(Payment.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Payment> searchEmailid(String emailid)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Payment.class);
		criteria.add(Restrictions.ilike("emailid", emailid+"%"));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Payment> getAllUsers()
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Payment.class);
		return criteria.list();
	}
	
	public  Serializable save(Payment payment)
	{
		return (Serializable)sessionFactory.getCurrentSession().save(payment);
	}
	
	public void update(Payment payment)
	{
		sessionFactory.getCurrentSession().merge(payment);
	}
	
	public void delete(String id)
	{
		Payment e = getById(id);
		sessionFactory.getCurrentSession().delete(e);
	}

	
}
