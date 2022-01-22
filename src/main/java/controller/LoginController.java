package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.User;
import service.UserService;
import service.UserServiceImpl;
import utils.JsonUtil;

public class LoginController {

	static UserService service = new UserServiceImpl();
	
	public static void authenticate(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		// If already logged in redirect to home page
		if (req.getSession(false) != null && req.getSession().getAttribute("account") != null) {
			resp.sendRedirect("http://" + req.getServerName() + ":" + req.getServerPort() + "/ExpenseReimbursement/home.html");
			return;
		}

		// If they don't have a session and aren't submitting a POST then send them to splash page
		if(!req.getMethod().equals("POST")) {
			resp.sendRedirect("http://" + req.getServerName() + ":" + req.getServerPort() + "/ExpenseReimbursement");
			return;
		}

		Map<String, Object> map = JsonUtil.getRequestJsonBodyAsMap(req);

		String username = (String)map.get("username");
		String password = (String)map.get("password");

		// Login through service layer
		User u = null;
		try {
			u = service.login(username, password);
		} catch (NoResultException e) {
			System.out.println("Unsuccessful login attempt");
		}

		// Send info back to client after successful login
		Map<String, Object> responseJson = new HashMap<>();
		responseJson.put("currentUser", u == null ? u : u.getId());
		responseJson.put("userFirstName", u == null ? u : u.getFirstName());
		responseJson.put("userRole", u == null ? u : u.getRole().getRole());
		req.getSession().setAttribute("account", u);

		
		resp.setContentType("application/json");
		resp.getWriter().write(new ObjectMapper().writeValueAsString(responseJson));		
	}
}
