package com.hyphenated.tasklist.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hyphenated.tasklist.domain.Task;
import com.hyphenated.tasklist.service.TaskService;

@Controller
public class TaskController {
	
	@Autowired
	private TaskService taskService;
	
	@RequestMapping(value="/task/all", produces="application/json",
			method=RequestMethod.GET)
	public @ResponseBody List<Task> getAllTasks(){
		return taskService.getAllTasks();
	}
	
	@RequestMapping(value="/task/active", produces="application/json",
			method=RequestMethod.GET)
	public @ResponseBody List<Task> getActiveTasks(){
		return taskService.getActiveTasks();
	}
	
	@RequestMapping(value="/task/add", produces="application/json",
			method={RequestMethod.POST})
	public @ResponseBody Task addTask(@RequestBody Task newTask){
		return taskService.createTask(newTask);
	}

	//TODO use task/{taskId} as the basis for task opperations.
		//determine what method to use based on method??
		//Alternative task/{taskId}/complete/{date}
		//Should not be GET as it alters state. 
		//Not PUT because does not replace the entire resource
	@RequestMapping(value="/task/complete", produces="application/json",
			method={RequestMethod.POST})
	@ResponseStatus(HttpStatus.OK)
	public void completeTask(@RequestParam long taskId, @RequestParam String date){
		//Date should be in format such as: 2007-12-03T10:15:30
		taskService.completeTask(taskId, LocalDateTime.parse(date));
	}

	
	@RequestMapping(value="/task/remove", produces="application/json",
			method={RequestMethod.DELETE})
	@ResponseStatus(HttpStatus.OK)
	public void deleteTask(@RequestParam long taskId){
		taskService.deleteTask(taskId);
	}
	
	@RequestMapping(value="/task/edit", produces="application/json",
			method={RequestMethod.PUT, RequestMethod.POST})
	@ResponseStatus(HttpStatus.OK)
	public void editTask(@RequestBody Task task){
		taskService.editTask(task);
	}
}
