package entity;

import java.sql.Timestamp;

public class Task {

	private long id;
	private String title;
	private String description;
	private Timestamp deadline;
	private boolean completed;
	private Timestamp createdAt;
	private Timestamp updatedAt;

	public Task() {
		this.id = 0;
		this.title = null;
		this.description = null;
		this.deadline = null;
		this.completed = false;
		this.createdAt = null;
		this.updatedAt = null;
	}

	public Task(long id, String title, String description, Timestamp deadline, boolean completed, Timestamp createdAt,
			Timestamp updatedAt) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.deadline = deadline;
		this.completed = completed;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getDeadline() {
		return deadline;
	}

	public void setDeadline(Timestamp deadline) {
		this.deadline = deadline;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
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
