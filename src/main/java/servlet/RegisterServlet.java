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

@WebServlet("/signup")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("confirmPassword");

		if (username == null || username.trim().isEmpty()) {
			request.setAttribute("errorMessage", "Username is required.");
			request.getRequestDispatcher("WEB-INF/views/register.jsp").forward(request, response);
			return;
		}

		if (email == null || email.trim().isEmpty()) {
			request.setAttribute("errorMessage", "Email is required.");
			request.getRequestDispatcher("WEB-INF/views/register.jsp").forward(request, response);
			return;
		}

		if (password == null || password.trim().isEmpty()) {
			request.setAttribute("errorMessage", "Password is required.");
			request.getRequestDispatcher("WEB-INF/views/register.jsp").forward(request, response);
			return;
		}

		if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
			request.setAttribute("errorMessage", "Confirm Password is required.");
			request.getRequestDispatcher("WEB-INF/views/register.jsp").forward(request, response);
			return;
		}

		if (!password.equals(confirmPassword)) {
			request.setAttribute("errorMessage", "Passwords do not match.");
			request.getRequestDispatcher("WEB-INF/views/register.jsp").forward(request, response);
			return;
		}

		boolean usernameExists = false;
		try {
			usernameExists = AuthUtil.checkUsernameExists(username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (usernameExists) {
			request.setAttribute("errorMessage", "Username already exists.");
			request.getRequestDispatcher("WEB-INF/views/register.jsp").forward(request, response);
			return;
		}

		boolean emailExists = false;
		try {
			emailExists = AuthUtil.checkEmailExists(email);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (emailExists) {
			request.setAttribute("errorMessage", "Email already exists.");
			request.getRequestDispatcher("WEB-INF/views/register.jsp").forward(request, response);
			return;
		}

		try {
			long generatedId = AuthUtil.createUser(username, password, email);

			User user = new User(generatedId, username, email);

			HttpSession session = request.getSession();
			session.setAttribute("user", user);

			response.sendRedirect(request.getContextPath() + "/home");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("WEB-INF/views/register.jsp").forward(request, response);
	}
}
