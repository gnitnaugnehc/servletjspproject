package servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entity.User;
import util.AuthUtil;

@WebServlet("/signin")
public class LogInServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String usernameOrEmail = request.getParameter("usernameOrEmail");
		String password = request.getParameter("password");

		if (usernameOrEmail == null || usernameOrEmail.trim().isEmpty()) {
			request.setAttribute("errorMessage", "Username or email is required.");
			request.getRequestDispatcher("WEB-INF/views/login.jsp").forward(request, response);
			return;
		}

		if (password == null || password.trim().isEmpty()) {
			request.setAttribute("errorMessage", "Password is required.");
			request.getRequestDispatcher("WEB-INF/views/login.jsp").forward(request, response);
			return;
		}

		User user = null;
		if (usernameOrEmail.contains("@")) {
			try {
				user = AuthUtil.authenticateUserByEmail(usernameOrEmail, password);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				user = AuthUtil.authenticateUserByUsername(usernameOrEmail, password);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (user != null) {
			HttpSession session = request.getSession();
			session.setAttribute("user", user);

			response.sendRedirect(request.getContextPath() + "/home");
		} else {
			request.setAttribute("errorMessage", "Invalid username/email or password.");
			request.getRequestDispatcher("WEB-INF/views/login.jsp").forward(request, response);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("WEB-INF/views/login.jsp").forward(request, response);
	}
}
