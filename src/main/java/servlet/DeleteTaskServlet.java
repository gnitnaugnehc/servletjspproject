package servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.TaskDao;
import entity.User;

@WebServlet("/deletetask")
public class DeleteTaskServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		if (user == null || !user.getUsername().equals("admin")) {
			response.sendRedirect(request.getContextPath() + "/home");
			session.invalidate();
			return;
		}

		long taskId = Long.parseLong(request.getParameter("id"));

		TaskDao taskDao = new TaskDao();
		try {
			taskDao.deleteTask(taskId);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		response.sendRedirect(request.getContextPath() + "/tasks");
	}
}
