package com.hyphenated.tasklist.exception;

public class UnauthorizedAccessException extends RuntimeException {
	
	private static final long serialVersionUID = -7345103981955080279L;

	public UnauthorizedAccessException(String message){
		super(message);
	}
}
