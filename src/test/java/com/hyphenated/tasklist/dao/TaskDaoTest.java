package com.hyphenated.tasklist.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyphenated.tasklist.arch.BaseUnitilsDatasetSpringText;
import com.hyphenated.tasklist.arch.DBUnitDataSet;
import com.hyphenated.tasklist.domain.Repeatable;
import com.hyphenated.tasklist.domain.Task;

@DBUnitDataSet("/task_data.xml")
public class TaskDaoTest extends BaseUnitilsDatasetSpringText {

	@Autowired
	private TaskDao taskDao;
	
	@Test
	public void testGetAllTasks(){
		List<Task> tasks = taskDao.findAll();
		assertEquals(4, tasks.size());
	}
	
	@Test
	public void testFindTask(){
		Task task = taskDao.findById(11l);
		assertEquals("Bills", task.getTitle());
		assertEquals("Pay bills all of the time, right here right now", task.getDescription());
		assertEquals(Repeatable.MONTHLY, task.getRepeatable());
		assertEquals("Important", task.getGroup());
		
		//Check the due date (2014-01-01) using Java8's java.time
		Instant dueDate = task.getDueDate().toInstant();
		assertTrue(dueDate.isBefore(Instant.now()));
		LocalDateTime localDueDate = LocalDateTime.ofInstant(dueDate, ZoneId.systemDefault());
		assertEquals(2014, localDueDate.getYear());
		assertEquals(Month.JANUARY, localDueDate.getMonth());
		assertEquals(1, localDueDate.getDayOfMonth());
		
		assertEquals(100l, task.getUser().getId());
	}
	
	@Test
	public void testAddTask(){
		Task task = new Task();
		task.setDescription("Test task");
		task.setDueDate(Timestamp.valueOf(LocalDateTime.now().plus(Duration.ofDays(5))));
		task.setGroup("TestGroup");
		task.setRepeatable(Repeatable.NONE);
		task.setTitle("ThisTest");
		
		Task savedTask = taskDao.save(task);
		assertTrue(savedTask.getId() > 0);
		
		Task noRepeatTask = new Task();
		noRepeatTask.setDescription("Null Repeat should be set to NONE");
		noRepeatTask.setGroup("TestGroup");
		noRepeatTask.setTitle("sample");
		
		noRepeatTask = taskDao.save(noRepeatTask);
		assertTrue(noRepeatTask.getId() > 0);
		assertNotNull(noRepeatTask.getRepeatable());
		assertEquals(Repeatable.NONE, noRepeatTask.getRepeatable());
	}
	
	@Test
	public void testSaveTask(){
		Task shoes = taskDao.findById(13l);
		assertNull(shoes.getCompleteDate());
		LocalDateTime complete = LocalDateTime.now();
		shoes.setCompleteDate(Timestamp.valueOf(complete));
		taskDao.save(shoes);
		
		Task savedShoes = taskDao.findById(13l);
		assertNotNull(savedShoes.getCompleteDate());
		assertTrue(complete.equals(savedShoes.getCompleteDate().toLocalDateTime()));
	}
	
}
