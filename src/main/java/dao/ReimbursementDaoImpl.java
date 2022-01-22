package dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import model.Reimbursement;
import utils.HibernateUtil;

public class ReimbursementDaoImpl implements ReimbursementDao {
	
	static {
		try {
			Class.forName("org.postgresql.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Static block has failed me");
		}
	}

	@Override
	public void add(Reimbursement r) {
		Transaction tx = null;

		try(Session s = HibernateUtil.getSession()) {
			tx = s.beginTransaction();
			s.save(r);
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
		}
	}

	@Override
	public void update(Reimbursement r) {
		Transaction tx = null;

		try(Session s = HibernateUtil.getSession()) {
			tx = s.beginTransaction();
			s.update(r);
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
		}
	}

	@Override
	public void delete(Reimbursement r) {
		Transaction tx = null;

		try(Session s = HibernateUtil.getSession()) {
			tx = s.beginTransaction();
			s.delete(r);
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
		}
	}

	@Override
	public Reimbursement getById(int id) {
		Transaction tx = null;
		Reimbursement result = null;
		
		try(Session s = HibernateUtil.getSession()) {
			tx = s.beginTransaction();
			result = s.byId(Reimbursement.class).load(id);
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
		}
		return result;
	}

	@Override
	public List<Reimbursement> getByAuthorId(int id) {
		Transaction tx = null;
		List<Reimbursement> results = null;
		
		try(Session s = HibernateUtil.getSession()) {
			tx = s.beginTransaction();
			results = s.createQuery(getQuery(s, id, "author")).getResultList();
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
		}
		return results;
	}
	
	@Override
	public List<Reimbursement> getByResolverId(int id) {
		Transaction tx = null;
		List<Reimbursement> results = null;
		
		try(Session s = HibernateUtil.getSession()) {
			tx = s.beginTransaction();
			results = s.createQuery(getQuery(s, id, "resolver")).getResultList();
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
		}
		return results;
	}
	
	private CriteriaQuery<Reimbursement> getQuery(Session s, int id, String column) {
		CriteriaBuilder cb = s.getCriteriaBuilder();
		CriteriaQuery<Reimbursement> cq = cb.createQuery(Reimbursement.class);
		Root<Reimbursement> root = cq.from(Reimbursement.class);
		cq.select(root).where(cb.equal(root.get(column), id));
		return cq;
	}

	@Override
	public List<Reimbursement> getAllReimbursements() {
		Transaction tx = null;
		List<Reimbursement> expenses = null;
		
		try(Session s = HibernateUtil.getSession()) {
			tx = s.beginTransaction();
			expenses = s.createQuery("FROM reimbursements", Reimbursement.class).getResultList();
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
		}
		return expenses;
	}
}
