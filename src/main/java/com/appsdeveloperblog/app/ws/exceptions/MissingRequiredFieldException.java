package com.appsdeveloperblog.app.ws.exceptions;

public class MissingRequiredFieldException extends RuntimeException{
	

	private static final long serialVersionUID = -8942619930771322009L;

	public MissingRequiredFieldException(String message){
		super(message);
	}
}
