package com.hyphenated.tasklist.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyphenated.tasklist.dao.UserDao;
import com.hyphenated.tasklist.domain.UserEntity;

@Service("userDetailsServiceImpl")
public class UserServiceImpl implements UserDetailsService, UserService {

	@Autowired
	private UserDao userDao;
	
	@Transactional(readOnly=true)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserEntity user = userDao.getUserByUsername(username);
		if(user == null){
			throw new UsernameNotFoundException(username);
		}
		
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		User userDetails = new User(user.getUsername(), user.getPassword(), authorities);
		return userDetails;
	}

	@Transactional
	@Override
	public UserEntity addUser(String username, String password) {
		UserEntity newUser = new UserEntity();
		newUser.setUsername(username);
		
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		newUser.setPassword(encoder.encode(password));
		return userDao.save(newUser);
	}

}
