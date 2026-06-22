package com.cms.custom_exceptions;

public class AssignmentNotFoundException extends RuntimeException
{

	public AssignmentNotFoundException(String message)
	{
		super(message);
	}
}
