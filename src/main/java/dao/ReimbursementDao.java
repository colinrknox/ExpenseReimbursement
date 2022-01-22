package dao;

import java.util.List;

import model.Reimbursement;

public interface ReimbursementDao {
	public void add(Reimbursement r);
	public void update(Reimbursement r);
	public void delete(Reimbursement r);
	public Reimbursement getById(int id);
	public List<Reimbursement> getByAuthorId(int id);
	public List<Reimbursement> getByResolverId(int id);
	public List<Reimbursement> getAllReimbursements();
}
