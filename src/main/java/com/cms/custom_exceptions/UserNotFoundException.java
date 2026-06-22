package com.cms.custom_exceptions;

public class UserNotFoundException extends RuntimeException
{

	public UserNotFoundException(String message) {
		super(message);
	}

	
}
