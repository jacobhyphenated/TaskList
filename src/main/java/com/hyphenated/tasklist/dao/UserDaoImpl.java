package com.hyphenated.tasklist.dao;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hyphenated.tasklist.domain.UserEntity;

@Repository
public class UserDaoImpl extends BaseDaoImpl<UserEntity> implements UserDao {

	@Override
	public UserEntity getUserByUsername(String username) {
		return (UserEntity) getSession().createCriteria(UserEntity.class)
			.add(Restrictions.eq("username", username))
			.uniqueResult();
	}

	
}
