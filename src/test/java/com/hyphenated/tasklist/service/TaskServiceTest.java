package com.hyphenated.tasklist.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.hyphenated.tasklist.arch.BaseMockitoTest;
import com.hyphenated.tasklist.arch.TimeUtils;
import com.hyphenated.tasklist.dao.TaskDao;
import com.hyphenated.tasklist.dao.UserDao;
import com.hyphenated.tasklist.domain.Repeatable;
import com.hyphenated.tasklist.domain.Task;
import com.hyphenated.tasklist.domain.UserEntity;

public class TaskServiceTest extends BaseMockitoTest implements TimeUtils {

	@Mock
	private UserDao userDao;
	
	@Mock
	private TaskDao taskDao;
	
	@InjectMocks
	private TaskServiceImpl taskService;
	
	@Test
	public void testCreateTask(){
		mockTaskCreation();
		mockSecurityContext();
		Task t = new Task();
		String description = "Test Description";
		String title = "Test Title";
		t.setDescription(description);
		t.setTitle(title);
		t.setRepeatable(Repeatable.MONTHLY);
		LocalDateTime dueTime = LocalDateTime.now();
		t.setDueDate(Timestamp.valueOf(dueTime));
		
		Task createdTask = taskService.createTask(t);
		assertEquals(10l, createdTask.getId());
		assertEquals(title, createdTask.getTitle());
		assertEquals(description, createdTask.getDescription());
		assertEquals(dueTime, createdTask.getDueDate().toLocalDateTime());
		assertEquals(Repeatable.MONTHLY, createdTask.getRepeatable());
		assertTrue(createdTask.isActiveTask());
	}
	
	@Test
	public void testGetActiveTasks(){
		mockSecurityContext();
		when(userDao.getUserByUsername(isA(String.class))).thenReturn(mockedUserTasks());
		List<Task> tasks = taskService.getActiveTasks();
		assertEquals(5, tasks.size());
	}
	
	@Test
	public void testGetAllTasks(){
		mockSecurityContext();
		when(userDao.getUserByUsername(isA(String.class))).thenReturn(mockedUserTasks());
		List<Task> tasks = taskService.getAllTasks();
		assertEquals(7, tasks.size());
	}
	
	private void mockTaskCreation(){
		when(taskDao.save(isA(Task.class))).thenAnswer(this::mockSameTaskWithId);
	}
	
	private Task mockSameTaskWithId(InvocationOnMock invocation) throws Throwable {
		Task t = (Task) invocation.getArguments()[0];
		t.setId(10l);
		return t;
	}
	
	private void mockSecurityContext(){
		UserDetails mockUserDetails = Mockito.mock(UserDetails.class);
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(authentication.getPrincipal()).thenReturn(mockUserDetails);
		when(mockUserDetails.getUsername()).thenReturn("testusername");
		SecurityContextHolder.setContext(securityContext);
	}
	
	private UserEntity mockedUserTasks(){
		UserEntity user = new UserEntity();
		user.setUsername("testusername");
		user.setPassword("testpassword");
		user.setId(10l);
		
		List<Task> tasks = new ArrayList<>();
		
		Task t = new Task();
		//Task 100: Non-repeatable, complete, not active
		t.setId(100l);
		t.setTitle("Task1");
		t.setDescription("Task1 Description");
		t.setGroup("Group1");
		t.setRepeatable(Repeatable.NONE);
		t.setUser(user);
		t.setDueDate(timeZoneAdjustedDate("2014-03-15T12:31:00"));
		t.setCompleteDate(timeZoneAdjustedDate("2014-02-19T15:59:00"));
		tasks.add(t);
		
		//Task 101: Non-repeatable, complete, not active
		t = new Task();
		t.setId(101l);
		t.setTitle("Task2");
		t.setDescription("Task2 Description");
		t.setGroup("Group1");
		t.setRepeatable(Repeatable.NONE);
		t.setUser(user);
		t.setDueDate(timeZoneAdjustedDate("2014-03-15T12:31:00"));
		t.setCompleteDate(timeZoneAdjustedDate("2014-04-19T15:59:00"));
		tasks.add(t);
		
		//Task 102: Non-repeatable, not complete, active
		t = new Task();
		t.setId(102l);
		t.setTitle("Task3");
		t.setDescription("Task3 Description");
		t.setGroup("Group1");
		t.setRepeatable(Repeatable.NONE);
		t.setUser(user);
		t.setDueDate(timeZoneAdjustedDate("2099-03-15T12:31:00"));
		tasks.add(t);
		
		//Task 103: Non-repeatable, no due date, active
		t = new Task();
		t.setId(103l);
		t.setTitle("Task4");
		t.setDescription("Task4 Description");
		t.setGroup("Group1");
		t.setRepeatable(Repeatable.NONE);
		t.setUser(user);
		tasks.add(t);
		
		//Task 104: Repeatable, complete, active
		t = new Task();
		t.setId(104l);
		t.setTitle("Task5");
		t.setDescription("Task5 Description");
		t.setGroup("Group1");
		t.setRepeatable(Repeatable.MONTHLY);
		t.setUser(user);
		t.setDueDate(timeZoneAdjustedDate("2014-01-15T12:31:00"));
		t.setCompleteDate(timeZoneAdjustedDate("2014-04-19T15:59:00"));
		tasks.add(t);
		
		//Task 105: Repeatable, not complete, active
		t = new Task();
		t.setId(105l);
		t.setTitle("Task6");
		t.setDescription("Task6 Description");
		t.setGroup("Group1");
		t.setRepeatable(Repeatable.MONTHLY);
		t.setUser(user);
		t.setDueDate(timeZoneAdjustedDate("2099-01-15T12:31:00"));
		tasks.add(t);
		
		//Task 106: Repeatable, no dates, active
		t = new Task();
		t.setId(106l);
		t.setTitle("Task7");
		t.setDescription("Task7 Description");
		t.setGroup("Group2");
		t.setRepeatable(Repeatable.YEARLY);
		t.setUser(user);
		tasks.add(t);
		
		user.setTasks(tasks);
		return user;
	}
}
