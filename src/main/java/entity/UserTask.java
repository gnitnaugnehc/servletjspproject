package entity;

import java.sql.Timestamp;

public class UserTask {

	private long id;
	private long userId;
	private long taskId;
	private Timestamp createdAt;
	private Timestamp updatedAt;

	public UserTask(long id, long userId, long taskId, Timestamp createdAt, Timestamp updatedAt) {
		super();
		this.id = id;
		this.userId = userId;
		this.taskId = taskId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public UserTask() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

}