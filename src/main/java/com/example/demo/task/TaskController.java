package com.example.demo.task;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {

	@Autowired
	TaskService service;

	@GetMapping
	public ResponseEntity<List<TaskEntity>> getTasks(@RequestParam Optional<String> sortBy,
			@RequestParam Optional<TaskPriority> priority, @RequestParam Optional<Boolean> completed) {
		return service.getTask(sortBy, priority, completed);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<TaskEntity> getTask(@PathVariable("id") Integer id) {
		return service.getTask(id);

	}

	@PostMapping
	public ResponseEntity<TaskEntity> createTask(@RequestBody TaskEntity task) {
		return service.createTask(task);
	}

	@PutMapping("/{id}")
	public ResponseEntity<TaskEntity> updateTask(@PathVariable("id") Integer id, @RequestBody TaskEntity task) {
		return service.updateTask(id, task);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<HttpStatus> deleteTask(@PathVariable("id") Integer id) {
		return service.deleteTask(id);
	}
}
