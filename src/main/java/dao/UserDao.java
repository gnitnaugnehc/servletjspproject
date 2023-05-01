package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import entity.User;
import util.DatabaseUtil;

public class UserDao {

	public User findByUsername(String username) throws SQLException {
		User user = null;

		try (Connection connection = DatabaseUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"SELECT * FROM users WHERE username=?")) {
			statement.setString(1, username);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					long id = resultSet.getLong("id");
					String password = resultSet.getString("password");
					String email = resultSet.getString("email");
					String profilePictureId = resultSet.getString("profile_picture_id");
					Timestamp CreatedAt = resultSet.getTimestamp("created_at");
					Timestamp lastLogin = resultSet.getTimestamp("last_login");
					user = new User(id, username, password, email, profilePictureId, CreatedAt, lastLogin);
				}
			}
		}

		return user;
	}

	public User findByEmail(String email) throws SQLException {
		User user = null;

		try (Connection connection = DatabaseUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"SELECT * FROM users WHERE email=?")) {
			statement.setString(1, email);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					long id = resultSet.getLong("id");
					String username = resultSet.getString("username");
					String password = resultSet.getString("password");
					String profilePictureId = resultSet.getString("profile_picture_id");
					Timestamp CreatedAt = resultSet.getTimestamp("created_at");
					Timestamp lastLogin = resultSet.getTimestamp("last_login");
					user = new User(id, username, password, email, profilePictureId, CreatedAt, lastLogin);
				}
			}
		}

		return user;
	}

	public long createUser(User user) throws SQLException {
		long generatedId = -1;

		try (Connection connection = DatabaseUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"INSERT INTO users (username, password, email, created_at, last_login) VALUES (?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)",
						Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, user.getUsername());
			statement.setString(2, user.getPassword());
			statement.setString(3, user.getEmail());
			statement.executeUpdate();

			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					generatedId = generatedKeys.getLong(1);
				}
			}
		}

		return generatedId;
	}

	public void deleteUser(long id) throws SQLException {
		try (Connection connection = DatabaseUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE id=?")) {
			statement.setLong(1, id);
			statement.executeUpdate();
		}
	}

	public List<User> getAllUsers() throws SQLException {
		List<User> users = new ArrayList<>();

		try (Connection connection = DatabaseUtil.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT id, username, email, profile_picture_id, created_at, last_login FROM users");
				ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				long id = resultSet.getLong("id");
				String username = resultSet.getString("username");
				String email = resultSet.getString("email");
				String profilePictureId = resultSet.getString("profile_picture_id");
				Timestamp createdAt = resultSet.getTimestamp("created_at");
				Timestamp lastLogin = resultSet.getTimestamp("last_login");

				User user = new User(id, username, email, profilePictureId, createdAt, lastLogin);
				users.add(user);
			}
		}

		return users;
	}

	public User getUserById(long id) throws SQLException {
		User user = null;

		try (Connection connection = DatabaseUtil.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT id, username, email, profile_picture_id, created_at, last_login FROM users WHERE id=?")) {
			statement.setLong(1, id);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					String username = resultSet.getString("username");
					String email = resultSet.getString("email");
					String profilePictureId = resultSet.getString("profile_picture_id");
					Timestamp createdAt = resultSet.getTimestamp("created_at");
					Timestamp lastLogin = resultSet.getTimestamp("last_login");
					user = new User(id, username, email, profilePictureId, createdAt, lastLogin);
				}
			}
		}

		return user;
	}

}
