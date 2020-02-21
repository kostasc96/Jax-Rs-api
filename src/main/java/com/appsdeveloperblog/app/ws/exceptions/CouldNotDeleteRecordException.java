package com.appsdeveloperblog.app.ws.exceptions;

public class CouldNotDeleteRecordException extends RuntimeException{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 9000161829309088210L;

	public CouldNotDeleteRecordException(String message){
		super(message);
	}
}
