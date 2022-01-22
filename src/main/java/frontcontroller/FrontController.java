package frontcontroller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="FrontController", urlPatterns = {"/api/*", "/login", "/logout"})
public class FrontController extends HttpServlet {
	
	
	/**
	 * Eclipse generated serialization ID
	 */
	private static final long serialVersionUID = -5601750430576132262L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("In FrontController's doGet");
		Dispatcher.virtualRouting(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}
}
