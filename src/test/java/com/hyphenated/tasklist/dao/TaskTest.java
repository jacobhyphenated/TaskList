package com.hyphenated.tasklist.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.hyphenated.tasklist.arch.TimeUtils;
import com.hyphenated.tasklist.domain.Repeatable;
import com.hyphenated.tasklist.domain.Task;

public class TaskTest implements TimeUtils {

	@Test
	public void testIsActive(){
		Task task = new Task();
		task.setDueDate(timeZoneAdjustedDate("2013-12-15T10:19:00"));
		assertTrue(task.isActiveTask());
		
		task.setDueDate(timeZoneAdjustedDate("2013-12-15T10:19:00"));
		task.setCompleteDate(timeZoneAdjustedDate("2014-03-19T15:59:00"));
		assertFalse(task.isActiveTask());
		
		task.setDueDate(timeZoneAdjustedDate("2015-12-15T10:19:00"));
		task.setCompleteDate(timeZoneAdjustedDate("2014-03-19T15:59:00"));
		assertFalse(task.isActiveTask());
		
		Task task2 = new Task();
		task2.setDueDate(timeZoneAdjustedDate("2014-12-15T10:19:00"));
		task2.setRepeatable(Repeatable.DAILY);
		assertTrue(task2.isActiveTask());
		
		task2.setDueDate(timeZoneAdjustedDate("2013-12-15T10:19:00"));
		task2.setCompleteDate(timeZoneAdjustedDate("2014-03-19T15:59:00"));
		assertTrue(task2.isActiveTask());
	}
}
