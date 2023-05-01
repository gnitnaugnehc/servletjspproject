package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import dao.TaskDao;
import dao.UserTaskDao;
import entity.Task;
import entity.User;
import entity.UserTask;

@WebServlet("/tasks/*")
public class TaskServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private TaskDao taskDao;

	@Override
	public void init() throws ServletException {
		taskDao = new TaskDao();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			List<Task> tasks;
			try {
				tasks = taskDao.getAllTasks();
				request.setAttribute("tasks", tasks);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			long id = Long.parseLong(pathInfo.substring(1));
			Task task;
			try {
				task = taskDao.getTaskById(id);
				if (task != null) {
					request.setAttribute("task", task);
				} else {
					response.sendError(HttpServletResponse.SC_NOT_FOUND);
					return;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		request.getRequestDispatcher("/WEB-INF/views/task.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    String pathInfo = request.getPathInfo();
	    if (pathInfo == null || pathInfo.equals("/")) {
	        String title = request.getParameter("title");
	        String description = request.getParameter("description");
	        String deadlineString = request.getParameter("deadline").replace("T", " ");
	        Timestamp deadline = Timestamp.valueOf(deadlineString + ":00");
	        boolean completed = false;
	        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
	        Timestamp updatedAt = createdAt;
	        Task task = new Task();
	        task.setTitle(title);
	        task.setDescription(description);
	        task.setDeadline(deadline);
	        task.setCompleted(completed);
	        task.setCreatedAt(createdAt);
	        task.setUpdatedAt(updatedAt);
	        try {
	            long generatedId = taskDao.createTask(task);

	            UserTask userTask = new UserTask();
	            userTask.setUserId(((User) request.getSession().getAttribute("user")).getId());
	            userTask.setTaskId(generatedId);
	            userTask.setCreatedAt(createdAt);
	            userTask.setUpdatedAt(updatedAt);
	            UserTaskDao userTaskDao = new UserTaskDao();
	            userTaskDao.addUserTask(userTask);

	            // Return the new task as a JSON object
	            Gson gson = new Gson();
	            String json = gson.toJson(task);
	            response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	            response.getWriter().write(json);
	        } catch (SQLException e) {
	            throw new ServletException("Unable to create task", e);
	        }
	    } else {
	        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	    }
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String pathInfo = request.getPathInfo();
		long taskId = Long.parseLong(pathInfo.substring(pathInfo.lastIndexOf("/") + 1));
		System.out.println(taskId);

		String method = request.getParameter("_method");
		if ("DELETE".equals(method)) {
			TaskDao taskDao = new TaskDao();
			try {
				taskDao.deleteTask(taskId);
				response.sendRedirect(request.getContextPath() + "/tasks");
			} catch (SQLException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().println("Error deleting task: " + e.getMessage());
			}
		} else {
			response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		}
	}

}
