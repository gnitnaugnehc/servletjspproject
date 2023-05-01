package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entity.UserTask;
import util.DatabaseUtil;

public class UserTaskDao {

	private Connection connection;

	public UserTaskDao() throws SQLException {
		connection = DatabaseUtil.getConnection();
	}

	public void addUserTask(UserTask userTask) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(
				"INSERT INTO user_task (user_id, task_id, created_at, updated_at) VALUES (?, ?, ?, ?)",
				Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setLong(1, userTask.getUserId());
		preparedStatement.setLong(2, userTask.getTaskId());
		preparedStatement.setTimestamp(3, userTask.getCreatedAt());
		preparedStatement.setTimestamp(4, userTask.getUpdatedAt());
		preparedStatement.executeUpdate();

		ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
		if (generatedKeys.next()) {
			userTask.setId(generatedKeys.getLong(1));
		} else {
			throw new SQLException("Failed to retrieve auto-generated ID for UserTask");
		}
	}

	public UserTask getUserTaskById(long id) throws SQLException {
		PreparedStatement preparedStatement = connection
				.prepareStatement("SELECT id, user_id, task_id, created_at, updated_at FROM user_task WHERE id = ?");
		preparedStatement.setLong(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			return new UserTask(resultSet.getLong("id"), resultSet.getLong("user_id"), resultSet.getLong("task_id"),
					resultSet.getTimestamp("created_at"), resultSet.getTimestamp("updated_at"));
		}
		return null;
	}

	public List<UserTask> getAllUserTasks() throws SQLException {
		List<UserTask> userTasks = new ArrayList<>();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement
				.executeQuery("SELECT id, user_id, task_id, created_at, updated_at FROM user_task");
		while (resultSet.next()) {
			userTasks.add(
					new UserTask(resultSet.getLong("id"), resultSet.getLong("user_id"), resultSet.getLong("task_id"),
							resultSet.getTimestamp("created_at"), resultSet.getTimestamp("updated_at")));
		}
		return userTasks;
	}

	public void updateUserTask(UserTask userTask) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(
				"UPDATE user_task SET user_id = ?, task_id = ?, created_at = ?, updated_at = ? WHERE id = ?");
		preparedStatement.setLong(1, userTask.getUserId());
		preparedStatement.setLong(2, userTask.getTaskId());
		preparedStatement.setTimestamp(3, userTask.getCreatedAt());
		preparedStatement.setTimestamp(4, userTask.getUpdatedAt());
		preparedStatement.setLong(5, userTask.getId());
		preparedStatement.executeUpdate();
	}

	public void deleteUserTask(long id) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM user_task WHERE id = ?");
		preparedStatement.setLong(1, id);
		preparedStatement.executeUpdate();
	}

	public List<Long> getTaskIdByUserId(long id) throws SQLException {
		List<Long> taskIds = new ArrayList<>();
		PreparedStatement preparedStatement = connection
				.prepareStatement("SELECT task_id FROM user_task WHERE user_id = ?");
		preparedStatement.setLong(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			long taskId = resultSet.getLong("task_id");
			taskIds.add(taskId);
		}
		return taskIds;
	}

	public List<UserTask> getUserTasksByUserId(long userId) throws SQLException {
		List<UserTask> userTasks = new ArrayList<>();
		PreparedStatement preparedStatement = connection
				.prepareStatement("SELECT id, task_id, created_at, updated_at FROM user_task WHERE user_id = ?");
		preparedStatement.setLong(1, userId);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			userTasks.add(new UserTask(resultSet.getLong("id"), userId, resultSet.getLong("task_id"),
					resultSet.getTimestamp("created_at"), resultSet.getTimestamp("updated_at")));
		}
		return userTasks;
	}

}
