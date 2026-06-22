package com.cms.custom_exceptions;

public class FeedbackAlreadyExistsException extends RuntimeException
{

	public FeedbackAlreadyExistsException(String message) {
		super(message);
	}

	
}
