package com.appsdeveloperblog.app.ws.exceptions;

public class NoRecordFoundException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6276765411196369111L;

	public NoRecordFoundException(String message){
		super(message);
	}
}
