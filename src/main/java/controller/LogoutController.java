package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutController {
	public static void logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("LOGGING OUT");
		req.getSession().invalidate();
		resp.sendRedirect("http://" + req.getServerName() + ":" + req.getServerPort() + "/ExpenseReimbursement");
	}
}
