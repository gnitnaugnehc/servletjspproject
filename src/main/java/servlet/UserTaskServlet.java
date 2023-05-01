package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.TaskDao;
import dao.UserTaskDao;
import entity.Task;
import entity.User;

@WebServlet("/usertask")
public class UserTaskServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private TaskDao taskDao;
	private UserTaskDao userTaskDao;

	public void init() {
		taskDao = new TaskDao();
		try {
			userTaskDao = new UserTaskDao();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			response.sendRedirect(request.getContextPath() + "/signin");
			return;
		}

		User user = (User) session.getAttribute("user");
		List<Long> taskIds = null;
		try {
			taskIds = userTaskDao.getTaskIdByUserId(user.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		List<Task> tasks = new ArrayList<>();
		for (Long taskId : taskIds) {
			Task task = null;
			try {
				task = taskDao.getTaskById(taskId);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (task != null) {
				tasks.add(task);
			}
		}

		session.setAttribute("tasks", tasks);

		request.getRequestDispatcher("WEB-INF/views/home.jsp").forward(request, response);
	}

}
