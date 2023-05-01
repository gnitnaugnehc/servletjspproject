package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.UserDao;
import dao.UserTaskDao;
import entity.User;
import entity.UserTask;

@WebServlet("/deleteuser")
public class DeleteUserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		if (session == null || session.getAttribute("user") == null) {
			response.sendRedirect(request.getContextPath() + "/home");
			return;
		}

		User user = (User) session.getAttribute("user");
		long userId = user.getId();

		try {
			UserDao userDao = new UserDao();
			UserTaskDao userTaskDao = new UserTaskDao();

			List<UserTask> userTasks = userTaskDao.getUserTasksByUserId(userId);
			for (UserTask userTask : userTasks) {
				userTaskDao.deleteUserTask(userTask.getId());
			}
			userDao.deleteUser(userId);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		session.invalidate();
		response.sendRedirect(request.getContextPath() + "/home");
	}
}
