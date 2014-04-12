package com.hyphenated.tasklist.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyphenated.tasklist.arch.BaseUnitilsDatasetSpringText;
import com.hyphenated.tasklist.arch.DBUnitDataSet;
import com.hyphenated.tasklist.domain.UserEntity;

@DBUnitDataSet("/user_data.xml")
public class UserDaoTest extends BaseUnitilsDatasetSpringText {

	@Autowired
	private UserDao userDao;
	
	@Test
	public void testFindById(){
		UserEntity user = userDao.findById(150l);
		assertEquals("Test 10", user.getUsername());
		assertEquals("BBBBBBB", user.getPassword());
		
		UserEntity user2 = userDao.findById(250l);
		assertEquals("TestTest", user2.getUsername());
		assertEquals("DDDDDDD", user2.getPassword());
	}
	
	@Test
	public void testFindAllUsers(){
		List<UserEntity> users = userDao.findAll();
		assertEquals(4, users.size());
	}
	
	@Test
	public void testRemoveUser(){
		UserEntity user = userDao.findById(150l);
		String username = user.getUsername();
		userDao.remove(user);
		List<UserEntity> users = userDao.findAll();
		assertEquals(3, users.size());
		for(UserEntity u : users){
			assertFalse(username.equals(u.getUsername()));
		}
	}
	
	@Test
	public void testAddUser(){
		UserEntity u = new UserEntity();
		u.setUsername("addtest");
		u.setPassword("123456");
		UserEntity saved = userDao.save(u);
		assertTrue(saved.getId() > 0);
		
		List<UserEntity> users = userDao.findAll();
		assertEquals(5, users.size());
	}
	
	@Test
	public void testUserByUsername(){
		UserEntity u = userDao.getUserByUsername("TestA");
		assertEquals(100l, u.getId());
		assertEquals("AAAAAAA", u.getPassword());
		
		UserEntity none = userDao.getUserByUsername("oafwijewf");
		assertNull(none);
	}
}
