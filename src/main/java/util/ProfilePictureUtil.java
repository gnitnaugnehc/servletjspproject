package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

public class ProfilePictureUtil {

	public static byte[] getProfilePicture(String profilePictureId) throws SQLException {
		String sql = "SELECT image FROM profile_pictures WHERE id = ?";
		byte[] imageBytes = null;

		try (Connection connection = DatabaseUtil.getConnection();
				PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, profilePictureId);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					imageBytes = rs.getBytes("image");
				}
			}
		}

		return imageBytes;
	}

	public static String updateProfilePicture(long userId, byte[] profilePicture) {
		
		String id = UUID.randomUUID().toString();
		
		try (Connection connection = DatabaseUtil.getConnection();
				PreparedStatement stmt = connection.prepareStatement(
						"INSERT INTO profile_pictures (id, user_id, image, updated_at) VALUES (?, ?, ?, ?)")) {

			stmt.setString(1, id);
			stmt.setLong(2, userId);
			stmt.setBytes(3, profilePicture);
			stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

			stmt.executeUpdate();

			try (PreparedStatement updateStmt = connection
					.prepareStatement("UPDATE users SET profile_picture_id = ? WHERE id = ?")) {
				updateStmt.setString(1, id);
				updateStmt.setLong(2, userId);
				updateStmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}

}
