package com.example.demo.task;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {

	List<TaskEntity> findByPriority(TaskPriority taskPriority, Sort sort);

	List<TaskEntity> findByCompleted(Boolean completed, Sort sort);

	List<TaskEntity> findByPriorityAndCompleted(TaskPriority taskPriority, Boolean completed, Sort sort);

}
