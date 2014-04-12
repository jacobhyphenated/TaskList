package com.hyphenated.tasklist.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.Month;

import org.junit.Test;

import com.hyphenated.tasklist.arch.TimeUtils;
import com.hyphenated.tasklist.domain.Repeatable;
import com.hyphenated.tasklist.domain.Task;

public class TaskTest implements TimeUtils {

	@Test
	public void testNextNonRepeatable(){
		Task task = new Task();
		task.setDueDate(timeZoneAdjustedDate("2014-02-01T12:00:00"));
		
		LocalDateTime nextDate = localDateFromInstant(task.getNextDueDate());
		assertEquals(2014, nextDate.getYear());
		assertEquals(Month.FEBRUARY ,nextDate.getMonth());
		assertEquals(1, nextDate.getDayOfMonth());
		assertEquals(12, nextDate.getHour());
		assertEquals(0, nextDate.getMinute());
		assertEquals(0, nextDate.getSecond());
	}
	
	@Test
	public void testNextForCompletedNonRepeatable(){
		Task task = new Task();
		task.setDueDate(timeZoneAdjustedDate("2014-02-01T12:00:00"));
		task.setCompleteDate(timeZoneAdjustedDate("2014-01-01T12:00:00"));
		
		assertNull(task.getNextDueDate());
	}
	
	@Test
	public void testNextForNoDateNonRepeatable(){
		Task task = new Task();
		assertNull(task.getNextDueDate());
	}
	
	@Test 
	public void testNextForNoDateRepeatable(){
		Task task = new Task();
		task.setRepeatable(Repeatable.WEEKLY);
		assertNull(task.getNextDueDate());
	}
	
	@Test
	public void testNextForRepeatableNoCompleteDate(){
		Task task = new Task();
		task.setDueDate(timeZoneAdjustedDate("2014-03-15T12:31:00"));
		task.setRepeatable(Repeatable.YEARLY);
		
		LocalDateTime nextDate = localDateFromInstant(task.getNextDueDate());
		assertEquals(2014, nextDate.getYear());
		assertEquals(Month.MARCH ,nextDate.getMonth());
		assertEquals(15, nextDate.getDayOfMonth());
		assertEquals(12, nextDate.getHour());
		assertEquals(31, nextDate.getMinute());
		assertEquals(0, nextDate.getSecond());
	}
	
	@Test
	public void testNextForRepeatableLaterDueDate(){
		Task task = new Task();
		task.setDueDate(timeZoneAdjustedDate("2014-03-15T12:31:00"));
		task.setCompleteDate(timeZoneAdjustedDate("2014-02-19T15:59:00"));
		task.setRepeatable(Repeatable.DAILY);
		
		LocalDateTime nextDate = localDateFromInstant(task.getNextDueDate());
		assertEquals(2014, nextDate.getYear());
		assertEquals(Month.MARCH ,nextDate.getMonth());
		assertEquals(15, nextDate.getDayOfMonth());
		assertEquals(12, nextDate.getHour());
		assertEquals(31, nextDate.getMinute());
		assertEquals(0, nextDate.getSecond());
	}
	
	@Test
	public void testNextForRepeatableDay(){
		Task task = new Task();
		task.setDueDate(timeZoneAdjustedDate("2014-03-15T12:31:00"));
		task.setCompleteDate(timeZoneAdjustedDate("2014-03-19T15:59:00"));
		task.setRepeatable(Repeatable.DAILY);
		
		LocalDateTime nextDate = localDateFromInstant(task.getNextDueDate());
		assertEquals(2014, nextDate.getYear());
		assertEquals(Month.MARCH ,nextDate.getMonth());
		assertEquals(20, nextDate.getDayOfMonth());
		assertEquals(12, nextDate.getHour());
		assertEquals(31, nextDate.getMinute());
		assertEquals(0, nextDate.getSecond());
	}
	
	@Test
	public void testNextForRepeatableWeek(){
		Task task = new Task();
		task.setDueDate(timeZoneAdjustedDate("2014-02-15T11:31:00"));
		task.setCompleteDate(timeZoneAdjustedDate("2014-03-19T15:59:00"));
		task.setRepeatable(Repeatable.WEEKLY);
		
		LocalDateTime nextDate = localDateFromInstant(task.getNextDueDate());
		assertEquals(2014, nextDate.getYear());
		assertEquals(Month.MARCH ,nextDate.getMonth());
		assertEquals(22, nextDate.getDayOfMonth());
		assertEquals(11, nextDate.getHour());
		assertEquals(31, nextDate.getMinute());
		assertEquals(0, nextDate.getSecond());
	}
	
	@Test
	public void testNextForRepeatableMonth(){
		Task task = new Task();
		task.setDueDate(timeZoneAdjustedDate("2013-12-15T11:31:00"));
		task.setCompleteDate(timeZoneAdjustedDate("2014-03-19T15:59:00"));
		task.setRepeatable(Repeatable.MONTHLY);
		
		LocalDateTime nextDate = localDateFromInstant(task.getNextDueDate());
		assertEquals(2014, nextDate.getYear());
		assertEquals(Month.APRIL ,nextDate.getMonth());
		assertEquals(15, nextDate.getDayOfMonth());
		assertEquals(11, nextDate.getHour());
		assertEquals(31, nextDate.getMinute());
		assertEquals(0, nextDate.getSecond());
	}
	
	@Test
	public void testNextForRepeatableYear(){
		Task task = new Task();
		task.setDueDate(timeZoneAdjustedDate("2013-12-15T10:19:00"));
		task.setCompleteDate(timeZoneAdjustedDate("2014-03-19T15:59:00"));
		task.setRepeatable(Repeatable.YEARLY);
		
		LocalDateTime nextDate = localDateFromInstant(task.getNextDueDate());
		assertEquals(2014, nextDate.getYear());
		assertEquals(Month.DECEMBER ,nextDate.getMonth());
		assertEquals(15, nextDate.getDayOfMonth());
		assertEquals(10, nextDate.getHour());
		assertEquals(19, nextDate.getMinute());
		assertEquals(0, nextDate.getSecond());
	}
	
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
