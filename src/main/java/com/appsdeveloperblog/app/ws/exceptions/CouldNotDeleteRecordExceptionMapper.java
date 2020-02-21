package com.appsdeveloperblog.app.ws.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessage;
import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessages;

public class CouldNotDeleteRecordExceptionMapper implements ExceptionMapper<CouldNotDeleteRecordException>{
	public Response toResponse(CouldNotDeleteRecordException exception){
		
		ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(),ErrorMessages.COULD_NOT_DELETE_RECORD.name(),"http://appsdeveloperblog.com");
		
		return Response.status(Response.Status.BAD_REQUEST).entity(errorMessage).build();
	}
}
