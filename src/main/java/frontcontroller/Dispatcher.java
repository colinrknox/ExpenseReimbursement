package frontcontroller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.DataController;
import controller.LoginController;
import controller.LogoutController;

public class Dispatcher {

	public static final String API_PATH = "/ExpenseReimbursement/api";
	public static final String INDEX_PATH = "/index.html";

	public static void virtualRouting(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String uri = req.getRequestURI();
		switch (uri) {
		case "/ExpenseReimbursement/login":
			LoginController.authenticate(req, resp);
			break;
		case "/ExpenseReimbursement/logout":
			LogoutController.logout(req, resp);
			break;
		case API_PATH + "/allexpenses":
			DataController.getAllExpenses(req, resp);
			break;
		case API_PATH + "/expensedetail":
			DataController.getExpenseDetail(req, resp);
			break;
		case API_PATH + "/addexpense":
			System.out.println("ADDING EXPENSE");
			DataController.addExpense(req, resp);
			break;
		case API_PATH + "/updatestatus":
			DataController.updateExpenseStatus(req, resp);
			break;
		default:
			System.out.println("Invalid URI redirecting to login");
			resp.sendRedirect("http://" + req.getServerName() + ":" + req.getServerPort() + "/ExpenseReimbursement");
			break;
		}
	}
}
