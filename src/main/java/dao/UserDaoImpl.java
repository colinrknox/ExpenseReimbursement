package dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import model.User;
import utils.HibernateUtil;

public class UserDaoImpl implements UserDao {
	
	static {
		try {
			Class.forName("org.postgresql.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Static block has failed me");
		}
	}

	@Override
	public void add(User user) {
		Transaction tx = null;
		
		try(Session s = HibernateUtil.getSession()) {
			tx = s.beginTransaction();
			s.save(user);
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
		}
	}

	@Override
	public void update(User user) {
		Transaction tx = null;
		
		try(Session s = HibernateUtil.getSession()) {
			tx = s.beginTransaction();
			s.update(user);
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
		}
	}

	@Override
	public User findById(int id) {
		Transaction tx = null;
		User result = null;
		
		try(Session s = HibernateUtil.getSession()) {
			tx = s.beginTransaction();
			result = s.byId(User.class).load(id);
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
		}
		return result;
	}
	
	@Override
	public User findByUsernamePassword(String username, String password) {
		Transaction tx = null;
		User result = null;
		
		try(Session s = HibernateUtil.getSession()) {
			tx = s.beginTransaction();
			CriteriaBuilder cb = s.getCriteriaBuilder();
			CriteriaQuery<User> cq = cb.createQuery(User.class);
			Root<User> root = cq.from(User.class);
			cq.select(root).where(cb.and(cb.equal(root.get("username"), username), cb.equal(root.get("password"), password)));
			result = s.createQuery(cq).getSingleResult();
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
		}
		return result;
	}

	@Override
	public void delete(User user) {
		Transaction tx = null;
		
		try(Session s = HibernateUtil.getSession()) {
			tx = s.beginTransaction();
			s.delete(user);
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
		}
	}

	@Override
	public List<User> findAll() {
		Transaction tx = null;
		List<User> users = null;
		
		try(Session s = HibernateUtil.getSession()) {
			tx = s.beginTransaction();
			users = s.createQuery("FROM users", User.class).getResultList();
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
		}
		return users;
	}
}
