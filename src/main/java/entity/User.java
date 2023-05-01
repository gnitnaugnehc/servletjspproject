package entity;

import java.sql.Timestamp;

public class User {

	private long id;
	private String username;
	private String password;
	private String email;
	private String profilePictureId;
	private Timestamp createdAt;
	private Timestamp lastLogin;
	
	public User(long id, String username, String email) {
		this.id = id;
		this.username = username;
		this.email = email;
	}

	public User(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
	}

	public User(long id, String username, String email, String profilePictureId, Timestamp createdAt, Timestamp lastLogin) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.createdAt = createdAt;
		this.lastLogin = lastLogin;
	}
	
	public User(long id, String username, String password, String email, String profilePictureId, Timestamp createdAt,
			Timestamp lastLogin) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.profilePictureId = profilePictureId;
		this.createdAt = createdAt;
		this.lastLogin = lastLogin;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProfilePictureId() {
		return profilePictureId;
	}

	public void setProfilePictureId(String profilePictureId) {
		this.profilePictureId = profilePictureId;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}

}
