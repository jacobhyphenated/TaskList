package com.hyphenated.tasklist.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/error")
public class ErrorController {

	@RequestMapping(value="notfound", produces="application/json")
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public @ResponseBody Map<String, String> handle404(){
		return Collections.singletonMap("error", "api call does not exist");
	}
	
	@RequestMapping(value="notauthorized", produces="application/json")
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public @ResponseBody Map<String, String> handleUnauthorized(){
		return Collections.singletonMap("error", "Invalid User Credentials");
	}
}
