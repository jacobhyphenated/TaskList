package com.hyphenated.tasklist.service;

import com.hyphenated.tasklist.domain.UserEntity;

public interface UserService {

	/**
	 * Create a new user
	 * @param username user's username
	 * @param password plain text password
	 * @return {@link UserEntity} with unique ID and hashed password
	 */
	public UserEntity addUser(String username, String password);
}
