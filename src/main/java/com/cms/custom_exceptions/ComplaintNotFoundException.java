package com.cms.custom_exceptions;

public class ComplaintNotFoundException extends RuntimeException
{

	public ComplaintNotFoundException(String message)
	{
		super(message);
	}
}
