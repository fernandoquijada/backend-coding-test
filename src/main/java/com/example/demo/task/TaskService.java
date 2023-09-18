package com.example.demo.task;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class TaskService {

	@Autowired
	TaskRepository taskRepository;

	public ResponseEntity<List<TaskEntity>> getTask(Optional<String> sortBy, Optional<TaskPriority> priority,
			Optional<Boolean> completed) {
		List<TaskEntity> tasks = null;
		Sort sort = Sort.by(Sort.Direction.ASC, sortBy.orElse("id"));
		if (priority.isPresent()) {
			if (completed.isPresent()) {
				tasks = taskRepository.findByPriorityAndCompleted(priority.get(), completed.get(), sort);
			} else {
				tasks = taskRepository.findByPriority(priority.get(), sort);
			}
		} else if (completed.isPresent()) {
			tasks = taskRepository.findByCompleted(completed.get(), sort);
		} else {
			tasks = taskRepository.findAll(sort);
		}

		ResponseEntity<List<TaskEntity>> response;

		if (CollectionUtils.isEmpty(tasks)) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(tasks, HttpStatus.OK);
		}
		return response;
	}

	public ResponseEntity<TaskEntity> getTask(Integer id) {
		Optional<TaskEntity> taskData = taskRepository.findById(id);
		ResponseEntity<TaskEntity> response;
		if (taskData.isEmpty()) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(taskData.get(), HttpStatus.OK);
		}

		return response;
	}

	public ResponseEntity<TaskEntity> createTask(TaskEntity task) {
		try {
			if (CollectionUtils.isEmpty(task.getSubtasks())) {
				taskRepository.save(task);
			} else {
				TaskEntity clonedTask = new TaskEntity();
				clonedTask.setDescription(task.getDescription());
				clonedTask.setCompleted(task.isCompleted());
				clonedTask.setPriority(task.getPriority());
				task.setCreationDate(task.getCreationDate());
				for (SubtaskEntity subtask : task.getSubtasks()) {
					clonedTask.addSubtask(subtask);
				}
				taskRepository.save(clonedTask);
			}
			return new ResponseEntity<>(task, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<TaskEntity> updateTask(Integer id, TaskEntity task) {
		Optional<TaskEntity> taskData = taskRepository.findById(id);

		try {
			if (taskData.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				// I want it to update only the new fields with data
				TaskEntity result = taskData.get();
				if (task.getDescription() != null)
					result.setDescription(task.getDescription());
				result.setCompleted(task.isCompleted());
				if (task.getPriority() != null)
					result.setPriority(task.getPriority());
				if (task.getCreationDate() != null)
					result.setCreationDate(task.getCreationDate());
				if (!CollectionUtils.isEmpty(task.getSubtasks())) {
					result.deleteSubtasks();
					for (SubtaskEntity subtask : task.getSubtasks()) {
						result.addSubtask(subtask);
					}
				}
				return new ResponseEntity<>(taskRepository.save(result), HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	public ResponseEntity<HttpStatus> deleteTask(@PathVariable("id") Integer id) {
		try {
			taskRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
