package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebFilter(urlPatterns = {"/home.html", "/api/*", "/logout"})
public class SessionFilter implements Filter {

	/***
	 * Deny access to all functionality (except login page, login controller, and static resources) if the person isn't signed in
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		if (req.getSession(false) == null || req.getSession().getAttribute("account") == null) {
			resp.sendRedirect("http://" + req.getServerName() + ":" + req.getServerPort() + "/ExpenseReimbursement/index.html");
			return;
		}

		chain.doFilter(request, response);
	}
}
