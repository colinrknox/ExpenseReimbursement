package service;

import java.util.List;

import dao.ReimbursementDao;
import dao.ReimbursementDaoImpl;
import model.Reimbursement;

public class ReimbursementServiceImpl implements ReimbursementService {
	static ReimbursementDao dao = new ReimbursementDaoImpl();

	@Override
	public void addReimbursement(Reimbursement r) {
		dao.add(r);
	}

	@Override
	public void updateReimbursement(Reimbursement r) {
		dao.update(r);
	}

	@Override
	public void deleteReimbursement(Reimbursement r) {
		dao.delete(r);
	}

	@Override
	public Reimbursement getById(int id) {
		return dao.getById(id);
	}

	@Override
	public List<Reimbursement> getByAuthorId(int id) {
		return dao.getByAuthorId(id);
	}

	@Override
	public List<Reimbursement> getByResolverId(int id) {
		return dao.getByResolverId(id);
	}
	
	@Override
	public List<Reimbursement> getAll() {
		return dao.getAllReimbursements();
	}
}
