package com.hyphenated.tasklist.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyphenated.tasklist.dao.TaskDao;
import com.hyphenated.tasklist.dao.UserDao;
import com.hyphenated.tasklist.domain.Task;
import com.hyphenated.tasklist.domain.UserEntity;
import com.hyphenated.tasklist.exception.UnauthorizedAccessException;

@Service
public class TaskServiceImpl implements TaskService{

	@Autowired
	private TaskDao taskDao;
	
	@Autowired
	private UserDao userDao;
	
	@Override
	@Transactional
	public Task createTask(Task task) {
		if(task.getId() > 0){
			throw new UnauthorizedAccessException("You are not allowed to create this task.");
		}
		UserEntity user = userDao.getUserByUsername(getAuthenticatedUsername());
		task.setUser(user);
		return taskDao.save(task);
	}

	@Override
	@Transactional(readOnly=true)
	public List<Task> getActiveTasks() {
		UserEntity user = userDao.getUserByUsername(getAuthenticatedUsername());
		return user.getTasks().stream()
				.filter(t -> t.isActiveTask())
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly=true)
	public List<Task> getAllTasks() {
		UserEntity user = userDao.getUserByUsername(getAuthenticatedUsername());
		user.getTasks().size(); //Lazy load them in
		return user.getTasks();
	}

	@Override
	@Transactional
	public void completeTask(long taskId, LocalDateTime completeTime) {
		Task task = taskDao.findById(taskId);
		if(task == null || !task.getUser().getUsername().equals(getAuthenticatedUsername())){
			throw new UnauthorizedAccessException("You do not have permission to edit this task.");
		}
		
		//Converting LocalDateTime to Timestamp requires specifying 0 time zone offset
		//Otherwise the local (server) time zone is used, which is undesirable
		task.setCompleteDate(Timestamp.from(completeTime.atZone(ZoneOffset.ofHours(0)).toInstant()));
		taskDao.save(task);
	}

	@Override
	@Transactional
	public void deleteTask(long taskId) {
		Task task = taskDao.findById(taskId);
		if(task != null && task.getUser().getUsername().equals(getAuthenticatedUsername())){
			taskDao.remove(task);			
		}
		else{
			throw new UnauthorizedAccessException("You do not have permission to delete this task.");
		}
	}

	@Override
	@Transactional
	public void editTask(Task task) {
		//TODO should probably refactor "Task" to use a DTO pattern.
		//Will need TaskRequest and TaskResponse. Request can be used for edit
		//Use stream and lambda to convert task to task response
		
		//task when used as a DTO does not have user field, add that back in
		UserEntity user = userDao.getUserByUsername(getAuthenticatedUsername());
		Task persistedTask = taskDao.findById(task.getId());
		if(persistedTask == null || persistedTask.getUser().getId() != user.getId()){
			throw new UnauthorizedAccessException("You do not have permission to edit this task.");
		}
		task.setUser(user);
		taskDao.merge(task);
	}
}
