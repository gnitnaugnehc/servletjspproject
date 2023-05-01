package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entity.Task;
import util.DatabaseUtil;

public class TaskDao {

	private static final String SELECT_ALL_SQL = "SELECT * FROM task";
	private static final String SELECT_ONE_SQL = "SELECT * FROM task WHERE id=?";
	private static final String INSERT_SQL = "INSERT INTO task (title, description, deadline, completed, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
	private static final String UPDATE_SQL = "UPDATE task SET title=?, description=?, deadline=?, completed=?, updated_at=? WHERE id=?";
	private static final String DELETE_SQL = "DELETE FROM task WHERE id=?";

	public List<Task> getAllTasks() throws SQLException {
		List<Task> tasks = new ArrayList<>();

		try (Connection connection = DatabaseUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);
				ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				Task task = new Task();
				task.setId(resultSet.getLong("id"));
				task.setTitle(resultSet.getString("title"));
				task.setDescription(resultSet.getString("description"));
				task.setDeadline(resultSet.getTimestamp("deadline"));
				task.setCompleted(resultSet.getBoolean("completed"));
				task.setCreatedAt(resultSet.getTimestamp("created_at"));
				task.setUpdatedAt(resultSet.getTimestamp("updated_at"));
				tasks.add(task);
			}
		}

		return tasks;
	}

	public Task getTaskById(long id) throws SQLException {
		Task task = null;

		try (Connection connection = DatabaseUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_ONE_SQL)) {
			statement.setLong(1, id);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					task = new Task();
					task.setId(resultSet.getLong("id"));
					task.setTitle(resultSet.getString("title"));
					task.setDescription(resultSet.getString("description"));
					task.setDeadline(resultSet.getTimestamp("deadline"));
					task.setCompleted(resultSet.getBoolean("completed"));
					task.setCreatedAt(resultSet.getTimestamp("created_at"));
					task.setUpdatedAt(resultSet.getTimestamp("updated_at"));
				}
			}
		}

		return task;
	}

	public long createTask(Task task) throws SQLException {
	    try (Connection connection = DatabaseUtil.getConnection();
	            PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
	        statement.setString(1, task.getTitle());
	        statement.setString(2, task.getDescription());
	        statement.setTimestamp(3, task.getDeadline());
	        statement.setBoolean(4, task.isCompleted());
	        statement.setTimestamp(5, task.getCreatedAt());
	        statement.setTimestamp(6, task.getUpdatedAt());
	        int affectedRows = statement.executeUpdate();
	        if (affectedRows == 0) {
	            throw new SQLException("Creating task failed, no rows affected.");
	        }
	        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	                return generatedKeys.getLong(1);
	            } else {
	                throw new SQLException("Creating task failed, no ID obtained.");
	            }
	        }
	    }
	}

	public void updateTask(Task task) throws SQLException {
		try (Connection connection = DatabaseUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
			statement.setString(1, task.getTitle());
			statement.setString(2, task.getDescription());
			statement.setTimestamp(3, task.getDeadline());
			statement.setBoolean(4, task.isCompleted());
			statement.setTimestamp(5, task.getUpdatedAt());
			statement.setLong(6, task.getId());
			statement.executeUpdate();
		}
	}

	public void deleteTask(long id) throws SQLException {
		try (Connection connection = DatabaseUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {
			statement.setLong(1, id);
			statement.executeUpdate();
		}
	}

}
