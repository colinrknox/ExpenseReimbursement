package controller;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.Reimbursement;
import model.ReimbursementStatus;
import model.ReimbursementType;
import model.User;
import service.ReimbursementService;
import service.ReimbursementServiceImpl;
import service.UserService;
import service.UserServiceImpl;
import utils.JsonUtil;

public class DataController {
	
	static UserService userService = new UserServiceImpl();
	static ReimbursementService expenseService = new ReimbursementServiceImpl();
	
	public static void getAllExpenses(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (req.getMethod().equals("POST"))
			return;
		User u = (User)req.getSession().getAttribute("account");
		if (u == null)
			return;
		
		// Get the expenses based on user type
		List<Reimbursement> expenses = new LinkedList<>();
		if (u.getRole().getId() == 1) { // employee
			expenses = expenseService.getByAuthorId(u.getId());
		} else { // finance manager
			expenses = expenseService.getAll();
		}
		
		// Send back expenses
		resp.setContentType("application/json");
		resp.getWriter().write(new ObjectMapper().writeValueAsString(expenses));
	}
	
	public static void getExpenseDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(!req.getMethod().equals("POST")) {
			req.getRequestDispatcher("/api/login").forward(req, resp);
			return;
		}

		// Parse user input from login page
		Map<String, Object> map = JsonUtil.getRequestJsonBodyAsMap(req);
		int expenseId = Integer.parseInt((String)map.get("id"));
				
		Reimbursement reimb = null;
		try {
			reimb = expenseService.getById(expenseId);
		} catch (NoResultException e) {
			
		}
		
		resp.setContentType("application/json");
		resp.getWriter().write(new ObjectMapper().writeValueAsString(reimb));
	}
	
	public static void addExpense(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(!req.getMethod().equals("POST")) {
			req.getRequestDispatcher("/api/login").forward(req, resp);
			return;
		}
		
		Map<String, Object> reqBody = JsonUtil.getRequestJsonBodyAsMap(req);
		System.out.println("ADDING NEW EXPENSE");
		System.out.println(reqBody.toString());
		Reimbursement r = new Reimbursement();
		r.setAmount(Float.parseFloat((String) reqBody.get("amount")));
		r.setAuthor((User)req.getSession().getAttribute("account"));
		r.setDescription((String)reqBody.get("description"));
		Date d = null;
		
		d = new Date((Long)reqBody.get("submitted"));
		
		r.setSubmitted(d);
		r.setStatus(new ReimbursementStatus(1, null));
		r.setType(new ReimbursementType(Integer.parseInt((String) reqBody.get("type")), null));
		
		r.setResolver(null);
		r.setReceipt(null);
		r.setResolved(null);
				
		expenseService.addReimbursement(r);
		
		resp.getWriter().write(new ObjectMapper().writeValueAsString(r));
	}
	
	public static void updateExpenseStatus(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(!req.getMethod().equals("POST")) {
			req.getRequestDispatcher("/api/login").forward(req, resp);
			return;
		}
		
		Reimbursement r = new ObjectMapper().readValue(req.getInputStream(), Reimbursement.class);
		r.setResolver((User) req.getSession().getAttribute("account"));
		expenseService.updateReimbursement(r);
		resp.getWriter().write(new ObjectMapper().writeValueAsString(r));
	}
}
