package com.hyphenated.tasklist.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.hyphenated.tasklist.domain.Task;

public interface TaskService {

	/**
	 * Create a new task
	 * @param task {@link Task} object to be created
	 * @return {@link Task} with ID defined after being persisted
	 */
	public Task createTask(Task task);
	
	/**
	 * Get all the uncompleted tasks for the authenticated user
	 * @return List of {@link Task} objects
	 */
	public List<Task> getActiveTasks();
	
	/**
	 * Get all tasks (including old or completed tasks)
	 * @return List of {@link Task} objects
	 */
	public List<Task> getAllTasks();
	
	/**
	 * Marks the task as completed. If the task is repeatable, changes the 
	 * last completion date to mark the most recent occurrence of this task
	 * as complete, moving the nextDueDate forward by the repeating interval.
	 * @param taskId unique ID for the task
	 * @param completeTime {@link LocalDateTime} when the task was completed
	 */
	public void completeTask(long taskId, LocalDateTime completeTime);
	
	/**
	 * Removes the task. The task will no longer be accessible and will
	 * no longer return from the {@link TaskService#getAllTasks()} method.
	 * @param taskId unique ID for the task
	 */
	public void deleteTask(long taskId);
	
	
	/**
	 * Edits the details of an existing task
	 * @param task detached task object to update
	 */
	public void editTask(Task task);
	
	default public String getAuthenticatedUsername(){
		UserDetails user = (UserDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		return user.getUsername();
	}
}
