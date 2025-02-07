package com.example.demo.task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

@Entity
public class TaskEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String description;
	private boolean completed;
	private TaskPriority priority;
	private LocalDateTime creationDate;

	@OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SubtaskEntity> subtaskList;

	@PrePersist
	public void onPrePersist() {
		if (creationDate == null)
			this.setCreationDate(LocalDateTime.now());
	}

	public void addSubtask(SubtaskEntity subtask) {
		if (subtaskList == null) {
			subtaskList = new ArrayList<>();
		}
		if (subtask.getCreationDate() == null) {
			subtask.setCreationDate(creationDate);
		}
		subtask.setTask(this);
		subtaskList.add(subtask);
	}

	public void deleteSubtasks() {
		subtaskList.removeAll(subtaskList);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public TaskPriority getPriority() {
		return priority;
	}

	public void setPriority(TaskPriority priority) {
		this.priority = priority;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public List<SubtaskEntity> getSubtasks() {
		return subtaskList;
	}

	public void setSubtasks(List<SubtaskEntity> subtasks) {
		this.subtaskList = subtasks;
	}

}
