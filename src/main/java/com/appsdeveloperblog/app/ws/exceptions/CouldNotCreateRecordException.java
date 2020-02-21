package com.appsdeveloperblog.app.ws.exceptions;

public class CouldNotCreateRecordException extends RuntimeException{
	

	private static final long serialVersionUID = -5865565853821170976L;

	public CouldNotCreateRecordException(String message){
		super(message);
	}

}