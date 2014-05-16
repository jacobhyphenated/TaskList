package com.hyphenated.tasklist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hyphenated.tasklist.domain.UserEntity;
import com.hyphenated.tasklist.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;

	@RequestMapping(value="create",method={RequestMethod.POST, RequestMethod.PUT},
			produces="application/json")
	public @ResponseBody UserEntity createUser(
			@RequestParam String username, @RequestParam String password){
		return userService.addUser(username, password);
	}
	
	@RequestMapping(value="alive", method={RequestMethod.GET})
	@ResponseStatus(HttpStatus.OK)
	public void keepAlive(){
		
	}
}
