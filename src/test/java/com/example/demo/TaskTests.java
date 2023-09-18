package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.task.TaskEntity;
import com.example.demo.task.TaskPriority;
import com.example.demo.task.TaskRepository;
import com.example.demo.task.TaskService;

@SpringBootTest
class TaskTests {

	private final TaskEntity task = new TaskEntity();

	private final int id = 1;

	@Mock
	private TaskRepository taskRepository;

	@InjectMocks
	private TaskService taskService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		initTaskServiceMockedMethods();
	}

	@Test
	void getTasksTest() {
		ResponseEntity<List<TaskEntity>> response = taskService.getTask(java.util.Optional.empty(),
				java.util.Optional.empty(), java.util.Optional.empty());

		Assertions.assertTrue(response.getStatusCode().equals(HttpStatus.OK), "No se han encontrado tareas");
	}

	@Test
	void getTasksFilteredByPriorityTest() {
		Optional<TaskPriority> priority = Optional.of(TaskPriority.LOW);

		ResponseEntity<List<TaskEntity>> response = taskService.getTask(java.util.Optional.empty(), priority,
				java.util.Optional.empty());

		Assertions.assertTrue(response.getStatusCode().equals(HttpStatus.OK), "No se han encontrado tareas");
	}

	@Test
	void getTasksFilteredByCompletedTest() {
		Optional<Boolean> completed = Optional.of(true);

		ResponseEntity<List<TaskEntity>> response = taskService.getTask(java.util.Optional.empty(),
				java.util.Optional.empty(), completed);

		Assertions.assertTrue(response.getStatusCode().equals(HttpStatus.OK), "No se han encontrado tareas");
	}

	@Test
	void getTaskFilteredAndSortedTest() {
		Optional<String> sort = Optional.of("priority");
		Optional<TaskPriority> priority = Optional.of(TaskPriority.LOW);
		Optional<Boolean> completed = Optional.of(true);

		ResponseEntity<List<TaskEntity>> response = taskService.getTask(sort, priority, completed);

		Assertions.assertTrue(response.getStatusCode().equals(HttpStatus.OK), "No se han encontrado tareas");
	}

	@Test
	void getTaskByIdTest() {
		ResponseEntity<TaskEntity> response = taskService.getTask(id);

		Assertions.assertTrue(response.getStatusCode().equals(HttpStatus.OK), "No se ha encontrado la tarea");
	}

	@Test
	void createTaskTest() {

		ResponseEntity<TaskEntity> response = taskService.createTask(task);

		Assertions.assertTrue(response.getStatusCode().equals(HttpStatus.CREATED), "No se ha creado la tarea");
	}

	@Test
	void updateTaskTest() {

		ResponseEntity<TaskEntity> response = taskService.updateTask(id, task);

		Assertions.assertTrue(response.getStatusCode().equals(HttpStatus.OK), "No se ha actualizado la tarea");
	}

	@Test
	void deleteTaskTest() {

		ResponseEntity<HttpStatus> response = taskService.deleteTask(id);

		Assertions.assertTrue(response.getStatusCode().equals(HttpStatus.NO_CONTENT), "No se ha eliminado la tarea");
	}

	private void initTaskServiceMockedMethods() {
		Mockito.when(taskRepository.findByPriorityAndCompleted(Mockito.any(TaskPriority.class), Mockito.anyBoolean(),
				Mockito.any(Sort.class))).then(new Answer<List<TaskEntity>>() {

					@Override
					public List<TaskEntity> answer(InvocationOnMock invocation) throws Throwable {
						List<TaskEntity> result = new ArrayList<>();
						result.add(task);

						return result;
					}
				});

		Mockito.when(taskRepository.findByPriority(Mockito.any(TaskPriority.class), Mockito.any(Sort.class)))
				.then(new Answer<List<TaskEntity>>() {

					@Override
					public List<TaskEntity> answer(InvocationOnMock invocation) throws Throwable {
						List<TaskEntity> result = new ArrayList<>();
						result.add(task);

						return result;
					}
				});

		Mockito.when(taskRepository.findByCompleted(Mockito.anyBoolean(), Mockito.any(Sort.class)))
				.then(new Answer<List<TaskEntity>>() {

					@Override
					public List<TaskEntity> answer(InvocationOnMock invocation) throws Throwable {
						List<TaskEntity> result = new ArrayList<>();
						result.add(task);

						return result;
					}
				});

		Mockito.when(taskRepository.findAll(Mockito.any(Sort.class))).then(new Answer<List<TaskEntity>>() {

			@Override
			public List<TaskEntity> answer(InvocationOnMock invocation) throws Throwable {
				List<TaskEntity> result = new ArrayList<>();
				result.add(task);

				return result;
			}
		});

		Mockito.when(taskRepository.findById(Mockito.anyInt())).then(new Answer<Optional<TaskEntity>>() {

			@Override
			public Optional<TaskEntity> answer(InvocationOnMock invocation) throws Throwable {
				Optional<TaskEntity> result = Optional.of(new TaskEntity());
				return result;
			}
		});

		Mockito.when(taskRepository.save(Mockito.any(TaskEntity.class))).then(new Answer<TaskEntity>() {

			@Override
			public TaskEntity answer(InvocationOnMock invocation) throws Throwable {
				return task;
			}
		});

	}
}
