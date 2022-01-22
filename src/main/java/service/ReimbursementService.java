package service;

import java.util.List;

import model.Reimbursement;

public interface ReimbursementService {
	public void addReimbursement(Reimbursement r);
	public void updateReimbursement(Reimbursement r);
	public void deleteReimbursement(Reimbursement r);
	public Reimbursement getById(int id);
	public List<Reimbursement> getByAuthorId(int id);
	public List<Reimbursement> getByResolverId(int id);
	public List<Reimbursement> getAll();
}
