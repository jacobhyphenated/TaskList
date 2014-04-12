package com.hyphenated.tasklist.dao;

import com.hyphenated.tasklist.domain.UserEntity;

public interface UserDao extends BaseDao<UserEntity> {

	/**
	 * Get the user by the existing unique username
	 * @param username
	 * @return {@link UserEntity} or null if user does not exist
	 */
	public UserEntity getUserByUsername(String username);
}
