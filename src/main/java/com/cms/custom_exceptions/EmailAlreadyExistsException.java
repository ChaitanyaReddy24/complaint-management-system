package com.cms.custom_exceptions;

public class EmailAlreadyExistsException extends RuntimeException
{

	public EmailAlreadyExistsException(String message) {
		super(message);
	}

	
}
