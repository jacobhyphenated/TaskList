package com.hyphenated.tasklist.controller;

import java.util.Collections;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hyphenated.tasklist.exception.UnauthorizedAccessException;

@ControllerAdvice
public class ExceptionHandlerController {
	
	Logger log = Logger.getLogger(this.getClass());

	@ExceptionHandler(UsernameNotFoundException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public @ResponseBody Map<String, String> badUsername(){
		return Collections.singletonMap("error", "Invalid User");
	}
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody Map<String,String> handleBadParameterExcpetion(MissingServletRequestParameterException e){
		return Collections.singletonMap("error", "The parameter " + e.getParameterName() + " is required and not present in the request.");
	}
	
	@ExceptionHandler(UnauthorizedAccessException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public @ResponseBody Map<String,String> badAccessException(UnauthorizedAccessException e){
		return Collections.singletonMap("error", e.getMessage());
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public @ResponseBody Map<String,String> badMethodException(HttpRequestMethodNotSupportedException e){
		return Collections.singletonMap("error", e.getMethod() + " is not supported.");
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody Map<String, String> catchAll(Exception e){
		log.error(e.getMessage());
		log.error(e.getClass());
		e.printStackTrace();
		return Collections.singletonMap("error", "Generic Error");
	}
}
